package com.lawnics.printingpartner;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lawnics.printingpartner.Pref.DataProccessor;

import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class OTPActivity extends AppCompatActivity {

    DataProccessor dataProccessor;
    TextView otpto, timer, resend;
    PhoneAuthCredential credential;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference reference = firebaseDatabase.getReference("Printing_partner");
    int tries = 1;
    private String mVerificationId;
    private OtpTextView editTextCode;
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            credential = phoneAuthCredential;
            try {
                String code = phoneAuthCredential.getSmsCode();
                if (code != null) {
                    editTextCode.setOTP(code);
                }
            } catch (Exception e) {
            }
            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OTPActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
        }
    };
    private FirebaseAuth mAuth;

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        timer = findViewById(R.id.timerTextview);
        mAuth = FirebaseAuth.getInstance();
        editTextCode = findViewById(R.id.editTextCode);
        otpto = findViewById(R.id.otpto);
        resend = findViewById(R.id.resendOTP);
        dataProccessor = new DataProccessor(this);
        String mobile = getIntent().getStringExtra("mobile");
        otpto.setText(mobile);
        sendVerificationCode(mobile);
        editTextCode.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
            }

            @Override
            public void onOTPComplete(String otp) {
                verifyVerificationCode(otp);
            }
        });

        new CountDownTimer(59000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                int ms = (int) (millisUntilFinished / 1000);
                if (Integer.toString(ms).length() == 1)
                    timer.setText("00:0" + ms);
                else {
                    timer.setText("00:" + ms);
                }
            }

            @Override
            public void onFinish() {
                timer.setVisibility(View.INVISIBLE);
                resend.setVisibility(View.VISIBLE);
            }
        }.start();
        findViewById(R.id.resendOTP).setOnClickListener(v -> {
            if (tries > 3) {
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Limit Exceeded! Try after some time.", Snackbar.LENGTH_LONG);
                snackbar.show();
                return;
            }
            Toast.makeText(OTPActivity.this, "Sending OTP Again...", Toast.LENGTH_LONG).show();
            sendVerificationCode(mobile);
            ++tries;
            timer.setVisibility(View.VISIBLE);
            resend.setVisibility(View.INVISIBLE);
            new CountDownTimer(59000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    int ms = (int) (millisUntilFinished / 1000);
                    if (Integer.toString(ms).length() == 1)
                        timer.setText("00:0" + ms);
                    else {
                        timer.setText("00:" + ms);
                    }
                }

                @Override
                public void onFinish() {
                    timer.setVisibility(View.INVISIBLE);
                    resend.setVisibility(View.VISIBLE);
                }
            }.start();
        });

        findViewById(R.id.wrongNumber).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editTextCode.getOTP();
                Log.i("tag", code);
                if (code.isEmpty() || code.length() < 6) {
                    Toast.makeText(OTPActivity.this, "Invalid OTP !", Toast.LENGTH_LONG).show();
                    return;
                }
                verifyVerificationCode(code);
            }
        });

    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);

    }

    private void verifyVerificationCode(String code) {
        try {
            //PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
            signInWithPhoneAuthCredential(credential);
            editTextCode.showSuccess();
        } catch (Exception e) {
            editTextCode.showError();
            Toast.makeText(this, "Verification Code is wrong.", Toast.LENGTH_SHORT).show();
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(OTPActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            ProgressDialog dialog = new ProgressDialog(OTPActivity.this);
                            dialog.setMessage("Processing Request...");
                            dialog.show();
                            dataProccessor.setStr("phone_no", otpto.getText().toString());
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            dataProccessor.setStr("uid", user.getUid());
                            DatabaseReference databaseReference = firebaseDatabase.getReference("Printing_partner/" + user.getUid());
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        Intent intent = new Intent(OTPActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        dialog.dismiss();
                                        finish();
                                    } else {
                                        Intent intent = new Intent(OTPActivity.this, Verification1Activity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        dialog.dismiss();
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        } else {
                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }
                        }
                    }
                });
    }
}