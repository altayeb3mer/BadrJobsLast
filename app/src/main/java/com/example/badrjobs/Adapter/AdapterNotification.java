package com.example.badrjobs.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.badrjobs.Activity.NotificationDetails;
import com.example.badrjobs.Activity.SignatureForm1;
import com.example.badrjobs.Activity.SignatureForm2;
import com.example.badrjobs.Model.ModelNotification;
import com.example.badrjobs.R;

import java.util.ArrayList;


public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.ViewHolder> {

//    Typeface tf;
    int current_page, last_page;
    private ArrayList<ModelNotification> arrayList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Activity activity;
//    RelativeLayout container;
    public AdapterNotification(Activity activity, ArrayList<ModelNotification> r) {
        this.mInflater = LayoutInflater.from(activity);
        this.arrayList = r;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.notification_item, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ModelNotification item = arrayList.get(position);
//        try {
//            Glide.with(activity).load(Api.ROOT_URL+item.getImage())
//                    .into(holder.imageView);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (item.getType()){
                    case "NORMAL":{
                        Intent intent = new Intent(activity, NotificationDetails.class);
                        intent.putExtra("title",item.getTitle());
                        intent.putExtra("body",item.getBody());
                        intent.putExtra("date",item.getDate());
                        activity.startActivity(intent);
                        break;
                    }
                    case "CONTRACT_REQUEST":{
                        Intent intent = new Intent(activity,SignatureForm1.class);
                        intent.putExtra("contractId",item.getContractId());
                        activity.startActivity(intent);
                        break;
                    }
                    case "SECOND_SIDE_SIGNATURE_REQUEST":{
                        Intent intent = new Intent(activity,SignatureForm2.class);
                        intent.putExtra("contractId",item.getContractId());
                        activity.startActivity(intent);
                        break;
                    }
                    default:{
                        Intent intent = new Intent(activity, NotificationDetails.class);
                        intent.putExtra("title",item.getTitle());
                        intent.putExtra("body",item.getBody());
                        intent.putExtra("date",item.getDate());
                        activity.startActivity(intent);
                        break;
                    }
                }

//                Intent intent = new Intent(activity, NotificationDetails.class);
//                Intent intent = new Intent(activity, SignatureForm2.class);
//                activity.startActivity(intent);
            }
        });
        holder.textViewTitle.setText(item.getTitle());
        holder.textViewBody.setText(item.getBody());
        holder.textViewDate.setText(item.getDate());

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

        ImageView imageView;
        ConstraintLayout container;
        TextView textViewTitle, textViewBody,textViewDate;

        ViewHolder(View itemView) {
            super(itemView);
//            layDel = itemView.findViewById(R.id.layDel);
//            imageView = itemView.findViewById(R.id.img);
            container = itemView.findViewById(R.id.container);
            textViewTitle = itemView.findViewById(R.id.title);
            textViewBody = itemView.findViewById(R.id.body);
            textViewDate = itemView.findViewById(R.id.date);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


}
