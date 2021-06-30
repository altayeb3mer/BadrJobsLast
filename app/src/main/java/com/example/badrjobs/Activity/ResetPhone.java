package com.example.badrjobs.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.badrjobs.R;
import com.example.badrjobs.Utils.ToolbarClass;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class ResetPhone extends ToolbarClass implements View.OnClickListener {

    AppCompatButton button;
    CountryCodePicker ccp;
    EditText edtPhone;
    LinearLayout progressLay;
    //firebase
    private FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;



    String phone = "";
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_reset_phone);
//        init();
//        //callback method
//        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//            @Override
//            public void onVerificationCompleted(PhoneAuthCredential credential) {
//                Toast.makeText(ResetPhone.this, R.string.sent_error, Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onVerificationFailed(FirebaseException e) {
//                progressLay.setVisibility(View.GONE);
//                Toast.makeText(ResetPhone.this, R.string.sent_error, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCodeSent(@NonNull String verificationId,
//                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
//                HashMap<String, String> hashMap = new HashMap<>();
//                hashMap.put("phone", phone);
//                hashMap.put("codeCountry","00"+ccp.getFullNumber());
//
//                progressLay.setVisibility(View.GONE);
//                Toast.makeText(ResetPhone.this, R.string.sent, Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getApplicationContext(), ConfirmPhone.class);
//                intent.putExtra("phone", "00"+ccp.getFullNumber() + phone);
//                intent.putExtra("verifyId", verificationId);
//                intent.putExtra("phoneReset", true);
//                intent.putExtra("hashMap", hashMap);
//                startActivity(intent);
//                finish();
//
//            }
//        };
//    }

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_reset_phone, "");
        init();
        //callback method
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Toast.makeText(ResetPhone.this, R.string.sent_error, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                progressLay.setVisibility(View.GONE);
                Toast.makeText(ResetPhone.this, R.string.sent_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("phone", phone);
                hashMap.put("codeCountry","00"+ccp.getFullNumber());

                progressLay.setVisibility(View.GONE);
                Toast.makeText(ResetPhone.this, R.string.sent, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ConfirmPhone.class);
                intent.putExtra("phone", "00"+ccp.getFullNumber() + phone);
                intent.putExtra("verifyId", verificationId);
                intent.putExtra("phoneReset", true);
                intent.putExtra("hashMap", hashMap);
                startActivity(intent);
                finish();

            }
        };
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(false);

        progressLay = findViewById(R.id.progressLay);

        button = findViewById(R.id.btn);
        button.setOnClickListener(this);
        ccp = findViewById(R.id.ccp);
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
//                Toast.makeText(ResetPhone.this, "" + ccp.getFullNumberWithPlus(), Toast.LENGTH_SHORT).show();
            }
        });
        edtPhone = findViewById(R.id.phone);

    }

    private void firebaseReg() {
        progressLay.setVisibility(View.VISIBLE);

//        try {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(ccp.getFullNumberWithPlus() +
                                phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(ResetPhone.this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


    }
    private void preReset() {
        phone = edtPhone.getText().toString().trim();
        if (phone.isEmpty()){
            Toast.makeText(this, R.string.pls_type_new_phone, Toast.LENGTH_SHORT).show();
            edtPhone.requestFocus();
            return;
        }
        firebaseReg();


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn:{
                preReset();
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