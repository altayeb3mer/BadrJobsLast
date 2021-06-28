package com.example.badrjobs.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.badrjobs.Model.ModelCountry;
import com.example.badrjobs.R;
import com.example.badrjobs.Utils.Api;
import com.example.badrjobs.Utils.SharedPrefManager;
import com.example.badrjobs.Utils.ToolbarClass;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Register extends ToolbarClass {
    LinearLayout progressLay;
    ConstraintLayout container;
    CountryCodePicker ccp;
    EditText edtPhone, editTextName, editTextNickname, editTextPassword1, editTextPassword2,
            editTextEmail, editTextConfirmEmail, editTextJob;
    AppCompatButton btn;
    AppCompatCheckBox checkbox;
    String phone = "", name = "", nickname = "", password1 = "", password2 = "", email1 = "", email2 = "", job = "";
    boolean agreeWithTerm = false;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    ArrayList<ModelCountry> arrayListNationality;
    ArrayList<String> arrayListSpinner;
    boolean isFixedAvailable = false;
    RelativeLayout layAvailable, layNotAvailable;
    Spinner spinner;
    String nationality = "";
    //firebase
    private FirebaseAuth mAuth;
    //language controller
    private LocaleChangerAppCompatDelegate localeChangerAppCompatDelegate;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
//        init();
//        getNationality();
//    }


    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_register, "");
        init();
        getNationality();
    }

    private void init() {
        //firebase
        mAuth = FirebaseAuth.getInstance();
        mAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(false);

        spinner = findViewById(R.id.spinner);
        layAvailable = findViewById(R.id.layAvailable);
        layNotAvailable = findViewById(R.id.layNotAvailable);
        progressLay = findViewById(R.id.progressLay);
        container = findViewById(R.id.container);
        checkbox = findViewById(R.id.checkbox);
        editTextName = findViewById(R.id.name);
        editTextNickname = findViewById(R.id.nicename);
        editTextNickname.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence cs, int start, int end, Spanned spanned, int dStart, int dEnd) {
                        // TODO Auto-generated method stub
                        if (cs.equals("")) { // for backspace
                            return cs;
                        }
                        if (cs.toString().matches("[a-zA-Z]+")) {
                            return cs;
                        }
                        return "";
                    }
                }
        });
        editTextNickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                layAvailable.setVisibility(View.GONE);
                layNotAvailable.setVisibility(View.GONE);
                if (!s.toString().isEmpty())
                    isFixedAvailable(s.toString());


                // filter your list from your input
//                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });
        editTextPassword1 = findViewById(R.id.password1);
        editTextPassword2 = findViewById(R.id.password2);
        editTextEmail = findViewById(R.id.email);
        editTextConfirmEmail = findViewById(R.id.confirm_email);
        editTextJob = findViewById(R.id.job);
        ccp = findViewById(R.id.ccp);
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                Toast.makeText(Register.this, "" + ccp.getFullNumberWithPlus(), Toast.LENGTH_SHORT).show();
            }
        });
        edtPhone = findViewById(R.id.phone);
        btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = editTextName.getText().toString().trim();
                nickname = editTextNickname.getText().toString().trim();
                password1 = editTextPassword1.getText().toString().trim();
                password2 = editTextPassword2.getText().toString().trim();
                email1 = editTextEmail.getText().toString().trim();
                email2 = editTextConfirmEmail.getText().toString().trim();
                job = editTextJob.getText().toString().trim();
                phone = edtPhone.getText().toString().trim();

                preRegister();

//                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
        //checkBox
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    agreeWithTerm = true;
                } else {
                    agreeWithTerm = false;
                }
            }
        });

        //callback method
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Toast.makeText(Register.this, "تعذر الارسال", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                progressLay.setVisibility(View.GONE);
                Toast.makeText(Register.this, "تعذر الارسال", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("name", name);
                hashMap.put("fixName", nickname);
                hashMap.put("email", email1);
                hashMap.put("phone", phone);
                hashMap.put("codeCountry","00"+ccp.getFullNumber());
                hashMap.put("password", password1);
                hashMap.put("password_confirmation", password2);
                hashMap.put("nationality_id", nationality);


                progressLay.setVisibility(View.GONE);
                Toast.makeText(Register.this, "تم الارسال", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ConfirmPhone.class);
                intent.putExtra("phone", "00"+ccp.getFullNumber() + phone);
                intent.putExtra("verifyId", verificationId);
                intent.putExtra("hashMap", hashMap);
                startActivity(intent);

            }
        };

    }

    private void isFixedAvailable(String fixedName) {
        progressLay.setVisibility(View.VISIBLE);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Content-Type", "application/json;");
                        ongoing.addHeader("Accept", "application/json");
                        ongoing.addHeader("Content-Type", "application/x-www-form-urlencoded");
                        ongoing.addHeader("lang", SharedPrefManager.getInstance(getApplicationContext()).GetAppLanguage());
//                        String token = SharedPrefManager.getInstance(getApplicationContext()).getAppToken();
//                        ongoing.addHeader("Authorization", token);
                        return chain.proceed(ongoing.build());
                    }
                })
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.ROOT_URL)
                .client(httpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api.RetrofitFixedAvailability service = retrofit.create(Api.RetrofitFixedAvailability.class);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("fixName", fixedName);
        Call<String> call = service.putParam(hashMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("code");
                    switch (statusCode) {
                        case "200": {
                            JSONObject objectData = object.getJSONObject("response");
                            isFixedAvailable = objectData.getBoolean("avalible");
                            if (isFixedAvailable) {
                                layAvailable.setVisibility(View.VISIBLE);
                                layNotAvailable.setVisibility(View.GONE);
                            } else {
                                layAvailable.setVisibility(View.GONE);
                                layNotAvailable.setVisibility(View.VISIBLE);
                            }

                            break;
                        }
                        default: {
                            Toast.makeText(getApplicationContext(), "حدث خطأ اثناء تسجيل الخروج الرجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    progressLay.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
//                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                progressLay.setVisibility(View.GONE);
            }
        });
    }

//    private String getToken(){
//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w("FCM", "Failed", task.getException());
//                            return;
//                        }
//
//                        String token = task.getResult().getToken();
//                        Log.i("FCM", "Current token=" + token);
//                    }
//                });
//    }

    private void preRegister() {
        if (!name.equals("") && !nickname.equals("") && !password1.equals("") && !password2.equals("") &&
                !email1.equals("") && !email2.equals("") && !job.equals("") && !phone.equals("")) {
            if (agreeWithTerm) {
                if (!Patterns.EMAIL_ADDRESS.matcher(email1).matches()) {
                    editTextEmail.setError("الرجاء كتابة بريد الكتروني صحيح");
                    editTextEmail.requestFocus();
                    return;
                }
                if (!email1.equals(email2)) {
                    editTextEmail.setError("البريد الالكتروني غير متطابق");
                    editTextEmail.requestFocus();
                    return;
                }
                if (!password1.equals(password2)) {
                    editTextPassword1.setError("كلمة السر غير متطابقة");
                    editTextPassword1.requestFocus();
                    return;
                }
                firebaseReg();
            } else {
                showSnackBar("لم تقم بالموافقة على الشروط والاحكام");
            }

        } else {
            showSnackBar("الرجاء تعبئة كل الحقول");
        }


    }

    private void firebaseReg() {
        progressLay.setVisibility(View.VISIBLE);

//        try {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(ccp.getFullNumberWithPlus() +
                                phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(Register.this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


    }

    public void showSnackBar(String msg) {
        Snackbar snackbar = Snackbar.make(container, msg, Snackbar.LENGTH_LONG);
        View snackview = snackbar.getView();
        snackview.setBackgroundColor(Color.RED);
        TextView masseage;
        masseage = snackview.findViewById(R.id.snackbar_text);
        masseage.setTextSize(16);
        masseage.setTextColor(Color.WHITE);
        snackbar.show();
    }

    private void getNationality() {
        arrayListNationality = new ArrayList<>();
        arrayListSpinner = new ArrayList<>();
        arrayListSpinner.add("اختر");
        progressLay.setVisibility(View.VISIBLE);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Content-Type", "application/json;");
                        ongoing.addHeader("Accept", "application/json");
                        ongoing.addHeader("Content-Type", "application/x-www-form-urlencoded");
                        ongoing.addHeader("lang", SharedPrefManager.getInstance(getApplicationContext()).GetAppLanguage());
//                        String token = SharedPrefManager.getInstance(getApplicationContext()).getAppToken();
//                        ongoing.addHeader("Authorization", token);
                        return chain.proceed(ongoing.build());
                    }
                })
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.ROOT_URL)
                .client(httpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api.RetrofitGetNationality service = retrofit.create(Api.RetrofitGetNationality.class);
        Call<String> call = service.putParam();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("code");
                    switch (statusCode) {
                        case "200": {
                            JSONArray arrayData = object.getJSONArray("response");
                            for (int i = 0; i < arrayData.length(); i++) {
                                JSONObject itemData = arrayData.getJSONObject(i);
                                ModelCountry modelCountry = new ModelCountry();
                                modelCountry.setId(itemData.getString("id"));
                                modelCountry.setName(itemData.getString("name"));
                                arrayListSpinner.add(itemData.getString("name"));

                                arrayListNationality.add(modelCountry);
                            }

                            if (arrayListSpinner.size()>0){
                                initSpinnerNationality(arrayListSpinner);
                            }
                            break;
                        }
                        default: {
                            Toast.makeText(getApplicationContext(), "حدث خطأ اثناء تسجيل الخروج الرجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    progressLay.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
//                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                progressLay.setVisibility(View.GONE);
            }
        });
    }

    private void initSpinnerNationality(ArrayList<String> array) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, array) {
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
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    nationality = "";
                } else {
                    nationality = arrayListNationality.get(position - 1).getId();
                }
                Toast.makeText(Register.this, nationality, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

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