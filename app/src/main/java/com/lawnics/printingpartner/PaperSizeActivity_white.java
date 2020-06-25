package com.lawnics.printingpartner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lawnics.printingpartner.Adapters.PaperSize_white_Adapter;
import com.lawnics.printingpartner.Interfaces.OnItemCheck;
import com.lawnics.printingpartner.Model.PaperQual_wh_Model;
import com.lawnics.printingpartner.Model.PaperSize_wh_Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PaperSizeActivity_white extends AppCompatActivity implements OnItemCheck {

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
    String papertypeStr;
    String gsm1;
    ArrayList<String> pos;
    ArrayList<String> posUncheck;
    boolean allSelected = false,flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_size_white);

        pos = new ArrayList<>();
        posUncheck = new ArrayList<>();
        paperSizeWhModelList = new ArrayList<>();
        rv_paper_size_wh = (RecyclerView) findViewById(R.id.rv_paper_size_activity);

        back_btn = (ImageView) findViewById(R.id.back_btn_act_paper_size);
      //  credits = (TextView) findViewById(R.id.tv_credits_per_page_val_detail_act);
        submit_paper_size_wh = (AppCompatButton) findViewById(R.id.btn_submit_paper_size_wh);

        selectAll_paper_Size_wh = (CheckBox) findViewById(R.id.chk_box_paper_size_wh);
        //decline_detail = (AppCompatButton) findViewById(R.id.btn_decline_details);


        paperSize_white_adapter = new PaperSize_white_Adapter(PaperSizeActivity_white.this, paperSizeWhModelList,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PaperSizeActivity_white.this);

        rv_paper_size_wh.setLayoutManager(layoutManager);
        rv_paper_size_wh.setAdapter(paperSize_white_adapter);
        papertypeStr = getIntent().getStringExtra("papertype");
        gsm1 = getIntent().getStringExtra("gsm");
        /*SharedPreferences sharedPreferences = getSharedPreferences("Management",MODE_PRIVATE);
        Map<String,?> entries= sharedPreferences.getAll();
        //List<String> papertype = new ArrayList<>();
        for(Map.Entry<String,?> entry : entries.entrySet()){
            String entireString = entry.getValue().toString();
            String[] paper_type = entireString.split(":",2);
            String[] gsm = paper_type[1].split(":a4:",2);
            String[] a4 = gsm[1].split(":legal:",2);
            String[] paper_size_a4 = paper_type[1].split(":",2);
            String paper_size_legal = paper_size_a4[1].split(":",2)[1].split(":",2)[1].split(":",2)[0];
            //databaseReference.child(user.getUid()).child("Management").child(paper_type[0]).child(gsm[0]).child("a4").setValue(a4[0]);
            //databaseReference.child(user.getUid()).child("Management").child(paper_type[0]).child(gsm[0]).child("legal").setValue(a4[1]);
            PaperSize_wh_Model paperSize_wh_model_a4 = new PaperSize_wh_Model();
            PaperSize_wh_Model paperSize_wh_model_legal = new PaperSize_wh_Model();
            if(papertype.equals(paper_type[0])){
                if(gsm1.equals(gsm[0])){
                    if(paper_size_a4[1].split(":",2)[0].equals("a4")){
                        paperSize_wh_model_a4.setPaperType("A4 Size Paper:"+a4[0]);
                        paperSizeWhModelList.add(paperSize_wh_model_a4);
                        if(paper_size_legal.equals("legal")){
                            paperSize_wh_model_legal.setPaperType("Legal Paper:"+a4[1]);
                            paperSizeWhModelList.add(paperSize_wh_model_legal);
                        }
                    }
                }

            }
        }*/
        List<String> paperSizeList = new ArrayList<>();
        SharedPreferences papertype = getSharedPreferences("paper_type",MODE_PRIVATE);
        SharedPreferences paper_gsm = getSharedPreferences("paper_gsm",MODE_PRIVATE);
        SharedPreferences paper_size = getSharedPreferences("paper_size",MODE_PRIVATE);
        Map<String,?> paper_type = papertype.getAll();
        Map<String,?> gsm = paper_gsm.getAll();
        Map<String,?> size = paper_size.getAll();
        for(Map.Entry<String,?> paperType:paper_type.entrySet()){
            if(paperType.getValue().equals(papertypeStr)){
                for(Map.Entry<String,?> paperGsm:gsm.entrySet()){
                    if(paperGsm.getValue().equals(gsm1)){
                        for(Map.Entry<String,?> paperSize:size.entrySet()){
                            PaperSize_wh_Model paperSize_wh_model = new PaperSize_wh_Model();
                            if(paperSize.getKey().split(paperGsm.getValue().toString())[0].equals(papertypeStr)){
                                if(paperGsm.getValue().toString().equals(gsm1)) {
                                    if(!paperSizeList.contains(paperSize.getValue().toString())){
                                        paperSize_wh_model.setPaperType(paperSize.getValue().toString());
                                        paperSizeWhModelList.add(paperSize_wh_model);
                                        paperSizeList.add(paperSize.getValue().toString());

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        selectAll_paper_Size_wh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    flag = false;
                    if(pos.size()<paperSizeWhModelList.size()){
                        for(int i = 0;i<paperSizeWhModelList.size();i++){
                            if(!pos.contains(String.valueOf(i))){
                                pos.add(String.valueOf(i));
                            }
                        }
                    }
                    allSelected = true;
                    posUncheck.clear();
                    paperSize_white_adapter.selectAllv();
                }
                if(!buttonView.isChecked()){
                    if(!flag){
                        pos.clear();
                        allSelected = false;
                        if(posUncheck.size()<paperSizeWhModelList.size()){
                            for(int i = 0;i<paperSizeWhModelList.size();i++){
                                if(!posUncheck.contains(String.valueOf(i))){
                                    posUncheck.add(String.valueOf(i));
                                }
                            }
                        }
                        paperSize_white_adapter.setSelectAllUncheck();
                    }
                }
            }
        });
        submit_paper_size_wh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*SharedPreferences sharedPreferences = getSharedPreferences("Management",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                DatabaseReference management = FirebaseDatabase.getInstance().getReference("Printing_partner");
                management = management.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Management").child(papertypeStr).child(gsm1);
                //pos = white_adapter.checkListener();
                boolean flag_a4 = false;
                boolean flag_legal = false;
                for(int i = 0;i<pos.size();i++){
                    if(paperSizeWhModelList.get(Integer.parseInt(pos.get(i))).getPaperType().split(":")[0].equals("A4 Size Paper")){
                        management.child("a4").setValue("available");
                        flag_a4 = true;
                        if(flag_legal==true){
                            editor.putString(papertype + ":"+ gsm1, papertype + ":"+ gsm1+":a4:"+"available"+":legal:"+"available");
                        }else{
                            editor.putString(papertype + ":"+ gsm1, papertype + ":"+ gsm1+":a4:"+"available"+":legal:"+"unavailable");
                        }
                    }
                    if(paperSizeWhModelList.get(Integer.parseInt(pos.get(i))).getPaperType().split(":")[0].equals("Legal Paper")){
                        flag_legal = true;
                        if(flag_a4==true){
                            editor.putString(papertype + ":"+ gsm1, papertype + ":"+ gsm1+":a4:"+"available"+":legal:"+"available");
                        }else{
                            editor.putString(papertype + ":"+ gsm1, papertype + ":"+ gsm1+":a4:"+"unavailable"+":legal:"+"available");
                        }
                        management.child("legal").setValue("available");
                    }
                }
                editor.commit();
                boolean flag_a4_uncheck = false;
                boolean flag_legal_uncheck = false;
                for(int i = 0;i<posUncheck.size();i++){
                    if(paperSizeWhModelList.get(Integer.parseInt(posUncheck.get(i))).getPaperType().split(":")[0].equals("A4 Size Paper")){
                        management.child("a4").setValue("unavailable");
                        flag_a4_uncheck = true;
                        if(flag_legal_uncheck==true){
                            editor.putString(papertype + ":"+ gsm1, papertype + ":"+ gsm1+":a4:"+"unavailable"+":legal:"+"unavailable");
                        }else{
                            editor.putString(papertype + ":"+ gsm1, papertype + ":"+ gsm1+":a4:"+"unavailable"+":legal:"+"available");
                        }
                    }
                    if(paperSizeWhModelList.get(Integer.parseInt(posUncheck.get(i))).getPaperType().split(":")[0].equals("Legal Paper")){
                        flag_legal_uncheck = true;
                        if(flag_a4_uncheck==true){
                            editor.putString(papertype + ":"+ gsm1, papertype + ":"+ gsm1+":a4:"+"unavailable"+":legal:"+"unavailable");
                        }else{
                            editor.putString(papertype + ":"+ gsm1, papertype + ":"+ gsm1+":a4:"+"available"+":legal:"+"unavailable");
                        }
                        management.child("legal").setValue("unavailable");
                    }
                }
                editor.commit();*/
                SharedPreferences papertype = getSharedPreferences("paper_type",MODE_PRIVATE);
                SharedPreferences paper_gsm = getSharedPreferences("paper_gsm",MODE_PRIVATE);
                SharedPreferences paper_size = getSharedPreferences("paper_size",MODE_PRIVATE);
                DatabaseReference management = FirebaseDatabase.getInstance().getReference("Printing_partner");
                management = management.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Management").child(papertypeStr).child(gsm1);
                for(int i =0;i<pos.size();i++){
                    management.child(paperSizeWhModelList.get(Integer.parseInt(pos.get(i))).getPaperType().split(":")[0]).setValue("available");
                    paper_size.edit().putString(papertypeStr+gsm1+paperSizeWhModelList.get(Integer.parseInt(pos.get(i))).getPaperType().split(":")[0],
                            paperSizeWhModelList.get(Integer.parseInt(pos.get(i))).getPaperType().split(":")[0]+":"+"available").commit();
                }
                for(int i =0;i<posUncheck.size();i++){
                    management.child(paperSizeWhModelList.get(Integer.parseInt(posUncheck.get(i))).getPaperType().split(":")[0]).setValue("unavailable");
                    paper_size.edit().putString(papertypeStr+gsm1+paperSizeWhModelList.get(Integer.parseInt(posUncheck.get(i))).getPaperType().split(":")[0],
                            paperSizeWhModelList.get(Integer.parseInt(posUncheck.get(i))).getPaperType().split(":")[0]+":"+"unavailable").commit();
                }
                startActivity(new Intent(PaperSizeActivity_white.this,OrderActivity.class));

            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



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
                                    if (attributes.getKey().equals("paper_type")) {
                                        recentOrdModel.setPaperType(attributes.getValue().toString());
                                    }
                                    if (attributes.getKey().equals("description")) {
                                        recentOrdModel.setDescriptions(attributes.getValue().toString());
                                    }

                                }
                                paperSizeWhModelList.add(recentOrdModel);
                            }
                        }
                    }
                }
                                            paperSize_white_adapter.notifyItemInserted(paperSizeWhModelList.size() - 1);
                                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/
        /*PaperSize_wh_Model recentOrdModel = new PaperSize_wh_Model();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Management").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot date:dataSnapshot.getChildren()){
                    for(DataSnapshot time:date.getChildren()){
                        for(DataSnapshot paper_type:time.getChildren()){
                            recentOrdModel.setPaperType(String.valueOf(paper_type.getKey()));
                            recentOrdModel.setDoc_img(String.valueOf(paper_type.child("doc_image").getValue()));
                            recentOrdModel.setDescriptions(String.valueOf(paper_type.child("description").getValue()));
                        }
                        paperSizeWhModelList.add(recentOrdModel);
                    }
                }
                paperSize_white_adapter = new PaperSize_white_Adapter(PaperSizeActivity_white.this, paperSizeWhModelList);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PaperSizeActivity_white.this);

                rv_paper_size_wh.setLayoutManager(layoutManager);
                rv_paper_size_wh.setAdapter(paperSize_white_adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }
    public void addCheck(String position){
        pos.add(position);
        if(pos.size()==paperSizeWhModelList.size()){
            selectAll_paper_Size_wh.setChecked(true);
        }
    }
    public void removeCheck(String position){
        pos.remove(position);
    }

    @Override
    public void addUncheck(String position) {
        posUncheck.add(position);
        if(posUncheck.size()>0){
            flag = true;
            selectAll_paper_Size_wh.setChecked(false);
        }
    }

    @Override
    public void removeUnCheck(String position) {
        posUncheck.remove(position);
    }

    @Override
    public boolean selectAll() {
        paperSize_white_adapter.notifyDataSetChanged();
        return allSelected;
    }

}

