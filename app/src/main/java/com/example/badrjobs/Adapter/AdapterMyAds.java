package com.example.badrjobs.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.badrjobs.Activity.AdsDetails;
import com.example.badrjobs.Model.ModelJob;
import com.example.badrjobs.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class AdapterMyAds extends RecyclerView.Adapter<AdapterMyAds.ViewHolder> {

//    Typeface tf;
    int current_page, last_page;
    private ArrayList<ModelJob> arrayList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Activity activity;
//    RelativeLayout container;
    public AdapterMyAds(Activity activity, ArrayList<ModelJob> r) {
        this.mInflater = LayoutInflater.from(activity);
        this.arrayList = r;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.my_ads_item, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ModelJob item = arrayList.get(position);

        try {
            if (!item.getOwnerImage().isEmpty()&&!item.getOwnerImage().equals("null")){
                Glide.with(activity).load(item.getOwnerImage())
                        .into(holder.imageViewProfile);

            }else{
                Glide.with(activity).load(ContextCompat.getDrawable(activity,R.drawable.ic_baseline_account_circle_24))
                        .into(holder.imageViewProfile);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
//
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, AdsDetails.class);
                intent.putExtra("id",item.getId());
                activity.startActivity(intent);
            }
        });
        holder.textViewTitle.setText(item.getTitle());
        holder.textViewOwnerName.setText(item.getOwnerName());
        holder.textViewFixName.setText(item.getOwnerNiceName());

        if (item.isActive().equals("YES"))
            holder.imageViewProfile.setBorderColor(activity.getResources().getColor(R.color.colorGreen1));
//
//
//
//        holder.layDel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                deleteAds(item.getType(),item.getId(),position);
//            }
//        });


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

        CircleImageView imageViewProfile;
        ConstraintLayout container;
        TextView textViewTitle, textViewOwnerName, textViewFixName;

        ViewHolder(View itemView) {
            super(itemView);
//            layDel = itemView.findViewById(R.id.layDel);
            imageViewProfile = itemView.findViewById(R.id.img);
            container = itemView.findViewById(R.id.container);
            textViewTitle = itemView.findViewById(R.id.title);
            textViewOwnerName = itemView.findViewById(R.id.name);
            textViewFixName = itemView.findViewById(R.id.fixName);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void updateList(ArrayList<ModelJob> list){
        arrayList = list;
        notifyDataSetChanged();
    }

}
