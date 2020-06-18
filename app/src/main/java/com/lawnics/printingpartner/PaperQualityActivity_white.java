package com.lawnics.printingpartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
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
import com.lawnics.printingpartner.Adapters.Paper_quality_col_adapter;
import com.lawnics.printingpartner.Adapters.Paper_quality_wh_adapter;
import com.lawnics.printingpartner.Adapters.Papersize_color_Adapter;
import com.lawnics.printingpartner.Model.DetailActivityModel;
import com.lawnics.printingpartner.Model.PaperQual_col_Model;
import com.lawnics.printingpartner.Model.PaperQual_wh_Model;
import com.lawnics.printingpartner.Model.PaperSize_col_Model;

import java.util.ArrayList;
import java.util.List;

public class PaperQualityActivity_white extends AppCompatActivity {

    DatabaseReference databaseReference;
    private Context mContext;
    Paper_quality_wh_adapter paper_quality_wh_adapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    List<PaperQual_wh_Model> paperQualWhModelList;

    private Toolbar toolbar;
    private ImageView back_btn;
    private RecyclerView rv_paper_qual_wh;

//    private TextView no_of_pages, credits;
//    private AppCompatButton submit_paper_size_col;
//    private CheckBox selectAll_paper_Size_col;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_quality_white);

        toolbar = findViewById(R.id.toolbar_paper_qual);
        setSupportActionBar(toolbar);


        paperQualWhModelList = new ArrayList<>();
        rv_paper_qual_wh = (RecyclerView) findViewById(R.id.rv_paper_qual_activity);

        back_btn = (ImageView) findViewById(R.id.back_btn_act_paper_qual);
//        credits = (TextView) findViewById(R.id.tv_credits_per_page_val_detail_act);
//        total_amt = (TextView) findViewById(R.id.tv_total_ant_val_detail_act);
//
//        accept_detail = (AppCompatButton) findViewById(R.id.btn_accept_details);
//        decline_detail = (AppCompatButton) findViewById(R.id.btn_decline_details);


        paper_quality_wh_adapter = new Paper_quality_wh_adapter(PaperQualityActivity_white.this, paperQualWhModelList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PaperQualityActivity_white.this);

        rv_paper_qual_wh.setLayoutManager(layoutManager);
        rv_paper_qual_wh.setAdapter(paper_quality_wh_adapter);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PaperQualityActivity_white.this, ManagementActivity.class);
                startActivity(intent);
                finish();
            }
        });

//        decline_detail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(DetailsActivity.this, PaperProduct.class);
//                startActivity(intent);
//
//            }
//        });


        /*databaseReference = FirebaseDatabase.getInstance().getReference("Management");
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

                    PaperQual_wh_Model recentOrdModel = new PaperQual_wh_Model(customer_name[0]);
                    for (DataSnapshot date : customerID.getChildren()) {
                        for (DataSnapshot time : date.getChildren()) {
                            //    recentOrdModel.setTime(time.getKey());
                            for (DataSnapshot fileName : time.getChildren()) {

                                for (DataSnapshot attributes : fileName.getChildren()) {
//                                                    if (attributes.getKey().equals("credits")) {
//                                                        recentOrdModel.setCredits(attributes.getValue().toString());
//                                                    }
//                                                    if (attributes.getKey().equals("file")) {
//                                                        recentOrdModel.setOrd_no(attributes.getValue().toString());
//                                                    }
                                    if (attributes.getKey().equals("gsm")) {
                                        recentOrdModel.setGsm(attributes.getValue().toString());
                                    }
                                    if (attributes.getKey().equals("image")) {
                                        recentOrdModel.setDoc_img(attributes.getValue().toString());
                                    }
//                                                    if (attributes.getKey().equals("pages")) {
//                                                        recentOrdModel.setNo_of_pages(attributes.getValue().toString());
//                                                    }
                                    if (attributes.getKey().equals("description")) {
                                        recentOrdModel.setDescription(attributes.getValue().toString());
                                    }
//                                                    if (attributes.getKey().equals("status")) {
//                                                        recentOrdModel.setOrientation(attributes.getValue().toString());
//                                                    }

                                }
                                paperQualWhModelList.add(recentOrdModel);
                            }
                        }
                    }
                }
                                paper_quality_wh_adapter.notifyItemInserted(paperQualWhModelList.size() - 1);
                                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/
        PaperQual_wh_Model recentOrdModel = new PaperQual_wh_Model();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Management").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot date:dataSnapshot.getChildren()){
                    for(DataSnapshot time:date.getChildren()){
                        for(DataSnapshot paper_type:time.getChildren()){
                            recentOrdModel.setGsm(String.valueOf(paper_type.child("gsm").getValue()));
                            recentOrdModel.setDoc_img(String.valueOf(paper_type.child("doc_image").getValue()));
                            recentOrdModel.setDescription(String.valueOf(paper_type.child("description").getValue()));
                        }
                        paperQualWhModelList.add(recentOrdModel);
                    }
                }
                paper_quality_wh_adapter = new Paper_quality_wh_adapter(PaperQualityActivity_white.this, paperQualWhModelList);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PaperQualityActivity_white.this);

                rv_paper_qual_wh.setLayoutManager(layoutManager);
                rv_paper_qual_wh.setAdapter(paper_quality_wh_adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
                }

            }

