package com.lawnics.printingpartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lawnics.printingpartner.Adapters.DetailsActivityAdapter;
import com.lawnics.printingpartner.Adapters.PrevOrdersFragAdapter;
import com.lawnics.printingpartner.Adapters.RecentOrdFragAdapter;
import com.lawnics.printingpartner.Model.DetailActivityModel;
import com.lawnics.printingpartner.Model.PrevOrdersModel;

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

public class DetailsActivity extends AppCompatActivity {
    private RecyclerView details_rv;
    DatabaseReference databaseReference;
    private Context mContext;
    DetailsActivityAdapter detailsActivityAdapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    List<DetailActivityModel> detailActivityModelList;
    String filename, filepath;

    private Toolbar toolbar;
    private TextView no_of_pages, credits, total_amt;
    private AppCompatButton accept_detail, decline_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        toolbar = findViewById(R.id.toolbar_act_details);
        setSupportActionBar(toolbar);

        detailActivityModelList = new ArrayList<>();
        details_rv = (RecyclerView) findViewById(R.id.rv_details_activity);

        no_of_pages = (TextView) findViewById(R.id.tv_no_of_pages_val_detail_act);
        credits = (TextView) findViewById(R.id.tv_credits_per_page_val_detail_act);
        total_amt = (TextView) findViewById(R.id.tv_total_ant_val_detail_act);

        accept_detail = (AppCompatButton) findViewById(R.id.btn_accept_details);
        decline_detail = (AppCompatButton) findViewById(R.id.btn_decline_details);


        detailsActivityAdapter = new DetailsActivityAdapter(DetailsActivity.this, detailActivityModelList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DetailsActivity.this);

        details_rv.setLayoutManager(layoutManager);
        details_rv.setAdapter(detailsActivityAdapter);

        accept_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(DetailsActivity.this, ManagementActivity.class);
//                startActivity(intent);
                new RetrievePDFStream(filename).execute(filepath);
            }
        });

        decline_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this, ManagementActivity.class);
                startActivity(intent);

            }
        });


        databaseReference = FirebaseDatabase.getInstance().getReference("Details");
      //  databaseReference.child("abcd").setValue("1234");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot customerID : dataSnapshot.getChildren()) {
                    final String[] customer_name = {""};
                    DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Users/" + customerID.getKey());
                    dr.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot name : dataSnapshot.getChildren()) {
                                if (name.getKey().equals("first_name")) {
                                    Log.i("cus", name.getValue().toString());
                                    customer_name[0] += name.getValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    DetailActivityModel recentOrdModel = new DetailActivityModel(customer_name[0]);
                    for (DataSnapshot date : customerID.getChildren()) {
                        for (DataSnapshot time : date.getChildren()) {
                            recentOrdModel.setTime(time.getKey());
                            for (DataSnapshot fileName : time.getChildren()) {
                                filename = fileName.getKey();
                                recentOrdModel.setFileName(fileName.getKey());
                                for (DataSnapshot attributes : fileName.getChildren()) {
                                    if (attributes.getKey().equals("credits")) {
                                        recentOrdModel.setCredits(attributes.getValue().toString());
                                        credits.setText(attributes.getValue().toString());
                                    }
                                    if (attributes.getKey().equals("paper_size")) {
                                        recentOrdModel.setPaper_size(attributes.getValue().toString());
                                    }
                                    if (attributes.getKey().equals("order_no")) {
                                        recentOrdModel.setOrd_no(attributes.getValue().toString());
                                    }
                                    if (attributes.getKey().equals("gsm")) {
                                        recentOrdModel.setGSM(attributes.getValue().toString());
                                    }
                                    if (attributes.getKey().equals("image")) {
                                        recentOrdModel.setDoc_image(attributes.getValue().toString());
                                    }
                                    if (attributes.getKey().equals("no_of_docs")) {
                                        recentOrdModel.setNo_of_docs(attributes.getValue().toString());
                                    }
                                    if (attributes.getKey().equals("no_of_pages")) {
                                        recentOrdModel.setNo_of_pages(attributes.getValue().toString());
                                        no_of_pages.setText(attributes.getValue().toString());
                                    }
                                    if (attributes.getKey().equals("paper_color")) {
                                        recentOrdModel.setPaper_color(attributes.getValue().toString());
                                    }
                                    if (attributes.getKey().equals("orientation")) {
                                        recentOrdModel.setOrientation(attributes.getValue().toString());
                                    }

                                    if (attributes.getKey().equals("file")) {
                                        filepath = (attributes.getValue().toString());
                                        recentOrdModel.setFilePath(attributes.getValue().toString());
                                    }

                                }
                                detailActivityModelList.add(recentOrdModel);
                            }
                        }
                    }
                }
                                detailsActivityAdapter.notifyItemInserted(detailActivityModelList.size() - 1);
            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }


                        });

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
            Intent intent = new Intent(DetailsActivity.this, Print_Paper.class);
            intent.putStringArrayListExtra("images", (ArrayList<String>) strings);
             startActivity(intent);
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
