package com.example.badrjobs.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.badrjobs.R;
import com.example.badrjobs.Utils.Api;
import com.example.badrjobs.Utils.SharedPrefManager;
import com.example.badrjobs.Utils.ToolbarClass;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
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

public class UserViewedProfile extends ToolbarClass {

    private String userId="",fcmToken="",
    name="",fixName="",image="";
    ImageView imageViewHeader,imageViewFlag;
    CircleImageView circleImageViewProfile;
    TextView textViewFix, textViewBio,textViewName,textViewEmail,textViewJob;
    AppCompatButton buttonBlock;
    LinearLayout layoutChat;



    private void init() {
        progressLay = findViewById(R.id.progressLay);
        imageViewHeader = findViewById(R.id.headerImg);
        imageViewFlag = findViewById(R.id.imgFlag);
        circleImageViewProfile = findViewById(R.id.img);
        textViewFix = findViewById(R.id.fixName);
        textViewBio = findViewById(R.id.bio);
        textViewName = findViewById(R.id.name);
        textViewEmail = findViewById(R.id.email);
        textViewJob = findViewById(R.id.job);
        buttonBlock = findViewById(R.id.btnBlock);
        layoutChat = findViewById(R.id.layChat);

    }
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_user_viewed_profile, "");
        Bundle args = getIntent().getExtras();
        if (args!=null){
            userId = args.getString("userId");
        }
        init();
        getProfile();
    }



    LinearLayout progressLay;

    private void getProfile() {
        progressLay.setVisibility(View.VISIBLE);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Content-Type", "application/json;");
                        ongoing.addHeader("Accept", "application/json");
                        ongoing.addHeader("lang", SharedPrefManager.getInstance(getApplicationContext()).GetAppLanguage());
                        String token = SharedPrefManager.getInstance(getApplicationContext()).getAppToken();
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

        Api.RetrofitShowProfileOther service = retrofit.create(Api.RetrofitShowProfileOther.class);
        Call<String> call = service.putParam(userId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("code");
                    switch (statusCode) {
                        case "200": {
                            JSONArray array = object.getJSONArray("response");
                            JSONObject objectData = array.getJSONObject(0);
                            JSONObject userData = objectData.getJSONObject("user");

                            Glide.with(UserViewedProfile.this).
                                    load(userData.getString("header_image")).into(imageViewHeader);
                            Glide.with(UserViewedProfile.this).
                                    load(userData.getString("image")).into(imageViewFlag);//todo load region flag here

                            image = userData.getString("image");
                            Glide.with(UserViewedProfile.this).
                                    load(image).into(circleImageViewProfile);

                            fixName = userData.getString("fixName");
                            textViewFix.setText(fixName);
                            textViewBio.setText(userData.getString("bio"));

                            name = userData.getString("name");
                            textViewName.setText(name);
                            textViewEmail.setText(userData.getString("email_verified_at"));
                            textViewJob.setText(userData.getString("job"));

                            fcmToken = userData.getString("job");


                            break;
                        }

                        default: {
                            Intent intent = new Intent(UserViewedProfile.this,BlockedUser.class);
                            intent.putExtra("name",name);
                            intent.putExtra("fixName",fixName);
                            intent.putExtra("image",image);
                            startActivity(intent);
                            break;
                        }
                    }
                    progressLay.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
//                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    warningMsg(e.getMessage());
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                progressLay.setVisibility(View.GONE);
                warningMsg("Time out .Try again");
            }
        });
    }


    //dialog message
    private void warningMsg(String message) {
        final Dialog dialog = new Dialog(this);
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





    //language controller
    private LocaleChangerAppCompatDelegate localeChangerAppCompatDelegate;
    @NonNull
    @Override
    public AppCompatDelegate getDelegate() {
        if (localeChangerAppCompatDelegate == null) {
            localeChangerAppCompatDelegate = new LocaleChangerAppCompatDelegate(super.getDelegate());
        }

        return localeChangerAppCompatDelegate;
    }
    @Override
    protected void onResume() {
        super.onResume();
        ActivityRecreationHelper.onResume(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityRecreationHelper.onDestroy(this);
    }

}