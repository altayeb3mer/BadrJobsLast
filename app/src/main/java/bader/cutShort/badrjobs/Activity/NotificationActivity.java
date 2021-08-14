package bader.cutShort.badrjobs.Activity;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import bader.cutShort.badrjobs.Adapter.AdapterNotification;
import bader.cutShort.badrjobs.Model.ModelNotification;
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

public class NotificationActivity extends ToolbarClass implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recyclerView;
    AdapterNotification adapterNotification;
    ArrayList<ModelNotification> arrayList;
    GridLayoutManager gridLayoutManager;
    //    ImageView icNotification;
    LinearLayout progressLay;
    LinearLayout noDataLay;
    //language controller
    private LocaleChangerAppCompatDelegate localeChangerAppCompatDelegate;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_notification, "");
        init();
    }

    private void init() {
        mSwipeRefreshLayout = findViewById(R.id.swipeContainer);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        noDataLay = findViewById(R.id.noDataLay);
        progressLay = findViewById(R.id.progressLay);
        recyclerView = findViewById(R.id.recycler);
//        icNotification = findViewById(R.id.icNotification);
//        icNotification.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                startActivity(new Intent(getApplicationContext(), JobsContracts.class));
//            }
//        });
        getNotificationFun();
    }

    private void getNotificationFun() {
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
                        String token = SharedPrefManager.getInstance(getApplicationContext()).getAppToken();
                        ongoing.addHeader("Authorization", token);
//                        ongoing.addHeader("Authorization", "Bearer"+" "+"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIzIiwianRpIjoiN2U4MzkyNjZkMDZiMjA3MTUzN2VlNTc3NjFhZTY5NmQxYTA0MWI0OWJlZTYyMDdmNzljMjg1MjlhYzdlZDkyYzllOGI5NmJkZGNlMjlhOWQiLCJpYXQiOiIxNjE2OTQwMzIxLjI5NjUzMyIsIm5iZiI6IjE2MTY5NDAzMjEuMjk2NTM3IiwiZXhwIjoiMTY0ODQ3NjMyMS4yNjA2MDgiLCJzdWIiOiI3Iiwic2NvcGVzIjpbXX0.jQNnXoovkDazDSV-4rsw7vRcGZuJVG9JOdDsSXQdpdmaCzkiEOqwIaw9qmvcTAC2QdWcwgUIHGowTeQ2WiEHJpgQXMOPbaRPLHNRfhzLAXGwX1733lkmSrKRrsLRZxI_RUgl-OuVLtyjfYznKcl4vU_mIxtiu_Or_Ri7eYOTfbnrBTx3czIVInvQpssyVWszt1aO0hG93-KE0AfNeXP6Hi-QVFwHx4sCNYf3N8LRb7Okv41UJ8uoUrzSmQYrL5o--Ru_bgIzYFcVcfiCaBxHZVAmGU5tXlYfkAhuVrW4jr0Q8b55n-GTYqdAUD3-PF2om7BZiV5VPD7XqNlY4XEsYWMPg32uVjiXdi5l00VX5DS4Mb9wufPsr-PFdp4WcgNXwtcu_K_V-88shf_6LCJFqbPKOlkqDKJnm39nVNaniuupjAFAiOj47vHxYy1kLW5A2oEYMy5VOxRYpyblByp0f5FYCb0xokj6J5DZLGPOZ8Iu_MvG-WH62CrY-KTrU8-IOLTL3vgFFOo44A8ZLJ5f0feifAFHsCgBf5q5TcLvpaL8NigXp5JTbJGJAM1bKyu3EI2pC7NUojlN3m7nOrudNbomVAyBoUO3OwmAsWoa85_7WM0DYLTtjnUGlVaavgVOhG2Q3Pr7R2JVspabKUMCiAkc7i8KPhJS-PEhdz304GA");
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

        Api.RetrofitGetNotification service = retrofit.create(Api.RetrofitGetNotification.class);

        Call<String> call = service.putParam();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("code");

                    if (statusCode.equals("200")) {
                        JSONArray data = object.getJSONArray("response");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject itemData = data.getJSONObject(i);
                            ModelNotification item = new ModelNotification();
                            item.setId(itemData.getString("id"));
                            item.setTitle(itemData.getString("title"));
                            item.setBody(itemData.getString("body"));
                            item.setDate(itemData.getString("created_at"));
                            item.setType(itemData.getString("type"));
                            if (!item.getType().equals("NORMAL")) {
                                JSONObject payload = itemData.getJSONObject("payload");
                                item.setContractId(payload.getString("contract_id"));
                            }
                            arrayList.add(item);
                        }
                    } else {
                        warningMsg(object.getString("message"), false);
                    }

                    if (arrayList.size() > 0) {
                        initAdapter(arrayList);
                        noDataLay.setVisibility(View.GONE);
                    } else {
                        warningMsg("ليس لديك اشعارات حتى الان", true);
                        noDataLay.setVisibility(View.VISIBLE);
                    }

                    progressLay.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    warningMsg(e.getMessage().toString(), false);
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                progressLay.setVisibility(View.GONE);
                warningMsg("time out", false);
            }
        });
    }

    private void initAdapter(ArrayList<ModelNotification> array) {
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapterNotification = new AdapterNotification(this, array);
        recyclerView.setAdapter(adapterNotification);
    }

    @Override
    public void onRefresh() {
        getNotificationFun();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);

            }
        }, 1000);
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