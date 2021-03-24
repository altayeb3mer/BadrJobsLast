package com.example.badrjobs.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "fcmsharedprefdemo";
    private static final String KEY_ACCESS_TOKEN = "token";
    private static final String KEY_APP_TOKEN = "appToken";

    private static Context mCtx;
    private static SharedPrefManager mInstance;

    public SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null)
            mInstance = new SharedPrefManager(context);
        return mInstance;
    }



    public void storeFirebaseToken(String token) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN, token);
        editor.apply();
    }



    public String getFireBaseToken() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, "");
    }
    public void storeAppToken(String token) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_APP_TOKEN, token);
        editor.apply();
    }


    public String getAppToken() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_APP_TOKEN, "");
    }
    //FCM token
    public void storeFcmToken(String token) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("FCM", token);
        editor.apply();
    }


    public String getFcmToken() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("FCM", "");
    }


    public void storeAppLanguage(String language) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language",  language);
        editor.apply();
    }


    public String GetAppLanguage() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("language", "en");
    }




    //save name
    public void SaveUserName(String name) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.apply();
    }
    public String GetUserName() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("name", "");
    }




    //save email
    public void SaveUserEmail(String email) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.apply();
    }
    public String GetUserEmail() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("email", "");
    }


    //save phone
    public void SaveUserPhone(String phone) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phone", phone);
        editor.apply();
    }
    public String GetUserPhone() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("phone", "");
    }



    //open state
    public void PutOpenState(boolean val) {
        SharedPreferences sp = mCtx.getSharedPreferences(SHARED_PREF_NAME, mCtx.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("is_first_open", val);
        edit.apply();
    }
    public boolean Is_first_open() {
        SharedPreferences sp = mCtx.getSharedPreferences(SHARED_PREF_NAME, mCtx.MODE_PRIVATE);
        return sp.getBoolean("is_first_open", true);
    }

    //open state
    public void putReceiveNotification(boolean val) {
        SharedPreferences sp = mCtx.getSharedPreferences(SHARED_PREF_NAME, mCtx.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("receive", val);
        edit.apply();
    }
    public boolean receiveNotification() {
        SharedPreferences sp = mCtx.getSharedPreferences(SHARED_PREF_NAME, mCtx.MODE_PRIVATE);
        return sp.getBoolean("receive", true);
    }



}