package com.example.badrjobs.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.badrjobs.R;
import com.example.badrjobs.Utils.Api;
import com.example.badrjobs.Utils.SharedPrefManager;
import com.example.badrjobs.Utils.ToolbarClass;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SignatureImage extends ToolbarClass {

    View view1,view2;
    ImageView imageViewSignature;
    AppCompatButton buttonSignature,button2;

    String contractId="",type="",contractImgUrl="";

    //language controller
    private LocaleChangerAppCompatDelegate localeChangerAppCompatDelegate;

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.signature_image, "");
        Bundle arg = getIntent().getExtras();
        if (arg!=null){
            contractId = arg.getString("contractId");
            type = arg.getString("type");
        }
        init();
        getContractImage();
        if (type.equals("prevContract")){
            buttonSignature.setText("حفظ");
            button2.setText("مشاركة");
        }
    }

    private void init() {
        button2 = findViewById(R.id.btn2);
        progressLay = findViewById(R.id.progressLay);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        imageViewSignature = findViewById(R.id.signatureImg);
        imageViewSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!contractImgUrl.isEmpty()){
                    Intent intent = new Intent(getApplicationContext(),ImageViewer.class);
                    intent.putExtra("imgUrl",contractImgUrl);
                    startActivity(intent);
                }
            }
        });
        buttonSignature = findViewById(R.id.btnSign);
        buttonSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (type) {
                    case "CONTRACT_REQUEST": {
                        Intent intent = new Intent(SignatureImage.this, SignatureForm1.class);
                        intent.putExtra("contractId", contractId);
                        startActivity(intent);
                        break;
                    }
                    case "SECOND_SIDE_SIGNATURE_REQUEST": {
                        Intent intent = new Intent(SignatureImage.this, SignatureForm2.class);
                        intent.putExtra("contractId", contractId);
                        startActivity(intent);
                        break;
                    }
                    case "prevContract": {
                        if (isStoragePermissionGranted()) {
                            TakeScreenshot();
                        }
                        break;
                    }
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (type) {
                    case "prevContract": {
                        if (isStoragePermissionGranted()) {
                            TakeScreenshotAndShare();

                        }
                        break;
                    }
                }
            }
        });
    }


    private void TakeScreenshotAndShare() {


        try {
            File folder = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "cotShort");
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdirs();
            }
            if (success) {
                // image naming and path  to include sd card  appending name you choose for file, you can change it to your path
                String mPath = "";
                mPath = folder.toString() + "/" +
                        "" + contractNo + ".jpg";

                // create bitmap screen capture
                View v1 = getWindow().getDecorView().getRootView();
                v1.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
                v1.setDrawingCacheEnabled(false);

                File imageFile = new File(mPath);

                FileOutputStream outputStream = new FileOutputStream(imageFile);
                int quality = 100;
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                outputStream.flush();
                outputStream.close();
                shareImage(imageFile);
                // Do something on success
            } else {
                // Do something else on failure
                Toast.makeText(this, "خطأ اثناء انشاء المجلد حاول مجددا", Toast.LENGTH_SHORT).show();
            }


        } catch (Throwable e) {
            e.printStackTrace();
            Toast.makeText(this, "حدث خطأ اثناء حفظ الصورة", Toast.LENGTH_SHORT).show();
        }
    }
    LinearLayout progressLay;

    private void shareImage(File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);

        } else {
            uri = Uri.fromFile(file);
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

//        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
//        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(Intent.createChooser(intent, "ارسال الفاتورة"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "حدث خطأ حاول ثانية", Toast.LENGTH_SHORT).show();
        }
    }
    private static final String TAG = "PERMISSION";
    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }
    private void TakeScreenshot() {

        try {
            File folder = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "cotShort");
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdirs();
            }
            if (success) {
                // image naming and path  to include sd card  appending name you choose for file, you can change it to your path
                String mPath =  folder.toString() + "/" +
                        "" +contractNo + ".jpg";

                // create bitmap screen capture
                View v1 = getWindow().getDecorView().getRootView();
                v1.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
                v1.setDrawingCacheEnabled(false);

                File imageFile = new File(mPath);

                FileOutputStream outputStream = new FileOutputStream(imageFile);
                int quality = 100;
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                outputStream.flush();
                outputStream.close();
//                openScreenshot(imageFile);
                warningMsg("تم حفظ الصورة");

                // Do something on success
            } else {
                // Do something else on failure
                Toast.makeText(this, "خطأ اثناء انشاء المجلد حاول مجددا", Toast.LENGTH_SHORT).show();
            }


        } catch (Throwable e) {
            e.printStackTrace();
            Toast.makeText(this, "حدث خطأ اثناء حفظ الصورة", Toast.LENGTH_SHORT).show();
        }
    }

    String contractNo="";

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

    private void getContractImage() {
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

        Api.RetrofitContractDetails service = retrofit.create(Api.RetrofitContractDetails.class);

        Call<String> call = service.putParam(contractId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    JSONObject data = object.getJSONObject("data");
                    if (data.getBoolean("isFirstSideSign")){
                        view1.setBackgroundColor(getResources().getColor(R.color.colorGreen1));
                    }
                    if (data.getBoolean("isSecondSideSign")){
                        view2.setBackgroundColor(getResources().getColor(R.color.colorGreen1));
                    }
                    contractImgUrl = data.getString("image");
                    contractNo = data.getString("uid");
                    Glide.with(getApplicationContext())
                            .load(contractImgUrl)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true).into(imageViewSignature);

                    switch (type) {
                        case "CONTRACT_REQUEST": {
                            if (data.getBoolean("isFirstSideSign")){
                                buttonSignature.setVisibility(View.GONE);
                            }
                            break;
                        }
                        case "SECOND_SIDE_SIGNATURE_REQUEST": {
                            if (data.getBoolean("isSecondSideSign")){
                                buttonSignature.setVisibility(View.GONE);
                            }
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