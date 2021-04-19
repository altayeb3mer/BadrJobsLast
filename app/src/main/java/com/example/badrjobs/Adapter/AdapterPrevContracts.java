package com.example.badrjobs.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.badrjobs.Activity.SignatureImage;
import com.example.badrjobs.Model.ModelPrevContracts;
import com.example.badrjobs.R;

import java.util.ArrayList;


public class AdapterPrevContracts extends RecyclerView.Adapter<AdapterPrevContracts.ViewHolder> {

//    Typeface tf;
    int current_page, last_page;
    private ArrayList<ModelPrevContracts> arrayList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Activity activity;
//    RelativeLayout container;
    public AdapterPrevContracts(Activity activity, ArrayList<ModelPrevContracts> r) {
        this.mInflater = LayoutInflater.from(activity);
        this.arrayList = r;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.job_contract_item, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ModelPrevContracts item = arrayList.get(position);

        holder.textViewDate.setText(item.getDate().substring(0,10));

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
                Intent intent = new Intent(activity, SignatureImage.class);
                intent.putExtra("contractId",item.getId());
                intent.putExtra("type","prevContract");
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
        CardView container;
        TextView textViewDate, textViewPrice;

        ViewHolder(View itemView) {
            super(itemView);
//            layDel = itemView.findViewById(R.id.layDel);
//            imageView = itemView.findViewById(R.id.img);
            container = itemView.findViewById(R.id.container);
            textViewDate = itemView.findViewById(R.id.date);
//            textViewPrice = itemView.findViewById(R.id.price);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


}
