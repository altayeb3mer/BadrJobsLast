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

import com.example.badrjobs.Activity.JobsActivity;
import com.example.badrjobs.Model.ModelDept;
import com.example.badrjobs.R;

import java.util.ArrayList;


public class AdapterSubDept extends RecyclerView.Adapter<AdapterSubDept.ViewHolder> {

    //    Typeface tf;
    int current_page, last_page;
    //    RelativeLayout container;
    String countryId;
    private ArrayList<ModelDept> arrayList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Activity activity;

    public AdapterSubDept(Activity activity, ArrayList<ModelDept> r, String countryId) {
        this.mInflater = LayoutInflater.from(activity);
        this.arrayList = r;
        this.activity = activity;
        this.countryId = countryId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.dept_item, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ModelDept item = arrayList.get(position);
//        try {
//            Glide.with(activity).load(Api.ROOT_URL+item.getImage())
//                    .into(holder.imageView);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (item.isHasSub()) {
//                    Toast.makeText(activity, "has sub category", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity, JobsActivity.class);
                    intent.putExtra("categoryId", item.getId());
                    intent.putExtra("countryId", countryId);
                    activity.startActivity(intent);
//                } else {
//                    Toast.makeText(activity, "has not", Toast.LENGTH_SHORT).show();
////                    Intent intent = new Intent(activity, SubDeptActivity.class);
////                    activity.startActivity(intent);
//                }
            }
        });
        holder.textViewTitle.setText(item.getName());
        holder.cardView.setCardBackgroundColor(Color.parseColor(item.getBgColor()));
        holder.textViewTitle.setTextColor(Color.parseColor(item.getTextColor()));

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

        CardView cardView;
        ConstraintLayout container;
        TextView textViewTitle;

        ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardBg);
            container = itemView.findViewById(R.id.container);
            textViewTitle = itemView.findViewById(R.id.name);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


}
