package com.example.badrjobs.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import com.example.badrjobs.R;
import com.example.badrjobs.Utils.Api;
import com.example.badrjobs.Utils.SharedPrefManager;
import com.franmontiel.localechanger.LocaleChanger;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Login extends AppCompatActivity {

    AppCompatButton btn, btnRegister;
    EditText editTextEmailPhone, editTextPassword;

    String emailOrPhone = "", password = "", lang = "ar";
    LinearLayout progressLay;


    Spinner spinner;
    private LocaleChangerAppCompatDelegate localeChangerAppCompatDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        initSpinnerLang();
    }

    private void init() {
        spinner = findViewById(R.id.spinner);
        progressLay = findViewById(R.id.progressLay);
        btnRegister = findViewById(R.id.btnRegister);
        editTextEmailPhone = findViewById(R.id.edtPhoneOrEmail);
        editTextPassword = findViewById(R.id.password);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emailOrPhone = editTextEmailPhone.getText().toString().trim();
                password = editTextPassword.getText().toString().trim();
                if (!emailOrPhone.isEmpty() && !password.isEmpty()) {
                    doLogin();
                } else {
                    Toast.makeText(Login.this, R.string.fill_fields, Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
                finish();
            }
        });
    }

    private void doLogin() {
        progressLay.setVisibility(View.VISIBLE);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Content-Type", "application/json;");
                        ongoing.addHeader("Accept", "application/json");
//                        ongoing.addHeader("Content-Type", "application/x-www-form-urlencoded");
//                        String token = SharedPrefManager.getInstance(context).GetToken();
//                        ongoing.addHeader("Authorization", token);
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

        Api.RetrofitLogin service = retrofit.create(Api.RetrofitLogin.class);

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("credential", emailOrPhone);
        hashMap.put("password", password);
        hashMap.put("fcm_token", SharedPrefManager.getInstance(getApplicationContext()).getFcmToken());

        Call<String> call = service.putParam(hashMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("code");
                    switch (statusCode) {
                        case "200": {
                            JSONObject responseObj = object.getJSONObject("response");
                            String appToken = responseObj.getString("access_token");
                            SharedPrefManager.getInstance(getApplicationContext()).storeAppToken("Bearer" + " " +appToken);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                            break;
                        }
                        default: {
                            Toast.makeText(Login.this, R.string.login_error, Toast.LENGTH_SHORT).show();
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



    private void initSpinnerLang() {
//        String[] array = {"اختر اللغة","العربية", "English"};
        String[] array = getResources().getStringArray(R.array.lang);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, array) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = null;
                v = super.getDropDownView(position, null, parent);
//                v.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                // If this is the selected item position
//                if (position == 0) {
//                    v.setBackgroundColor(getResources().getColor(R.color.spinnerHeaderItem));
//                }
//
//                else {
//                    if (position % 2 == 0) {
//                        v.setBackgroundColor(getResources().getColor(R.color.spinner_bg_design1));
//                    } else {
//                        v.setBackgroundColor(getResources().getColor(R.color.spinner_bg_design2));
//                    }
//
//                }
                return v;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1: {
                        lang = "ar";
                        SharedPrefManager.getInstance(getApplicationContext()).storeAppLanguage(lang);
                        changeLocale(lang);
                        break;
                    }
                    case 2: {
                        lang = "en";
                        SharedPrefManager.getInstance(getApplicationContext()).storeAppLanguage(lang);
                        changeLocale(lang);
                        break;
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void changeLocale(String language) {
        Locale locale = new Locale(language);
        LocaleChanger.setLocale(locale);
        ActivityRecreationHelper.recreate(this,true);
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