package com.example.badrjobs;

import android.app.Application;
import android.content.res.Configuration;

import com.franmontiel.localechanger.LocaleChanger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        List<Locale> localeList = new ArrayList<>();
        localeList.add(new Locale("ar"));
        localeList.add(new Locale("en"));

        LocaleChanger.initialize(getApplicationContext(),localeList);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleChanger.onConfigurationChanged();
    }
}
