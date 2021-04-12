package com.example.badrjobs.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.example.badrjobs.R;
import com.example.badrjobs.Utils.ToolbarClass;
import com.jsibbold.zoomage.ZoomageView;

public class ImageViewer extends ToolbarClass {
    String imgUrl="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        changeStatusBarColor();
        init();
        Bundle args = getIntent().getExtras();
        if (args!=null){
            imgUrl = args.getString("imgUrl");
            Glide.with(ImageViewer.this).load(imgUrl)
                    .into(myZoomageView);
        }
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.black));
        }
    }

    ZoomageView myZoomageView;
    private void init() {
        myZoomageView = findViewById(R.id.myZoomageView);
    }

}