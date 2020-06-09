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
import com.lawnics.printingpartner.Adapters.PaperSize_white_Adapter;
import com.lawnics.printingpartner.Adapters.Papersize_color_Adapter;
import com.lawnics.printingpartner.Model.DetailActivityModel;
import com.lawnics.printingpartner.Model.PaperSize_col_Model;
import com.lawnics.printingpartner.Model.PaperSize_wh_Model;

import java.util.ArrayList;
import java.util.List;

public class PaperSizeAct_color extends AppCompatActivity {

    DatabaseReference databaseReference;
    private Context mContext;
    Papersize_color_Adapter paperSize_color_adapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    List<PaperSize_col_Model> paperSizeColModelList;

    private Toolbar toolbar;
    private ImageView back_btn;
    private RecyclerView rv_paper_size_col;

    private TextView no_of_pages, credits;
    private AppCompatButton submit_paper_size_col;
    private CheckBox selectAll_paper_Size_col;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_size_act_color);

        toolbar = findViewById(R.id.toolbar_paper_size_color);
        setSupportActionBar(toolbar);


        paperSizeColModelList = new ArrayList<>();
        rv_paper_size_col = (RecyclerView) findViewById(R.id.rv_paper_size_activity_color);

        back_btn = (ImageView) findViewById(R.id.back_btn_act_paper_size_color);
       // credits = (TextView) findViewById(R.id.tv_credits_per_page_val_detail_act);
        submit_paper_size_col = (AppCompatButton) findViewById(R.id.btn_submit_paper_size_col);

        selectAll_paper_Size_col = (CheckBox) findViewById(R.id.chk_box_paper_size_col);

        paperSize_color_adapter = new Papersize_color_Adapter(PaperSizeAct_color.this, paperSizeColModelList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PaperSizeAct_color.this);

        rv_paper_size_col.setLayoutManager(layoutManager);
        rv_paper_size_col.setAdapter(paperSize_color_adapter);

        submit_paper_size_col.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PaperSizeAct_color.this, PaperQualityActivity_color.class);
                startActivity(intent);
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PaperSizeAct_color.this, PaperQualityActivity_color.class);
                startActivity(intent);
                finish();
            }
        });



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
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }


                    });

                    PaperSize_col_Model recentOrdModel = new PaperSize_col_Model(customer_name[0]);
                    for (DataSnapshot date : customerID.getChildren()) {
                        for (DataSnapshot time : date.getChildren()) {
                            //     recentOrdModel.setTime(time.getKey());
                            for (DataSnapshot fileName : time.getChildren()) {

                                for (DataSnapshot attributes : fileName.getChildren()) {
//                                                    if (attributes.getKey().equals("credits")) {
//                                                        recentOrdModel.setCredits(attributes.getValue().toString());
//                                                    }
//                                                    if (attributes.getKey().equals("file")) {
//                                                        recentOrdModel.setOrd_no(attributes.getValue().toString());
//                                                    }
//                                                    if (attributes.getKey().equals("gsm")) {
//                                                        recentOrdModel.setGSM(attributes.getValue().toString());
//                                                    }
                                    if (attributes.getKey().equals("image")) {
                                        recentOrdModel.setDoc_img(attributes.getValue().toString());
                                    }
//                                                    if (attributes.getKey().equals("pages")) {
//                                                        recentOrdModel.setNo_of_pages(attributes.getValue().toString());
//                                                    }
                                    if (attributes.getKey().equals("paper_color")) {
                                        recentOrdModel.setPaperType(attributes.getValue().toString());
                                    }
                                    if (attributes.getKey().equals("status")) {
                                        recentOrdModel.setDescriptions(attributes.getValue().toString());
                                    }

                                }
                                paperSizeColModelList.add(recentOrdModel);
                            }
                        }
                    }
                }
                                            paperSize_color_adapter.notifyItemInserted(paperSizeColModelList.size() - 1);
                                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

