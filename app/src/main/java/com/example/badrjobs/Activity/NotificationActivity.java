package com.example.badrjobs.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.badrjobs.Adapter.AdapterNotification;
import com.example.badrjobs.Model.ModelNotification;
import com.example.badrjobs.R;
import com.example.badrjobs.Utils.ToolbarClass;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;

import java.util.ArrayList;

public class NotificationActivity extends ToolbarClass {
    RecyclerView recyclerView;
    AdapterNotification adapterNotification;
    ArrayList<ModelNotification> arrayList;
    GridLayoutManager gridLayoutManager;
    ImageView icNotification;

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_notification, "");
        init();
    }

    private void init() {
        arrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler);
        icNotification = findViewById(R.id.icNotification);
        icNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),JobsContracts.class));
            }
        });


        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        for (int i = 0; i < 15; i++) {
            ModelNotification item = new ModelNotification();
            item.setId(i + "");

            arrayList.add(item);
        }
        adapterNotification = new AdapterNotification(this, arrayList);
        recyclerView.setAdapter(adapterNotification);
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