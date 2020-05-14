package com.lawnics.printingpartner.CustomGallery.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lawnics.printingpartner.CustomGallery.PhotosInterface;
import com.lawnics.printingpartner.CustomGallery.config.ImageConfig;
import com.lawnics.printingpartner.CustomGallery.enums.PickerOrientation;
import com.lawnics.printingpartner.CustomGallery.listener.MediaPickerListener;
import com.lawnics.printingpartner.CustomGallery.util.ImageUtil;
import com.lawnics.printingpartner.CustomGallery.util.PermissionUtil;
import com.lawnics.printingpartner.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ImagePickActivity extends AppCompatActivity {

    public static final String IMAGE_CONFIG = "IMAGE_CONFIG";
    public static final int EXTERNAL_STORAGE = 100;

    public static void startActivity(Context context, Intent intent, MediaPickerListener newListener) {
        context.startActivity(intent);
        listener = newListener;
    }

    public int[] position = new int[]{0};

    private static MediaPickerListener listener;
    private ImageConfig config;

    private Toolbar tbImage;
    public Button bt_Image;
    private TextView tvSelect;
    private LinearLayout llCount;
    private TextView tvCurrent;
    private TextView tvMax;
    private TextView tvSlash;
    private RecyclerView rvImage;
    private RecyclerView rvFolder;
    private ImageView goback;
    private ImageListAdapter adapter;
    private FolderListAdapter folderListAdapter;

    @Override
    public Resources.Theme getTheme() {
        config = (ImageConfig) getIntent().getSerializableExtra(IMAGE_CONFIG);
        if (config == null) {
            throw new IllegalStateException("ImageConfig can not be null! If it's happened, please leave a issue!");
        }

        Resources.Theme theme = super.getTheme();
        theme.applyStyle(config.getTheme(), true);
        return theme;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED)
                Toast.makeText(this, config.getPermissionRequireText(), Toast.LENGTH_SHORT).show();
            else {
                adapter.setImages(PhotosInterface.AllPhotos.get(0));
            }
        } else super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pick);

        if (!PermissionUtil.isReadExternalStorageAllow(this))
            PermissionUtil.requestReadExternalStorage(this, EXTERNAL_STORAGE);

        setupOrientation();
        setupFindViewById();
        setupTextAndColor();
        setupSingleMode();
        setupRecyclerView();
        setupSelect();

        bt_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFloatButtons(true);
            }
        });

        rvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findViewById(R.id.theCardView).getVisibility() == View.VISIBLE){
                    toggleFloatButtons(false);
                }
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setupOrientation() {
        if (config.getOrientation() == PickerOrientation.PORTRAIT)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else if (config.getOrientation() == PickerOrientation.LANDSCAPE)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    private void setupFindViewById() {
        tbImage = findViewById(R.id.tb_image);
        bt_Image = findViewById(R.id.bt_title);
        tvSelect = findViewById(R.id.tv_select);
        llCount = findViewById(R.id.ll_count);
        tvCurrent = findViewById(R.id.tv_current);
        tvMax = findViewById(R.id.tv_max);
        tvSlash = findViewById(R.id.tv_slash);
        rvImage = findViewById(R.id.rv_image);
        rvFolder = findViewById(R.id.rv_Folder);
        goback = findViewById(R.id.goBack);
    }

    private void setupTextAndColor() {
        tvSelect.setText(config.getToolbarSelectText());
        tbImage.setTitle(config.getToolbarTitle());
        tvCurrent.setText("0");
        tvMax.setText(String.valueOf(config.getMaxImageCount()));
        bt_Image.setBackgroundColor(config.getToolbarBackgroundColor());
        bt_Image.setTextColor(getResources().getColor(R.color.control_background));
        tbImage.setBackgroundResource(config.getToolbarBackgroundColor());
        tbImage.setTitleTextColor(config.getToolbarTextColor());
        tvSelect.setTextColor(getColorFromId(config.getToolbarTextColor()));
        tvCurrent.setTextColor(getColorFromId(config.getToolbarTextColor()));
        tvMax.setTextColor(getColorFromId(config.getToolbarTextColor()));
        tvSlash.setTextColor(getColorFromId(config.getToolbarTextColor()));
    }

    private void setupSingleMode() {
        boolean isSingle = config.getMaxImageCount() == 1;

        if (isSingle) {
            llCount.setVisibility(View.INVISIBLE);
        } else {
            llCount.setVisibility(View.VISIBLE);
        }
    }

    private void setupRecyclerView() {
        GridLayoutManager layoutManager = (GridLayoutManager) rvImage.getLayoutManager();
        if (isPortrait() && layoutManager != null) {
            layoutManager.setSpanCount(config.getPortraitSpan());
        } else if (layoutManager != null) {
            layoutManager.setSpanCount(config.getLandscapeSpan());
        }
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext());

        adapter = new ImageListAdapter(config.getMaxImageCount(), (selectedCount) -> {
            tvCurrent.setText(String.valueOf(selectedCount));
        },ImagePickActivity.this);
        rvImage.setAdapter(adapter);

        View redLayout = findViewById(R.id.theCardView);
        ViewGroup parent = findViewById(R.id.parent);
        try {
            folderListAdapter = new FolderListAdapter(ImagePickActivity.this, PhotosInterface.AlbumPhotos.get(0), adapter, redLayout, parent);
            rvFolder.setLayoutManager(layoutManager1);
            rvFolder.setAdapter(folderListAdapter);
        }
        catch (Exception e){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ImagePickActivity.this,"Unable to fetch directories",Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (PermissionUtil.isReadExternalStorageAllow(this))
            adapter.setImages(PhotosInterface.AllPhotos.get(0));
    }

    private void setupSelect() {

        tvSelect.setOnClickListener(v -> {
            new ConvertTask().execute();
        });
    }

    public String getPathFromURI(ImagePickActivity activity, Uri contentUri) {
        OutputStream out;
        File file = new File(getFilename(activity));

        try {
            if (file.createNewFile()) {
                InputStream iStream = activity != null ? activity.getContentResolver().openInputStream(contentUri) : activity.getContentResolver().openInputStream(contentUri);
                byte[] inputData = getBytes(iStream);
                out = new FileOutputStream(file);
                out.write(inputData);
                out.close();
                return file.getAbsolutePath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private String getFilename(Context context) {
        File mediaStorageDir = context.getExternalFilesDir("");

        String mImageName = "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        return mediaStorageDir.getAbsolutePath() + "/" + mImageName;
    }

    private int getColorFromId(int colorId) {
        return ContextCompat.getColor(this, colorId);
    }

    private boolean isPortrait() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public void toggleFloatButtons(boolean show){
        View redLayout = findViewById(R.id.theCardView);
        ViewGroup parent = findViewById(R.id.parent_layout);
        Transition transition = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            transition = new Slide(Gravity.BOTTOM);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (transition != null) {
                transition.setDuration(600);
                transition.addTarget(R.id.theCardView);
                TransitionManager.beginDelayedTransition(parent, transition);
            }
        }
        redLayout.setVisibility(show ? View.VISIBLE : View.GONE);
        if(show){
            rvImage.setAlpha(0.1f);
        }
        else{
            rvImage.setAlpha(1f);
        }
    }

    @Override
    public void onBackPressed() {
        if (findViewById(R.id.theCardView).getVisibility() == View.VISIBLE){
            toggleFloatButtons(false);
        }
        else {
            super.onBackPressed();
        }
    }

    public class ConvertTask extends AsyncTask<Void,String,List<String>>{

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ImagePickActivity.this);
            dialog.setProgressStyle(ProgressDialog. STYLE_HORIZONTAL);
            dialog.setMessage("Enhancing Images...");
            dialog.show();
        }

        @Override
        protected List<String> doInBackground(Void... voids) {
            ArrayList<String> realPaths = new ArrayList<>();

            if (adapter.getSelectedUris().size() == 0) return null;
            List<String> uris = new ArrayList<>();
            dialog.setMax(adapter.getSelectedUris().size());
            int i=1;
            for (Uri uri : adapter.getSelectedUris()) {
                String path = getPathFromURI(ImagePickActivity.this,uri);
                uris.add(path);
                realPaths.add(ImageUtil.getRealPathFromUri(ImagePickActivity.this, uri));
                publishProgress(i+"");
                ++i;
            }
            return uris;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            dialog.setProgress(Integer.parseInt(values[0]));
        }



        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);
            if (strings == null)finish();
            if (listener != null) {
                listener.onSelected(strings);
            } else {
                Intent intent = new Intent();
                intent.putStringArrayListExtra("result", (ArrayList<String>) strings);
                setResult(RESULT_OK, intent);
            }
            finish();
            dialog.dismiss();
        }
    }
}
