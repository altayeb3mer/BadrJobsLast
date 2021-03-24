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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class ProfileEdit extends AppCompatActivity implements View.OnClickListener {

    TextView textViewNickName,textViewFullName,textViewJob,textViewContracts,textViewBlockedUser,textViewDeleteAccount,
            textViewPasswordReset, textViewPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        init();
    }

    private void init() {
        progressLay = findViewById(R.id.progressLay);

        textViewPhone = findViewById(R.id.phone);
        textViewPhone.setOnClickListener(this);
        textViewPasswordReset = findViewById(R.id.passwordReset);
        textViewPasswordReset.setOnClickListener(this);
        textViewDeleteAccount = findViewById(R.id.deleteAccount);
        textViewDeleteAccount.setOnClickListener(this);
        textViewBlockedUser = findViewById(R.id.blockedUser);
        textViewBlockedUser.setOnClickListener(this);
        textViewContracts = findViewById(R.id.contracts);
        textViewContracts.setOnClickListener(this);
        textViewNickName = findViewById(R.id.nicename);
        textViewFullName = findViewById(R.id.fullName);
        textViewJob = findViewById(R.id.job);
        textViewNickName.setOnClickListener(this);
        textViewFullName.setOnClickListener(this);
        textViewJob.setOnClickListener(this);
    }

    Dialog dialog;
    private void dialogEdit(String title,String key) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.diallog_edit_field);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        try {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView textViewTitle = dialog.findViewById(R.id.title);
        textViewTitle.setText(title);
        AppCompatButton yes = dialog.findViewById(R.id.yes);
        AppCompatButton no = dialog.findViewById(R.id.no);
        yes.setText("تعديل");
        no.setText("الغاء");

        EditText editTextField = dialog.findViewById(R.id.field);



        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = editTextField.getText().toString().trim();
                if (!value.isEmpty()){
                    doEdition(key,value);
                    dialog.dismiss();
                }else {
                   editTextField.setError("يجب تحديد قيمة للتعديل");
                   editTextField.requestFocus();
                }


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

    LinearLayout progressLay;
    private void doEdition(String key, String value) {
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
        hashMap.put(key,value);
        Call<String> call = service.putParam(hashMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("code");
                    switch (statusCode) {
                        case "200": {
                            warningMsg("تم التعديل بنجاح\n"+value);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.nicename:{
//                dialogEdit("تعديل الايم الامستعار","","");
                break;
            }
            case R.id.fullName:{
                dialogEdit("تعديل الاسم","name");
                break;
            }
            case R.id.job:{
                dialogEdit("تعديل المهنة","job");
                break;
            }
            case R.id.contracts:{
               startActivity(new Intent(getApplicationContext(),JobsContracts.class));
                break;
            }
            case R.id.blockedUser:{
               startActivity(new Intent(getApplicationContext(),BlockedUsersActivity.class));
                break;
            }
            case R.id.deleteAccount:{
               startActivity(new Intent(getApplicationContext(),DeleteAccount.class));
                break;
            }
            case R.id.passwordReset:{
               startActivity(new Intent(getApplicationContext(),ResetPassword2.class));
                break;
            }
            case R.id.phone:{
               startActivity(new Intent(getApplicationContext(),ResetPhone.class));
                break;
            }
        }
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