package com.lawnics.printingpartner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;

public class Onboarding_animationActivity extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    int current = 0;
    private int dotscount;
    private ImageView dot1, dot2, dot3;
    private LottieAnimationView lottieAnimationView;
    private ImageView[] dots;
    private Integer[] animations = {R.raw.document_scan, R.raw.document_storage, R.raw.print_from_shop};

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_animation);
        sliderDotspanel = findViewById(R.id.dotsLayout);
        dot1 = findViewById(R.id.dot1);
        dot2 = findViewById(R.id.dot2);
        dot3 = findViewById(R.id.dot3);
        lottieAnimationView = findViewById(R.id.mainpagerAnimationView);
        current = 0;
        dots = new ImageView[3];
        dots[0] = dot1;
        dots[1] = dot2;
        dots[2] = dot3;
        for (int i = 0; i < 3; i++) {
            dots[i].setImageResource(R.drawable.inactive_dots);
        }

        dots[0].setImageResource(R.drawable.active_dots);
        lottieAnimationView.setOnTouchListener(new OnSwipeTouchListener(Onboarding_animationActivity.this) {
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                for (int i = 0; i < 3; i++) {
                    dots[i].setImageResource(R.drawable.inactive_dots);
                }
                if (current > 0) {
                    --current;
                    lottieAnimationView.setAnimation(animations[current]);
                    lottieAnimationView.playAnimation();
                    dots[current].setImageResource(R.drawable.active_dots);
                    if (current < 2) {
                        findViewById(R.id.getstarted).setVisibility(View.GONE);
                        findViewById(R.id.skipPager).setVisibility(View.VISIBLE);
                        findViewById(R.id.nextPager).setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                for (int i = 0; i < 3; i++) {
                    dots[i].setImageResource(R.drawable.inactive_dots);
                }
                if (current < 2) {
                    ++current;
                    lottieAnimationView.setAnimation(animations[current]);
                    lottieAnimationView.playAnimation();
                    dots[current].setImageResource(R.drawable.active_dots);
                    if (current == 2) {
                        findViewById(R.id.getstarted).setVisibility(View.VISIBLE);
                        findViewById(R.id.skipPager).setVisibility(View.GONE);
                        findViewById(R.id.nextPager).setVisibility(View.GONE);
                    }
                }
            }
        });


        findViewById(R.id.getstarted).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Onboarding_animationActivity.this, CreateAccount.class);
                if (isConnected()) {
                    intent.putExtra("emails_pass", getIntent().getStringExtra("emails_pass"));
                }
                startActivity(intent);
            }
        });

        findViewById(R.id.getstartedbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Onboarding_animationActivity.this, CreateAccount.class);
                if (isConnected()) {
                    intent.putExtra("emails_pass", getIntent().getStringExtra("emails_pass"));
                }
                startActivity(intent);
            }
        });

        findViewById(R.id.skipPager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Onboarding_animationActivity.this, CreateAccount.class);
                if (isConnected()) {
                    intent.putExtra("emails_pass", getIntent().getStringExtra("emails_pass"));
                }
                startActivity(intent);
            }
        });

        findViewById(R.id.nextPager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 3; i++) {
                    dots[i].setImageResource(R.drawable.inactive_dots);
                }
                if (current < 2) {
                    ++current;
                    lottieAnimationView.setAnimation(animations[current]);
                    lottieAnimationView.playAnimation();
                    dots[current].setImageResource(R.drawable.active_dots);
                    if (current == 2) {
                        findViewById(R.id.getstarted).setVisibility(View.VISIBLE);
                        findViewById(R.id.skipPager).setVisibility(View.GONE);
                        findViewById(R.id.nextPager).setVisibility(View.GONE);
                    }
                }
            }
        });

    }
}
