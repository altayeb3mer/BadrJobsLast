package com.example.badrjobs.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.badrjobs.Adapter.AdapterDept;
import com.example.badrjobs.Model.ModelDept;
import com.example.badrjobs.R;
import com.example.badrjobs.Utils.Api;
import com.example.badrjobs.Utils.SharedPrefManager;
import com.example.badrjobs.Utils.ToolbarClass;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;

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

public class DeptsActivity extends ToolbarClass {

    RecyclerView recyclerView;
    AdapterDept adapterDept;
    ArrayList<ModelDept> arrayList;
    GridLayoutManager gridLayoutManager;
    String countryId = "";
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_depts, "");
        Bundle arg = getIntent().getExtras();
        if (arg!=null){
            countryId = arg.getString("countryId");
        }
        init();
        getDepts();
    }


    private void init() {
        recyclerView = findViewById(R.id.recycler);
        progressLay = findViewById(R.id.progressLay);
    }

    LinearLayout progressLay;

    private void getDepts() {
        arrayList = new ArrayList<>();
        progressLay.setVisibility(View.VISIBLE);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Content-Type", "application/json;");
                        ongoing.addHeader("Accept", "application/json");
                        ongoing.addHeader("lang", SharedPrefManager.getInstance(getApplicationContext()).GetAppLanguage());
//                        String token = SharedPrefManager.getInstance(context).GetToken();
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

        Api.RetrofitGetCategory service = retrofit.create(Api.RetrofitGetCategory.class);
        HashMap<String,String> hashMap = new HashMap<>();
//        hashMap.put("countryId",countryId);
        hashMap.put("countryId",countryId);
        Call<String> call = service.putParam(hashMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("code");
                    switch (statusCode) {
                        case "200": {

                            JSONArray array = object.getJSONArray("response");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject arrayObj = array.getJSONObject(i);
                                ModelDept item = new ModelDept();
                                item.setId(arrayObj.getString("id"));
                                item.setName(arrayObj.getString("transName"));
                                item.setBgColor(arrayObj.getString("background_color"));
                                item.setTextColor(arrayObj.getString("font_color"));
                                item.setHasSub(arrayObj.getBoolean("hasChild"));
//                                item.setCountryId(arrayObj.getString("hasChild"));

                                arrayList.add(item);
                            }



                            if (arrayList.size()>0){
                                setRecycler(arrayList);
                            }else{
                                Toast.makeText(getApplicationContext(), "لاتوجد عناصر", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }

                        default: {
                            Toast.makeText(getApplicationContext(), "حدث خطأ حاول مجددا", Toast.LENGTH_SHORT).show();
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

    private void setRecycler(ArrayList<ModelDept> arrayList) {
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapterDept = new AdapterDept(this,arrayList,countryId);
        recyclerView.setAdapter(adapterDept);
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