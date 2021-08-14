package bader.cutShort.badrjobs.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import bader.cutShort.badrjobs.Adapter.AdapterGallery;
import com.example.badrjobs.R;
import bader.cutShort.badrjobs.Utils.ToolbarClass;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;

import java.util.ArrayList;

public class Gallery extends ToolbarClass {


    RecyclerView recyclerView;
    AdapterGallery adapterGallery;
    LinearLayout progressLay;


    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_gallery, "");
        Bundle arg = getIntent().getExtras();
        init();
        if (arg!=null){
            ArrayList<String> arrayList = null;
            try {
                arrayList = (ArrayList<String>) getIntent().getSerializableExtra("list");
            } catch (Exception e) {
                e.printStackTrace();
            }
            initAdapter(arrayList);
        }

    }

    private void initAdapter(ArrayList<String> arrayList) {
        progressLay.setVisibility(View.VISIBLE);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapterGallery = new AdapterGallery(this,arrayList);
        recyclerView.setAdapter(adapterGallery);
        progressLay.setVisibility(View.GONE);
    }

    private void init() {
        progressLay = findViewById(R.id.progressLay);
        recyclerView = findViewById(R.id.recycler);
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