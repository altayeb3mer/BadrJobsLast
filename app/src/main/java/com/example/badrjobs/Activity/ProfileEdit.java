package com.example.badrjobs.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.badrjobs.R;
import com.example.badrjobs.Utils.Api;
import com.example.badrjobs.Utils.SharedPrefManager;
import com.example.badrjobs.Utils.ToolbarClass;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ProfileEdit extends ToolbarClass implements View.OnClickListener {

    TextView textViewNickName,textViewFullName,textViewJob,textViewContracts,textViewBlockedUser,textViewDeleteAccount,
            textViewPasswordReset, textViewPhone,textViewDescription,txtLength;
    RelativeLayout profileImageLay;
    CircleImageView profileImage;
    CardView cardBio;
    ImageView imageViewHeader;
    RelativeLayout layHeader;
    ImageButton addHeaderImg;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile_edit);
//        init();
//        loadImages();
//    }
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_profile_edit, "");
        init();
        loadImages();
    }

    private void loadImages() {
        Bundle args = getIntent().getExtras();
        if (args!=null){
            String imgProfileUrl = args.getString("profile");
            String imgHeaderUrl = args.getString("header");
            String bio = args.getString("bio");

            try {
                if (!imgProfileUrl.isEmpty()&&!imgProfileUrl.equals("null")){
                    Glide.with(this).load(imgProfileUrl).into(profileImage);
                }else{
                    Glide.with(this).load(ContextCompat.getDrawable(this,R.drawable.ic_baseline_account_circle_24))
                            .into(profileImage);
                }
            } catch (Exception e) {

                e.printStackTrace();
            }

            try {
                if (!imgHeaderUrl.isEmpty()&&!imgHeaderUrl.equals("null")){
                    Glide.with(this).load(imgHeaderUrl).into(imageViewHeader);
                }else{
                    Glide.with(this).load(ContextCompat.getDrawable(this,R.drawable.shape_btn_nav_bg))
                            .into(imageViewHeader);
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
//            Glide .with(this).load(imgHeaderUrl).into(imageViewHeader);
//            Glide .with(this).load(imgProfileUrl).into(profileImage);
            textViewDescription = findViewById(R.id.description);
            if (!bio.isEmpty()&&!bio.equals("null")){
                textViewDescription.setText(bio);
                int length = 120 - bio.length();
                txtLength.setText(String.valueOf(length));
            }else{
                textViewDescription.setText("");
            }
        }
    }

    private void init() {

        txtLength = findViewById(R.id.txtLength);

        addHeaderImg = findViewById(R.id.addHeaderImg);
        addHeaderImg.setOnClickListener(this);
        layHeader = findViewById(R.id.headerImageLay);
        layHeader.setOnClickListener(this);
        imageViewHeader = findViewById(R.id.headerImage);
        imageViewHeader.setOnClickListener(this);
        cardBio = findViewById(R.id.bio);
        cardBio.setOnClickListener(this);
        profileImageLay = findViewById(R.id.profileImageLay);
        profileImageLay.setOnClickListener(this);
        profileImage = findViewById(R.id.img);
        progressLay = findViewById(R.id.progressLay);

        textViewPhone = findViewById(R.id.phone);
        textViewPhone.setOnClickListener(this);
        textViewPasswordReset = findViewById(R.id.passwordReset);
        textViewPasswordReset.setOnClickListener(this);
        textViewDeleteAccount = findViewById(R.id.deleteAccount);
        textViewDeleteAccount.setOnClickListener(this);
        textViewBlockedUser = findViewById(R.id.blockedUser);
        textViewBlockedUser.setOnClickListener(this);
        textViewContracts = findViewById(R.id.contracts);
        textViewContracts.setOnClickListener(this);
        textViewNickName = findViewById(R.id.nicename);
        textViewFullName = findViewById(R.id.fullName);
        textViewJob = findViewById(R.id.job);
        textViewNickName.setOnClickListener(this);
        textViewFullName.setOnClickListener(this);
        textViewJob.setOnClickListener(this);
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
        yes.setText(R.string.edit);
        no.setText(R.string.cancel);

        EditText editTextField = dialog.findViewById(R.id.field);
        TextView txtLength2 = dialog.findViewById(R.id.txtLength2);
        txtLength2.setVisibility(View.GONE);
        if (key.equals("bio")){

            txtLength2.setVisibility(View.VISIBLE);
            editTextField.addTextChangedListener(new TextWatcher() {
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
                    int length = s.length();

                    txtLength.setText(String.valueOf(120-length));
                    txtLength2.setText(String.valueOf(120-length));


                    //you can use runnable postDelayed like 500 ms to delay search text
                }
            });
        }


        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = editTextField.getText().toString().trim();
                if (!value.isEmpty()){
                    doEdition(key,value);
                    dialog.dismiss();
                }else {
                   editTextField.setError(getString(R.string.edition_value_not_found));
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

    LinearLayout progressLay;
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

        Api.RetrofitUpdateProfile service = retrofit.create(Api.RetrofitUpdateProfile.class);
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put(key,value);
        Call<String> call = service.putParam(hashMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("code");
                    switch (statusCode) {
                        case "200": {
                            if (key.equals("image")){
                                warningMsg(getString(R.string.add_image_done));
                            }
                            else if (key.equals("header_image")){
                                warningMsg(getString(R.string.cover_added));
                            }
                            else if (key.equals("bio")){
                                warningMsg(getString(R.string.edit_done)+value);
                                textViewDescription.setText(value);
                            }

                            else{
                                warningMsg(getString(R.string.edit_done)+"\n"+value);
                            }
//                            SharedPrefManager.getInstance(getContext()).storeAppToken("");
//                            startActivity(new Intent(getActivity(),Login.class));
//                            getActivity().finish();
                            break;
                        }
                        default: {
                            warningMsg("خطأ في التحويل الرجاء المحاولة مره اخرى");
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.nicename:{
//                dialogEdit("تعديل الايم الامستعار","","");
                break;
            }
            case R.id.fullName:{
                dialogEdit(getString(R.string.edt__name),"name");
                break;
            }
            case R.id.job:{
                dialogEdit(getString(R.string._job_edit),"job");
                break;
            }
            case R.id.bio:{
                dialogEdit(getString(R.string._bio_edit),"bio");
                break;
            }
            case R.id.contracts:{
               startActivity(new Intent(getApplicationContext(), PrevContracts.class));
                break;
            }
            case R.id.blockedUser:{
               startActivity(new Intent(getApplicationContext(),BlockedUsersActivity.class));
                break;
            }
            case R.id.deleteAccount:{
               startActivity(new Intent(getApplicationContext(),DeleteAccount.class));
                break;
            }
            case R.id.passwordReset:{
               startActivity(new Intent(getApplicationContext(),ResetPassword2.class));
                break;
            }
            case R.id.phone:{
               startActivity(new Intent(getApplicationContext(),ResetPhone.class));
                break;
            }
            case R.id.profileImageLay:{
                imageType = "profile";
                checkPermission();
                break;
            }
            case R.id.headerImageLay:{
                imageType = "header";
                checkPermission();
                break;
            }
            case R.id.headerImage:{
                imageType = "header";
                checkPermission();
                break;
            }

            case R.id.addHeaderImg:{
                imageType = "header";
                checkPermission();
                break;
            }
        }
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(ProfileEdit.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileEdit.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(ProfileEdit.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                pickImage();
            }
        } else {
            pickImage();
        }


    }

    public static final int PICK_IMAGE = 1;

    private void pickImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PICK_IMAGE);
    }

    String imageType="profile";
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                selectedImage = Bitmap.createScaledBitmap(selectedImage, 500, 500, false);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                if (imageType.equals("profile")){
                    profileImage.setImageBitmap(selectedImage);
                    doEdition("image",getStringFromImg(selectedImage));
                }else{
                    imageViewHeader.setImageBitmap(selectedImage);
                    doEdition("header_image",getStringFromImg(selectedImage));
                }





            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(ProfileEdit.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(ProfileEdit.this, "لم تقم باختيار صورة", Toast.LENGTH_LONG).show();
        }
    }

    private String getStringFromImg(Bitmap bitmap){
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteStream);
        byte[] byteArray = byteStream.toByteArray();
        String baseString = Base64.encodeToString(byteArray,Base64.DEFAULT);
        return baseString;
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