package com.example.badrjobs.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;


import com.bumptech.glide.Glide;
import com.example.badrjobs.Activity.ImageViewer;
import com.example.badrjobs.R;

import java.util.ArrayList;


public class SlideShow_adapter extends PagerAdapter {

    LayoutInflater inflater;



    public ArrayList<String> urls;
    int detail;

    Activity activity;


    public SlideShow_adapter(Activity activity, ArrayList<String> images) {
        this.activity = activity;

        this.urls=images;
        //this.detail=det;
      }


    @Override
    public int getCount() {

            return urls.size();
        }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(ConstraintLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        inflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view=inflater.inflate(R.layout.slide_show_layout,container,false);
//        TextView textView_trade_det_img_note = view.findViewById(R.id.trade_det_img_note);
        ImageView imgView=view.findViewById(R.id.img);

        if(!urls.isEmpty()){
            Glide.with(activity).load(urls.get(position))
                    .into(imgView);
        }

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ImageViewer.class);
                intent.putExtra("imgUrl",urls.get(position));
                activity.startActivity(intent);
            }
        });

//        imgView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!urls.get(position).equals("")){
//
//
//                    Intent intent = new Intent(activity, IndividualsGallery.class);
//                    intent.putExtra("list",urls);
//                    intent.putExtra("title","معرض الصور");
//                    activity.startActivity(intent);
//
////                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M){
////
////                    }
////                    try {
////                        Intent intent = new Intent(activity, ImageViewer.class);
////                        intent.putExtra("imgUrl",URL.ROOT_IMG+urls.get(position));
////                        activity.startActivity(intent);
////                    }catch (Exception e){
////                        e.printStackTrace();
////                        Toast.makeText(activity, "قريبا", Toast.LENGTH_SHORT).show();
////                    }
//                }
//            }
//        });


        container.addView(view);


        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
