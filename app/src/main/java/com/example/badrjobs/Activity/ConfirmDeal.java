package com.example.badrjobs.Activity;

import androidx.annotation.NonNull;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.badrjobs.Model.ModelDept;
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

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ConfirmDeal extends ToolbarClass implements View.OnClickListener {

    EditText editTextNo,editTextCode;
    String no="", code="";
    AppCompatButton button;
    LinearLayout layout1,layout2,progressLay;
    private String contractId="";

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_confirm_deal, "");

        init();
    }

    private void init() {
        progressLay = findViewById(R.id.progressLay);
        editTextNo = findViewById(R.id.edtContractNo);
        editTextCode = findViewById(R.id.edtContractCode);
        button = findViewById(R.id.btn);
        button.setOnClickListener(this);
        layout1 = findViewById(R.id.lay1);
        layout2 = findViewById(R.id.lay2);
        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);


    }

    private void confirmContract() {
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

        Api.RetrofitConfirmContract service = retrofit.create(Api.RetrofitConfirmContract.class);
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("uid",no);
        hashMap.put("code",code);
        Call<String> call = service.putParam(hashMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("code");
                    String message = object.getString("message");
                    switch (statusCode) {
                        case "200": {
                            JSONObject objectData = object.getJSONObject("response");
                            contractId = objectData.getString("id");
                            Intent intent = new Intent(ConfirmDeal.this, SignatureImage.class);
                            intent.putExtra("contractId",contractId);
                            intent.putExtra("type","prevContract");
                            startActivity(intent);
//                            warningMsg(message,true);
                            break;
                        }

                        default: {
                            warningMsg(message,false);
                            break;
                        }
                    }
                    progressLay.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
//                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    warningMsg(e.getMessage(),false);
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                progressLay.setVisibility(View.GONE);
                warningMsg("Time out .Try again",false);
            }
        });
    }

    //dialog message
    private void warningMsg(String message,boolean verified) {
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn:{
                preConfirm();
                break;
            }
            case R.id.lay1:{
                warningMsg("الرقم موجود في عقد العمل في اعلى الصفحة جهة اليسار",false);
                break;
            }
            case R.id.lay2:{
                warningMsg("تم ارسالة لصاحب الاعلان عبر الايميل",false);
                break;
            }
        }
    }

    private void preConfirm() {
        no = editTextNo.getText().toString().trim();
        code = editTextCode.getText().toString().trim();
        if (no.isEmpty()){
            editTextNo.setError("الرجاء كتابة رقم العقد");
            editTextNo.requestFocus();
            return;
        }
        if (code.isEmpty()){
            editTextCode.setError("الرجاء كتابة كود العقد");
            editTextCode.requestFocus();
            return;
        }
        confirmContract();


    }
}