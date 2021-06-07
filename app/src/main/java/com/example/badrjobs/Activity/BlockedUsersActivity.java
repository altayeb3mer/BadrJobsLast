package com.example.badrjobs.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.badrjobs.Adapter.AdapterBlockedUser;
import com.example.badrjobs.Model.ModelUserBlocked;
import com.example.badrjobs.R;
import com.example.badrjobs.Utils.ToolbarClass;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;

import java.util.ArrayList;

public class BlockedUsersActivity extends ToolbarClass {

    TextView textViewTitle;
    RecyclerView recyclerView;
    AdapterBlockedUser adapterBlockedUser;
    ArrayList<ModelUserBlocked> arrayList;
    GridLayoutManager gridLayoutManager;



    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_blocked_users, "");
        init();
    }

    private void init() {
        //textView title
//        textViewTitle = findViewById(R.id.txtTitle);
//        SpannableString content = new SpannableString("عند ما يكون الإعلان منتهي سوف يتم حذفه من المفضلة تلقائي");
//        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
//        textViewTitle.setText(content);


        arrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler);

        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        for (int i = 0; i < 15; i++) {
            ModelUserBlocked item = new ModelUserBlocked();
            item.setId(i+"");

            arrayList.add(item);
        }
        adapterBlockedUser = new AdapterBlockedUser(this,arrayList);
        recyclerView.setAdapter(adapterBlockedUser);
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