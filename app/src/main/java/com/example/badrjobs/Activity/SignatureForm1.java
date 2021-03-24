package com.example.badrjobs.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.badrjobs.R;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;

public class SignatureForm1 extends AppCompatActivity implements View.OnClickListener {

    AppCompatButton button,addSignature;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_fourm);
        init();
    }

    private void init() {
        button = findViewById(R.id.btn);
        button.setOnClickListener(this);
        addSignature = findViewById(R.id.addSignature);
        addSignature.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn:{
                startActivity(new Intent(getApplicationContext(),SignatureForm2.class));
                break;
            }
            case R.id.addSignature:{
                startActivity(new Intent(getApplicationContext(),SignatureDraw.class));
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