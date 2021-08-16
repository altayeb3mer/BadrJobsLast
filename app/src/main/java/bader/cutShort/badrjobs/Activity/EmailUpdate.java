package bader.cutShort.badrjobs.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.badrjobs.R;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.HashMap;

import bader.cutShort.badrjobs.Utils.ToolbarClass;

public class EmailUpdate extends ToolbarClass {


    EditText newEmail1,newEmail2;
    AppCompatButton button;
    String email1="",email2="";
    LinearLayout progressLay;

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.email_update, "");

        init();
    }

    private  void init(){
        progressLay = findViewById(R.id.progressLay);
        newEmail1 = findViewById(R.id.newEmail1);
        newEmail2 = findViewById(R.id.newEmail2);
        //disable copy & paste
        disableCopyPaste(newEmail1);
        disableCopyPaste(newEmail2);


        button = findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email1 = newEmail1.getText().toString().trim();
                email2 = newEmail2.getText().toString().trim();
                if (!email1.isEmpty()&&!email2.isEmpty()){
                    if (!Patterns.EMAIL_ADDRESS.matcher(email1).matches()) {
                        newEmail1.setError("الرجاء كتابة بريد الكتروني صحيح");
                        newEmail1.requestFocus();
                        return;
                    }
                    if (!Patterns.EMAIL_ADDRESS.matcher(email2).matches()) {
                        newEmail2.setError("الرجاء كتابة بريد الكتروني صحيح");
                        newEmail2.requestFocus();
                        return;
                    }

                    if (!email1.equals(email2)){
                        warningMsg("البريد غير متطابق راجع المدخلات",false);
                        return;
                    }

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    progressLay.setVisibility(View.VISIBLE);
                    user.updateEmail(email1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressLay.setVisibility(View.GONE);
                            if (task.isSuccessful()){
                                warningMsg(getString(R.string.email_update_done),true);
                            }else{
                                warningMsg(getString(R.string.error_try_again),false);
                            }
                        }
                    });


                }else{
                    warningMsg(getString(R.string.email_update_empty_feilds),false);
                }
            }
        });
    }



    private void disableCopyPaste(EditText editText){
        editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
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