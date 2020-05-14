package com.lawnics.printingpartner;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {

    String currentLanguage = "en";
    Locale myLocale;
    CheckBox hin, eng;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        button = findViewById(R.id.next);
        hin = findViewById(R.id.hin);
        eng = findViewById(R.id.eng);
        eng.setChecked(true);

        hin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hin.setChecked(true);
                eng.setChecked(false);
                hin.setTextColor(getResources().getColor(R.color.colorPrimary));
                eng.setTextColor(getResources().getColor(R.color.textColorGrey));
            }
        });
        eng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eng.setChecked(true);
                hin.setChecked(false);

                hin.setTextColor(getResources().getColor(R.color.textColorGrey));
                eng.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LanguageActivity.this, Onboarding_animationActivity.class);
                if (hin.isChecked()) {
                    setLocale("hi");
                } else if (eng.isChecked()) {
                    setLocale(Locale.getDefault().getLanguage());
                }
                startActivity(intent);
            }
        });
    }

    public void setLocale(String localeName) {
        if (!localeName.equals(currentLanguage)) {
            myLocale = new Locale(localeName);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
        }
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
