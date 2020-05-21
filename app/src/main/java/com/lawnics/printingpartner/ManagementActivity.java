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
import com.lawnics.printingpartner.Adapters.Management_Adapter;
import com.lawnics.printingpartner.Adapters.Paper_quality_col_adapter;
import com.lawnics.printingpartner.Model.DetailActivityModel;
import com.lawnics.printingpartner.Model.ManagementModel;
import com.lawnics.printingpartner.Model.PaperQual_col_Model;

import java.util.ArrayList;
import java.util.List;

public class ManagementActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    private Context mContext;
    Management_Adapter management_adapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    List<ManagementModel> managementModelList;

    private Toolbar toolbar;
    private ImageView back_btn;
    private RecyclerView rv_management;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);

        toolbar = findViewById(R.id.toolbar_management);
        setSupportActionBar(toolbar);

        managementModelList = new ArrayList<>();
        rv_management = (RecyclerView) findViewById(R.id.rv_management);

        back_btn = (ImageView) findViewById(R.id.back_btn_act_management);
//        total_amt = (TextView) findViewById(R.id.tv_total_ant_val_detail_act);
//
//        accept_detail = (AppCompatButton) findViewById(R.id.btn_accept_details);
//        decline_detail = (AppCompatButton) findViewById(R.id.btn_decline_details);


        management_adapter = new Management_Adapter(ManagementActivity.this, managementModelList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ManagementActivity.this);

        rv_management.setLayoutManager(layoutManager);
        rv_management.setAdapter(management_adapter);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(ManagementActivity.this, DetailsActivity.class);
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

                                    ManagementModel recentOrdModel = new ManagementModel(customer_name[0]);
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
                                                        recentOrdModel.setPaper_type(attributes.getValue().toString());
                                                    }
//                                                    if (attributes.getKey().equals("status")) {
//                                                        recentOrdModel.setOrientation(attributes.getValue().toString());
//                                                    }

                                                }
                                                managementModelList.add(recentOrdModel);
                                            }
                                            management_adapter.notifyItemInserted(managementModelList.size() - 1);
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
