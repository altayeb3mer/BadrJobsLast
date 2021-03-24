package com.example.badrjobs.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.badrjobs.R;
import com.example.badrjobs.Utils.Api;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

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

import static android.icu.text.Normalizer.YES;

public class Register extends AppCompatActivity {
    LinearLayout progressLay;
    ConstraintLayout container;
    CountryCodePicker ccp;
    EditText edtPhone, editTextName, editTextNickname, editTextPassword1, editTextPassword2,
            editTextEmail, editTextConfirmEmail, editTextJob;
    AppCompatButton btn;
    AppCompatCheckBox checkbox;
    String phone = "", name = "", nickname = "", password1 = "", password2 = "", email1 = "", email2 = "", job = "";
    boolean agreeWithTerm = false;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    //firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        //firebase
        mAuth = FirebaseAuth.getInstance();
        mAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(false);


        progressLay = findViewById(R.id.progressLay);
        container = findViewById(R.id.container);
        checkbox = findViewById(R.id.checkbox);
        editTextName = findViewById(R.id.name);
        editTextNickname = findViewById(R.id.nicename);
        editTextPassword1 = findViewById(R.id.password1);
        editTextPassword2 = findViewById(R.id.password2);
        editTextEmail = findViewById(R.id.email);
        editTextConfirmEmail = findViewById(R.id.confirm_email);
        editTextJob = findViewById(R.id.job);
        ccp = findViewById(R.id.ccp);
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                Toast.makeText(Register.this, "" + ccp.getFullNumberWithPlus(), Toast.LENGTH_SHORT).show();
            }
        });
        edtPhone = findViewById(R.id.phone);
        btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = editTextName.getText().toString().trim();
                nickname = editTextNickname.getText().toString().trim();
                password1 = editTextPassword1.getText().toString().trim();
                password2 = editTextPassword2.getText().toString().trim();
                email1 = editTextEmail.getText().toString().trim();
                email2 = editTextConfirmEmail.getText().toString().trim();
                job = editTextJob.getText().toString().trim();
                phone = edtPhone.getText().toString().trim();

                preRegister();

//                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
        //checkBox
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    agreeWithTerm = true;
                } else {
                    agreeWithTerm = false;
                }
            }
        });

        //callback method
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Toast.makeText(Register.this, "تعذر الارسال", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                progressLay.setVisibility(View.GONE);
                Toast.makeText(Register.this, "تعذر الارسال", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("name",name);
                hashMap.put("fixName",nickname);
                hashMap.put("email",email1);
                hashMap.put("phone",phone);
                hashMap.put("codeCountry",ccp.getFullNumberWithPlus());
                hashMap.put("password",password1);
                hashMap.put("password_confirmation",password2);


                progressLay.setVisibility(View.GONE);
                Toast.makeText(Register.this, "تم الارسال", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ConfirmPhone.class);
                intent.putExtra("phone", ccp.getFullNumber() + phone);
                intent.putExtra("verifyId", verificationId);
                intent.putExtra("hashMap", hashMap);
                startActivity(intent);

            }
        };

    }

    private void preRegister() {
        if (!name.equals("") && !nickname.equals("") && !password1.equals("") && !password2.equals("") &&
                !email1.equals("") && !email2.equals("") && !job.equals("") && !phone.equals("")) {
            if (agreeWithTerm) {
                if (!Patterns.EMAIL_ADDRESS.matcher(email1).matches()){
                    editTextEmail.setError("الرجاء كتابة بريد الكتروني صحيح");
                    editTextEmail.requestFocus();
                    return;
                }
                if (!email1.equals(email2)) {
                    editTextEmail.setError("البريد الالكتروني غير متطابق");
                    editTextEmail.requestFocus();
                    return;
                }
                if (!password1.equals(password2)) {
                    editTextPassword1.setError("كلمة السر غير متطابقة");
                    editTextPassword1.requestFocus();
                    return;
                }
                firebaseReg();
            } else {
                showSnackBar("لم تقم بالموافقة على الشروط والاحكام");
            }

        } else {
            showSnackBar("الرجاء تعبئة كل الحقول");
        }


    }

    private void firebaseReg() {
        progressLay.setVisibility(View.VISIBLE);

//        try {
            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber(ccp.getFullNumberWithPlus()+
                                    phone)       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(Register.this)                 // Activity (for callback binding)
                            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);



    }


    public void showSnackBar(String msg) {
        Snackbar snackbar = Snackbar.make(container, msg, Snackbar.LENGTH_LONG);
        View snackview = snackbar.getView();
        snackview.setBackgroundColor(Color.RED);
        TextView masseage;
        masseage = snackview.findViewById(R.id.snackbar_text);
        masseage.setTextSize(16);
        masseage.setTextColor(Color.WHITE);
        snackbar.show();
    }

//    private String getToken(){
//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w("FCM", "Failed", task.getException());
//                            return;
//                        }
//
//                        String token = task.getResult().getToken();
//                        Log.i("FCM", "Current token=" + token);
//                    }
//                });
//    }


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