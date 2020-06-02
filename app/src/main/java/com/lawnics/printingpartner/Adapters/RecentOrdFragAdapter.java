package com.lawnics.printingpartner.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.lawnics.printingpartner.DetailsActivity;
import com.lawnics.printingpartner.ManagementActivity;
import com.lawnics.printingpartner.Model.RecentOrdModel;
import com.lawnics.printingpartner.PaperProduct;
import com.lawnics.printingpartner.R;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecentOrdFragAdapter extends RecyclerView.Adapter <RecentOrdFragAdapter.ViewHolder> {

    private Context mContext;
    private List<RecentOrdModel> itemList;
    public RecentOrdFragAdapter(Context mContext, List<RecentOrdModel> itemList)
    {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_recent_orders, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        RecentOrdModel single_bid_item = itemList.get(position);
        holder.cust_name.setText(single_bid_item.getFileName());
        holder.order_no.setText(single_bid_item.getOrd_no());
        holder.location.setText(single_bid_item.getLocation());
        holder.doc_no.setText(single_bid_item.getNo_docs());
        holder.pages.setText(single_bid_item.getNo_pages());
        holder.price.setText(single_bid_item.getItemPrice());
        holder.time.setText(single_bid_item.getTime());

        Picasso.get().load(single_bid_item.getCust_image()).into(holder.cust_img);

        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailsActivity.class);
                mContext.startActivity(intent);
            }
        });

        holder.accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new RetrievePDFStream(single_bid_item.getFilename()).execute(single_bid_item.getFile_path());

                //Intent intent = new Intent(mContext, RecentOrdersFragCompletedAdapter.class);
                //mContext.startActivity(intent);


            }
        });

        holder.decline_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(mContext, DetailsActivity.class);
//                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cust_name, order_no, location, doc_no, pages, time, price, details;
        public CircleImageView cust_img;
        public AppCompatButton accept_btn, decline_btn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cust_name = itemView.findViewById(R.id.tv_cust_name_cv_rec_ord);
            order_no = itemView.findViewById(R.id.tv_order_no_cv_rec_ord_val);
            location = itemView.findViewById(R.id.tv_location_cv_rec_ord);
            doc_no = itemView.findViewById(R.id.tv_docs_cv_rec_ord);
            pages = itemView.findViewById(R.id.tv_pages_cv_rec_ord);
            time = itemView.findViewById(R.id.tv_time_cv_rec_ord);
            price = itemView.findViewById(R.id.tv_rec_cv_rec_ord_price);
            details = itemView.findViewById(R.id.tv_details_cv_rec_ord);
            cust_img = itemView.findViewById(R.id.circular_img_view_cv_recent_orders);

            accept_btn = itemView.findViewById(R.id.btn_cv_rec_orders_btn1);
            decline_btn = itemView.findViewById(R.id.btn_cv_rec_orders_btn2);

        }
    }

    class RetrievePDFStream extends AsyncTask<String, Void, List<String>>
    {
        String filename;
        ProgressDialog dialog;

        public RetrievePDFStream(String filename) {
            this.filename = filename;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(mContext);
            dialog.setMessage("Downloading...");
            dialog.show();
        }

        @Override
        protected List<String> doInBackground(String... strings) {

            InputStream inputStream = null;
            try
            {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                File outputFile = new File(mContext.getExternalFilesDir(null),filename+".pdf");
                FileOutputStream fos = new FileOutputStream(outputFile);
                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                }
                fos.flush();
                fos.close();
                inputStream.close();
                return RenderBitmapsFromPDF(mContext, Uri.fromFile(outputFile));
            }
            catch (IOException e)
            {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);
            Intent intent = new Intent(mContext,PaperProduct.class);
            intent.putStringArrayListExtra("images", (ArrayList<String>) strings);
            ((Activity)mContext).startActivity(intent);
        }
    }


    public List<String> RenderBitmapsFromPDF(Context context,Uri path){
        List<String> bmpPaths = new ArrayList<>();
        try {
            System.out.println(getPath(context,path));
            File file1 = new File(getPath(context,path));
            PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(file1,ParcelFileDescriptor.MODE_READ_WRITE));

            final int pageCount = renderer.getPageCount();
            for (int i = 0; i < pageCount; i++) {
                PdfRenderer.Page page = renderer.openPage(i);
                Bitmap mBitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(),
                        Bitmap.Config.ARGB_8888);
                page.render(mBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                File file = new File(context.getExternalFilesDir(null),"rendered_"+System.currentTimeMillis()+".jpg");
                try{

                    OutputStream stream;

                    stream = new FileOutputStream(file);

                    mBitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);

                    stream.flush();

                    stream.close();

                }catch (IOException e)
                {
                    e.printStackTrace();
                }

                Uri mImageUri = Uri.parse(file.getAbsolutePath());

                bmpPaths.add(file.getAbsolutePath());
                page.close();
            }
            renderer.close();
            return bmpPaths;
        } catch (IOException e) {
            return null;
        }
    }

    public String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
