package com.example.badrjobs.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.badrjobs.Activity.ConfirmDeal;
import com.example.badrjobs.Activity.Login;
import com.example.badrjobs.Activity.MyAds;
import com.example.badrjobs.Activity.PaymentPackage;
import com.example.badrjobs.Activity.ProfileEdit;
import com.example.badrjobs.R;
import com.example.badrjobs.Utils.Api;
import com.example.badrjobs.Utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class FragmentMyProfile extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    ImageView imgProfileEdt, imgLogOut,coverImg;
    View view;
    CardView cardViewMyAds, cardPayPackage,cardViewBalance;
    Context context;
    LinearLayout progressLay;

    TextView textViewDesc,textViewName,textViewPhone,textViewEmail,textViewJob,
            textViewNationality,textViewFixName;

    CircleImageView circleImageViewMyImg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_account, container, false);
        init();
        getMyProfile();
        return view;
    }

    private void init() {
        mSwipeRefreshLayout = view. findViewById(R.id.swipeContainer);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //init textView
        textViewFixName = view.findViewById(R.id.fixName);
        textViewDesc = view.findViewById(R.id.description);
        textViewName = view.findViewById(R.id.name);
        textViewPhone = view.findViewById(R.id.phone);
        textViewEmail = view.findViewById(R.id.email);
        textViewJob = view.findViewById(R.id.job);
        textViewNationality = view.findViewById(R.id.nationality);
        circleImageViewMyImg = view.findViewById(R.id.img);
        coverImg = view.findViewById(R.id.coverImg);

        cardViewBalance = view.findViewById(R.id.balance);
        cardViewBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ConfirmDeal.class));
            }
        });
        cardViewMyAds = view.findViewById(R.id.myAds);
        cardViewMyAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MyAds.class));
            }
        });
        cardPayPackage = view.findViewById(R.id.cardPayPackage);
        cardPayPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PaymentPackage.class));
            }
        });
        progressLay = view.findViewById(R.id.progressLay);
        imgProfileEdt = view.findViewById(R.id.imgProfileEdt);
        imgProfileEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ProfileEdit.class);
                intent.putExtra("profile",imgProfileUrl);
                intent.putExtra("header",imgHeaderUrl);
                intent.putExtra("bio",bio);
                startActivity(intent);
            }
        });
        imgLogOut = view.findViewById(R.id.imgLogOut);
        imgLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                warningMsg();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void warningMsg() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_yes_no);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        try {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        AppCompatButton yes = dialog.findViewById(R.id.yes);
        AppCompatButton no = dialog.findViewById(R.id.no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogOut();
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

    private void doLogOut() {
        progressLay.setVisibility(View.VISIBLE);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Content-Type", "application/json;");
                        ongoing.addHeader("Accept", "application/json");
                        ongoing.addHeader("Content-Type", "application/x-www-form-urlencoded");
                        String token = SharedPrefManager.getInstance(context).getAppToken();
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

        Api.RetrofitLogOut service = retrofit.create(Api.RetrofitLogOut.class);

        Call<String> call = service.putParam();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("code");
                    switch (statusCode) {
                        case "200": {
                            SharedPrefManager.getInstance(getContext()).storeAppToken("");
                            startActivity(new Intent(getActivity(), Login.class));
                            getActivity().finish();
                            break;
                        }
                        default: {
                            Toast.makeText(context, "حدث خطأ اثناء تسجيل الخروج الرجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    progressLay.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
//                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                progressLay.setVisibility(View.GONE);
            }
        });
    }


    String imgProfileUrl="",imgHeaderUrl="",bio="";

    private void getMyProfile() {
        progressLay.setVisibility(View.VISIBLE);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Content-Type", "application/json;");
                        ongoing.addHeader("Accept", "application/json");
                        ongoing.addHeader("Content-Type", "application/x-www-form-urlencoded");
                        ongoing.addHeader("lang", SharedPrefManager.getInstance(context).GetAppLanguage());
                        String token = SharedPrefManager.getInstance(context).getAppToken();
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

        Api.RetrofitGetMyProfile service = retrofit.create(Api.RetrofitGetMyProfile.class);

        Call<String> call = service.putParam();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("code");
                    switch (statusCode) {
                        case "200": {
                            JSONArray responseArray = object.getJSONArray("response");
                            JSONObject jsonObjectOfArray = responseArray.getJSONObject(0);
                            JSONObject dataObject = jsonObjectOfArray.getJSONObject("user");

                            textViewFixName.setText(dataObject.getString("fixName"));
                            bio = dataObject.getString("bio");
                            textViewDesc.setText(bio);
                            textViewName.setText(dataObject.getString("name"));
                            textViewPhone.setText(dataObject.getString("codeCountry")+dataObject.getString("phone"));
                            textViewEmail.setText(dataObject.getString("email"));
                            textViewJob.setText(dataObject.getString("job"));
                            textViewNationality.setText(dataObject.getString("nationality"));

                            //images
                            imgProfileUrl = dataObject.getString("image");
                            imgHeaderUrl = dataObject.getString("header_image");
                            Glide.with(context).load(imgProfileUrl).into(circleImageViewMyImg);
                            Glide.with(context).load(imgHeaderUrl).into(coverImg);

                            //no nationality on response


                            break;
                        }
                        default: {
                            Toast.makeText(context, "حدث خطأ اثناء تسجيل الخروج الرجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    progressLay.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
//                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                progressLay.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        getMyProfile();
    }

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onRefresh() {
        getMyProfile();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);

            }
        }, 1000);
    }
}
