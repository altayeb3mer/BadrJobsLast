package com.example.badrjobs.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.badrjobs.Adapter.AdapterAds;
import com.example.badrjobs.GlobalVar;
import com.example.badrjobs.Model.ModelJob;
import com.example.badrjobs.R;
import com.example.badrjobs.Utils.Api;
import com.example.badrjobs.Utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class FragmentFavorite extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    Context context;
    View view;
    TextView textViewTitle;
    RecyclerView recyclerView;
    AdapterAds adapterJobs;
    ArrayList<ModelJob> arrayList;
    GridLayoutManager gridLayoutManager;
    EditText edtSearch;

    //progressLay
    LinearLayout progressLay;

    public FragmentFavorite() {
        // Required empty public constructor
    }

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_favorite, container, false);
        init();
        getFavorite();
        return view;
    }

    private void init() {
        mSwipeRefreshLayout = view. findViewById(R.id.swipeContainer);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //textView title
        edtSearch = view.findViewById(R.id.edtSearch);
        edtSearch.addTextChangedListener(new TextWatcher() {
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

                // filter your list from your input
                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });
        textViewTitle = view.findViewById(R.id.txtTitle);
        progressLay = view.findViewById(R.id.progressLay);
//        SpannableString content = new SpannableString("عند ما يكون الإعلان منتهي سوف يتم حذفه من المفضلة تلقائي");
//        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
//        textViewTitle.setText(content);
        textViewTitle.setText(new GlobalVar().underLinerTextView("عند ما يكون الإعلان منتهي سوف يتم حذفه من المفضلة تلقائي"));
    }


    void filter(String text){
        text = text.toLowerCase();
        ArrayList<ModelJob> temp = new ArrayList();
        for(ModelJob d: arrayList){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getTitle().toLowerCase().contains(text)){
                temp.add(d);
            }
        }
        //update recyclerview
        adapterJobs.updateList(temp);
    }

    private void initAdapter(ArrayList<ModelJob> jobArrayList){
        recyclerView = view.findViewById(R.id.recycler);

        gridLayoutManager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapterJobs = new AdapterAds(getActivity(),jobArrayList);
        recyclerView.setAdapter(adapterJobs);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }


    private void getFavorite() {
        arrayList = new ArrayList<>();
        progressLay.setVisibility(View.VISIBLE);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Content-Type", "application/json;");
                        ongoing.addHeader("Accept", "application/json");
                        ongoing.addHeader("lang", SharedPrefManager.getInstance(getContext()).GetAppLanguage());
                        String token = SharedPrefManager.getInstance(context).getAppToken();
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

        Api.RetrofitGetFavorite service = retrofit.create(Api.RetrofitGetFavorite.class);
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
                                JSONObject job = arrayObj.getJSONObject("job");
                                JSONObject owner_info = job.getJSONObject("owner_info");

                                ModelJob item = new ModelJob();
                                item.setId(job.getString("id"));
                                item.setTitle(job.getString("job_title"));
                                //owner_info
                                item.setOwnerName(owner_info.getString("name"));
                                item.setOwnerNiceName(owner_info.getString("fixName"));
                                item.setOwnerImage(owner_info.getString("image"));
                                item.setLiked(job.getBoolean("is_liked"));
                                item.setActive(job.getString("active"));

                                arrayList.add(item);
                            }


                            if (arrayList.size()>0){
//                                setRecycler(arrayList);
                               initAdapter(arrayList);
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


    @Override
    public void onRefresh() {
        getFavorite();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);

            }
        }, 1000);
    }
}