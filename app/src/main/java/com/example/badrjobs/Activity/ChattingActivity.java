package com.example.badrjobs.Activity;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.badrjobs.Adapter.AdapterChatList;
import com.example.badrjobs.Adapter.AdapterChatMsg;
import com.example.badrjobs.Model.ModelChatList;
import com.example.badrjobs.Model.ModelChatMsg;
import com.example.badrjobs.R;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import sdk.chat.ui.activities.ChatActivity;
import sdk.chat.ui.activities.MainActivity;


public class ChattingActivity extends ChatActivity {
    //recycler
    RecyclerView recyclerView;
    AdapterChatMsg adapterChatMsg;
    ArrayList<ModelChatMsg> arrayList;
    GridLayoutManager gridLayoutManager;

//    protected @LayoutRes
//    int getLayout() {
//        return R.layout.activity_chat;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private void initAdapter(){
        arrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler);

        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        for (int i = 0; i < 15; i++) {
            ModelChatMsg item = new ModelChatMsg();
            item.setId(i+"");

            arrayList.add(item);
        }
        adapterChatMsg = new AdapterChatMsg(ChattingActivity.this,arrayList);
        recyclerView.setAdapter(adapterChatMsg);
    }





    @Override
    protected int getLayout() {
        return  R.layout.activity_chat;
//        return  R.layout.activity_chating;
    }


//
//    //language controller
//    private LocaleChangerAppCompatDelegate localeChangerAppCompatDelegate;
//    @NonNull
//    @Override
//    public AppCompatDelegate getDelegate() {
//        if (localeChangerAppCompatDelegate == null) {
//            localeChangerAppCompatDelegate = new LocaleChangerAppCompatDelegate(super.getDelegate());
//        }
//
//        return localeChangerAppCompatDelegate;
//    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        ActivityRecreationHelper.onResume(this);
//    }
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        ActivityRecreationHelper.onDestroy(this);
//    }


    @Override
    public void onBackPressed() {
        finish();
//        super.onBackPressed();

    }
}