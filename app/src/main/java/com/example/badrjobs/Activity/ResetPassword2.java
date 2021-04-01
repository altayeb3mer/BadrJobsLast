package com.example.badrjobs.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.badrjobs.R;
import com.example.badrjobs.Utils.Api;
import com.example.badrjobs.Utils.SharedPrefManager;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;

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

public class ResetPassword2 extends AppCompatActivity implements View.OnClickListener {

    EditText editTextOldPass,editTextNewPass1,editTextNewPass2;
    AppCompatButton button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password2);

        init();


    }

    private void init() {
        progressLay = findViewById(R.id.progressLay);
        editTextOldPass = findViewById(R.id.oldPass);
        editTextNewPass1 = findViewById(R.id.newPass1);
        editTextNewPass2 = findViewById(R.id.newPass2);
        button = findViewById(R.id.btn);
        button.setOnClickListener(this);
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
                preChangePass();
                break;
            }
        }
    }
    String oldPass="",newPass1="",newPass2="";
    private void preChangePass() {
        oldPass = editTextOldPass.getText().toString().trim();
        newPass1 = editTextNewPass1.getText().toString().trim();
        newPass2 = editTextNewPass2.getText().toString().trim();

        if (oldPass.isEmpty()){
            editTextOldPass.setError("الرجاء كتابة كلمة السر الحالية");
            editTextOldPass.requestFocus();
            return;
        }
        if (newPass1.isEmpty()){
            editTextNewPass1.setError("الرجاء كتابة كلمة السر  الجديدة");
            editTextNewPass1.requestFocus();
            return;
        }
        if (newPass2.isEmpty()){
            editTextNewPass2.setError("الرجاء تأكيد كلمة السر الجديدة");
            editTextNewPass2.requestFocus();
            return;
        }
        if (!newPass2.equals(newPass1)){
            editTextNewPass2.setError("كلمة السر الجديدة غير متطابقة");
            editTextNewPass2.requestFocus();
            return;
        }
        doEdition();
     //todo
    }




    LinearLayout progressLay;
    private void doEdition() {
        progressLay.setVisibility(View.VISIBLE);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Content-Type", "application/json;");
                        ongoing.addHeader("Accept", "application/json");
                        ongoing.addHeader("Content-Type", "application/x-www-form-urlencoded");
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

        Api.RetrofitUpdateProfile service = retrofit.create(Api.RetrofitUpdateProfile.class);
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("password",newPass1);
        hashMap.put("old_password",oldPass);
        Call<String> call = service.putParam(hashMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("code");
                    switch (statusCode) {
                        case "200": {
                            warningMsg("تم تغيير كلمة السر");
//                            SharedPrefManager.getInstance(getContext()).storeAppToken("");
//                            startActivity(new Intent(getActivity(),Login.class));
//                            getActivity().finish();
                            break;
                        }
                        default: {
                            warningMsg("خطأ في التحويل الرجاء المحاولة مره اخرى");
                            break;
                        }
                    }
                    progressLay.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    warningMsg(e.getMessage());
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                progressLay.setVisibility(View.GONE);
                warningMsg("time out");
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