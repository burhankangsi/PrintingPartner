package com.lawnics.printingpartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.lawnics.printingpartner.Model.DetailActivityModel;
import com.lawnics.printingpartner.Model.PrevOrdersModel;

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

    private TextView no_of_pages, credits, total_amt;
    private AppCompatButton accept_detail, decline_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

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

                Intent intent = new Intent(DetailsActivity.this, ManagementActivity.class);
                startActivity(intent);
            }
        });

        decline_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this, ManagementActivity.class);
                startActivity(intent);

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

                                    DetailActivityModel recentOrdModel = new DetailActivityModel(customer_name[0]);
                                    for (DataSnapshot date : customerID.getChildren()) {
                                        for (DataSnapshot time : date.getChildren()) {
                                            recentOrdModel.setTime(time.getKey());
                                            for (DataSnapshot fileName : time.getChildren()) {

                                                for (DataSnapshot attributes : fileName.getChildren()) {
                                                    if (attributes.getKey().equals("credits")) {
                                                        recentOrdModel.setCredits(attributes.getValue().toString());
                                                    }
                                                    if (attributes.getKey().equals("file")) {
                                                        recentOrdModel.setOrd_no(attributes.getValue().toString());
                                                    }
                                                    if (attributes.getKey().equals("gsm")) {
                                                        recentOrdModel.setGSM(attributes.getValue().toString());
                                                    }
                                                    if (attributes.getKey().equals("image")) {
                                                        recentOrdModel.setDoc_image(attributes.getValue().toString());
                                                    }
                                                    if (attributes.getKey().equals("pages")) {
                                                        recentOrdModel.setNo_of_pages(attributes.getValue().toString());
                                                    }
                                                    if (attributes.getKey().equals("paper_color")) {
                                                        recentOrdModel.setPaper_color(attributes.getValue().toString());
                                                    }
                                                    if (attributes.getKey().equals("status")) {
                                                        recentOrdModel.setOrientation(attributes.getValue().toString());
                                                    }

                                                }
                                                detailActivityModelList.add(recentOrdModel);
                                            }
                                            detailsActivityAdapter.notifyItemInserted(detailActivityModelList.size() - 1);
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
