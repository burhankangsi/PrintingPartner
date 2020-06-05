package com.lawnics.printingpartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lawnics.printingpartner.Adapters.DetailsActivityAdapter;
import com.lawnics.printingpartner.Adapters.PrintPaperAdapter;
import com.lawnics.printingpartner.Adapters.SectionsPagerAdapter;
import com.lawnics.printingpartner.Model.DetailActivityModel;
import com.lawnics.printingpartner.Model.PaperProductModel;
import com.lawnics.printingpartner.Model.Print_Paper_Model;

import java.util.ArrayList;
import java.util.List;

public class Print_Paper extends AppCompatActivity {

    private Toolbar toolbar;

    private ViewPager2 print_paper_vp;
    DatabaseReference databaseReference;
    PrintPaperAdapter printPaperAdapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private Context mContext;
    private TextView name, copies, paper_size, orientation, print_side, print_color, paper_color;
    private SwipeRefreshLayout swipe;

    List<Print_Paper_Model> printPaperModelList;
    List<String> urls = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print__paper);

        toolbar = findViewById(R.id.toolbar_print_paper);
        setSupportActionBar(toolbar);

        printPaperModelList = new ArrayList<>();
        print_paper_vp = (ViewPager2) findViewById(R.id.viewpager_print_paper);

        name = (TextView) findViewById(R.id.tv_header_print_paper_activity);
        copies = (TextView) findViewById(R.id.tv_print_paper_copies);
        paper_size = (TextView) findViewById(R.id.tv_print_paper_size);

        orientation = (TextView) findViewById(R.id.tv_print_paper_orientation);
        print_side = (TextView) findViewById(R.id.tv_print_paper_print_side);
        print_color = (TextView) findViewById(R.id.tv_print_paper_print_color);
        paper_color = (TextView) findViewById(R.id.tv_print_paper_paper_color);

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_print_paper);
     //   decline_detail = (AppCompatButton) findViewById(R.id.btn_decline_details);


        printPaperAdapter = new PrintPaperAdapter(Print_Paper.this, printPaperModelList);

        List<String> images = getIntent().getStringArrayListExtra("images");
        for (String path : images){
            System.out.println(path);
            printPaperModelList.add(new Print_Paper_Model(path));
        }
    //    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Print_Paper.this);
//
       // print_paper_vp.setLayoutManager(layoutManager);
        print_paper_vp.setAdapter(printPaperAdapter);



        databaseReference = FirebaseDatabase.getInstance().getReference("Printing_partner/abcdef/Orders");
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

                                    Print_Paper_Model recentOrdModel = new Print_Paper_Model(customer_name[0]);
                                    for (DataSnapshot date : customerID.getChildren()) {
                                        for (DataSnapshot time : date.getChildren()) {
                                           // recentOrdModel.setTime(time.getKey());
                                            for (DataSnapshot fileName : time.getChildren()) {

                                                for (DataSnapshot attributes : fileName.getChildren()) {
                                                    if (attributes.getKey().equals("file")) {
                                                      //  recentOrdModel.setCredits(attributes.getValue().toString());
                                                        String url = attributes.getValue().toString();
                                                        urls.add(url);
                                                        new LoadImagesTask();
                                                    }
                                                    // Copies
                                                    if (attributes.getKey().equals("credits")) {
                                                        copies.setText(attributes.getValue().toString());
                                                    }
                                                    // Size
                                                    if (attributes.getKey().equals("gsm")) {
                                                        paper_size.setText(attributes.getValue().toString());
                                                    }
                                                    // Print Side
                                                    if (attributes.getKey().equals("status")) {
                                                        print_side.setText(attributes.getValue().toString());
                                                    }

                                                    if (attributes.getKey().equals("paper_color")) {
                                                        paper_color.setText(attributes.getValue().toString());
                                                    }
                                                    if (attributes.getKey().equals("paper_color")) {
                                                        print_color.setText(attributes.getValue().toString());
                                                    }

                                                    if (attributes.getKey().equals("image")) {
                                                        recentOrdModel.setImages(attributes.getValue().toString());
                                                    }

                                                    //Orientation
                                                    if (attributes.getKey().equals("status")) {
                                                        orientation.setText(attributes.getValue().toString());
                                                    }

                                                }
                                                printPaperModelList.add(recentOrdModel);
                                            }
                                            printPaperAdapter.notifyItemInserted(printPaperModelList.size() - 1);
                                        }
                                    }
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }


        });
    }

    public class LoadImagesTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            printPaperAdapter.notifyDataSetChanged();
        }
    }
}
