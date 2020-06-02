package com.lawnics.printingpartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.lawnics.printingpartner.Adapters.PrintPaperAdapter;
import com.lawnics.printingpartner.Model.DetailActivityModel;
import com.lawnics.printingpartner.Model.Print_Paper_Model;

import java.util.ArrayList;
import java.util.List;

public class Print_Paper extends AppCompatActivity {

    private Toolbar toolbar;

    private RecyclerView print_paper_rv;
    DatabaseReference databaseReference;
    PrintPaperAdapter printPaperAdapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private Context mContext;
    private TextView name, copies, paper_size, orientation, print_side, print_color, paper_color;
    private SwipeRefreshLayout swipe;

    List<Print_Paper_Model> printPaperModelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print__paper);

        toolbar = findViewById(R.id.toolbar_print_paper);
        setSupportActionBar(toolbar);

        printPaperModelList = new ArrayList<>();
        print_paper_rv = (RecyclerView) findViewById(R.id.rv_details_activity);

        name = (TextView) findViewById(R.id.tv_header_print_paper_activity);
        copies = (TextView) findViewById(R.id.tv_credits_per_page_val_detail_act);
        paper_size = (TextView) findViewById(R.id.tv_total_ant_val_detail_act);

        orientation = (TextView) findViewById(R.id.tv_total_ant_val_detail_act);
        print_side = (TextView) findViewById(R.id.tv_total_ant_val_detail_act);
        print_color = (TextView) findViewById(R.id.tv_total_ant_val_detail_act);
        paper_color = (TextView) findViewById(R.id.tv_total_ant_val_detail_act);

        swipe = (SwipeRefreshLayout) findViewById(R.id.btn_accept_details);
     //   decline_detail = (AppCompatButton) findViewById(R.id.btn_decline_details);


        printPaperAdapter = new PrintPaperAdapter(Print_Paper.this, printPaperModelList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Print_Paper.this);

        print_paper_rv.setLayoutManager(layoutManager);
        print_paper_rv.setAdapter(printPaperAdapter);

//        accept_detail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(Print_Paper.this, ManagementActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        decline_detail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Print_Paper.this, ManagementActivity.class);
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

                                    DetailActivityModel recentOrdModel = new DetailActivityModel(customer_name[0]);
                                    for (DataSnapshot date : customerID.getChildren()) {
                                        for (DataSnapshot time : date.getChildren()) {
                                            recentOrdModel.setTime(time.getKey());
                                            for (DataSnapshot fileName : time.getChildren()) {

                                                for (DataSnapshot attributes : fileName.getChildren()) {
                                                    if (attributes.getKey().equals("credits")) {
                                                        recentOrdModel.setCredits(attributes.getValue().toString());
                                                    }
                                                    if (attributes.getKey().equals("credits")) {
                                                        recentOrdModel.setPaper_size(attributes.getValue().toString());
                                                    }
                                                    if (attributes.getKey().equals("credits")) {
                                                        recentOrdModel.setOrd_no(attributes.getValue().toString());
                                                    }
                                                    if (attributes.getKey().equals("credits")) {
                                                        recentOrdModel.setGSM(attributes.getValue().toString());
                                                    }
                                                    if (attributes.getKey().equals("image")) {
                                                        recentOrdModel.setDoc_image(attributes.getValue().toString());
                                                    }
                                                    if (attributes.getKey().equals("credits")) {
                                                        recentOrdModel.setNo_of_docs(attributes.getValue().toString());
                                                    }
                                                    if (attributes.getKey().equals("credits")) {
                                                        recentOrdModel.setNo_of_pages(attributes.getValue().toString());
                                                    }
                                                    if (attributes.getKey().equals("paper_color")) {
                                                        recentOrdModel.setPaper_color(attributes.getValue().toString());
                                                    }
                                                    if (attributes.getKey().equals("status")) {
                                                        recentOrdModel.setOrientation(attributes.getValue().toString());
                                                    }

                                                }
                                               // printPaperModelList.add(recentOrdModel);
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
}
