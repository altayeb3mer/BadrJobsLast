package com.example.badrjobs.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;

import android.os.Bundle;
import android.widget.TextView;

import com.example.badrjobs.R;
import com.example.badrjobs.Utils.ToolbarClass;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;

public class NotificationDetails extends ToolbarClass {


    TextView textViewTitle,textViewBody,textViewDate;
    String title="",body="",date="";

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_notification_details, "");
        Bundle args = getIntent().getExtras();
        if (args!=null){
            title = args.getString("title");
            body = args.getString("body");
            date = args.getString("date");
        }
        init();
    }

    private void init() {
        textViewTitle = findViewById(R.id.title);
        textViewDate = findViewById(R.id.date);
        textViewBody = findViewById(R.id.body);
        textViewTitle.setText(title);
        textViewBody.setText(body);
        textViewDate.setText(date);
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