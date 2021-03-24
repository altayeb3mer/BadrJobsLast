package com.example.badrjobs.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.badrjobs.R;

public class ResetPhone extends AppCompatActivity implements View.OnClickListener {

    AppCompatButton button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_phone);
        init();
    }

    private void init() {
        button = findViewById(R.id.btn);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn:{
                startActivity(new Intent( getApplicationContext(),ConfirmPhoneReset.class));
                break;
            }
        }
    }
}