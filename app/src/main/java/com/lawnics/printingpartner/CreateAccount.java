package com.lawnics.printingpartner;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.hbb20.CountryCodePicker;

public class CreateAccount extends AppCompatActivity {

    Button proceed;
    EditText phone;
    CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        proceed = findViewById(R.id.create_account_proceed);
        phone = findViewById(R.id.create_account_phone_number);
        ccp = findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(phone);
        phone.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 11) {
                    String ss = s.toString();
                    phone.setText(ss.substring(s.length() - 10));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_no = ccp.getFullNumberWithPlus();
                if (!ccp.isValidFullNumber()) {
                    phone.setError("Enter a valid number.");
                    phone.requestFocus();
                    return;
                }

                Intent intent = new Intent(CreateAccount.this, OTPActivity.class);
                intent.putExtra("mobile", phone_no);
                startActivity(intent);
            }
        });
    }
}
