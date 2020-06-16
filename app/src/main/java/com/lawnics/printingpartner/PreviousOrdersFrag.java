package com.lawnics.printingpartner;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lawnics.printingpartner.Adapters.PrevOrdersFragAdapter;
import com.lawnics.printingpartner.Adapters.RecentOrdFragAdapter;
import com.lawnics.printingpartner.Model.PrevOrdersModel;
import com.lawnics.printingpartner.Model.RecentOrdModel;
import com.lawnics.printingpartner.R;
import com.lawnics.printingpartner.ui.home.HomeViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreviousOrdersFrag extends Fragment {

    private RecyclerView prev_ord_rv;
    DatabaseReference databaseReference;
    private Context mContext;
    PrevOrdersFragAdapter prevOrdFragAdapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    List<PrevOrdersModel> prevOrdModelList;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_prev_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prevOrdModelList = new ArrayList<>();
        prev_ord_rv = view.findViewById(R.id.prevOrderrv);

        prevOrdFragAdapter = new PrevOrdersFragAdapter(getContext(),prevOrdModelList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        prev_ord_rv.setLayoutManager(layoutManager);
        prev_ord_rv.setAdapter(prevOrdFragAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Orders");
       // databaseReference.child("abcd").setValue("1234");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot customerID : dataSnapshot.getChildren()){
                    final String[] customer_name = {""};
                    DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Users/"+customerID.getKey());
                    dr.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot name : dataSnapshot.getChildren()){
                                if (name.getKey().equals("first_name"))
                                {
                                    customer_name[0] +=name.getValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    PrevOrdersModel recentOrdModel = new PrevOrdersModel(customer_name[0]);
                    for(DataSnapshot date : customerID.getChildren()){
                        for (DataSnapshot time :date.getChildren()){
                            recentOrdModel.setTime(time.getKey());
                            for (DataSnapshot fileName : time.getChildren()){

                                for (DataSnapshot attributes : fileName.getChildren()) {
                                    if (attributes.getKey().equals("price")){
                                        recentOrdModel.setItemPrice(attributes.getValue().toString());
                                    }
                                    if (attributes.getKey().equals("order_no")){
                                        recentOrdModel.setOrd_no(attributes.getValue().toString());
                                    }
                                    if (attributes.getKey().equals("CustName")){
                                        recentOrdModel.setFileName(attributes.getValue().toString());
                                    }
                                    if (attributes.getKey().equals("location")){
                                        recentOrdModel.setLocation(attributes.getValue().toString());
                                    }
                                    if (attributes.getKey().equals("image")){
                                        recentOrdModel.setCust_image(attributes.getValue().toString());
                                    }
                                    if (attributes.getKey().equals("no_of_pages")){
                                        recentOrdModel.setNo_pages(attributes.getValue().toString());
                                    }
                                    if (attributes.getKey().equals("no_of_docs")){
                                        recentOrdModel.setNo_docs(attributes.getValue().toString());
                                    }
                                    if (attributes.getKey().equals("status")){
                                        recentOrdModel.setStatus(attributes.getValue().toString());
                                    }

                                }
                                prevOrdModelList.add(recentOrdModel);
                            }
                        }
                    }
                }
                prevOrdFragAdapter.notifyItemInserted(prevOrdModelList.size()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
