package com.example.badrjobs.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.badrjobs.GlobalVar;
import com.example.badrjobs.R;
import com.example.badrjobs.Utils.SharedPrefManager;

public class Splash2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash2);


        new GlobalVar().changeStatusBarColor(this,getColor(R.color.colorPrimary));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String token = SharedPrefManager.getInstance(getApplicationContext()).getAppToken();
                if (!token.isEmpty()){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }else{
                    startActivity(new Intent(getApplicationContext(), Login.class));
                }
                overridePendingTransition(R.anim.in_right,R.anim.out_right);
                finish();
            }
        }, 1000);
    }

}