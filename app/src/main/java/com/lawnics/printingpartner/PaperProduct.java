package com.lawnics.printingpartner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class PaperProduct extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView cust_name, no_of_pages;
    private ImageView paper_img;
    private AppCompatButton btn_accept, btn_decline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_product);

        toolbar = findViewById(R.id.toolbar_paper_product);
        setSupportActionBar(toolbar);

        cust_name = (TextView)findViewById(R.id.tv_cust_name_paper_prod_activity);
        no_of_pages = (TextView)findViewById(R.id.tv_page_val_paper_prod_activity);

        btn_accept = (AppCompatButton) findViewById(R.id.btn_accept_paper_prod);
        btn_decline = (AppCompatButton) findViewById(R.id.btn_decline_paper_prod);

    }
}
