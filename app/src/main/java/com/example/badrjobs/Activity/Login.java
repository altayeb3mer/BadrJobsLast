package com.example.badrjobs.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import sdk.chat.core.dao.User;
import sdk.chat.core.session.ChatSDK;
import sdk.chat.core.types.AccountDetails;

public class Login extends AppCompatActivity {

    AppCompatButton btn, btnRegister;
    EditText editTextEmailPhone, editTextPassword;

    String emailOrPhone = "", password = "", lang = "ar";
    LinearLayout progressLay;


    Spinner spinner;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String phone = "";
    String s_token = "";
    private LocaleChangerAppCompatDelegate localeChangerAppCompatDelegate;
    private FirebaseAuth mAuth;
    TextView forgetPassword;

    private void firebaseAuth() {
        progressLay.setVisibility(View.VISIBLE);

//        try {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(Login.this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(false);

        init();
        initSpinnerLang();
//        chatNewUser();

        //callback method
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                progressLay.setVisibility(View.GONE);
                Toast.makeText(Login.this, "تعذر الارسال", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                progressLay.setVisibility(View.GONE);
                Toast.makeText(Login.this, "تعذر الارسال", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("phone", phone);
                hashMap.put("token", s_token);


                Toast.makeText(Login.this, "تم الارسال", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ConfirmPhone.class);
                intent.putExtra("phone", phone);
                intent.putExtra("verifyId", verificationId);
                intent.putExtra("hashMap", hashMap);
                intent.putExtra("firebaseAuth", true);
                startActivity(intent);
                progressLay.setVisibility(View.GONE);
                finish();

            }
        };

        languageBik();
    }

    private void languageBik() {
        try {
            String lang = Locale.getDefault().getLanguage();
            if (lang.equals("en") || lang.equals("ar")) {
                SharedPrefManager.getInstance(getApplicationContext()).storeAppLanguage(lang);
            } else {
                SharedPrefManager.getInstance(getApplicationContext()).storeAppLanguage("en");
            }
//            Toast.makeText(this, lang, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void chatNewUser() {

        User user = ChatSDK.core().getUserNowForEntityID("");
        AccountDetails details1 = AccountDetails.token("cuAzJpPf7tbv7nci6tLfuxjwtKp3");

        AccountDetails details = AccountDetails.signUp("+249116774941", "123456");
        ChatSDK.auth().authenticate(details1).subscribe(new Action() {
            @Override
            public void run() throws Exception {
                Toast.makeText(Login.this, "yes", Toast.LENGTH_SHORT).show();

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Toast.makeText(Login.this, "no", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void chatNewUserAuth() {
        AccountDetails details = AccountDetails.username("Joe@a.com", "Joe123");
        ChatSDK.auth().authenticate(details).subscribe(new Action() {
            @Override
            public void run() throws Exception {
                Toast.makeText(Login.this, "yes", Toast.LENGTH_SHORT).show();

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Toast.makeText(Login.this, "no", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void init() {
        forgetPassword = findViewById(R.id.forgetPassword);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ResetPassword1.class));
            }
        });
        spinner = findViewById(R.id.spinner);
        progressLay = findViewById(R.id.progressLay);
        btnRegister = findViewById(R.id.btnRegister);
        editTextEmailPhone = findViewById(R.id.edtPhoneOrEmail);
        editTextEmailPhone.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence cs, int start, int end, Spanned spanned, int dStart, int dEnd) {
                        // TODO Auto-generated method stub
                        if (cs.equals("")) { // for backspace
                            return cs;
                        }
                        if (cs.toString().matches("[a-zA-Z0-9@._-]+")) {
                            return cs;
                        }
                        if (cs.length() > 0) {
                            return cs.subSequence(0, cs.length() - 1);
                        } else {
                            return cs;
                        }
                    }
                }
        });
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
//                finish();
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
                            s_token = "Bearer" + " " + responseObj.getString("access_token");
//                            SharedPrefManager.getInstance(getApplicationContext()).storeAppToken("Bearer" + " " + appToken);
//                            startActivity(new Intent(getApplicationContext(), MainActivity.class));


                            JSONObject userObj = responseObj.getJSONObject("user");
//                            if (userObj.has("firebase_uid"))
//                                loginChatSdk(userObj.getString("firebase_uid"));

                            phone = userObj.getString("codeCountry") + userObj.getString("phone");
                            firebaseAuth();
//                            loginChatSdk(userObj.getString("phone"), hashMap.get("password"));

//                            chatNewUserAuth();

//                            finish();
                            break;
                        }
                        default: {
                            Toast.makeText(Login.this, R.string.login_error, Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
//                    progressLay.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
//                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(Login.this, R.string.login_error, Toast.LENGTH_SHORT).show();
                    warningMsg(getString(R.string.login_error), false);
                    progressLay.setVisibility(View.GONE);
                }
//                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                progressLay.setVisibility(View.GONE);
            }
        });
    }

    private void loginChatSdk(String userName, String pass) {
        // TODO: 31/07/2021 when user change his phone ==> ChatSDK.currentUser().setEntityID("");
        AccountDetails details = AccountDetails.username(userName, pass);
        ChatSDK.auth().authenticate(details).subscribe(new Action() {
            @Override
            public void run() throws Exception {
                Toast.makeText(Login.this, "yes", Toast.LENGTH_SHORT).show();

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Toast.makeText(Login.this, "no", Toast.LENGTH_SHORT).show();
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


    private void warningMsg(String message, boolean finish) {
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
                if (finish) {
                    finish();
                } else {
                    dialog.dismiss();
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


    //lang controller
    private void changeLocale(String language) {
        Locale locale = new Locale(language);
        LocaleChanger.setLocale(locale);
        ActivityRecreationHelper.recreate(this, true);
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