package com.lawnics.printingpartner;
/**
 *
 * Created By : Girish (theSkynet)
 *
 * **/

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;
import com.lawnics.printingpartner.Pref.DataProccessor;

import org.json.JSONObject;

public class SplashScreen extends AppCompatActivity {

    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
    private int REQUEST_CODE_PERMISSIONS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (allPermissionsGranted()) {
            func();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                func();
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void func() {
        Thread timer = new Thread() {
            public void run() {
                try {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (ActivityCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(SplashScreen.this, new String[]{
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION},
                                1337);

                    }
                    if (user!=null) {
                        DataProccessor dataProccessor = new DataProccessor(SplashScreen.this);

                        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                        dataProccessor.setStr("uid", user1.getUid());
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Printing_partner/" + user1.getUid());
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    retriveManagement();
                                    Intent intent = new Intent(SplashScreen.this, OrderActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(SplashScreen.this, Verification1Activity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                 else {
                        sleep(2000);
                        managementDataRetrive();
                        Intent intent = new Intent(SplashScreen.this, LanguageActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.start();
    }
    /*public void managementDataRetrive(){
        SharedPreferences sharedPreferences = getSharedPreferences("Management",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("Management").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot paper_type:dataSnapshot.getChildren()){
                    String paper = paper_type.getKey();
                    for(DataSnapshot gsm:paper_type.getChildren()){
                        editor.putString(paper + ":"+ gsm.getKey(), paper + ":"+ gsm.getKey()+":a4:"+String.valueOf(gsm.child("a4").getValue())+":legal:"+String.valueOf(gsm.child("legal").getValue()));

                    }

                }
                editor.commit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/
    public void managementDataRetrive(){
        SharedPreferences papertype = getSharedPreferences("paper_type",MODE_PRIVATE);
        SharedPreferences paper_gsm = getSharedPreferences("paper_gsm",MODE_PRIVATE);
        SharedPreferences paper_size = getSharedPreferences("paper_size",MODE_PRIVATE);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("Management").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot paper_type:dataSnapshot.getChildren()){
                    String paper = paper_type.getKey();
                    papertype.edit().putString(paper_type.getKey(),paper_type.getKey()).commit();
                    for(DataSnapshot gsm:paper_type.getChildren()){
                        //editor.putString(paper + ":"+ gsm.getKey(), paper + ":"+ gsm.getKey()+":a4:"+ gsm.child("a4").getValue() +":legal:"+ gsm.child("legal").getValue());
                        paper_gsm.edit().putString(paper_type.getKey()+gsm.getKey(),gsm.getKey()).commit();
                        for(DataSnapshot paperSize:gsm.getChildren()){
                            paper_size.edit().putString(paper_type.getKey()+gsm.getKey()+paperSize.getKey(),paperSize.getKey()+":"+paperSize.getValue()).commit();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void retriveManagement(){
        SharedPreferences papertype = getSharedPreferences("paper_type",MODE_PRIVATE);
        SharedPreferences paper_gsm = getSharedPreferences("paper_gsm",MODE_PRIVATE);
        SharedPreferences paper_size = getSharedPreferences("paper_size",MODE_PRIVATE);
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Printing_partner");
        databaseReference1.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Management").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot paper_type:dataSnapshot.getChildren()){
                    String paper = paper_type.getKey();
                    papertype.edit().putString(paper_type.getKey(),paper_type.getKey()).commit();
                    for(DataSnapshot gsm:paper_type.getChildren()){
                        //editor.putString(paper + ":"+ gsm.getKey(), paper + ":"+ gsm.getKey()+":a4:"+ gsm.child("a4").getValue() +":legal:"+ gsm.child("legal").getValue());
                        paper_gsm.edit().putString(paper_type.getKey()+gsm.getKey(),gsm.getKey()).commit();
                        for(DataSnapshot paperSize:gsm.getChildren()){
                            paper_size.edit().putString(paper_type.getKey()+gsm.getKey()+paperSize.getKey(),paperSize.getKey()+":"+paperSize.getValue()).commit();
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
