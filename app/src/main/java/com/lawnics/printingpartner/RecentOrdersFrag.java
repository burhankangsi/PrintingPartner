package com.lawnics.printingpartner;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lawnics.printingpartner.Adapters.RecentOrdFragAdapter;
import com.lawnics.printingpartner.Model.PrevOrdersModel;
import com.lawnics.printingpartner.Model.RecentOrdModel;
import com.lawnics.printingpartner.R;
import com.lawnics.printingpartner.ui.gallery.GalleryViewModel;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RecentOrdersFrag extends Fragment {

    private RecyclerView recent_ord_rv;
    DatabaseReference databaseReference;
    private Context mContext;
    RecentOrdFragAdapter recentOrdFragAdapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    List<RecentOrdModel> recentOrdModelList;
    boolean flag = false;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.frag_recent_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recentOrdModelList = new ArrayList<>();
        recent_ord_rv = (RecyclerView) view.findViewById(R.id.recentOrderrv);
        databaseReference = FirebaseDatabase.getInstance().getReference("Printing_partner");
      //  databaseReference.child("abcd").setValue("1234");
        /*databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot customerID : dataSnapshot.getChildren()) {

                    final String[] customer_name = {""};
                    DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Users/" + customerID.getKey());
                    dr.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot name : dataSnapshot.getChildren()) {
                                customer_name[0] = "";
                                if (name.getKey().equals("first_name")) {
                                    Log.i("cus", name.getValue().toString());
                                    customer_name[0] += name.getValue().toString();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    RecentOrdModel recentOrdModel = new RecentOrdModel(customer_name[0]);
                    for (DataSnapshot date : customerID.getChildren()) {
                        recentOrdModel.setDate_recent(date.getKey());
                        for (DataSnapshot time : date.getChildren()) {
                            recentOrdModel.setTime(time.getKey());
                            for (DataSnapshot fileName : time.getChildren()) {
                               recentOrdModel.setFilename(fileName.getKey());
                                //recentOrdModel.setFileName((fileName.getValue().toString()));
                                //   RecentOrdModel recentOrdModel = new RecentOrdModel(fileName.getKey());
                                for (DataSnapshot attributes : fileName.getChildren()) {
                                    if (attributes.getKey().equals("price")) {
                                        recentOrdModel.setItemPrice(attributes.getValue().toString());
                                    }
                                    if (attributes.getKey().equals("order_no")) {
                                        recentOrdModel.setOrd_no(attributes.getValue().toString());
                                    }

                                    if (attributes.getKey().equals("CustName")) {
                                        recentOrdModel.setFileName(attributes.getValue().toString());
                                    }
                                    // This one ok
                                    if (attributes.getKey().equals("location")) {
                                        recentOrdModel.setLocation(attributes.getValue().toString());
                                    }

                                    if (attributes.getKey().equals("image")) {
                                        recentOrdModel.setCust_image(attributes.getValue().toString());
                                    }
                                    if (attributes.getKey().equals("no_of_pages")) {
                                        recentOrdModel.setNo_pages(attributes.getValue().toString());
                                    }

                                    if (attributes.getKey().equals("no_of_docs")) {
                                        recentOrdModel.setNo_docs(attributes.getValue().toString());
                                    }
                                    if (attributes.getKey().equals("status")) {
                                        recentOrdModel.setStatus_time(attributes.getValue().toString());
                                    }
                                    if (attributes.getKey().equals("file")) {
                                        recentOrdModel.setFile_path(attributes.getValue().toString());
                                    }
                                }
                                recentOrdModelList.add(recentOrdModel);
                            }
                        }
                    }
                }
                                recentOrdFragAdapter.notifyItemInserted(recentOrdModelList.size()-1);
            }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/
        databaseReference = databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Orders");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot buyerId : dataSnapshot.getChildren()) {
                    DatabaseReference name = FirebaseDatabase.getInstance().getReference("Users").child(buyerId.getKey());
                    name.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String buyerName = null;
                            for (DataSnapshot b_name : dataSnapshot.getChildren()) {
                                if (b_name.getKey().equals("first_name")) {
                                    buyerName = b_name.getValue().toString();
                                }
                                if (b_name.getKey().equals("last_name")) {
                                    buyerName = buyerName + " " + b_name.getValue().toString();
                                }
                            }

                            for (DataSnapshot date : buyerId.getChildren()) {
                                for (DataSnapshot time : date.getChildren()) {
                                    RecentOrdModel recentOrdModel = new RecentOrdModel();
                                    for (DataSnapshot file_name : time.getChildren()) {
                                        try {
                                            flag = false;
                                            if (!file_name.child("status").getValue().equals("Completed") || file_name.child("status").getValue().equals("Rejected")) {
                                                flag = true;
                                                recentOrdModel.setCustName(buyerName);
                                                recentOrdModel.setOrd_no(file_name.getKey());
                                                recentOrdModel.setFile_path(file_name.child("file").getValue().toString());
                                                recentOrdModel.setLocation(file_name.child("area").getValue().toString());
                                                recentOrdModel.setNo_docs(String.valueOf(time.getChildrenCount()));
                                                recentOrdModel.setNo_pages(file_name.child("pages").getValue().toString());
                                                recentOrdModel.setItemPrice(file_name.child("credits").getValue().toString());
                                                recentOrdModel.setTime(time.getKey());
                                                recentOrdModel.setDate_recent(date.getKey());
                                                recentOrdModel.setCust_image(file_name.child("image").getValue().toString());
                                                recentOrdModel.setStatus(file_name.child("status").getValue().toString());
                                                recentOrdModel.setBuyerId(buyerId.getKey());
                                                recentOrdModel.setGsm(file_name.child("gsm").getValue().toString());
                                                recentOrdModel.setCopies(file_name.child("copies").getValue().toString());
                                                recentOrdModel.setOrientation(file_name.child("orientation").getValue().toString());
                                                recentOrdModel.setPaper_side(file_name.child("paper_side").getValue().toString());
                                                recentOrdModel.setPaper_color(file_name.child("paper_color").getValue().toString());
                                                recentOrdModel.setPrint_color(file_name.child("print_color").getValue().toString());
                                                recentOrdModel.setPaper_size(file_name.child("paper_size").getValue().toString());
                                            }
                                        } catch (Exception e) {}

                                    }if(flag){
                                        recentOrdModelList.add(recentOrdModel);
                                    }
                                }
                            }
                            recentOrdFragAdapter = new RecentOrdFragAdapter(getContext(), recentOrdModelList);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            layoutManager.setReverseLayout(true);
                            layoutManager.setStackFromEnd(true);
                            recent_ord_rv.setLayoutManager(layoutManager);
                            recent_ord_rv.setAdapter(recentOrdFragAdapter);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"On Cancelled",Toast.LENGTH_LONG).show();
            }
        });
    }
}






