package bader.cutShort.badrjobs.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import bader.cutShort.badrjobs.Adapter.AdapterCountry;
import bader.cutShort.badrjobs.Model.ModelCountry;
import com.example.badrjobs.R;
import bader.cutShort.badrjobs.Utils.Api;
import bader.cutShort.badrjobs.Utils.SharedPrefManager;

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


public class FragmentMain extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    AdapterCountry adapterCountry;
    ArrayList<ModelCountry> arrayList;
    GridLayoutManager gridLayoutManager;
    LinearLayout progressLay;
    public FragmentMain() {
        // Required empty public constructor
    }

    private SwipeRefreshLayout mSwipeRefreshLayout;

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main, container, false);
        init();
        getCountries();
        return view;
    }

    private void init() {
        mSwipeRefreshLayout = view. findViewById(R.id.swipeContainer);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        recyclerView = view.findViewById(R.id.recycler);
        progressLay = view.findViewById(R.id.progressLay);
        recyclerView.addItemDecoration(new DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL));
        gridLayoutManager = new GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

    }

    private void setRecycler(ArrayList<ModelCountry> list){
        adapterCountry = new AdapterCountry(getActivity(),list);
        recyclerView.setAdapter(adapterCountry);
    }

    Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    //
    private void getCountries() {
        arrayList = new ArrayList<>();
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

                                arrayList.add(item);
                            }



                            if (arrayList.size()>0){
                                setRecycler(arrayList);
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

    @Override
    public void onRefresh() {
        getCountries();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);

            }
        }, 1000);
    }

}