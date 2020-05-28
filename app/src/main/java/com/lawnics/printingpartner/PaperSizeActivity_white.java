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
import com.lawnics.printingpartner.Model.DetailActivityModel;
import com.lawnics.printingpartner.Model.PaperSize_wh_Model;

import java.util.ArrayList;
import java.util.List;

public class PaperSizeActivity_white extends AppCompatActivity {

    DatabaseReference databaseReference;
    private Context mContext;
    PaperSize_white_Adapter paperSize_white_adapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    List<PaperSize_wh_Model> paperSizeWhModelList;

    private Toolbar toolbar;
    private ImageView back_btn;
    private RecyclerView rv_paper_size_wh;

    private TextView no_of_pages, credits;
    private AppCompatButton submit_paper_size_wh;
    private CheckBox selectAll_paper_Size_wh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_size_white);

        toolbar = findViewById(R.id.toolbar_paper_size);
        setSupportActionBar(toolbar);

        paperSizeWhModelList = new ArrayList<>();
        rv_paper_size_wh = (RecyclerView) findViewById(R.id.rv_paper_size_activity);

        back_btn = (ImageView) findViewById(R.id.back_btn_act_paper_size);
      //  credits = (TextView) findViewById(R.id.tv_credits_per_page_val_detail_act);
        submit_paper_size_wh = (AppCompatButton) findViewById(R.id.btn_submit_paper_size_wh);

        selectAll_paper_Size_wh = (CheckBox) findViewById(R.id.chk_box_paper_size_wh);
        //decline_detail = (AppCompatButton) findViewById(R.id.btn_decline_details);


        paperSize_white_adapter = new PaperSize_white_Adapter(PaperSizeActivity_white.this, paperSizeWhModelList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PaperSizeActivity_white.this);

        rv_paper_size_wh.setLayoutManager(layoutManager);
        rv_paper_size_wh.setAdapter(paperSize_white_adapter);

        submit_paper_size_wh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(PaperSizeActivity_white.this, ManagementActivity.class);
//////                startActivity(intent);
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PaperSizeActivity_white.this, PaperQualityActivity_white.class);
                startActivity(intent);
                finish();
            }
        });



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

                                    PaperSize_wh_Model recentOrdModel = new PaperSize_wh_Model(customer_name[0]);
                                    for (DataSnapshot date : customerID.getChildren()) {
                                        for (DataSnapshot time : date.getChildren()) {
                                     //       recentOrdModel.setTime(time.getKey());
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
                                                paperSizeWhModelList.add(recentOrdModel);
                                            }
                                            paperSize_white_adapter.notifyItemInserted(paperSizeWhModelList.size() - 1);
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
}
