package bader.cutShort.badrjobs.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import bader.cutShort.badrjobs.Adapter.AdapterAds;
import bader.cutShort.badrjobs.GlobalVar;
import bader.cutShort.badrjobs.Model.ModelJob;
import com.example.badrjobs.R;
import bader.cutShort.badrjobs.Utils.Api;
import bader.cutShort.badrjobs.Utils.SharedPrefManager;
import bader.cutShort.badrjobs.Utils.ToolbarClass;
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

import static android.content.ContentValues.TAG;

public class AdsActivity extends ToolbarClass implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    AdapterAds adapterDept;
    ArrayList<ModelJob> arrayList;
    GridLayoutManager gridLayoutManager;
    LinearLayout progressLay;

    String categoryId = "",countryId = "";

    //pagination var
    NestedScrollView nestedScrollView;
    boolean isLoading = true;
    String s_current_page = "", s_last_page = "", s_perPage = "";

    TextView textViewDept,textViewSupDept;
    ImageView imageViewFlag;
    LinearLayout noDataLay;


    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.ads_activity, "");
        arrayList = new ArrayList<>();
        Bundle arg = getIntent().getExtras();
        if (arg!=null){
            categoryId = arg.getString("categoryId");
            countryId = arg.getString("countryId");
        }
        init();
        setRecycler(arrayList);
        getJobs(s_current_page);
    }
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private void init() {
        mSwipeRefreshLayout =  findViewById(R.id.swipeContainer);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        noDataLay = findViewById(R.id.noDataLay);
        textViewDept = findViewById(R.id.txtDept);
        textViewSupDept = findViewById(R.id.txtSupDept);
        imageViewFlag = findViewById(R.id.imgFlag);

        progressLay = findViewById(R.id.progressLay);
        recyclerView = findViewById(R.id.recycler);
        nestedScrollView = findViewById(R.id.nestedScroll);
        paginationFun();
    }

    private void paginationFun() {
        //get last view on nestedScrollView
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY > oldScrollY) {
                    Log.i(TAG, "Scroll DOWN");
                }
                if (scrollY < oldScrollY) {
                    Log.i(TAG, "Scroll UP");
                }

                if (scrollY == 0) {
                    Log.i(TAG, "TOP SCROLL");
                }

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    Log.i(TAG, "BOTTOM SCROLL");

                    if (Double.parseDouble(s_last_page) > Double.parseDouble(s_current_page) && !isLoading )
                        getJobs(Integer.parseInt(s_current_page) + 1 + "");}

            }
        });
    }


    public static String countryImage = "",s_dept="",s_sub_dept="";

    private void getJobs(String page) {
        progressLay.setVisibility(View.VISIBLE);
        isLoading = false;
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

        Api.RetrofitGetJobs service = retrofit.create(Api.RetrofitGetJobs.class);
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("page",page);
        hashMap.put("countryId",countryId);
        hashMap.put("categoryId",categoryId);
        Call<String> call = service.putParam(hashMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("code");
                    JSONObject responseObj = object.getJSONObject("response");
                    //country_info
                    JSONObject countryObj = responseObj.getJSONObject("country_info");
                    countryImage = countryObj.getString("image");
                    Glide.with(getApplicationContext()).load(countryImage).into(imageViewFlag);

                    //bread_crumb
                    JSONArray deptInfoArray = responseObj.getJSONArray("bread_crumb");
                    JSONObject breadObj = deptInfoArray.getJSONObject(0);
                    s_dept = breadObj.getString("transName");
                    textViewDept.setText(new GlobalVar().underLinerTextView(s_dept));

                    if (deptInfoArray.length()==2){
                        JSONObject breadObj2 = deptInfoArray.getJSONObject(1);
                        s_sub_dept = breadObj2.getString("transName");
                        textViewSupDept.setText(new GlobalVar().underLinerTextView(s_sub_dept));
                    }


                    //pagination
                    s_current_page = responseObj.getString("current_page");
                    s_last_page = responseObj.getString("last_page");
                    s_perPage = responseObj.getString("per_page");
                    String total =  responseObj.getString("total");
                    switch (statusCode) {
                        case "200": {

                            JSONArray array = responseObj.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject arrayObj = array.getJSONObject(i);
                                JSONObject owner_info = arrayObj.getJSONObject("owner_info");

                                ModelJob item = new ModelJob();
                                item.setId(arrayObj.getString("id"));
                                item.setTitle(arrayObj.getString("job_title"));
                                //owner_info
                                item.setOwnerName(owner_info.getString("name"));
                                item.setOwnerNiceName(owner_info.getString("fixName"));
                                item.setOwnerImage(owner_info.getString("image"));
                                item.setActive(arrayObj.getString("active"));
                                item.setLiked(arrayObj.getBoolean("is_liked"));
                                item.setDate(arrayObj.getString("before_x_days"));

                                arrayList.add(item);
                            }


                            if (arrayList.size()>0){
                                if (s_current_page==s_last_page){
                                    int lastItem = Integer.parseInt(total)%Integer.parseInt(s_perPage);
                                    adapterDept.notifyItemInserted(arrayList.size()-lastItem);
                                }else{
                                    adapterDept.notifyItemInserted(arrayList.size()-Integer.parseInt(s_perPage));
                                }
                                noDataLay.setVisibility(View.GONE);
                            }else{
                                Toast.makeText(getApplicationContext(), "لاتوجد عناصر", Toast.LENGTH_SHORT).show();
                                noDataLay.setVisibility(View.VISIBLE);
                            }
                            break;
                        }

                        default: {
                            Toast.makeText(getApplicationContext(), R.string.error_try_again, Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    progressLay.setVisibility(View.GONE);
                    isLoading = false;
                } catch (Exception e) {
                    isLoading = false;
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressLay.setVisibility(View.GONE);
                isLoading = false;
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                progressLay.setVisibility(View.GONE);
                isLoading = false;
            }
        });
    }

    private void setRecycler(ArrayList<ModelJob> arrayList) {
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapterDept = new AdapterAds(this,arrayList);
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

    @Override
    public void onRefresh() {
        arrayList = new ArrayList<>();
        setRecycler(arrayList);
        s_current_page = "";
        getJobs(s_current_page);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);

            }
        }, 1000);
    }
}