package com.example.badrjobs.Activity;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.badrjobs.R;
import com.example.badrjobs.Utils.ToolbarClass;
import com.jsibbold.zoomage.ZoomageView;

public class ImageViewer extends ToolbarClass {
    String imgUrl = "";
    ZoomageView zoomageView;

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_image_viewer, "");
        changeStatusBarColor();
        init();
        Bundle args = getIntent().getExtras();
        if (args != null) {
            imgUrl = args.getString("imgUrl");

            Glide.with(getApplicationContext())
                    .load(imgUrl)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(zoomageView);

        }
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.black));
        }
    }

    private void init() {
        zoomageView = findViewById(R.id.myZoomageView);
    }

}