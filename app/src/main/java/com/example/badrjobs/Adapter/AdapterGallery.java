package com.example.badrjobs.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.badrjobs.R;
import com.jsibbold.zoomage.ZoomageView;

import java.util.ArrayList;


public class AdapterGallery extends RecyclerView.Adapter<AdapterGallery.ViewHolder> {

//    Typeface tf;
    int current_page, last_page;
    private ArrayList<String> arrayList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Activity activity;




    //    RelativeLayout container;
    public AdapterGallery(Activity activity, ArrayList<String> r) {
        this.mInflater = LayoutInflater.from(activity);
        this.arrayList = r;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.gallery_item, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder,int indx) {


        try {
            Glide.with(activity).load(arrayList.get(indx))
                    .into(holder.zoomageView);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ZoomageView zoomageView;

        ViewHolder(View itemView) {
            super(itemView);

            zoomageView = itemView.findViewById(R.id.myZoomageView);


        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }



}
