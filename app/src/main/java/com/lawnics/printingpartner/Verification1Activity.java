package com.lawnics.printingpartner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lawnics.printingpartner.Interfaces.CardVerificationInterface;
import com.lawnics.printingpartner.Pref.DataProccessor;

import java.util.ArrayList;
import java.util.List;

public class Verification1Activity extends AppCompatActivity {


    ImageView goBack;
    ImageView idcardCard;
    Button Finish;
    EditText editText;
    Spinner stateSpinner;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("Printing_partner");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    List<String> States;
    DataProccessor dataProccessor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification1);
        Finish = findViewById(R.id.Finish);
        idcardCard = findViewById(R.id.idcardCard);
        editText = findViewById(R.id.EnrollmentNumberEdittext);
        stateSpinner = findViewById(R.id.StateSpinner);

        if (CardVerificationInterface.textData.size() > 0) {
            idcardCard.setImageBitmap(CardVerificationInterface.bitmaps.get(0));
            if (CardVerificationInterface.textData.get(0) != null && CardVerificationInterface.textData.get(0).get("number") != null)
                editText.setText(CardVerificationInterface.textData.get(0).get("number"));
        }
        dataProccessor = new DataProccessor(Verification1Activity.this);

        States = new ArrayList<String>();
        States.add("Rajasthan");
        States.add("Uttar Pradesh");
        States.add("Gujarat");
        States.add("Himachal Pradesh");
        States.add("Rajasthan");
        States.add("Uttar Pradesh");
        States.add("Gujarat");
        States.add("Himachal Pradesh");
        States.add("Rajasthan");
        States.add("Uttar Pradesh");
        States.add("Gujarat");
        States.add("Himachal Pradesh");
        States.add("Rajasthan");
        States.add("Uttar Pradesh");
        States.add("Gujarat");
        States.add("Himachal Pradesh");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(Verification1Activity.this, android.R.layout.simple_spinner_dropdown_item, States);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(dataAdapter);

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CardVerificationInterface.bitmaps.size() > 0) {
                    Intent intent = new Intent(Verification1Activity.this, Verification2Activity.class);
                    dataProccessor.setStr("enroll_no", editText.getText().toString());
                    dataProccessor.setStr("state", States.get(stateSpinner.getSelectedItemPosition()));
                    startActivity(intent);
                } else {
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "ID Card Missing", Snackbar.LENGTH_LONG);
                    snackbar.getView().setBackgroundColor(getResources().getColor(R.color.textColorGrey));
                    snackbar.show();
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

        idcardCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Verification1Activity.this, VerifyCamera.class);
                startActivity(intent);

            }
        });
    }
}
