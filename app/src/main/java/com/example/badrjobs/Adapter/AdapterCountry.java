package com.example.badrjobs.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.badrjobs.Activity.DeptsActivity;
import com.example.badrjobs.Model.ModelCountry;
import com.example.badrjobs.R;
import com.example.badrjobs.Utils.Api;

import java.util.ArrayList;


public class AdapterCountry extends RecyclerView.Adapter<AdapterCountry.ViewHolder> {

//    Typeface tf;
    int current_page, last_page;
    private ArrayList<ModelCountry> arrayList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Activity activity;
//    RelativeLayout container;
    public AdapterCountry(Activity activity, ArrayList<ModelCountry> r) {
        this.mInflater = LayoutInflater.from(activity);
        this.arrayList = r;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.country_item, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ModelCountry item = arrayList.get(position);
        try {
            Glide.with(activity).load(item.getImage())
                    .into(holder.imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
//
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, DeptsActivity.class);
                intent.putExtra("countryId",item.getId());
                activity.startActivity(intent);
            }
        });
        holder.textViewTitle.setText(item.getName());



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

        ImageView imageView;
        ConstraintLayout container;
        TextView textViewTitle;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img);
            container = itemView.findViewById(R.id.container);
            textViewTitle = itemView.findViewById(R.id.title);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


}
