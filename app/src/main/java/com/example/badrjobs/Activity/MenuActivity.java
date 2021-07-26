package com.example.badrjobs.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.badrjobs.BuildConfig;
import com.example.badrjobs.R;
import com.example.badrjobs.Utils.SharedPrefManager;
import com.example.badrjobs.Utils.ToolbarClass;
import com.franmontiel.localechanger.LocaleChanger;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;

import java.util.Locale;

public class MenuActivity extends ToolbarClass {

    RelativeLayout layContactUs,layAbout,layTerm,layShare,copyFromContract;

    //lang
    Spinner spinnerLanguage;
    String lang="";
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_menu, "");
        init();
        initSpinnerLang();
    }

    private void init() {
        spinnerLanguage = findViewById(R.id.spinner);
        copyFromContract = findViewById(R.id.copyFromContract);
        copyFromContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ConfirmDeal.class));
            }
        });
        layContactUs = findViewById(R.id.layContactUs);
        layContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ContactUs.class));
            }
        });
        layAbout = findViewById(R.id.layAbout);
        layAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                intent.putExtra("acType","about");
                startActivity(intent);
            }
        });
        layTerm = findViewById(R.id.layTerm);
        layTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                intent.putExtra("acType","term");
                startActivity(intent);
            }
        });
        layShare = findViewById(R.id.layShare);
        layShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), TestingAc.class));
                shareAppLink();
            }
        });
    }



    private void initSpinnerLang() {
//        String[] array = {"اللغة","العربية", "English"};
        String[] array = getResources().getStringArray(R.array.lang);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_txt_size_normal, array) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = null;
                v = super.getDropDownView(position, null, parent);
//                v.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                // If this is the selected item position
//                if (position == 0) {
//                    v.setBackgroundColor(getResources().getColor(R.color.spinnerHeaderItem));
//                }
//
//                else {
//                    if (position % 2 == 0) {
//                        v.setBackgroundColor(getResources().getColor(R.color.spinner_bg_design1));
//                    } else {
//                        v.setBackgroundColor(getResources().getColor(R.color.spinner_bg_design2));
//                    }
//
//                }
                return v;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerLanguage.setAdapter(adapter);
        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1: {
                        lang = "ar";
                        SharedPrefManager.getInstance(getApplicationContext()).storeAppLanguage(lang);
                        changeLocale(lang);
                        break;
                    }
                    case 2: {
                        lang = "en";
                        SharedPrefManager.getInstance(getApplicationContext()).storeAppLanguage(lang);
                        changeLocale(lang);
                        break;
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void changeLocale(String language) {
        Locale locale = new Locale(language);
        LocaleChanger.setLocale(locale);
        ActivityRecreationHelper.recreate(this,true);
    }


    private void shareAppLink(){
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String shareMessage= "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch(Exception e) {
            Toast.makeText(getApplicationContext(), "حدث خطأ الرجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
            //e.toString();
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