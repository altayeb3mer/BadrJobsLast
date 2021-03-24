package com.example.badrjobs.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.badrjobs.Activity.AddJob;
import com.example.badrjobs.GlobalVar;
import com.example.badrjobs.Model.ModelCountry;
import com.example.badrjobs.Model.ModelDept;
import com.example.badrjobs.R;
import com.example.badrjobs.Utils.Api;
import com.example.badrjobs.Utils.SharedPrefManager;

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

public class FragmentAdd extends Fragment {
    View view;
    TextView textViewTitle;
    Context context;
    AppCompatButton button;
    ArrayList<String> arrayList,arrayListSubDept;
    LinearLayout progressLay,laySub;
    Spinner spinner1, spinner2, spinner3;

    ArrayList<ModelCountry> arrayListData;

    String countryId = "";
    private String deptId="",subDeptId="";
    private boolean hasSub=false;

    public FragmentAdd() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add, container, false);
        init();
        getCountries();
        return view;
    }

    private void init() {
        laySub = view.findViewById(R.id.laySub);
        spinner1 = view.findViewById(R.id.spinner1);
        spinner2 = view.findViewById(R.id.spinner2);
        spinner3 = view.findViewById(R.id.spinner3);
        progressLay = view.findViewById(R.id.progressLay);
        button = view.findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddJob.class);
                intent.putExtra("countryId",countryId);
                intent.putExtra("deptId",deptId);
                intent.putExtra("supDeptId",subDeptId);
                startActivity(intent);
            }
        });
        textViewTitle = view.findViewById(R.id.txtTitle);
        textViewTitle.setText(new GlobalVar().underLinerTextView("الرجاء اختيار الموقع الصحيح لعرض علانك"));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    //get countries
    private void getCountries() {
        arrayListData = new ArrayList<>();
        arrayList = new ArrayList<>();
        arrayList.add("اختار البلد..");
        progressLay.setVisibility(View.VISIBLE);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Content-Type", "application/json;");
                        ongoing.addHeader("Accept", "application/json");
                        ongoing.addHeader("lang", SharedPrefManager.getInstance(context).GetAppLanguage());
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

        Api.RetrofitGetMainCountry service = retrofit.create(Api.RetrofitGetMainCountry.class);

        Call<String> call = service.putParam();
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
                                ModelCountry item = new ModelCountry();
                                item.setId(arrayObj.getString("id"));
                                item.setName(arrayObj.getString("transName"));
                                item.setImage(arrayObj.getString("image"));

                                arrayListData.add(item);
                                arrayList.add(arrayObj.getString("transName"));
                            }


                            if (arrayList.size() > 0) {
                                initSpinnerCountry(arrayList);
                            } else {
                                Toast.makeText(context, "لاتوجد عناصر", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }

                        default: {
                            Toast.makeText(context, "حدث خطأ حاول مجددا", Toast.LENGTH_SHORT).show();
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

    private void initSpinnerCountry(ArrayList<String> array) {
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, array) {
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
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner1.setAdapter(adapter2);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    countryId = "";
                } else {
                    countryId = arrayListData.get(position-1).getId();
                    getDepts();
                    laySub.setVisibility(View.INVISIBLE);
                }

                Toast.makeText(context, ""+countryId, Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    ArrayList<String> arrayListDept;
    ArrayList<ModelDept> arrayListDeptData;
    private void getDepts() {
        arrayListDept = new ArrayList<>();
        arrayListDept.add("اختر قسم");
        arrayListDeptData = new ArrayList<>();
        progressLay.setVisibility(View.VISIBLE);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Content-Type", "application/json;");
                        ongoing.addHeader("Accept", "application/json");
                        ongoing.addHeader("lang", SharedPrefManager.getInstance(context).GetAppLanguage());
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

                                arrayListDeptData.add(item);
                                arrayListDept.add(arrayObj.getString("transName"));
                            }



                            if (arrayList.size()>0){
                            initSpinnerDept(arrayListDept);
                            }else{
                                Toast.makeText(context, "لاتوجد عناصر", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }
                        default: {
                            Toast.makeText(context, "حدث خطأ حاول مجددا", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    progressLay.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                progressLay.setVisibility(View.GONE);
            }
        });
    }

    //init spinner dept
    private void initSpinnerDept(ArrayList<String> array) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, array) {
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
        spinner2.setAdapter(adapter);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    deptId = "";
                } else {
                    deptId = arrayListDeptData.get(position-1).getId();
                    if (arrayListDeptData.get(position-1).isHasSub()){
                        getSubDept();
                        laySub.setVisibility(View.VISIBLE);
                        hasSub = true;
                    }else{
                        hasSub = false;
                        laySub.setVisibility(View.INVISIBLE);
                    }
                }

//                Toast.makeText(context, ""+countryId, Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    ArrayList<ModelDept> arrayListSubDeptData;;
    //get sub dept
    private void getSubDept() {
        arrayListSubDept = new ArrayList<>();
        arrayListSubDept.add("اختر قسم فرعي");
        arrayListSubDeptData = new ArrayList<>();
        progressLay.setVisibility(View.VISIBLE);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Content-Type", "application/json;");
                        ongoing.addHeader("Accept", "application/json");
                        ongoing.addHeader("lang", SharedPrefManager.getInstance(context).GetAppLanguage());
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

        Api.RetrofitGetSubCategory service = retrofit.create(Api.RetrofitGetSubCategory.class);
        HashMap<String,String> hashMap = new HashMap<>();
//        hashMap.put("countryId",countryId);
        hashMap.put("countryId",countryId);
        hashMap.put("categoryId",deptId);
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
                                item.setCountryId(arrayObj.getString("category_id"));
                                item.setTextColor(arrayObj.getString("font_color"));
                                item.setHasSub(arrayObj.getBoolean("hasChild"));

                                arrayListSubDeptData.add(item);
                                arrayListSubDept.add(arrayObj.getString("transName"));
                            }



                            if (arrayListSubDept.size()>0){
                                initSpinnerSubDept(arrayListSubDept);
                            }else{
                                Toast.makeText(context, "لاتوجد عناصر", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }

                        default: {
                            Toast.makeText(context, "حدث خطأ حاول مجددا", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    progressLay.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                progressLay.setVisibility(View.GONE);
            }
        });
    }



    private void initSpinnerSubDept(ArrayList<String> array) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, array) {
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
        spinner3.setAdapter(adapter);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    subDeptId = "";
                } else {
                    subDeptId = arrayListSubDeptData.get(position-1).getId();
                }

//                Toast.makeText(context, ""+countryId, Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }




}