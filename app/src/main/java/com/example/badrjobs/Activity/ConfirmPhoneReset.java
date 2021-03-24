package com.example.badrjobs.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.badrjobs.GlobalVar;
import com.example.badrjobs.R;

public class ConfirmPhoneReset extends AppCompatActivity {

    TextView textViewReSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_phone_reset);
        init();
    }

    private void init() {
        textViewReSend = findViewById(R.id.reSend);
        textViewReSend.setText(new GlobalVar().underLinerTextView("اعادة الارسال"));
    }
}