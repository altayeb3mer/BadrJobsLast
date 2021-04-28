package com.example.badrjobs.Activity;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.badrjobs.Adapter.AdapterChatList;
import com.example.badrjobs.Adapter.AdapterChatMsg;
import com.example.badrjobs.Model.ModelChatList;
import com.example.badrjobs.Model.ModelChatMsg;
import com.example.badrjobs.R;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;

import java.util.ArrayList;


public class ChattingActivity extends AppCompatActivity {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chating);
//        initAdapter();
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




    //language controller
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