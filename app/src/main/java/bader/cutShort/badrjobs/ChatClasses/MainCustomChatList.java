package bader.cutShort.badrjobs.ChatClasses;



import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.badrjobs.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

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
