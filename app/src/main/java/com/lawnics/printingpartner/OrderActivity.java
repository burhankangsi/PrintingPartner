package com.lawnics.printingpartner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.lawnics.printingpartner.Adapters.SectionsPagerAdapter;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

public class OrderActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private AppCompatButton btn_prevOrd, btn_recOrd;
    private View navHeader;
    private TextView tv_nav_header;
    private ImageView imgView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        searchView = (SearchView)findViewById(R.id.search_view_order);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

        //Select Home by default
        navigationView.setCheckedItem(R.id.nav_home);
        Fragment fragment = new PreviousOrdersFrag();
        displaySelectedFragment(fragment);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        tv_nav_header = (TextView) navHeader.findViewById(R.id.txt_nav_header);
       // btn_prevOrd = (AppCompatButton)findViewById(R.id.pre)
        imgView = (ImageView) navHeader.findViewById(R.id.imageView_nav_header);


        Glide.with(this).load(R.drawable.lawnics)
                .apply(RequestOptions.circleCropTransform())
                .thumbnail(0.5f)
                .into(imgView);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_wallet, R.id.nav_my_orders, R.id.nav_management, R.id.nav_settings)
                .setDrawerLayout(drawer)
                .build();

        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
       // NavigationView navView = findViewById(R.id.nav_view);
        //NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                if(id==R.id.nav_management){
                    startActivity(new Intent(OrderActivity.this,ManagementActivity.class));
                }
                //NavigationUI.onNavDestinationSelected(item,navController);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }

        });

//        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
      //  NavigationUI.setupWithNavController(navigationView, navController);

      //  SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
         viewPager = (ViewPager) findViewById(R.id.view_pager_order_activity);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
      //  viewPager.setAdapter(sectionsPagerAdapter);
        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);

   }





    private void displaySelectedFragment(Fragment fragment)
    {
        FragmentManager fragmentManager=getSupportFragmentManager();
        //fragmentManager.popBackStack();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        //fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
        //  android.R.anim.fade_out);
                 // fragmentTransaction.replace(R.id.nav_host_fragment,fragment);
        //fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

    private void setupViewPager(ViewPager viewPager)
    {
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        sectionsPagerAdapter.addFragment(new RecentOrdersFrag(), "Recent Orders");
        sectionsPagerAdapter.addFragment(new PreviousOrdersFrag(), "Previous Orders");
        viewPager.setAdapter(sectionsPagerAdapter);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.order, menu);
//        return true;
//    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}
