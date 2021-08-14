package bader.cutShort.badrjobs.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.badrjobs.R;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import bader.cutShort.badrjobs.Adapter.ViewPagerAdapter;
import bader.cutShort.badrjobs.Fragment.FragmentAdd;
import bader.cutShort.badrjobs.Fragment.FragmentChat;
import bader.cutShort.badrjobs.Fragment.FragmentFavorite;
import bader.cutShort.badrjobs.Fragment.FragmentMain;
import bader.cutShort.badrjobs.Fragment.FragmentMyProfile;
import bader.cutShort.badrjobs.Utils.CustomViewPager;
import bader.cutShort.badrjobs.Utils.SharedPrefManager;
import sdk.chat.core.dao.Keys;
import sdk.chat.core.push.BaseBroadcastHandler;
import sdk.chat.core.utils.StringChecker;

public class MainActivity extends sdk.chat.ui.activities.MainActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    ImageView btnMenu, icNotification;
    BottomNavigationMenuView menuView;
    BottomNavigationItemView itemView;
    View notificationBadge;
    boolean doubleBackToExitPressedOnce = false;
    int animVar = 0;
    private CustomViewPager viewPager;
    private LocaleChangerAppCompatDelegate localeChangerAppCompatDelegate;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        BaseBroadcastHandler bbc = new BaseBroadcastHandler();

        @Override
        public void onReceive(Context context, Intent intent) {
            try {

                Bundle extras = intent.getExtras();

                final String threadEntityID = extras.getString(Keys.PushKeyThreadEntityID);
                final String userEntityID = extras.getString(Keys.PushKeyUserEntityID);

                if (!StringChecker.isNullOrEmpty(threadEntityID) && !StringChecker.isNullOrEmpty(userEntityID)) {
                    // Valid Chat SDK push, send it to the Chat SDK
                    bbc.onReceive(context, intent);
                    Toast.makeText(getApplicationContext(), "new message ^^^^^^^^^^", Toast.LENGTH_SHORT).show();
                    setBadgeToChatTap();
                    if (SharedPrefManager.getInstance(getApplicationContext()).hasNotification()
                            &&!SharedPrefManager.getInstance(getApplicationContext()).hasView())
                        setBadgeToChatTap();

                } else {
                    Toast.makeText(getApplicationContext(), "another receiver", Toast.LENGTH_SHORT).show();
                    // Handle the push yourself
                }

//                setBadgeToChatTap();
//                if (SharedPrefManager.getInstance(getApplicationContext()).hasNotification()
//                        &&!SharedPrefManager.getInstance(getApplicationContext()).hasView())
//                    setBadgeToChatTap();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.in_right, R.anim.out_right);
//        Animatoo.animateSlideRight(this);
        init();

        try {
            if (SharedPrefManager.getInstance(this).hasNotification())
                setBadgeToChatTap();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void setBadgeToChatTap() {
        menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        itemView = (BottomNavigationItemView) menuView.getChildAt(1);
        notificationBadge = LayoutInflater.from(this).inflate(R.layout.notification_badge, menuView, false);
//        TextView textView = notificationBadge.findViewById(R.id.counter_badge);
//        textView.setText("*");
        itemView.addView(notificationBadge);
        SharedPrefManager.getInstance(getApplicationContext()).putViewState(true);
    }

    @Override
    protected boolean searchEnabled() {
        return false;
    }

    @Override
    protected void search(String s) {

    }

    @Override
    protected MaterialSearchView searchView() {
        return null;
    }

    private void init() {
        icNotification = findViewById(R.id.icNotification);
        btnMenu = findViewById(R.id.btnMenu);
        icNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
            }
        });
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MenuActivity.class));
            }
        });
        bottomNavigationView = findViewById(R.id.btn_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(5);
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        FragmentMain fragmentMain = new FragmentMain();
        FragmentChat fragmentChat = new FragmentChat();
        FragmentAdd fragmentAdd = new FragmentAdd();
        FragmentFavorite fragmentFavorite = new FragmentFavorite();
        FragmentMyProfile fragmentAccount = new FragmentMyProfile();

        adapter.addFragment(fragmentMain, "الرئيسية");
        adapter.addFragment(fragmentChat, "المحادثات");
        adapter.addFragment(fragmentAdd, "اضف اعلانك");
        adapter.addFragment(fragmentFavorite, "المفضلة");
        adapter.addFragment(fragmentAccount, "الحساب");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_nav1: {
                switchToFragment(1);
                break;
            }
            case R.id.btn_nav2: {
                switchToFragment(2);
                if (SharedPrefManager.getInstance(getApplicationContext()).hasNotification()&&
                        SharedPrefManager.getInstance(getApplicationContext()).hasView()) {
                    itemView.removeView(notificationBadge);
                    SharedPrefManager.getInstance(getApplicationContext()).putHasNotification(false);
                    SharedPrefManager.getInstance(getApplicationContext()).putViewState(false);
                }

                break;
            }
            case R.id.btn_nav3: {
                switchToFragment(3);
                break;
            }
            case R.id.btn_nav4: {
                switchToFragment(4);
                break;
            }
            case R.id.btn_nav5: {
                switchToFragment(5);
//                SharedPrefManager.getInstance(getApplicationContext()).putHasNotification(true);
                break;
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            if (doubleBackToExitPressedOnce) {
//                super.onBackPressed();
                finish();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.click_again_exit, Toast.LENGTH_SHORT).show();

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
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

    private void SetNavigationItemSelected(int id) {
        bottomNavigationView.getMenu().findItem(id).setChecked(true);
    }

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
        if (animVar > 0) {
            Animatoo.animateSlideLeft(this);
        }
        animVar++;
    }

    @Override
    protected void reloadData() {

    }

    @Override
    protected void clearData() {

    }

    @Override
    protected void updateLocalNotificationsForTab() {

    }

    @Override
    protected int getLayout() {
        return 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityRecreationHelper.onDestroy(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver),
                new IntentFilter("MyData"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }
}
