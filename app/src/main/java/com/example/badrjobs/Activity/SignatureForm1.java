package com.example.badrjobs.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import com.example.badrjobs.R;
import com.example.badrjobs.Utils.Api;
import com.example.badrjobs.Utils.SharedPrefManager;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SignatureForm1 extends AppCompatActivity implements View.OnClickListener {
    static final int REQUEST_CODE = 1;
    AppCompatButton button, addSignature;
    RadioGroup radioGroup;
    TextView textViewName;
    Bitmap bitmap;
    ImageView imgSignature;
    LinearLayout sigCompany1Lay, sigCompany2Lay;
    //edt
    EditText editTextPersonName, editTextPersonalId,
    editTextComName,editTextAddress, editTextOwnerName, editTextOwnerId,editTextSalary,
    editTextAgreeable_money,editTextJob,editTextCountry,editTextNationality,
    editTextContractPeriod;
    String companyName="",address="",ownerName="",ownerId="",salary="",agreeableMoney="",
            job="",country="", nationality="",contractPeriod="";
    String type = "PERSONAL";
    String contractId = "1";
    LinearLayout progressLay;
    String s_name = "", s_personal_id = "";
    String image = "";
    //language controller
    private LocaleChangerAppCompatDelegate localeChangerAppCompatDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_form1);
        init();
    }

    private void init() {
        progressLay = findViewById(R.id.progressLay);
        //edt personal
        editTextPersonName = findViewById(R.id.edtPersonName);
        editTextPersonalId = findViewById(R.id.edtPersonalId);
        //edt office
        editTextComName = findViewById(R.id.companyName);
        editTextAddress = findViewById(R.id.address);
        editTextOwnerName = findViewById(R.id.ownerName);
        editTextOwnerId = findViewById(R.id.ownerPersonalId);
        editTextSalary = findViewById(R.id.salary);
        editTextAgreeable_money = findViewById(R.id.agreeMoney);
        editTextJob = findViewById(R.id.job);
        editTextCountry= findViewById(R.id.country);
        editTextNationality= findViewById(R.id.nationalaty);
        editTextContractPeriod= findViewById(R.id.contractPeriod);


//        sigSomeoneLay = findViewById(R.id.sigSomeone);
        sigCompany1Lay = findViewById(R.id.sigCompany1);
        sigCompany2Lay = findViewById(R.id.sigCompany2);

        textViewName = findViewById(R.id.name);

        imgSignature = findViewById(R.id.imgSignature);
        radioGroup = findViewById(R.id.radioGroupType);

        //btn
        button = findViewById(R.id.btn);
        button.setOnClickListener(this);
        addSignature = findViewById(R.id.addSignature);
        addSignature.setOnClickListener(this);

        setDefaultCheck();


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.radioBtnType1: {
                        type = "PERSONAL";
                        textViewName.setText("اسم الشخص الموقع");

//                        sigSomeoneLay.setVisibility(View.VISIBLE);
                        sigCompany1Lay.setVisibility(View.GONE);
//                        sigCompany2Lay.setVisibility(View.VISIBLE);
                        break;
                    }
                    case R.id.radioBtnType2: {
                        type = "OFFICE";
                        textViewName.setText("اسم من ينوب عنها الطرف الاول");

//                        sigSomeoneLay.setVisibility(View.VISIBLE);
                        sigCompany1Lay.setVisibility(View.VISIBLE);
//                        sigCompany2Lay.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }
        });
    }

    //get countries
    private void addSignatureFun(HashMap<String, String> hashMap) {
        progressLay.setVisibility(View.VISIBLE);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Content-Type", "application/json;");
                        ongoing.addHeader("Accept", "application/json");
                        ongoing.addHeader("lang", SharedPrefManager.getInstance(getApplicationContext()).GetAppLanguage());
                        String token = SharedPrefManager.getInstance(getApplicationContext()).getAppToken();
                        ongoing.addHeader("Authorization", token);
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

        Api.RetrofitSignContract service = retrofit.create(Api.RetrofitSignContract.class);


        Call<String> call = service.putParam(contractId, hashMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("code");
                    switch (statusCode) {
                        case "200": {
                            warningMsg("تم اضافة التوقيع بنجاح", true);
                            break;
                        }

                        default: {
                            warningMsg("حدث خطأ حاول مجددا",false);
                            break;
                        }
                    }
                    progressLay.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    warningMsg(e.getMessage().toString(),false);
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                progressLay.setVisibility(View.GONE);
                warningMsg("time out",false);
            }
        });
    }

    private void setDefaultCheck() {
        radioGroup.check(R.id.radioBtnType1);
//        sigSomeoneLay.setVisibility(View.VISIBLE);
        sigCompany1Lay.setVisibility(View.GONE);
        sigCompany2Lay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addSignature: {
                startActivityForResult(new Intent(getApplicationContext(), SignatureDraw.class), REQUEST_CODE);
                break;
            }
            case R.id.btn: {
                preAdd();
                break;
            }
        }
    }

    private void preAdd() {
        switch (type) {
            case "PERSONAL": {
                companyName = editTextComName.getText().toString().trim();
                address = editTextAddress.getText().toString().trim();
                s_name = editTextPersonName.getText().toString().trim();
                s_personal_id = editTextPersonalId.getText().toString().trim();
                ownerName = editTextOwnerName.getText().toString().trim();
                ownerId = editTextOwnerId.getText().toString().trim();
                salary = editTextSalary.getText().toString().trim();
                agreeableMoney = editTextAgreeable_money.getText().toString().trim();
                job = editTextJob.getText().toString().trim();
                country = editTextCountry.getText().toString().trim();
                nationality = editTextNationality.getText().toString().trim();
                contractPeriod = editTextContractPeriod.getText().toString().trim();

                if (s_name.isEmpty()) {
                    editTextPersonName.setError("الرجاء كتابة الاسم");
                    editTextPersonName.requestFocus();
                    return;
                }
                if (s_personal_id.isEmpty()) {
                    editTextPersonalId.setError("الرجاء كتابة الرقم المدني");
                    editTextPersonalId.requestFocus();
                    return;
                }
                if (ownerName.isEmpty()) {
                    editTextOwnerName.setError("الرجاء كتابة اسم صاحب المهنة");
                    editTextOwnerName.requestFocus();
                    return;
                }
                if (ownerId.isEmpty()) {
                    editTextOwnerId.setError("الرجاء كتابة الرقم المدني لصاحب المهنة");
                    editTextOwnerId.requestFocus();
                    return;
                }
                if (salary.isEmpty()) {
                    editTextSalary.setError("الرجاء كتابة الراتب الشهري");
                    editTextSalary.requestFocus();
                    return;
                }
                if (agreeableMoney.isEmpty()) {
                    editTextAgreeable_money.setError("الرجاء كتابة الراتب الشهري");
                    editTextAgreeable_money.requestFocus();
                    return;
                }
                if (job.isEmpty()) {
                    editTextJob.setError("الرجاء كتابة المهنة");
                    editTextJob.requestFocus();
                    return;
                }
                if (country.isEmpty()) {
                    editTextCountry.setError("الرجاء كتابة الدولة");
                    editTextCountry.requestFocus();
                    return;
                }
                if (nationality.isEmpty()) {
                    editTextNationality.setError("الرجاء كتابة الجنسية");
                    editTextNationality.requestFocus();
                    return;
                }
                if (contractPeriod.isEmpty()) {
                    editTextContractPeriod.setError("الرجاء مدة العقد");
                    editTextContractPeriod.requestFocus();
                    return;
                }
                if (image.isEmpty()) {
                    warningMsg("الرجاء اضافة التوقيع",false);
                    return;
                }

                //init the hashMap
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("first_side_type", type);
                hashMap.put("name", s_name);
                hashMap.put("personal_id", s_personal_id);
                hashMap.put("first_side_signature", image);

                hashMap.put("org_name", companyName);
                hashMap.put("org_address", address);
                hashMap.put("org_owner_name", ownerName);
                hashMap.put("signature_personal_id", ownerId);
                hashMap.put("salary", salary);
                hashMap.put("agreeable_money", agreeableMoney);
                hashMap.put("job", job);
                hashMap.put("country", country);
                hashMap.put("nationality", nationality);
                hashMap.put("contract_period", contractPeriod);
                addSignatureFun(hashMap);

                break;
            }
            case "OFFICE": {
                companyName = editTextComName.getText().toString().trim();
                address = editTextAddress.getText().toString().trim();
                s_name = editTextPersonName.getText().toString().trim();
                s_personal_id = editTextPersonalId.getText().toString().trim();
                ownerName = editTextOwnerName.getText().toString().trim();
                ownerId = editTextOwnerId.getText().toString().trim();
                salary = editTextSalary.getText().toString().trim();
                agreeableMoney = editTextAgreeable_money.getText().toString().trim();
                job = editTextJob.getText().toString().trim();
                country = editTextCountry.getText().toString().trim();
                nationality = editTextNationality.getText().toString().trim();
                contractPeriod = editTextContractPeriod.getText().toString().trim();


                if (companyName.isEmpty()) {
                    editTextComName.setError("الرجاء كتابة اسم الشركة");
                    editTextComName.requestFocus();
                    return;
                }
                if (address.isEmpty()) {
                    editTextAddress.setError("الرجاء كتابة |عنوان الشركة");
                    editTextAddress.requestFocus();
                    return;
                }
                if (s_name.isEmpty()) {
                    editTextPersonName.setError("الرجاء كتابة الاسم");
                    editTextPersonName.requestFocus();
                    return;
                }
                if (s_personal_id.isEmpty()) {
                    editTextPersonalId.setError("الرجاء كتابة الرقم المدني");
                    editTextPersonalId.requestFocus();
                    return;
                }
                if (ownerName.isEmpty()) {
                    editTextOwnerName.setError("الرجاء كتابة اسم صاحب المهنة");
                    editTextOwnerName.requestFocus();
                    return;
                }
                if (ownerId.isEmpty()) {
                    editTextOwnerId.setError("الرجاء كتابة الرقم المدني لصاحب المهنة");
                    editTextOwnerId.requestFocus();
                    return;
                }
                if (salary.isEmpty()) {
                    editTextSalary.setError("الرجاء كتابة الراتب الشهري");
                    editTextSalary.requestFocus();
                    return;
                }
                if (agreeableMoney.isEmpty()) {
                    editTextAgreeable_money.setError("الرجاء كتابة الراتب الشهري");
                    editTextAgreeable_money.requestFocus();
                    return;
                }
                if (job.isEmpty()) {
                    editTextJob.setError("الرجاء كتابة المهنة");
                    editTextJob.requestFocus();
                    return;
                }
                if (country.isEmpty()) {
                    editTextCountry.setError("الرجاء كتابة الدولة");
                    editTextCountry.requestFocus();
                    return;
                }
                if (nationality.isEmpty()) {
                    editTextNationality.setError("الرجاء كتابة الجنسية");
                    editTextNationality.requestFocus();
                    return;
                }
                if (contractPeriod.isEmpty()) {
                    editTextContractPeriod.setError("الرجاء مدة العقد");
                    editTextContractPeriod.requestFocus();
                    return;
                }
                if (image.isEmpty()) {
                    warningMsg("الرجاء اضافة التوقيع",false);
                    return;
                }

                //init the hashMap
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("first_side_type", type);
                hashMap.put("name", s_name);
                hashMap.put("personal_id", s_personal_id);
                hashMap.put("first_side_signature", image);

                hashMap.put("org_name", companyName);
                hashMap.put("org_address", address);
                hashMap.put("org_owner_name", ownerName);
                hashMap.put("signature_personal_id", ownerId);
                hashMap.put("salary", salary);
                hashMap.put("agreeable_money", agreeableMoney);
                hashMap.put("job", job);
                hashMap.put("country", country);
                hashMap.put("nationality", nationality);
                hashMap.put("contract_period", contractPeriod);
                addSignatureFun(hashMap);
                break;
            }
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (data.hasExtra("bitmap")) {
                bitmap = BitmapFactory.decodeByteArray(
                        data.getByteArrayExtra("bitmap"), 0, data.getByteArrayExtra("bitmap").length);
                imgSignature.setImageBitmap(bitmap);

                image = getStringFromImg(bitmap);
            } else {
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                image = "";
            }


        }
        if (resultCode == Activity.RESULT_CANCELED) {
            //Write your code if there's no result
            Toast.makeText(this, "لم تقم بكتابة توقيع", Toast.LENGTH_SHORT).show();
        }

    }


    //get string from bitmap
    private String getStringFromImg(Bitmap bitmap) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteStream);
        byte[] byteArray = byteStream.toByteArray();
        String baseString = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return baseString;
    }

    //dialog message
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


}