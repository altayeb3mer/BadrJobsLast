package bader.cutShort.badrjobs.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import bader.cutShort.badrjobs.Adapter.AdapterBlockedUser;
import bader.cutShort.badrjobs.Model.ModelUserBlocked;
import com.example.badrjobs.R;
import bader.cutShort.badrjobs.Utils.Api;
import bader.cutShort.badrjobs.Utils.SharedPrefManager;
import bader.cutShort.badrjobs.Utils.ToolbarClass;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;

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

public class BlockedUsersActivity extends ToolbarClass {

    RecyclerView recyclerView;
    AdapterBlockedUser adapterBlockedUser;
    GridLayoutManager gridLayoutManager;

    LinearLayout progressLay;
    //language controller
    private LocaleChangerAppCompatDelegate localeChangerAppCompatDelegate;

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_blocked_users, "");
        init();
        getBlockedList();
    }

    private void init() {
        recyclerView = findViewById(R.id.recycler);
        progressLay = findViewById(R.id.progressLay);
        noDataLay = findViewById(R.id.noDataLay);
    }

    private void getBlockedList() {
        ArrayList<ModelUserBlocked> arrayList = new ArrayList<>();
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

        Api.RetrofitGetBlockedList service = retrofit.create(Api.RetrofitGetBlockedList.class);
        Call<String> call = service.putParam();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("code");
                    switch (statusCode) {
                        case "200": {
                            boolean success = object.getBoolean("success");
                            if (success) {
                                JSONArray dataArray = object.getJSONArray("response");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObj = dataArray.getJSONObject(i);
                                    JSONObject bad_user_info = dataObj.getJSONObject("bad_user_info");
                                    ModelUserBlocked item = new ModelUserBlocked();

                                    item.setId(bad_user_info.getString("id"));
                                    item.setFixName(bad_user_info.getString("fixName"));
                                    item.setName(bad_user_info.getString("name"));
                                    item.setImgProfile(bad_user_info.getString("image"));


                                    arrayList.add(item);


                                }
                            } else {
                                Toast.makeText(BlockedUsersActivity.this, R.string.error_try_again, Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }
                        default: {
                            Toast.makeText(BlockedUsersActivity.this, R.string.error_try_again, Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                    if (arrayList.size() > 0) {
                        iniAdapter(arrayList);
                        noDataLay.setVisibility(View.GONE);
                    } else {
                        noDataLay.setVisibility(View.VISIBLE);
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

    LinearLayout noDataLay;
    private void iniAdapter(ArrayList<ModelUserBlocked> list) {
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapterBlockedUser = new AdapterBlockedUser(this, list, progressLay,noDataLay);
        recyclerView.setAdapter(adapterBlockedUser);
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