package com.example.badrjobs.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.badrjobs.R;
import com.example.badrjobs.Utils.Api;
import com.example.badrjobs.Utils.SharedPrefManager;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

public class AddJob extends AppCompatActivity {

    Spinner spinnerSalary,spinnerBill;
    RadioGroup radioGroupType,radioGroupApplication,radioGroupReligion,
            radioGroupSex,radioGroupContactType;

    AppCompatCheckBox checkBoxTermAndPolicy;
    boolean termAndPolicy = false;

    public static final int PICK_IMAGE = 1;
    Bitmap bitmap1,bitmap2,bitmap3;
    RelativeLayout imgBtn1,imgBtn2,imgBtn3;
    ImageView imageViewBtn1,imageViewBtn2,imageViewBtn3;
    CircleImageView circleImageView1,circleImageView2,circleImageView3;

    //value
    String categoryId="",countryId="";
    //image
    String image1="",image2="",image3="";

    //edt
    EditText edtOfficeName,edtAddress,edtOwnerName, edtBirthDay,edtJob,edtExperience,
            edtSalary,edtBill,edtRegion,edtDescription,edtPhone;
    String officeName="",address="",ownerName="",birthDay="",job="",experience="",
    salary="",bill="", religion ="",description="",phone="",
    applicationType="NEW",ownerType="",sex="",region="";

    AppCompatButton button;
    private boolean additionPhone=false;
    LinearLayout layOffice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);
        getBundles();
        init();
        initSpinnerSalary();
        initSpinnerBill();
        initRadios();
        setAplicationType("OFFICE");
    }

    private void getBundles() {
        Bundle args = getIntent().getExtras();
        if (args!=null){
            String subCategory = args.getString("supDeptId");
            if (!subCategory.isEmpty()){
                categoryId = subCategory;
            }else{
                categoryId = args.getString("deptId");
            }

            countryId = args.getString("countryId");

        }
    }

    private void initRadios() {
        checkBoxTermAndPolicy = findViewById(R.id.checkbox);
        checkBoxTermAndPolicy .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    termAndPolicy = true;
                }else{
                    termAndPolicy = false;
                }
            }
        });
        edtOfficeName = findViewById(R.id.edtOfficeName);
        edtAddress = findViewById(R.id.edtAddress);
        edtOwnerName = findViewById(R.id.edtOwnerName);
        edtBirthDay = findViewById(R.id.edtBirthDate);
        edtJob = findViewById(R.id.edtJob);
        edtExperience = findViewById(R.id.edtExperience);
        edtSalary = findViewById(R.id.edtSalary);
        edtBill = findViewById(R.id.edtBill);
        edtRegion = findViewById(R.id.edtRegion);
        edtDescription = findViewById(R.id.edtDescription);


        edtPhone = findViewById(R.id.edtPhone);

        radioGroupType = findViewById(R.id.radioGroupType);
        radioGroupApplication = findViewById(R.id.radioGroupApplication);
        radioGroupReligion = findViewById(R.id.radioGroupReligion);
        radioGroupSex = findViewById(R.id.radioGroupSex);
        radioGroupContactType = findViewById(R.id.radioGroupContactPhone);
        radioGroupType.check(R.id.radioBtnType1);
        radioGroupType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId){
                    case R.id.radioBtnType1:{
                        applicationType = "NEW";
                        break;
                    }
                    case R.id.radioBtnType2:{
                        applicationType = "NO_NEW";
                        break;
                    }
                }
            }
        });
        radioGroupApplication.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId){
                    case R.id.radioBtnOwnerType1:{
                        ownerType = "PERSONAL";
                        break;
                    }
                    case R.id.radioBtnOwnerType2:{
                        ownerType = "OFFICE";
                        break;
                    }
                }
                setAplicationType(ownerType);
            }
        });
        radioGroupReligion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId){
                    case R.id.radioBtnReligion1:{
                        religion = "MUSLIM";
                        break;
                    }
                    case R.id.radioBtnReligion2:{
                        religion = "NON_MUSLIM";
                        break;
                    }
                }
            }
        });
        radioGroupSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId){
                    case R.id.radioBtnSex1:{
                        sex = "MALE";
                        break;
                    }
                    case R.id.radioBtnSex2:{
                        sex = "FEMALE";
                        break;
                    }
                }
            }
        });
        radioGroupContactType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId){
                    case R.id.radioBtnContactPhone1:{
                        phone = "";
                        additionPhone = false;
                        edtPhone.setVisibility(View.GONE);
                        break;
                    }
                    case R.id.radioBtnContactPhone2:{
                        additionPhone = true;
                        edtPhone.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }
        });

    }

    private void setAplicationType(String oType) {
        switch (oType){
            case "PERSONAL":{
                layOffice.setVisibility(View.GONE);
                radioGroupApplication.check(R.id.radioBtnOwnerType1);
                break;
            }
            case "OFFICE":{
                layOffice.setVisibility(View.VISIBLE);
                radioGroupApplication.check(R.id.radioBtnOwnerType2);
                break;
            }
        }
    }

    private void preAddJob() {
        if (officeName.isEmpty()&&ownerType.equals("OFFICE")){
            edtOfficeName.setError("الرجاء كتابة اسم صحيح");
            edtOfficeName.requestFocus();
            return;
        }
        if (address.isEmpty()&&ownerType.equals("OFFICE")){
            edtAddress.setError("الرجاء كتابة اسم صحيح");
            edtAddress.requestFocus();
            return;
        }
        if (ownerName.isEmpty()){
            edtOwnerName.setError("الرجاء كتابة اسم صحيح");
            edtOwnerName.requestFocus();
            return;
        }
        if (birthDay.isEmpty()){
            edtBirthDay.setError("الرجاء كتابة اسم صحيح");
            edtBirthDay.requestFocus();
            return;
        }
        if (job.isEmpty()){
            edtJob.setError("الرجاء كتابة اسم صحيح");
            edtJob.requestFocus();
            return;
        }
        if (experience.isEmpty()){
            edtExperience.setError("الرجاء كتابة اسم صحيح");
            edtExperience.requestFocus();
            return;
        }
        if (salary.isEmpty()&&(salaryType==0||salaryType==1)){
            edtSalary.setError("الرجاء كتابة رقم صحيح");
            edtSalary.requestFocus();
            return;
        }
        if (bill.isEmpty()&&(billType==0||billType==1)){
            edtBill.setError("الرجاء كتابة رقم صحيح");
            edtBill.requestFocus();
            return;
        }
        if (region.isEmpty()){
            edtRegion.setError("الرجاء كتابة اسم صحيح");
            edtRegion.requestFocus();
            return;
        }
        if (description.isEmpty()){
            edtDescription.setError("الرجاء كتابة اسم صحيح");
            edtDescription.requestFocus();
            return;
        }
        if (additionPhone&&phone.isEmpty()){
            edtPhone.setError("الرجاء كتابة اسم صحيح");
            edtPhone.requestFocus();
            return;
        }
        if (!isValidDate(birthDay)){
            edtBirthDay.setError("صيغة تاريخ الميلاد غير صحيحة");
            edtBirthDay.requestFocus();
            return;
        }
        if (!termAndPolicy){
            Toast.makeText(this, "لم توافق على صحة بياناتك!", Toast.LENGTH_SHORT).show();
            return;
        }

        addJob();

    }

    private void init() {
        layOffice = findViewById(R.id.layOffice);
        button = findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                officeName = edtOfficeName.getText().toString().trim();
                address = edtAddress.getText().toString().trim();
                ownerName = edtOwnerName.getText().toString().trim();
                birthDay = edtBirthDay.getText().toString().trim();
                job = edtJob.getText().toString().trim();
                experience = edtExperience.getText().toString().trim();
                salary = edtSalary.getText().toString().trim();
                bill = edtBill.getText().toString().trim();
                region = edtRegion.getText().toString().trim();
                description = edtDescription.getText().toString().trim();
                phone = edtPhone.getText().toString().trim();
                if (salaryType>1){
                    salary = "حسب الاتفاق";
                }
                if (billType>1){
                    switch (billType){
                        case 2:{
                            bill = "تكلفة اجراءات السفر";
                            break;
                        }
                        case 3:{
                            bill = "حسب الاتفاق";
                            break;
                        }
                        case 4:{
                            bill = "لاشئ";
                            break;
                        }
                    }
                }
                preAddJob();
            }
        });
        progressLay = findViewById(R.id.progressLay);
        spinnerSalary = findViewById(R.id.spinnerSalary);
        spinnerBill = findViewById(R.id.spinnerBill);

        imgBtn1 = findViewById(R.id.imgBtn1);
        imgBtn2 = findViewById(R.id.imgBtn2);
        imgBtn3 = findViewById(R.id.imgBtn3);

        circleImageView1 = findViewById(R.id.img1);
        circleImageView2 = findViewById(R.id.img2);
        circleImageView3 = findViewById(R.id.img3);

        imageViewBtn1 = findViewById(R.id.imageViewBtn1);
        imageViewBtn2 = findViewById(R.id.imageViewBtn2);
        imageViewBtn3 = findViewById(R.id.imageViewBtn3);

        imgBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePicker = 1;
                checkPermission();
            }
        });
        imgBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePicker = 2;
                checkPermission();
            }
        });
        imgBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePicker = 3;
                checkPermission();
            }
        });




    }

    int salaryType=0;
    private void initSpinnerSalary() {
        String[] array = {"اختر","اكتب الرقم","حسب الاتفاق"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, array) {
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
        spinnerSalary.setAdapter(adapter);
        spinnerSalary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                salaryType = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    int billType=0;
    private void initSpinnerBill() {
        String[] array = {"اختر","اكتب الرقم","تكلفة اجراءات السفر","حسب الاتفاق","لاشئ"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, array) {
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
        spinnerBill.setAdapter(adapter);
        spinnerBill.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                billType = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    int imagePicker = 1;

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(AddJob.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(AddJob.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(AddJob.this,
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

    private void pickImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PICK_IMAGE);
    }

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


                switch (imagePicker){
                    case 1:{
                        bitmap1 = selectedImage;
                        imageViewBtn1.setVisibility(View.GONE);
                        circleImageView1.setImageBitmap(selectedImage);
                        circleImageView1.setVisibility(View.VISIBLE);
                        image1 = getStringFromImg(selectedImage);
                        break;
                    }
                    case 2:{
                        bitmap2 = selectedImage;
                        imageViewBtn2.setVisibility(View.GONE);
                        circleImageView2.setImageBitmap(selectedImage);
                        circleImageView2.setVisibility(View.VISIBLE);
                        image2 = getStringFromImg(selectedImage);
                        break;
                    }
                    case 3:{
                        bitmap3 = selectedImage;
                        imageViewBtn3.setVisibility(View.GONE);
                        circleImageView3.setImageBitmap(selectedImage);
                        circleImageView3.setVisibility(View.VISIBLE);
                        image3 = getStringFromImg(selectedImage);
                        break;
                    }
                }




            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(AddJob.this, e.getMessage(), Toast.LENGTH_LONG).show();

            }

        } else {
            Toast.makeText(AddJob.this, "لم تقم باختيار صورة", Toast.LENGTH_LONG).show();
        }
    }

    private String getStringFromImg(Bitmap bitmap){
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteStream);
        byte[] byteArray = byteStream.toByteArray();
        String baseString = Base64.encodeToString(byteArray,Base64.DEFAULT);
        return baseString;
    }

    LinearLayout progressLay;
    private void addJob() {
        progressLay.setVisibility(View.VISIBLE);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Content-Type", "application/json;");
                        ongoing.addHeader("Accept", "application/json");
//                        ongoing.addHeader("lang", SharedPrefManager.getInstance(getApplicationContext()).GetAppLanguage());
                        String token = SharedPrefManager.getInstance(getApplicationContext()).getAppToken();
                        ongoing.addHeader("Authorization", token);
                        return chain.proceed(ongoing.build());
                    }
                })
                .readTimeout(60*5, TimeUnit.SECONDS)
                .connectTimeout(60*5, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.ROOT_URL)
                .client(httpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api.RetrofitAddJob service = retrofit.create(Api.RetrofitAddJob.class);

        HashMap<String,String> hashMap = new HashMap();
        hashMap.put("job_category",categoryId);
        hashMap.put("country",countryId);
        hashMap.put("job_title",job);
        hashMap.put("image1","image1");
        hashMap.put("image2","image2");
        hashMap.put("image3","image3");
        hashMap.put("applaction_type",applicationType);
        hashMap.put("owner_type",ownerType);
        hashMap.put("organization_name",officeName);
        hashMap.put("owner_name",ownerName);
        hashMap.put("experience",experience);
        hashMap.put("salary",salary);
        hashMap.put("bailing_money",bill);
        hashMap.put("country_of_residencey",region);
        hashMap.put("religion",religion);
        hashMap.put("sex",sex);
        hashMap.put("description",description);
        hashMap.put("custom_phone",phone);
        hashMap.put("housing","YES");
        hashMap.put("birthday",birthDay);
        Call<String> call = service.putParam(hashMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("code");
                    switch (statusCode) {
                        case "200": {
                            Toast.makeText(AddJob.this, "تم اضافة الاعلان", Toast.LENGTH_SHORT).show();
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

    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
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