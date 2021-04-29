package com.example.badrjobs.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.badrjobs.Adapter.SlideShow_adapter;
import com.example.badrjobs.R;
import com.example.badrjobs.Utils.Api;
import com.example.badrjobs.Utils.SharedPrefManager;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AdsDetails extends AppCompatActivity implements View.OnClickListener {

    CardView cardSignature;
    String id = "";
    LinearLayout progressLay;
    TextView addToFavorite, textViewName,textViewFixName,textViewType,textViewOwnerName,textViewBirthday,
    textViewJob,textViewExperience,textViewSalary,textViewBillingMoney,textViewSex,
    textViewReligion, textViewCountry,textViewDesc,textViewViews,
    textViewReportAds,textViewOfficeName,textViewOfficeAddress,textViewDate;
    CircleImageView circleImageViewOwner;

    String ownerId="",isActive="NO";
    ImageView imgFlag,imageViewBack;

    ViewPager viewPager;
    SlideShow_adapter slideShow_adapter;
    ArrayList<String> arrayListImages;
    CircleIndicator circleIndicator;
    LinearLayout layOwner,layOffice;
    AppCompatButton btnStopAd;
    NestedScrollView nestedScroll;
    boolean isMyAd=false;

    //language controller
    private LocaleChangerAppCompatDelegate localeChangerAppCompatDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);
        Bundle args = getIntent().getExtras();
        if (args != null) {
            id = args.getString("id");
        }
        init();
        getJobDetails();
    }


    Dialog dialog;
    private void dialogEdit(String title,String key) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.diallog_edit_field);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        try {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView textViewTitle = dialog.findViewById(R.id.title);
        textViewTitle.setText(title);
        AppCompatButton yes = dialog.findViewById(R.id.yes);
        AppCompatButton no = dialog.findViewById(R.id.no);
        yes.setText(getResources().getString(R.string.send));
        no.setText("الغاء");

        EditText editTextField = dialog.findViewById(R.id.field);



        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = editTextField.getText().toString().trim();
                if (!value.isEmpty()){
                    doEdition(key,value);
                    dialog.dismiss();
                }else {
                    editTextField.setError("يجب تحديد قيمة للتعديل");
                    editTextField.requestFocus();
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

    private void doEdition(String key, String value) {
        progressLay.setVisibility(View.VISIBLE);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Content-Type", "application/json;");
                        ongoing.addHeader("Accept", "application/json");
                        ongoing.addHeader("Content-Type", "application/x-www-form-urlencoded");
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

        Api.RetrofitReportJob service = retrofit.create(Api.RetrofitReportJob.class);
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put(key,value);
        hashMap.put("job_id",id);
        Call<String> call = service.putParam(hashMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("code");
                    switch (statusCode) {
                        case "200": {
                            warningMsg(object.getString("message"));
                            break;
                        }
                        default: {
                            warningMsg("لقد قم بالابلاغ عن هذا الاعلان مسبقا");
                            break;
                        }
                    }
                    progressLay.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    warningMsg(e.getMessage());
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                progressLay.setVisibility(View.GONE);
                warningMsg("time out");
            }
        });
    }

    private void init() {
        nestedScroll = findViewById(R.id.nestedScroll);
        nestedScroll.setVisibility(View.GONE);
        btnStopAd = findViewById(R.id.btnStopAd);
        btnStopAd.setVisibility(View.GONE);
        btnStopAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activateJob();
            }
        });
        textViewDate = findViewById(R.id.date);
        textViewOfficeName = findViewById(R.id.officeName);
        textViewOfficeAddress = findViewById(R.id.officeAddress);
        layOffice = findViewById(R.id.layOffice);

        textViewReportAds = findViewById(R.id.reportAds);
        textViewReportAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogEdit("الابلاغ عن اساءة","reason");
            }
        });
        layOwner = findViewById(R.id.layOwner);
        layOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdsDetails.this,UserViewedProfile.class);
                i.putExtra("userId",ownerId);
                startActivity(i);
            }
        });
        imageViewBack = findViewById(R.id.back);
        imageViewBack.setOnClickListener(this);
        addToFavorite = findViewById(R.id.AddToFavorite);
        addToFavorite.setOnClickListener(this);
        progressLay = findViewById(R.id.progressLay);
        cardSignature = findViewById(R.id.cardSignature);
        cardSignature.setOnClickListener(this);

        //textView
        textViewType = findViewById(R.id.type);
        textViewFixName = findViewById(R.id.fixName);
        textViewName = findViewById(R.id.name);
        textViewOwnerName = findViewById(R.id.jobOwnerName);
        textViewBirthday = findViewById(R.id.birthday);
        textViewJob = findViewById(R.id.job);
        textViewExperience = findViewById(R.id.experience);
        textViewSalary = findViewById(R.id.salary);
        textViewBillingMoney = findViewById(R.id.bailing_money);
        textViewReligion = findViewById(R.id.religion);
        textViewCountry = findViewById(R.id.country);
        textViewSex = findViewById(R.id.sex);
        textViewDesc = findViewById(R.id.description);
        textViewViews = findViewById(R.id.views);
        //imageView
        circleImageViewOwner = findViewById(R.id.img);
        imgFlag = findViewById(R.id.imgFlag);



    }

    private void initSlider(ArrayList<String> list){
        viewPager = findViewById(R.id.viewpager);
        circleIndicator = findViewById(R.id.indicator);
        slideShow_adapter = new SlideShow_adapter(this,list);
        viewPager.setAdapter(slideShow_adapter);
        circleIndicator.setViewPager(viewPager);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cardSignature: {
//                startActivity(new Intent(getApplicationContext(),SignatureForm1.class));
                createContract();
                break;
            }
            case R.id.AddToFavorite: {
                addToFavoriteFun();
                break;
            }
            case R.id.back: {
                onBackPressed();
                break;
            }
        }
    }

    private void createContract() {
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

        Api.RetrofitCreateContract service = retrofit.create(Api.RetrofitCreateContract.class);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("job_id", id);
        Call<String> call = service.putParam(hashMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String code = object.getString("code");
                    switch (code) {
                        case "200": {
                            warningMsg("تم انشاء العقد في انتظار موافقة صاحب الاعلان");
                            break;
                        }
                        default: {
                            warningMsg("حدث خطأ الرجاء المحاولة مرة اخرى");
                            break;
                        }
                    }


                    progressLay.setVisibility(View.GONE);

                } catch (Exception e) {

                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressLay.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                progressLay.setVisibility(View.GONE);

            }
        });
    }

    private void activateJob() {
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

        Api.RetrofitCreateContract service = retrofit.create(Api.RetrofitCreateContract.class);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("job_id", id);
        Call<String> call = service.putParam(hashMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String code = object.getString("code");
                    switch (code) {
                        case "200": {
                            if (isActive.equals("YES")){
                                isActive = "NO";
                                btnStopAd.setText("تفعيل الاعلان");
                                warningMsg("تم ايقاف الاعلان");
                            }else{
                                isActive = "YES";
                                btnStopAd.setText("ايقاف مؤقت للاعلان");
                                warningMsg("تم تفعيل الاعلان");
                            }
                            break;
                        }
                        default: {
                            warningMsg("حدث خطأ الرجاء المحاولة مرة اخرى");
                            break;
                        }
                    }

                    progressLay.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressLay.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                progressLay.setVisibility(View.GONE);

            }
        });
    }

    private void addToFavoriteFun() {
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

        Api.RetrofitAddToFavorite service = retrofit.create(Api.RetrofitAddToFavorite.class);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("job_id", id);
        Call<String> call = service.putParam(hashMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String code = object.getString("code");
                    switch (code) {
                        case "200": {

                            if (is_liked){
                                is_liked = false;
                                addToFavorite.setText("الى مفضلتي");
                                warningMsg("تم ازالة الاعلان من المفضلة");
                            }else{
                                is_liked = true;
                                addToFavorite.setText("ازالة من المفضلة");
                                warningMsg("تم اضافة الاعلان للمفضلة");
                            }
                            break;
                        }
                        default: {
                            warningMsg("حدث خطأ الرجاء المحاولة مرة اخرى");
                            break;
                        }
                    }


                    progressLay.setVisibility(View.GONE);

                } catch (Exception e) {

                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressLay.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                progressLay.setVisibility(View.GONE);

            }
        });
    }

    private void getJobDetails() {
        arrayListImages = new ArrayList<>();
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

        Api.RetrofitGetJobDetails service = retrofit.create(Api.RetrofitGetJobDetails.class);
        Call<String> call = service.putParam(id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String code = object.getString("code");
                    switch (code) {
                        case "200": {
                            nestedScroll.setVisibility(View.VISIBLE);
                            JSONObject data = object.getJSONObject("response");
                            //owner info
                            JSONObject owner_info = data.getJSONObject("owner_info");
                            textViewFixName.setText(owner_info.getString("fixName"));
                            String ownerImgProfile = owner_info.getString("image");

                            try {
                                if (!ownerImgProfile.isEmpty() && !ownerImgProfile.equals("null")) {
                                    Glide.with(getApplicationContext()).load(ownerImgProfile).into(circleImageViewOwner);

                                } else {
                                    Glide.with(getApplicationContext()).load(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_account_circle_24))
                                            .into(circleImageViewOwner);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            ownerId = owner_info.getString("id");
                            textViewName.setText( owner_info.getString("name"));
                            //job info

                            String img1 = data.getString("image1");
                            if (!img1.isEmpty()&&!img1.equals("null")){
                                arrayListImages.add(img1);
                            }
                            String img2 = data.getString("image2");
                            if (!img2.isEmpty()&&!img2.equals("null")){
                                arrayListImages.add(img2);
                            }
                            String img3 = data.getString("image3");
                            if (!img3.isEmpty()&&!img3.equals("null")){
                                arrayListImages.add(img3);
                            }
                            if (arrayListImages.size()>0){
                                initSlider(arrayListImages);
                            }

                            String owner_name = data.getString("owner_name");
                            if (!owner_name.isEmpty()&&!owner_name.equals("null")){
                                textViewOwnerName.setText(owner_name);
                            }else{
                                findViewById(R.id.layOwnerName).setVisibility(View.GONE);
                            }
                            String owner_type=data.getString("owner_type");
                            textViewType.setText(owner_type);
                            if (owner_type.equals("PERSONAL")){
                                layOffice.setVisibility(View.GONE);
                            }else if(owner_type.equals("OFFICE")){
                                layOffice.setVisibility(View.VISIBLE);
                                textViewOfficeName.setText(data.getString("organization_name"));
                                textViewOfficeAddress.setText(data.getString("country_of_residencey"));
                            }
                            textViewDate.setText(data.getString("before_x_days"));

                            textViewBirthday.setText(data.getString("birthday"));
                            textViewJob.setText(data.getString("job_title"));
                            textViewExperience.setText(data.getString("experience"));
                            textViewSalary.setText(data.getString("salary"));
                            String bailing_money = data.getString("bailing_money");
                            if (!bailing_money.isEmpty()&&!bailing_money.equals("null")){
                                textViewBillingMoney.setText(bailing_money);
                            }else{
                                findViewById(R.id.laybailing_money).setVisibility(View.GONE);
                            }

                            isActive =  data.getString("active");
                            textViewSex.setText(data.getString("sex"));
                            textViewReligion.setText(data.getString("religion"));
                            textViewCountry.setText(data.getString("country_of_residencey"));
                            textViewDesc.setText(data.getString("description"));
                            textViewViews.setText(data.getString("views"));






                            isMyAd = data.getBoolean("is_owner");
                            if (isMyAd||isActive.equals("NO")){
                                cardSignature.setVisibility(View.GONE);
                            }
                            if (isMyAd){
                                btnStopAd.setVisibility(View.VISIBLE);
                            }
                            if (isActive.equals("YES")){
                                btnStopAd.setText("ايقاف مؤقت للاعلان");
                            }else {
                                btnStopAd.setText("تفعيل الاعلان");
                            }
                            is_liked = data.getBoolean("is_liked");
                            if (is_liked){
                                addToFavorite.setText("ازالة من المفضلة");
                            }
                            break;
                        }
                        default: {
                            warningMsg("حدث خطأ الرجاء المحاولة مرة اخرى");
                            break;
                        }
                    }


                    progressLay.setVisibility(View.GONE);

                } catch (Exception e) {

                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    warningMsg("حدث خطأ الرجاء المحاولة مرة اخرى");
                }
                progressLay.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                progressLay.setVisibility(View.GONE);
                warningMsg("حدث خطأ الرجاء المحاولة مرة اخرى");
            }
        });
    }

    boolean is_liked = false;

    //dialog message
    private void warningMsg(String message) {
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
                dialog.dismiss();

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