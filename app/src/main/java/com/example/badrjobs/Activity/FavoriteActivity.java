package com.example.badrjobs.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

import com.example.badrjobs.Adapter.AdapterJobs;
import com.example.badrjobs.GlobalVar;
import com.example.badrjobs.Model.ModelJob;
import com.example.badrjobs.R;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {

    TextView textViewTitle;
    RecyclerView recyclerView;
    AdapterJobs adapterDept;
    ArrayList<ModelJob> arrayList;
    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorate);
        init();
    }


    private void init() {
        //textView title
        textViewTitle = findViewById(R.id.txtTitle);
//        SpannableString content = new SpannableString("عند ما يكون الإعلان منتهي سوف يتم حذفه من المفضلة تلقائي");
//        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
//        textViewTitle.setText(content);
        textViewTitle.setText(new GlobalVar().underLinerTextView("عند ما يكون الإعلان منتهي سوف يتم حذفه من المفضلة تلقائي"));

        arrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler);

        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        for (int i = 0; i < 15; i++) {
            ModelJob item = new ModelJob();
            item.setId(i+"");

            arrayList.add(item);
        }
        adapterDept = new AdapterJobs(this,arrayList);
        recyclerView.setAdapter(adapterDept);
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