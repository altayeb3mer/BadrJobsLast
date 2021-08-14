package bader.cutShort.badrjobs.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import bader.cutShort.badrjobs.Model.ModelUserBlocked;
import com.example.badrjobs.R;
import bader.cutShort.badrjobs.Utils.Api;
import bader.cutShort.badrjobs.Utils.SharedPrefManager;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class AdapterBlockedUser extends RecyclerView.Adapter<AdapterBlockedUser.ViewHolder> {

//    Typeface tf;
    int current_page, last_page;
    private ArrayList<ModelUserBlocked> arrayList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Activity activity;
    LinearLayout progressLay,noDataLay;
    public AdapterBlockedUser(Activity activity, ArrayList<ModelUserBlocked> r,LinearLayout progressLay,LinearLayout noDataLay) {
        this.mInflater = LayoutInflater.from(activity);
        this.arrayList = r;
        this.activity = activity;
        this.progressLay = progressLay;
        this.noDataLay = noDataLay;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.blocked_user_item, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ModelUserBlocked item = arrayList.get(position);

            Glide.with(activity).load(item.getImgProfile())
                    .into(holder.imageView);

        holder.textViewName.setText(item.getName());
        holder.textViewFixName.setText(item.getFixName());

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unBlockUser(item.getId(),position);
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

        ImageView imageView;
        TextView textViewName, textViewFixName,button;

        ViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.name);
            textViewFixName = itemView.findViewById(R.id.fixName);
            imageView = itemView.findViewById(R.id.img);

            button = itemView.findViewById(R.id.blockBtn);


        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());

        }
    }


    private void unBlockUser(String userId,int index) {
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

        Api.RetrofitUnBlockUser service = retrofit.create(Api.RetrofitUnBlockUser.class);
        Call<String> call = service.putParam(userId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String code = object.getString("code");
                    if (code.equals("200")){
                        boolean success = object.getBoolean("success");
                        if (success){
                            arrayList.remove(index);
                            notifyItemRemoved(index);
                            notifyDataSetChanged();
                            if (arrayList.isEmpty())
                                noDataLay.setVisibility(View.VISIBLE);

                        }else{
                            Toast.makeText(activity, R.string.error_try_again, Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(activity, R.string.error_try_again, Toast.LENGTH_SHORT).show();
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

    //dialog message
    private void warningMsg(String message) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_yes_no);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        try {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView textViewMsg = dialog.findViewById(R.id.msg);
        textViewMsg.setText(message);
        AppCompatButton yes = dialog.findViewById(R.id.yes);
        AppCompatButton no = dialog.findViewById(R.id.no);
        no.setVisibility(View.GONE);
        yes.setText("موافق");


        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (verified){
//                    Intent intent = new Intent(ConfirmDeal.this, SignatureImage.class);
//                    intent.putExtra("contractId",contractId);
//                    intent.putExtra("type","prevContract");
//                    startActivity(intent);
//                }else{
//                    dialog.dismiss();
//                }
                dialog.dismiss();

            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

}
