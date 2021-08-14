package bader.cutShort.badrjobs.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;

import bader.cutShort.badrjobs.GlobalVar;
import com.example.badrjobs.R;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;

public class Splash extends AppCompatActivity {

    //language controller
    private LocaleChangerAppCompatDelegate localeChangerAppCompatDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);

        new GlobalVar().changeStatusBarColor(this, getColor(R.color.colorPrimary));

        startActivity(new Intent(getApplicationContext(), Splash2.class));

//        String token = SharedPrefManager.getInstance(this).getAppToken();
//        Animatoo.animateSlideRight(this);
//        if (!token.isEmpty()){
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//        }else{
//            startActivity(new Intent(getApplicationContext(), Login.class));
//        }

        finish();
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityRecreationHelper.onDestroy(this);
    }

}