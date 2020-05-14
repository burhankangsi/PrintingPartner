package com.lawnics.printingpartner;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lawnics.printingpartner.Interfaces.CardVerificationInterface;
import com.lawnics.printingpartner.Pref.DataProccessor;

import java.util.Calendar;

public class Verification2Activity extends AppCompatActivity {
    ImageView goBack, mImage, fImage, oImage;
    LinearLayout male, female, other;
    Button Next, DatepickerButton;
    TextInputEditText firstName, lastName;

    int day, month, year;
    boolean m = false, f = false, o = false;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference reference = firebaseDatabase.getReference("Printing_partner");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DataProccessor dataProccessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification2);
        Next = findViewById(R.id.Next);
        firstName = findViewById(R.id.FirstName);
        lastName = findViewById(R.id.lastName);
        mImage = findViewById(R.id.maleImage);
        fImage = findViewById(R.id.femaleImage);
        oImage = findViewById(R.id.otherImage);
        DatepickerButton = findViewById(R.id.date_picker_button);
        dataProccessor = new DataProccessor(this);
        male = findViewById(R.id.Male);
        female = findViewById(R.id.Female);
        other = findViewById(R.id.Other);


        if (CardVerificationInterface.textData.size() > 0) {
            if (CardVerificationInterface.textData.get(0).containsKey("name")) {
                String[] name = CardVerificationInterface.textData.get(0).get("name").split(" ");
                firstName.setText(name[0]);
                if (name.length > 1)
                    lastName.setText(name[1]);
            }
            if (CardVerificationInterface.textData.get(0).containsKey("birth_date")) {
                DatepickerButton.setText(CardVerificationInterface.textData.get(0).get("birth_date"));
            }

        }
        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!m) {
                    mImage.setImageResource(R.drawable.ic_men_avtar);
                } else {
                    mImage.setImageResource(R.drawable.men_greyscale);
                }
                fImage.setImageResource(R.drawable.ic_women_greyscale);
                oImage.setImageResource(R.drawable.ic_other_greyscale);
                m = !m;
                f = false;
                o = false;
            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!f) {
                    fImage.setImageResource(R.drawable.ic_women_avtar);
                } else {
                    fImage.setImageResource(R.drawable.ic_women_greyscale);
                }
                mImage.setImageResource(R.drawable.men_greyscale);
                oImage.setImageResource(R.drawable.ic_other_greyscale);
                f = !f;
                m = false;
                o = false;
            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!o) {
                    oImage.setImageResource(R.drawable.ic_other_icon);
                } else {
                    oImage.setImageResource(R.drawable.ic_other_greyscale);
                }
                mImage.setImageResource(R.drawable.men_greyscale);
                fImage.setImageResource(R.drawable.ic_women_greyscale);
                o = !o;
                m = false;
                f = false;
            }
        });
        goBack = findViewById(R.id.goBack);

        DatepickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Verification2Activity.this, AlertDialog.THEME_HOLO_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                DatepickerButton.setText(Integer.toString(dayOfMonth) + "-" + Integer.toString(monthOfYear + 1) + "-" + Integer.toString(year));

                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstName.getText().equals("") || lastName.getText().equals("") || (!m && !o && !f) || DatepickerButton.getText().equals("-- / -- / ----")) {
                    Toast.makeText(Verification2Activity.this, "Please fill out the credentials", Toast.LENGTH_LONG).show();
                } else {
                    String fname = firstName.getText().toString();
                    String lname = lastName.getText().toString();
                    if (m) {

                        dataProccessor.setStr("gender", "male");
                    } else if (f) {
                        dataProccessor.setStr("gender", "female");
                    } else {
                        dataProccessor.setStr("gender", "other");
                    }
                    dataProccessor.setStr("first_name", fname);
                    dataProccessor.setStr("last_name", lname);
                    dataProccessor.setStr("DOB", DatepickerButton.getText().toString());
                    Intent intent = new Intent(Verification2Activity.this, Verification3Activity.class);
                    startActivity(intent);
                    finish();
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
