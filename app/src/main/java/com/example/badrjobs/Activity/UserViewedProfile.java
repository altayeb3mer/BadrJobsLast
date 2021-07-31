package com.example.badrjobs.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.badrjobs.R;
import com.example.badrjobs.Utils.Api;
import com.example.badrjobs.Utils.SharedPrefManager;
import com.example.badrjobs.Utils.ToolbarClass;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.disposables.Disposable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import sdk.chat.core.dao.Thread;
import sdk.chat.core.dao.User;
import sdk.chat.core.session.ChatSDK;
import sdk.guru.common.RX;

public class UserViewedProfile extends ToolbarClass {

    ImageView imageViewHeader, imageViewFlag;
    CircleImageView circleImageViewProfile;
    TextView textViewFix, textViewBio, textViewName, textViewEmail, textViewJob;
    AppCompatButton buttonBlock;
    LinearLayout layoutChat, layContainer;
    LinearLayout progressLay;
    String firebase_uid = "";
    boolean isMyProfile = false;
    boolean blocked = false;
    private String userId = "", fcmToken = "",
            name = "", fixName = "", image = "";
    //language controller
    private LocaleChangerAppCompatDelegate localeChangerAppCompatDelegate;

    private void init() {
        layContainer = findViewById(R.id.layContainer);
        layContainer.setVisibility(View.GONE);
        progressLay = findViewById(R.id.progressLay);
        imageViewHeader = findViewById(R.id.headerImg);
        imageViewFlag = findViewById(R.id.imgFlag);
        circleImageViewProfile = findViewById(R.id.img);
        textViewFix = findViewById(R.id.fixName);
        textViewBio = findViewById(R.id.bio);
        textViewName = findViewById(R.id.name);
        textViewEmail = findViewById(R.id.email);
        textViewJob = findViewById(R.id.job);
        buttonBlock = findViewById(R.id.btnBlock);
        buttonBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blockUser(userId);
            }
        });
        layoutChat = findViewById(R.id.layChat);
        layoutChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!firebase_uid.isEmpty() && firebase_uid.equals("null")) {
                    User user = ChatSDK.core().getUserNowForEntityID(firebase_uid);
//                    User user = ChatSDK.core().getUserNowForEntityID("57RbHQC9RsZ9ocBn19nwPR95kyI2");

                    createThread(user);
                }
            }
        });

    }

    public void createThread(User user) {
        Disposable d = ChatSDK.thread().createThread(user.getName(), user, ChatSDK.currentUser())
                .observeOn(RX.main())
                .doFinally(() -> {
                    // Runs when process completed from error or success
                })
                .subscribe(thread -> {
                    try {
                        openChatActivityWithThread(UserViewedProfile.this, thread);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, throwable -> {
                    // If there type an error
                    Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }

    public void openChatActivityWithThread(Context context, Thread thread) {
        ChatSDK.ui().startChatActivityForID(context, thread.getEntityID());
    }

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_user_viewed_profile, "");
        Bundle args = getIntent().getExtras();
        if (args != null) {
            userId = args.getString("userId");
            isMyProfile = args.getBoolean("isMyProfile");
            blocked = args.getBoolean("blocked");
        }
        init();
        getProfile();
        if (isMyProfile) {
//            layoutChat.setVisibility(View.INVISIBLE);
            try {
                findViewById(R.id.layCallChat).setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        uiBlock(blocked);
    }

    private void blockUser(String userId) {
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

        Api.RetrofitBlockSomeone service = retrofit.create(Api.RetrofitBlockSomeone.class);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("bad_user_id", userId);
        Call<String> call = service.putParam(hashMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String code = object.getString("code");
                    boolean success = object.getBoolean("success");
                    switch (code) {
                        case "200": {
                            if (success) {
                                warningMsg(fixName + "\n" + getString(R.string.user_blocked));
                                blocked = true;
                                uiBlock(blocked);
                            }
                            break;
                        }
                        default: {
                            warningMsg(getString(R.string.error_try_again));
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

    private void uiBlock(boolean b) {
        if (b){
            findViewById(R.id.layBlocked).setVisibility(View.VISIBLE);
            findViewById(R.id.layNotBlocked).setVisibility(View.GONE);
        }

    }

    private void getProfile() {
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

        Api.RetrofitShowProfileOther service = retrofit.create(Api.RetrofitShowProfileOther.class);
        Call<String> call = service.putParam(userId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("code");
                    switch (statusCode) {
                        case "200": {
                            layContainer.setVisibility(View.VISIBLE);
                            JSONArray array = object.getJSONArray("response");
                            JSONObject objectData = array.getJSONObject(0);
                            JSONObject userData = objectData.getJSONObject("user");


                            String header_image = userData.getString("header_image");
                            firebase_uid = userData.getString("firebase_uid");
                            if (!header_image.isEmpty() && !header_image.equals("null")) {
                                Glide.with(UserViewedProfile.this).load(header_image).into(imageViewHeader);
                            } else {
                                Glide.with(UserViewedProfile.this).load(ContextCompat.getDrawable(UserViewedProfile.this, R.drawable.shape_btn_nav_bg))
                                        .into(imageViewHeader);
                            }


                            Glide.with(UserViewedProfile.this).
                                    load(userData.getString("nationality_flag")).into(imageViewFlag);

                            image = userData.getString("image");
                            if (!image.isEmpty() && !image.equals("null")) {
                                Glide.with(UserViewedProfile.this).load(image).into(circleImageViewProfile);
                            } else {
                                Glide.with(UserViewedProfile.this).load(ContextCompat.getDrawable(UserViewedProfile.this, R.drawable.ic_baseline_account_circle_24))
                                        .into(circleImageViewProfile);
                            }
//                            Glide.with(UserViewedProfile.this).
//                                    load(image).into(circleImageViewProfile);


                            fixName = userData.getString("fixName");
                            textViewFix.setText(fixName);
                            String bio = userData.getString("bio");
                            if (!bio.isEmpty() && !bio.equals("null")) {
                                textViewBio.setText(bio);
                            } else {
                                textViewBio.setText("");
                            }

                            name = userData.getString("name");
                            textViewName.setText(name);
                            textViewEmail.setText(userData.getString("email_verified_at"));
                            textViewJob.setText(userData.getString("job"));

                            fcmToken = userData.getString("job");


                            break;
                        }

                        default: {
                            Intent intent = new Intent(UserViewedProfile.this, BlockedUser.class);
                            intent.putExtra("name", name);
                            intent.putExtra("fixName", fixName);
                            intent.putExtra("image", image);
                            startActivity(intent);
                            break;
                        }
                    }
                    progressLay.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
//                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    warningMsg(e.getMessage());
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                progressLay.setVisibility(View.GONE);
                warningMsg("Time out .Try again");
            }
        });
    }

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
        yes.setText(R.string.ok);


        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (verified){
//                    Intent intent = new Intent(ConfirmDeal.this, SignatureImage.class);
//                    intent.putExtra("contractId",contractId);
//                    intent.putExtra("type","prevContract");
//                    startActivity(intent);
//                }else{
//                    dialog.dismiss();
//                }
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