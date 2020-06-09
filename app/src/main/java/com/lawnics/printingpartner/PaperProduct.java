package com.lawnics.printingpartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.lawnics.printingpartner.Adapters.DetailsActivityAdapter;
import com.lawnics.printingpartner.Adapters.PaperProductAdapter;
import com.lawnics.printingpartner.Model.DetailActivityModel;
import com.lawnics.printingpartner.Model.PaperProductModel;

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

public class PaperProduct extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView cust_name, no_of_pages;
    public ImageView paper_img;
    private AppCompatButton btn_accept, btn_decline;

    private ViewPager2 paper_prod_vp;
    DatabaseReference databaseReference;
    PaperProductAdapter paperProductAdapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private ProgressDialog progressDialog;

    private Context mContext;

    List<PaperProductModel> paperProductModelList;
    List<String> urls = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_product);

        toolbar = findViewById(R.id.toolbar_paper_product);
        setSupportActionBar(toolbar);

        paper_prod_vp = (ViewPager2) findViewById(R.id.viewpager_paper_product);
        paperProductModelList = new ArrayList<>();
        // details_rv = (RecyclerView) findViewById(R.id.rv_details_activity);

        cust_name = (TextView) findViewById(R.id.tv_cust_name_paper_prod_activity);
        no_of_pages = (TextView) findViewById(R.id.tv_page_val_paper_prod_activity);

        btn_accept = (AppCompatButton) findViewById(R.id.btn_accept_paper_prod);
        btn_decline = (AppCompatButton) findViewById(R.id.btn_decline_paper_prod);

        paperProductAdapter = new PaperProductAdapter(PaperProduct.this, paperProductModelList);
        List<String> images = getIntent().getStringArrayListExtra("images");
        for (String path : images){
            System.out.println(path);
            paperProductModelList.add(new PaperProductModel(path));
        }

        paper_prod_vp.setAdapter(paperProductAdapter);
        //new LoadImagesTask().execute();
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Management activity will come in navigation drawer not here
                //Intent intent = new Intent(PaperProduct.this, ManagementActivity.class);
                //startActivity(intent);
            }
        });

        btn_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(PaperProduct.this, ManagementActivity.class);
                //startActivity(intent);

            }
        });

//        paper_prod_vp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//
//                if(position == paperProductAdapter.getItemCount()-1)
//                {
//
//                }
//            }
//        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Printing_partner/Orders");
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
                                    cust_name.setText(customer_name[0]);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }


                    });
                    PaperProductModel recentOrdModel = new PaperProductModel(customer_name[0]);
                    for (DataSnapshot date : customerID.getChildren()) {
                        for (DataSnapshot time : date.getChildren()) {
                            //   recentOrdModel.setTime(time.getKey());
                            for (DataSnapshot fileName : time.getChildren()) {

                                for (DataSnapshot attributes : fileName.getChildren()) {
                                    if (attributes.getKey().equals("file")) {
                                        //               recentOrdModel.setImages(attributes.getValue().toString());
//                                                        recentOrdModel = attributes.getValue(PaperProductModel.class);
//                                                        paperProductModelList.add(recentOrdModel);

                                        //  String filename = attributes.getKey();
                                        String url = attributes.getValue().toString();
                                        urls.add(url);
                                        new LoadImagesTask();
                                    }
                                    if (attributes.getKey().equals("pages")) {
                                        //recentOrdModel.setPaper_size(attributes.getValue().toString());
                                        no_of_pages.setText(attributes.getValue().toString());
                                    }
                                    if (attributes.getKey().equals("image")) {
                                        recentOrdModel.setImages(attributes.getValue().toString());
                                        //no_of_pages.setText(attributes.getValue().toString());
                                    }
//
                                }
                                paperProductModelList.add(recentOrdModel);
                            }
                        }
                    }
                }
                                            paperProductAdapter.notifyItemInserted(paperProductModelList.size() - 1);
            }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }



    public class LoadImagesTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            paperProductAdapter.notifyDataSetChanged();
        }
    }

}


