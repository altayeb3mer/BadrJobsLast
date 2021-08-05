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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.badrjobs.GlobalVar;
import com.example.badrjobs.R;
import com.example.badrjobs.Utils.SharedPrefManager;
import com.example.badrjobs.Utils.ToolbarClass;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class ConfirmPhoneReset extends ToolbarClass {

//    TextView textViewReSend;
    EditText edtOtp;
    AppCompatButton btn;

    private FirebaseAuth mAuth;
    String otp = "", verifyId = "", phone = "";

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.confirm_phone_reset, "");
        init();
        mAuth = FirebaseAuth.getInstance();

        //args
        Bundle args = getIntent().getExtras();
        phone = args.getString("phone");
        verifyId = args.getString("verifyId");



        init();
    }



    private void init() {
        progressLay = findViewById(R.id.progressLay);
        edtOtp = findViewById(R.id.edtOtp);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 05/08/2021 do phone change
                otp = edtOtp.getText().toString().trim();
                if (otp.isEmpty()){
                    edtOtp.setError(getString(R.string.enter_6_digit));
                    edtOtp.requestFocus();
                    return;
                }
                if (otp.length()!=6){
                    edtOtp.setError(getString(R.string.otp_must6));
                    edtOtp.requestFocus();
                    return;
                }

                progressLay.setVisibility(View.VISIBLE);
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verifyId, otp);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.updatePhoneNumber(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()){
                            warningMsg(getString(R.string.done_phone_changed),true);
                        }else{
                            warningMsg(getString(R.string.digit6error),false);
                        }
                        progressLay.setVisibility(View.GONE);
                    }
                });
//                signInWithPhoneAuthCredential(credential);

            }
        });


//        textViewReSend = findViewById(R.id.reSend);
//        textViewReSend.setText(new GlobalVar().underLinerTextView("اعادة الارسال"));
    }



    LinearLayout progressLay;
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressLay.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");

//                            FirebaseUser user = task.getResult().getUser();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            user.updatePhoneNumber(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isComplete()){
                                        warningMsg(getString(R.string.done_phone_changed),true);
                                    }else{
                                        warningMsg(getString(R.string.error_try_again),false);
                                    }
                                }
                            });


                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                warningMsg(getString(R.string.digit6error),false);
                            }
                        }
                    }
                });
    }
//    private void signInWithPhoneAuthCredential2(PhoneAuthCredential credential) {
//        mAuth.
//    }


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
        yes.setText(getString(R.string.ok));

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