package com.lawnics.printingpartner;

import android.content.Intent;
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
import com.lawnics.printingpartner.Pref.DataProccessor;

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
}
