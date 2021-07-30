package com.example.badrjobs.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import com.example.badrjobs.App;
import com.example.badrjobs.R;
import com.example.badrjobs.Utils.Api;
import com.example.badrjobs.Utils.SharedPrefManager;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
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
import sdk.chat.core.api.SimpleAPI;
import sdk.chat.core.session.ChatSDK;
import sdk.chat.core.types.AccountDetails;

public class ConfirmPhone extends AppCompatActivity {

    EditText edtCode;
    AppCompatButton button;
    LinearLayout progressLay;
    HashMap<String, String> hashMap = new HashMap<>();
    String otp = "", verifyId = "", phone = "";
    private FirebaseAuth mAuth;
    //language controller
    private LocaleChangerAppCompatDelegate localeChangerAppCompatDelegate;

    private boolean phoneReset = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_phone);
        //firebase
        mAuth = FirebaseAuth.getInstance();
        //args
        Bundle args = getIntent().getExtras();
        if (args.containsKey("phoneReset"))
            phoneReset = true;

        phone = args.getString("phone");
        verifyId = args.getString("verifyId");
        try {
            hashMap = (HashMap<String, String>) getIntent().getSerializableExtra("hashMap");
        } catch (Exception e) {

        }
        init();
    }

    private void init() {
        edtCode = findViewById(R.id.edtCode);
        button = findViewById(R.id.btn);
        progressLay = findViewById(R.id.progressLay);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otp = edtCode.getText().toString().trim();
                if (!otp.isEmpty()) {
                    progressLay.setVisibility(View.VISIBLE);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verifyId, otp);
                    signInWithPhoneAuthCredential(credential);
                } else {
                    Toast.makeText(ConfirmPhone.this, "الرجاء ادخال كود مقبول من 6 ارقام", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressLay.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            //store token
                            SharedPrefManager.getInstance(getApplicationContext()).storeFirebaseToken(user.getUid());
                            hashMap.put("firebase_uid", user.getUid());
                            hashMap.put("fcm_token", SharedPrefManager.getInstance(ConfirmPhone.this).getFcmToken());

                            if (phoneReset){
                                doPhoneReset();
                            }else {
                                doRegister();
                            }


                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(ConfirmPhone.this, "الكود خاطئ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    //
    private void doRegister() {
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

        Api.RetrofitRegister service = retrofit.create(Api.RetrofitRegister.class);

        Call<String> call = service.putParam(hashMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("code");
                    switch (statusCode) {
                        case "200": {
//                            Toast.makeText(ConfirmPhone.this, "تم التسجيل بنجاح", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            warningMsg("تم اتمام تسجيلك\n قم بتسجيل الدخول");
                            signUpChatSdk(hashMap.get("email"),hashMap.get("password"));

//                            String fireBaseToken = SharedPrefManager.getInstance(ConfirmPhone.this).getFireBaseToken();
//                            if (!fireBaseToken.isEmpty()){
//                                AccountDetails details = AccountDetails.token(hashMap.get("firebase_uid"));
//                                ChatSDK.auth().authenticate(details).subscribe(new Action() {
//                                    @Override
//                                    public void run() throws Exception {
//                                        Toast.makeText(ConfirmPhone.this, "تم تفعيل الدردشة", Toast.LENGTH_SHORT).show();
//                                        SimpleAPI.updateUser(hashMap.get("fixName"),"");
////                                        signUpChatSdk(hashMap);
//                                        Logout();
//                                    }
//                                }, new Consumer<Throwable>() {
//                                    @Override
//                                    public void accept(Throwable throwable) throws Exception {
//                                        Toast.makeText(ConfirmPhone.this, "خطأ في تفعيل الدردشة", Toast.LENGTH_SHORT).show();
//
//                                    }
//                                });
//                            }

//                            finish();
                            break;
                        }

                        default: {
                            Toast.makeText(ConfirmPhone.this, "حدث خطأ حاول مجددا", Toast.LENGTH_SHORT).show();
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

    private void signUpChatSdk(String username,String password) {
        ChatSDK.auth().authenticate(AccountDetails.signUp(username+"@chatSdk.com",password)).subscribe(new Action() {
            @Override
            public void run() throws Exception {
                Toast.makeText(ConfirmPhone.this, "تم تفعيل الدردشة", Toast.LENGTH_SHORT).show();
                SimpleAPI.updateUser(hashMap.get("fixName"),"");
                Logout();

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Toast.makeText(ConfirmPhone.this, "خطأ في تفعيل الدردشة", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void Logout(){
        ChatSDK.auth().logout().subscribe(new Action() {
            @Override
            public void run() throws Exception {
                Toast.makeText(ConfirmPhone.this, "logged out from chatSdk", Toast.LENGTH_SHORT).show();
//                                        SimpleAPI.updateUser(hashMap.get("fixName"),"");
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Toast.makeText(ConfirmPhone.this, "logged out error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void doPhoneReset() {
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

        Api.RetrofitRegister service = retrofit.create(Api.RetrofitRegister.class);

        Call<String> call = service.putParam(hashMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("code");
                    switch (statusCode) {
                        case "200": {
//                            Toast.makeText(ConfirmPhone.this, "تم التسجيل بنجاح", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            warningMsg(getString(R.string.reset_phone_successfuly)+"\n"+
                                    hashMap.get("codeCountry")+hashMap.get("phone"));

//                            finish();
                            break;
                        }

                        default: {
                            Toast.makeText(ConfirmPhone.this, R.string.error_try_again, Toast.LENGTH_SHORT).show();
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

                if (phoneReset){
                    finish();
                }else{
                    startActivity(new Intent(getApplicationContext(), Login.class));
                }
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
    public void onBackPressed() {
//        super.onBackPressed();
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