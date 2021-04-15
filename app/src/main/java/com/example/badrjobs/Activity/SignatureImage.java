package com.example.badrjobs.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.example.badrjobs.R;
import com.example.badrjobs.Utils.Api;
import com.example.badrjobs.Utils.SharedPrefManager;
import com.example.badrjobs.Utils.ToolbarClass;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SignatureImage extends ToolbarClass {

    View view1,view2;
    ImageView imageViewSignature;
    AppCompatButton buttonSignature;

    String contractId="",type="",contractImgUrl="";

    //language controller
    private LocaleChangerAppCompatDelegate localeChangerAppCompatDelegate;

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.signature_image, "");
        Bundle arg = getIntent().getExtras();
        if (arg!=null){
            contractId = arg.getString("contractId");
            type = arg.getString("type");
        }
        init();
        getContractImage();
    }

    private void init() {
        progressLay = findViewById(R.id.progressLay);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        imageViewSignature = findViewById(R.id.signatureImg);
        imageViewSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!contractImgUrl.isEmpty()){
                    Intent intent = new Intent(getApplicationContext(),ImageViewer.class);
                    intent.putExtra("imgUrl",contractImgUrl);
                    startActivity(intent);
                }
            }
        });
        buttonSignature = findViewById(R.id.btnSign);
        buttonSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (type) {
                    case "CONTRACT_REQUEST": {
                        Intent intent = new Intent(SignatureImage.this, SignatureForm1.class);
                        intent.putExtra("contractId", contractId);
                        startActivity(intent);
                        break;
                    }
                    case "SECOND_SIDE_SIGNATURE_REQUEST": {
                        Intent intent = new Intent(SignatureImage.this, SignatureForm2.class);
                        intent.putExtra("contractId", contractId);
                        startActivity(intent);
                        break;
                    }
                }
            }
        });
    }

    LinearLayout progressLay;

    private void getContractImage() {
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

        Api.RetrofitContractDetails service = retrofit.create(Api.RetrofitContractDetails.class);

        Call<String> call = service.putParam(contractId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    JSONObject data = object.getJSONObject("data");
                    if (data.getBoolean("isFirstSideSign")){
                        view1.setBackgroundColor(getResources().getColor(R.color.colorGreen1));
                    }
                    if (data.getBoolean("isSecondSideSign")){
                        view2.setBackgroundColor(getResources().getColor(R.color.colorGreen1));
                    }
                    contractImgUrl = data.getString("image");
                    Glide.with(getApplicationContext()).load(contractImgUrl).into(imageViewSignature);
                    switch (type) {
                        case "CONTRACT_REQUEST": {
                            if (data.getBoolean("isFirstSideSign")){
                                buttonSignature.setVisibility(View.GONE);
                            }
                            break;
                        }
                        case "SECOND_SIDE_SIGNATURE_REQUEST": {
                            if (data.getBoolean("isSecondSideSign")){
                                buttonSignature.setVisibility(View.GONE);
                            }
                            break;
                        }
                    }


                    progressLay.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                progressLay.setVisibility(View.GONE);
            }
        });
    }


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