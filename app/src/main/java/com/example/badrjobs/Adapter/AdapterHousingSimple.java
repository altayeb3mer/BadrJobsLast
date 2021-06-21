package com.example.badrjobs.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.badrjobs.Activity.AdsActivity;
import com.example.badrjobs.Activity.SubDeptActivity;
import com.example.badrjobs.Model.ModelDept;
import com.example.badrjobs.R;

import java.util.ArrayList;


public class AdapterHousingSimple extends RecyclerView.Adapter<AdapterHousingSimple.ViewHolder> {

//    Typeface tf;
    int current_page, last_page;
    private ArrayList<String> arrayList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Activity activity;
//    RelativeLayout container;
    public AdapterHousingSimple(Activity activity, ArrayList<String> r) {
        this.mInflater = LayoutInflater.from(activity);
        this.arrayList = r;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.housing_list_item, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.textViewTitle.setText(arrayList.get(position));


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

//        CardView cardView;
//        ConstraintLayout container;
        TextView textViewTitle;

        ViewHolder(View itemView) {
            super(itemView);
//            layDel = itemView.findViewById(R.id.layDel);
//            cardView = itemView.findViewById(R.id.cardBg);
//            container = itemView.findViewById(R.id.container);
            textViewTitle = itemView.findViewById(R.id.title);
//            textViewPrice = itemView.findViewById(R.id.price);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


}
