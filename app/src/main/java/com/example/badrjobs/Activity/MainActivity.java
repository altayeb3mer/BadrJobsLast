package com.example.badrjobs.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.badrjobs.Adapter.ViewPagerAdapter;
import com.example.badrjobs.Fragment.FragmentMyProfile;
import com.example.badrjobs.Fragment.FragmentAdd;
import com.example.badrjobs.Fragment.FragmentChat;
import com.example.badrjobs.Fragment.FragmentFavorite;
import com.example.badrjobs.Fragment.FragmentMain;
import com.example.badrjobs.R;
import com.example.badrjobs.Utils.CustomViewPager;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements  BottomNavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bottomNavigationView;
    private CustomViewPager viewPager;
    ImageView btnMenu,icNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        icNotification = findViewById(R.id.icNotification);
        btnMenu = findViewById(R.id.btnMenu);
        icNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),NotificationActivity.class));
            }
        });
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MenuActivity.class));
            }
        });
        bottomNavigationView = findViewById(R.id.btn_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        viewPager =  findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(5);
        setupViewPager(viewPager);
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        FragmentMain fragmentMain= new FragmentMain();
        FragmentChat fragmentChat = new FragmentChat();
        FragmentAdd fragmentAdd = new FragmentAdd();
        FragmentFavorite fragmentFavorite = new FragmentFavorite();
        FragmentMyProfile fragmentAccount = new FragmentMyProfile();

        adapter.addFragment(fragmentMain,"الرئيسية");
        adapter.addFragment(fragmentChat,"المحادثات");
        adapter.addFragment(fragmentAdd,"اضف اعلانك");
        adapter.addFragment(fragmentFavorite,"المفضلة");
        adapter.addFragment(fragmentAccount,"الحساب");
        viewPager.setAdapter(adapter);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.btn_nav1:{
                switchToFragment(1);
                break;
            }
            case R.id.btn_nav2:{
                switchToFragment(2);
                break;
            }
            case R.id.btn_nav3:{
                switchToFragment(3);
                break;
            }
            case R.id.btn_nav4:{
                switchToFragment(4);
                break;
            }
            case R.id.btn_nav5:{
                switchToFragment(5);
                break;
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem()==0){
            super.onBackPressed();
        }else{
            switchToFragment(1);
        }
//        super.onBackPressed();
    }

    public void switchToFragment(int f_no) {
//        FragmentManager manager = getSupportFragmentManager();
        switch (f_no) {
            case 1: {
                viewPager.setCurrentItem(0);
                SetNavigationItemSelected(R.id.btn_nav1);
                break;
            }
            case 2: {
                viewPager.setCurrentItem(1);
                SetNavigationItemSelected(R.id.btn_nav2);
                break;
            }
            case 3: {
                viewPager.setCurrentItem(2);
                SetNavigationItemSelected(R.id.btn_nav3);
                break;
            }
            case 4: {
                viewPager.setCurrentItem(3);
                SetNavigationItemSelected(R.id.btn_nav4);
                break;
            }
            case 5: {
                viewPager.setCurrentItem(4);
                SetNavigationItemSelected(R.id.btn_nav5);
                break;
            }

        }
    }

    private void SetNavigationItemSelected(int id){
        bottomNavigationView.getMenu().findItem(id).setChecked(true);
    }



    private LocaleChangerAppCompatDelegate localeChangerAppCompatDelegate;
    @NonNull
    @Override
    public AppCompatDelegate getDelegate() {
        if (localeChangerAppCompatDelegate == null) {
            localeChangerAppCompatDelegate = new LocaleChangerAppCompatDelegate(super.getDelegate());
        }

        return localeChangerAppCompatDelegate;
    }
    @Override
    protected void onResume() {
        super.onResume();
        ActivityRecreationHelper.onResume(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityRecreationHelper.onDestroy(this);
    }



}