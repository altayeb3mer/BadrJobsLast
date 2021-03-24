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

import com.example.badrjobs.Activity.ChattingActivity;
import com.example.badrjobs.Model.ModelChatList;
import com.example.badrjobs.Model.ModelUserBlocked;
import com.example.badrjobs.R;

import java.util.ArrayList;


public class AdapterChatList extends RecyclerView.Adapter<AdapterChatList.ViewHolder> {

//    Typeface tf;
    int current_page, last_page;
    private ArrayList<ModelChatList> arrayList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Activity activity;
//    RelativeLayout container;
    public AdapterChatList(Activity activity, ArrayList<ModelChatList> r) {
        this.mInflater = LayoutInflater.from(activity);
        this.arrayList = r;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.chat_list, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ModelChatList item = arrayList.get(position);
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
                Intent intent = new Intent(activity, ChattingActivity.class);
                activity.startActivity(intent);
            }
        });
//        holder.textViewTitle.setText(item.getTitle());
//        holder.textViewPrice.setText(item.getPrice()+" "+"جنيه سوداني");
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
        TextView textViewTitle, textViewPrice;

        ViewHolder(View itemView) {
            super(itemView);
//            layDel = itemView.findViewById(R.id.layDel);
//            imageView = itemView.findViewById(R.id.img);
            container = itemView.findViewById(R.id.container);
            textViewTitle = itemView.findViewById(R.id.title);
//            textViewPrice = itemView.findViewById(R.id.price);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


}
