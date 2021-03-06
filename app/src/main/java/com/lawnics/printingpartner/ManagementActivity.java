package com.lawnics.printingpartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import java.util.Map;

public class ManagementActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    private Context mContext;
    Management_Adapter management_adapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<ManagementModel> managementModelList1;

    private Toolbar toolbar;
    private ImageView back_btn;
    private RecyclerView rv_management;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);


        managementModelList1 = new ArrayList<>();

        rv_management = (RecyclerView) findViewById(R.id.rv_management);

        back_btn = (ImageView) findViewById(R.id.back_btn_act_management);
//        total_amt = (TextView) findViewById(R.id.tv_total_ant_val_detail_act);
//
//        accept_detail = (AppCompatButton) findViewById(R.id.btn_accept_details);
//        decline_detail = (AppCompatButton) findViewById(R.id.btn_decline_details);

        management_adapter = new Management_Adapter(ManagementActivity.this, managementModelList1);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ManagementActivity.this);

        rv_management.setLayoutManager(layoutManager);
        rv_management.setAdapter(management_adapter);


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(ManagementActivity.this, OrderActivity.class);
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
                                    if (attributes.getKey().equals("paper_type")) {
                                        recentOrdModel.setPaper_type(attributes.getValue().toString());
                                    }
//                                                    if (attributes.getKey().equals("status")) {
//                                                        recentOrdModel.setOrientation(attributes.getValue().toString());
//                                                    }

                                }
                                managementModelList.add(recentOrdModel);
                            }
                        }
                    }
                }
                                            management_adapter.notifyItemInserted(managementModelList.size() - 1);
                                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/
        /*ManagementModel recentOrdModel = new ManagementModel();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Management").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot date:dataSnapshot.getChildren()){
                    for(DataSnapshot time:date.getChildren()){
                        for(DataSnapshot paper_type:time.getChildren()){
                            recentOrdModel.setPaper_type(String.valueOf(paper_type.getKey()));
                            recentOrdModel.setDoc_img(String.valueOf(paper_type.child("doc_image").getValue()));
                        }
                        managementModelList.add(recentOrdModel);
                    }
                }
                management_adapter = new Management_Adapter(ManagementActivity.this, managementModelList);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ManagementActivity.this);

                rv_management.setLayoutManager(layoutManager);
                rv_management.setAdapter(management_adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        /*SharedPreferences sharedPreferences = getSharedPreferences("Management",MODE_PRIVATE);
        Map<String,?> entries= sharedPreferences.getAll();
        List<String> papertype = new ArrayList<>();
        for(Map.Entry<String,?> entry : entries.entrySet()){
            String entireString = entry.getValue().toString();
            String[] paper_type = entireString.split(":",2);
            String[] gsm = paper_type[1].split(":a4:",2);
            String[] a4 = gsm[1].split(":legal:",2);
            //databaseReference.child(user.getUid()).child("Management").child(paper_type[0]).child(gsm[0]).child("a4").setValue(a4[0]);
            //databaseReference.child(user.getUid()).child("Management").child(paper_type[0]).child(gsm[0]).child("legal").setValue(a4[1]);
            ManagementModel managementModel = new ManagementModel();
            if(!papertype.contains(paper_type[0])){
                managementModel.setPaper_type(paper_type[0]);
                int n = managementModelList1.size();
                managementModelList1.add(n,managementModel);
                papertype.add(paper_type[0]);
            }

        }*/
        List<String> papertypelist = new ArrayList<>();
        SharedPreferences papertype = getSharedPreferences("paper_type",MODE_PRIVATE);
        Map<String,?> paper_type = papertype.getAll();
        for(Map.Entry<String,?> paperType:paper_type.entrySet()){
            ManagementModel managementModel = new ManagementModel();
            if(!papertypelist.contains(paperType.getValue().toString())){
                managementModel.setPaper_type(paperType.getValue().toString());
                managementModelList1.add(managementModel);
                papertypelist.add(paperType.getValue().toString());
            }
        }

    }


}

