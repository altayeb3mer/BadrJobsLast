package bader.cutShort.badrjobs.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import bader.cutShort.badrjobs.Activity.AdsDetails;
import bader.cutShort.badrjobs.Model.ModelJob;
import com.example.badrjobs.R;
import bader.cutShort.badrjobs.Utils.Api;
import bader.cutShort.badrjobs.Utils.SharedPrefManager;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class AdapterAds extends RecyclerView.Adapter<AdapterAds.ViewHolder> {

//    Typeface tf;
    int current_page, last_page;
    private ArrayList<ModelJob> arrayList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Activity activity;




    //    RelativeLayout container;
    public AdapterAds(Activity activity, ArrayList<ModelJob> r) {
        this.mInflater = LayoutInflater.from(activity);
        this.arrayList = r;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.ads_item, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder,int indx) {

        final ModelJob item = arrayList.get(indx);


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


        if (item.isLiked()){
            Glide.with(activity).load(ContextCompat.getDrawable(activity,R.drawable.ic_favorite_red))
                    .into(holder.imageViewFavorite);
        }else{
            Glide.with(activity).load(ContextCompat.getDrawable(activity,R.drawable.ic_favorite_border))
                    .into(holder.imageViewFavorite);
        }


        holder.imageViewFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               addToFavoriteFun(item,indx,holder.progressLay,holder.imageViewFavorite);
            }
        });
        holder.textViewDate.setText(item.getDate());


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
        TextView textViewTitle,textViewOwnerName, textViewFixName,
        textViewDate;
        LinearLayout progressLay;
        ImageView imageViewFavorite;
        ViewHolder(View itemView) {
            super(itemView);
            progressLay = itemView.findViewById(R.id.progressLay);
            imageViewFavorite = itemView.findViewById(R.id.ic_favorite);
            imageViewProfile = itemView.findViewById(R.id.img);
            container = itemView.findViewById(R.id.container);
            textViewTitle = itemView.findViewById(R.id.title);
            textViewOwnerName = itemView.findViewById(R.id.name);
            textViewFixName = itemView.findViewById(R.id.fixName);
            textViewDate = itemView.findViewById(R.id.edtDate);

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


    private void addToFavoriteFun(ModelJob modelJob,int index,
                                  LinearLayout progressLay,ImageView imageView) {
        progressLay.setVisibility(View.VISIBLE);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Content-Type", "application/json;");
                        ongoing.addHeader("Accept", "application/json");
                        ongoing.addHeader("lang", SharedPrefManager.getInstance(activity).GetAppLanguage());
                        String token = SharedPrefManager.getInstance(activity).getAppToken();
                        ongoing.addHeader("Authorization", token);
                        return chain.proceed(ongoing.build());
                    }
                })
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.ROOT_URL)
                .client(httpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api.RetrofitAddToFavorite service = retrofit.create(Api.RetrofitAddToFavorite.class);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("job_id", modelJob.getId());
        Call<String> call = service.putParam(hashMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String code = object.getString("code");
                    switch (code) {
                        case "200": {

                            if (!modelJob.isLiked()){
//                                Glide.with(activity).load(ContextCompat.getDrawable(activity,R.drawable.ic_favorite_red))
//                                        .into(imageView);
                                modelJob.setLiked(true) ;
                            }else{
//                                Glide.with(activity).load(ContextCompat.getDrawable(activity,R.drawable.ic_favorite_border))
//                                    .into(imageView);
                                modelJob.setLiked(false) ;
                            }

                            arrayList.set(index,modelJob);
//                            AdsActivity.arrayList.set(index,modelJob);
//                            notifyDataSetChanged();
                            notifyItemChanged(index,arrayList);

                            break;
                        }
                        default: {
                            Toast.makeText(activity, "حدث خطأ الرجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }


                    progressLay.setVisibility(View.GONE);

                } catch (Exception e) {

                    e.printStackTrace();
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressLay.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                progressLay.setVisibility(View.GONE);

            }
        });
    }


}
