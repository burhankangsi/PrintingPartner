package com.lawnics.printingpartner.CustomGallery.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.FileUtils;
import android.provider.MediaStore;

import com.lawnics.printingpartner.CustomGallery.Model_images;
import com.lawnics.printingpartner.CustomGallery.PhotosInterface;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ImageUtil {


    public static List<Uri> getAllShownImagesPath(Activity activity) {
        Uri uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ArrayList<Uri> listOfAllImages = new ArrayList<>();
        String[] projection = new String[]{MediaStore.Images.Media._ID};
        Cursor cursor = activity.getContentResolver().query(uriExternal, projection, null, null, MediaStore.Images.ImageColumns.DATE_ADDED + " DESC");
        if (cursor != null) {
            int columnIndexId = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            long imageId;
            while (cursor.moveToNext()) {
                imageId = cursor.getLong(columnIndexId);
                Uri uriImage = Uri.withAppendedPath(uriExternal, String.valueOf(imageId));
                if (new File(getRealPathFromUri(activity, uriImage)).exists())
                    listOfAllImages.add(uriImage);
            }
            cursor.close();
        }
        PhotosInterface.AllPhotos.add(listOfAllImages);
        return listOfAllImages;
    }

    public static String getRealPathFromUri(Activity activity, Uri contentUri) {
        String path = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(contentUri, proj, null, null, null);
        assert cursor != null;
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }

    public static boolean checkIsImage(Context context, Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        String type = contentResolver.getType(uri);
        if (type != null) {
            return type.startsWith("image/");
        } else {
            // try to decode as image (bounds only)
            InputStream inputStream = null;
            try {
                inputStream = contentResolver.openInputStream(uri);
                if (inputStream != null) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(inputStream, null, options);
                    return options.outWidth > 0 && options.outHeight > 0;
                }
            } catch (IOException e) {
                // ignore
            } finally {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    FileUtils.closeQuietly(inputStream);
                }
            }
        }
        // default outcome if image not confirmed
        return false;
    }

    public static ArrayList<Model_images> fn_imagespath(Activity activity) {
        ArrayList<Model_images> al_images = new ArrayList<>();
        boolean boolean_folder = false;
        int int_position = 0;
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;

        String absolutePathOfImage = null;
        int columnIndexId;
        long imageId;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media._ID};

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = activity.getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        columnIndexId = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
        boolean to_remove = false;
        int index_to_remove = 0;
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            imageId = cursor.getLong(columnIndexId);
            Uri uriImage = Uri.withAppendedPath(uri, String.valueOf(imageId));

            for (int i = 0; i < al_images.size(); i++) {
                if (al_images.get(i).getStr_folder() != null) {
                    if (al_images.get(i).getStr_folder().equals(cursor.getString(column_index_folder_name))) {
                        boolean_folder = true;
                        int_position = i;
                        break;
                    } else {
                        boolean_folder = false;
                    }
                } else {
                    if (!to_remove) {
                        to_remove = true;
                        index_to_remove = i;
                    }
                }
            }
            if (to_remove) {
                al_images.remove(index_to_remove);
                to_remove = false;
            }
            if (boolean_folder) {
                if (new File(absolutePathOfImage).exists()) {
                    ArrayList<Uri> al_Uri = new ArrayList<>();
                    ArrayList<String> al_path = new ArrayList<>();
                    al_path.addAll(al_images.get(int_position).getAl_imagepath());
                    al_Uri.addAll(al_images.get(int_position).getAl_imageuri());
                    al_path.add(absolutePathOfImage);
                    al_Uri.add(uriImage);
                    al_images.get(int_position).setAl_imagepath(al_path);
                    al_images.get(int_position).setAl_imageuri(al_Uri);
                }
            } else {
                if (new File(absolutePathOfImage).exists()) {
                    ArrayList<String> al_path = new ArrayList<>();
                    ArrayList<Uri> al_uri = new ArrayList<>();
                    al_path.add(absolutePathOfImage);
                    al_uri.add(uriImage);
                    Model_images obj_model = new Model_images();
                    obj_model.setStr_folder(cursor.getString(column_index_folder_name));
                    obj_model.setAl_imagepath(al_path);
                    obj_model.setAl_imageuri(al_uri);
                    al_images.add(obj_model);
                }
            }


        }
        Model_images modelImages = new Model_images();
        modelImages.setStr_folder("All Photos");
        List<Uri> listOfAllImages = new ArrayList<>();
        List<String> listOfAllPaths = new ArrayList<>();
        for (int i = 0; i < al_images.size(); i++) {
            //Log.e("FOLDER", al_images.get(i).getStr_folder());
            listOfAllImages.addAll(al_images.get(i).getAl_imageuri());
            listOfAllPaths.addAll(al_images.get(i).getAl_imagepath());
            //for (int j = 0; j < al_images.get(i).getAl_imagepath().size(); j++) {
            //Log.e("FILE", al_images.get(i).getAl_imagepath().get(j));
            //Log.e("URI",al_images.get(i).getAl_imageuri().get(j).toString());
            //}
        }
        modelImages.setAl_imagepath((ArrayList<String>) listOfAllPaths);
        modelImages.setAl_imageuri((ArrayList<Uri>) listOfAllImages);
        al_images.add(0, modelImages);
        PhotosInterface.AllPhotos.clear();
        PhotosInterface.AlbumPhotos.clear();
        PhotosInterface.AllPhotos.add(listOfAllImages);
        PhotosInterface.AlbumPhotos.add(al_images);
        return al_images;
    }
}