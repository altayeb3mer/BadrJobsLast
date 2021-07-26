package com.example.badrjobs.ChatClasses;



import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.badrjobs.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import sdk.chat.core.dao.Thread;
import sdk.chat.core.session.ChatSDK;
import sdk.chat.ui.activities.MainActivity;

public class MainCustomChatList extends  MainActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        super.initViews();
    }

    @Override
    protected boolean searchEnabled() {
        return true;
    }

    @Override
    protected void search(String s) {

    }

    @Override
    protected MaterialSearchView searchView() {
        return null;
    }

    @Override
    protected void reloadData() {
//        super.reloadData();
    }

    @Override
    protected void clearData() {

    }

    @Override
    protected void updateLocalNotificationsForTab() {

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main_drawer;
    }
//    protected @LayoutRes
//    int getLayout() {
//        return sdk.chat.ui.R.layout.activity_login;
//    }

    @Override
    public void onBackPressed() {
        finish();
//        super.onBackPressed();
    }
}
