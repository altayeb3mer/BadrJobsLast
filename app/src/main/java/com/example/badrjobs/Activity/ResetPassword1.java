package com.example.badrjobs.Activity;

import androidx.annotation.NonNull;
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

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class ResetPassword1 extends ToolbarClass {

    AppCompatButton btn;
    EditText newPass1,newPass2,edtPhone;
    LinearLayout progressLay ;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_reset_password1);
//        mAuth = FirebaseAuth.getInstance();
//        mAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(false);
//
//        init();
//        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//            @Override
//            public void onVerificationCompleted(PhoneAuthCredential credential) {
//                progressLay.setVisibility(View.GONE);
//                Toast.makeText(ResetPassword1.this, "تعذر الارسال", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onVerificationFailed(FirebaseException e) {
//                progressLay.setVisibility(View.GONE);
//                Toast.makeText(ResetPassword1.this, "تعذر الارسال", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCodeSent(@NonNull String verificationId,
//                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
//                HashMap<String, String> hashMap = new HashMap<>();
//
//                Toast.makeText(ResetPassword1.this, "تم الارسال", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getApplicationContext(), ConfirmPhone.class);
//                intent.putExtra("phone", phone);
//                intent.putExtra("verifyId", verificationId);
//                intent.putExtra("hashMap", hashMap);
//                intent.putExtra("phoneReset", true);
//                intent.putExtra("newPassword", pass1);
//                startActivity(intent);
//                progressLay.setVisibility(View.GONE);
//                finish();
//
//            }
//        };
//
//    }


    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_reset_password1, "");
        mAuth = FirebaseAuth.getInstance();
        mAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(false);

        init();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                progressLay.setVisibility(View.GONE);
                Toast.makeText(ResetPassword1.this, "تعذر الارسال", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                progressLay.setVisibility(View.GONE);
                Toast.makeText(ResetPassword1.this, "تعذر الارسال", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                HashMap<String, String> hashMap = new HashMap<>();

                Toast.makeText(ResetPassword1.this, "تم الارسال", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ConfirmPhone.class);
                intent.putExtra("phone", phone);
                intent.putExtra("verifyId", verificationId);
                intent.putExtra("hashMap", hashMap);
                intent.putExtra("phoneReset", true);
                intent.putExtra("newPassword", pass1);
                startActivity(intent);
                progressLay.setVisibility(View.GONE);
                finish();

            }
        };
    }

    String pass1="",pass2="",phone="";
    private void init() {
        newPass1 = findViewById(R.id.newPass1);
        newPass2 = findViewById(R.id.newPass2);
        edtPhone = findViewById(R.id.edtPhone);
        progressLay = findViewById(R.id.progressLay);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pass1 = newPass1.getText().toString().trim();
                pass2 = newPass2.getText().toString().trim();
                phone = edtPhone.getText().toString().trim();


                if (phone.isEmpty()){
                    edtPhone.setError("هذا الحقل مطلوب");
                    edtPhone.requestFocus();
                    return;
                }
                if (pass1.isEmpty()){
                    newPass1.setError("هذا الحقل مطلوب");
                    newPass1.requestFocus();
                    return;
                }
                if (pass2.isEmpty()){
                    newPass2.setError("هذا الحقل مطلوب");
                    newPass2.requestFocus();
                    return;
                }

                if (!pass2.equals(pass1)){
                    newPass2.setError("كلمة السر غير متطابقة");
                    newPass2.requestFocus();
                    return;
                }
                changePassword();

            }
        });
    }


    private FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private void changePassword() {
        progressLay.setVisibility(View.VISIBLE);

//        try {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(ResetPassword1.this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

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