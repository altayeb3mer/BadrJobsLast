package com.example.badrjobs.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.badrjobs.Model.ModelHousing;
import com.example.badrjobs.Model.ModelHousingRequest;
import com.example.badrjobs.R;

import java.util.ArrayList;


public class AdapterHousing extends RecyclerView.Adapter<AdapterHousing.ViewHolder> {

    //    Typeface tf;
    int current_page, last_page;
    private ArrayList<ModelHousing> arrayList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Activity activity;
    ArrayList<String> requestArrayList=new ArrayList<>();

    boolean checked = false;


    //    RelativeLayout container;
    public AdapterHousing(Activity activity, ArrayList<ModelHousing> r) {
        this.mInflater = LayoutInflater.from(activity);
        this.arrayList = r;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.housing_item, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int indx) {

        final ModelHousing item = arrayList.get(indx);
        holder.checkbox.setText(item.getTransName());


        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
//                    checked = true;
                    addToArray(indx);
                } else {
                    removeFromArray(indx);
//                    checked = false;
                }
            }
        });


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

        CheckBox checkbox;

        ViewHolder(View itemView) {
            super(itemView);
            checkbox = itemView.findViewById(R.id.checkbox);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    private void addToArray(int index){
        requestArrayList.add(arrayList.get(index).getId());
    }
    private void removeFromArray(int index){
        requestArrayList.remove(arrayList.get(index).getId());
    }


    public ArrayList<String> getModelHousingRequest(){
//        ModelHousingRequest modelHousingRequest = new ModelHousingRequest();
//        modelHousingRequest.setHousing_types(requestArrayList);
        return requestArrayList;
    }



}
