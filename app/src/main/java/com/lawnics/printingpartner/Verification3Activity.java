package com.lawnics.printingpartner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lawnics.printingpartner.Model.PaperSize_wh_Model;
import com.lawnics.printingpartner.Pref.DataProccessor;

import java.util.Map;

public class Verification3Activity extends AppCompatActivity {
    Button next;
    ImageView goBack;
    TextInputEditText city, address, email, designation;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("Printing_partner");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DataProccessor dataProccessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification3);
        city = findViewById(R.id.City);
        designation = findViewById(R.id.designation);
        address = findViewById(R.id.shopAddress);
        email = findViewById(R.id.EmailId);
        next = findViewById(R.id.Next);
        dataProccessor = new DataProccessor(this);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (city.getText().equals("") || designation.getText().equals("") || address.getText().equals("") || email.getText().equals("")) {
                    Toast.makeText(Verification3Activity.this, "Please fill out the details", Toast.LENGTH_LONG).show();
                } else {
                    databaseReference.child(user.getUid()).child("address").setValue(address.getText().toString());
                    databaseReference.child(user.getUid()).child("designation").setValue(designation.getText().toString());
                    databaseReference.child(user.getUid()).child("email").setValue(email.getText().toString());
                    databaseReference.child(user.getUid()).child("city").setValue(city.getText().toString());
                    databaseReference.child(user.getUid()).child("Enrollment_Number").setValue(dataProccessor.getStr("enroll_no"));
                    databaseReference.child(user.getUid()).child("state").setValue(dataProccessor.getStr("state"));
                    databaseReference.child(user.getUid()).child("gender").setValue(dataProccessor.getStr("gender"));
                    databaseReference.child(user.getUid()).child("first_name").setValue(dataProccessor.getStr("first_name"));
                    databaseReference.child(user.getUid()).child("last_name").setValue(dataProccessor.getStr("last_name"));
                    databaseReference.child(user.getUid()).child("DOB").setValue(dataProccessor.getStr("DOB"));
                    intializeManagement();
                    dataProccessor.setStr("address", address.getText().toString());
                    dataProccessor.setStr("designation", designation.getText().toString());
                    dataProccessor.setStr("email", email.getText().toString());
                    dataProccessor.setStr("city", city.getText().toString());
                    Intent intent = new Intent(Verification3Activity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        goBack = findViewById(R.id.goBack);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    /*public void intializeManagement(){
        SharedPreferences sharedPreferences = getSharedPreferences("Management",MODE_PRIVATE);
        Map<String,?> entries= sharedPreferences.getAll();
        /*String entireString = sharedPreferences.getString("Management","");
        String[] paper_type = entireString.split(":",2);
        String[] gsm = paper_type[1].split(":a4:",2);
        String[] a4 = gsm[1].split(":legal:",2);
        databaseReference.child(user.getUid()).child("Management").child(paper_type[0]).child(gsm[0]).child("a4").setValue(a4[0]);
        databaseReference.child(user.getUid()).child("Management").child(paper_type[0]).child(gsm[0]).child("legal").setValue(a4[1]);*/
        /*for(Map.Entry<String,?> entry : entries.entrySet()){
            String entireString = entry.getValue().toString();
            String[] paper_type = entireString.split(":",2);
            String[] gsm = paper_type[1].split(":a4:",2);
            String[] a4 = gsm[1].split(":legal:",2);
            databaseReference.child(user.getUid()).child("Management").child(paper_type[0]).child(gsm[0]).child("a4").setValue(a4[0]);
            databaseReference.child(user.getUid()).child("Management").child(paper_type[0]).child(gsm[0]).child("legal").setValue(a4[1]);
        }
    }*/
    public void intializeManagement(){
        /*SharedPreferences papertype = getSharedPreferences("paper_type",MODE_PRIVATE);
        SharedPreferences paper_gsm = getSharedPreferences("paper_gsm",MODE_PRIVATE);
        SharedPreferences paper_size = getSharedPreferences("paper_size",MODE_PRIVATE);
        Map<String,?> paper_type = papertype.getAll();
        Map<String,?> gsm = paper_gsm.getAll();
        Map<String,?> size = paper_size.getAll();
        for(Map.Entry<String,?> paperType:paper_type.entrySet()){
            for(Map.Entry<String,?> paperGsm:gsm.entrySet()){
                for(Map.Entry<String,?> paperSize:size.entrySet()){
                            //.child(paperSize.getValue().toString().split(":")[0])
                            if(paperSize.getKey().split(paperType.getKey())[1].split(paperSize.getValue().toString())[0].equals(paperGsm.getValue().toString())){
                                   databaseReference.child(user.getUid()).child("Management").child(paperGsm.getKey().split(paperGsm.getValue().toString())[0])
                                                    .child(paperGsm.getValue().toString())
                                                    .child(paperSize.getValue().toString().split(":")[0])
                                                    .setValue(paperSize.getValue().toString().split(":")[1]);

                            }

                }
            }
        }*/
        SharedPreferences papertype = getSharedPreferences("paper_type",MODE_PRIVATE);
        SharedPreferences paper_gsm = getSharedPreferences("paper_gsm",MODE_PRIVATE);
        SharedPreferences paper_size = getSharedPreferences("paper_size",MODE_PRIVATE);
        Map<String,?> paper_type = papertype.getAll();
        Map<String,?> gsm = paper_gsm.getAll();
        Map<String,?> size = paper_size.getAll();
        for(Map.Entry<String,?> paperType:paper_type.entrySet()){

                for(Map.Entry<String,?> paperGsm:gsm.entrySet()){

                        for(Map.Entry<String,?> paperSize:size.entrySet()){
                            PaperSize_wh_Model paperSize_wh_model = new PaperSize_wh_Model();
                            if(paperSize.getKey().split(paperGsm.getValue().toString())[0].equals(paperType.getValue())){
                                if(paperSize.getKey().split(paperType.getValue().toString())[1].split(paperSize.getValue().toString().split(":")[0])[0].equals(paperGsm.getValue().toString())) {

                                        /*paperSize_wh_model.setPaperType(paperSize.getValue().toString());
                                        paperSizeWhModelList.add(paperSize_wh_model);
                                        paperSizeList.add(paperSize.getValue().toString());*/
                                        databaseReference.child(user.getUid()).child("Management").child(paperType.getValue().toString())
                                                .child(paperGsm.getValue().toString()).child(paperSize.getValue().toString().split(":")[0])
                                                .setValue(paperSize.getValue().toString().split(":")[1]);

                                }
                            }
                        }

                }
        }
    }
}
