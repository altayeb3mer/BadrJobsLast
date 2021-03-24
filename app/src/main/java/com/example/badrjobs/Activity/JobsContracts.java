package com.example.badrjobs.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.badrjobs.Adapter.AdapterJobContracts;
import com.example.badrjobs.Adapter.AdapterNotification;
import com.example.badrjobs.Model.ModelJobContracts;
import com.example.badrjobs.Model.ModelNotification;
import com.example.badrjobs.R;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;

import java.util.ArrayList;

public class JobsContracts extends AppCompatActivity {

    RecyclerView recyclerView;
    AdapterJobContracts adapterJobContracts;
    ArrayList<ModelJobContracts> arrayList;

    GridLayoutManager gridLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_contracts);
        init();
    }

    private void init() {
        recyclerView = findViewById(R.id.recycler);
        initRecycler();
    }

    private void initRecycler() {
        arrayList = new ArrayList<>();
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        for (int i = 0; i < 15; i++) {
            ModelJobContracts item = new ModelJobContracts();
            item.setId(i + "");

            arrayList.add(item);
        }
        adapterJobContracts = new AdapterJobContracts(this, arrayList);
        recyclerView.setAdapter(adapterJobContracts);
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