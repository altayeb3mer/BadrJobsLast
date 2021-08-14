package bader.cutShort.badrjobs.Activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import bader.cutShort.badrjobs.Adapter.AdapterHousing;
import bader.cutShort.badrjobs.Model.ModelHousing;
import com.example.badrjobs.R;
import bader.cutShort.badrjobs.Utils.Api;
import bader.cutShort.badrjobs.Utils.SharedPrefManager;
import bader.cutShort.badrjobs.Utils.ToolbarClass;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AddJob extends ToolbarClass {

    public static final int PICK_IMAGE = 1;
    CountryCodePicker ccp;
    Spinner spinnerSalary, spinnerBill;
    RadioGroup radioGroupType, radioGroupApplication, radioGroupReligion,
            radioGroupSex, radioGroupContactType;
    AppCompatCheckBox checkBoxTermAndPolicy;
    boolean termAndPolicy = false;
    Bitmap bitmap1, bitmap2, bitmap3;
    RelativeLayout imgBtn1, imgBtn2, imgBtn3;
    ImageView imageViewBtn1, imageViewBtn2, imageViewBtn3;
    CircleImageView circleImageView1, circleImageView2, circleImageView3;
    final Calendar myCalendar = Calendar.getInstance();
    //value
    String categoryId = "", countryId = "";
    //image
    String image1 = "", image2 = "", image3 = "";

    //edt
    EditText edtOfficeName, edtAddress, edtOwnerName, edtBirthDay, edtJob, edtExperience,
            edtSalary, edtBill, edtRegion, edtDescription, edtPhone;
    String officeName = "", address = "", ownerName = "", birthDay = "", job = "", experience = "",
            salary = "", bill = "", religion = "", description = "", phone = "",
            applicationType = "NEW", ownerType = "", sex = "", region = "";

    AppCompatButton button;
    LinearLayout layOffice;
    ImageView imageViewFlag;
    TextView textViewDept, textViewSubDept, textViewBioCount;
    ConstraintLayout layPhone;
    String countryImage = "", deptName = "", supDeptName = "";
    int salaryType = 0;
    int imagePicker = 1;
    LinearLayout progressLay;
    //housing
    CardView housingCard;
    RecyclerView recyclerViewHousing;
    AdapterHousing adapterHousing;
    CheckBox checkboxHousing;
    boolean housing = false;
    GridLayoutManager gridLayoutManager;
    private boolean additionPhone = false;
    //language controller
    private LocaleChangerAppCompatDelegate localeChangerAppCompatDelegate;


    DatePickerDialog.OnDateSetListener date;

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

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_add_job, "");
        getBundles();
        init();
        initSpinnerSalary();
        initSpinnerBill();
        initRadios();
        setAplicationType("OFFICE");
    }

    private void getBundles() {
        Bundle args = getIntent().getExtras();
        if (args != null) {
            String subCategory = args.getString("supDeptId");
            if (!subCategory.isEmpty()) {
                categoryId = subCategory;
            } else {
                categoryId = args.getString("deptId");
            }

            countryId = args.getString("countryId");

            countryImage = args.getString("countryImage");
            deptName = args.getString("deptName");
            supDeptName = args.getString("supDeptName");


        }
    }
    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edtBirthDay.setText(sdf.format(myCalendar.getTime()));
    }
    private void initRadios() {
        checkBoxTermAndPolicy = findViewById(R.id.checkbox);
        checkBoxTermAndPolicy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    termAndPolicy = true;
                } else {
                    termAndPolicy = false;
                }
            }
        });
        edtOfficeName = findViewById(R.id.edtOfficeName);
        edtAddress = findViewById(R.id.edtAddress);
        edtOwnerName = findViewById(R.id.edtOwnerName);

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabel();
            }

        };
        edtBirthDay = findViewById(R.id.edtBirthDate);
        edtBirthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new DatePickerDialog(AddJob.this, date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                new DatePickerDialog(AddJob.this, date, 1990, 1,
                        1).show();
            }
        });

        edtJob = findViewById(R.id.edtJob);
        edtExperience = findViewById(R.id.edtExperience);
        edtSalary = findViewById(R.id.edtSalary);
        edtBill = findViewById(R.id.edtBill);
        edtRegion = findViewById(R.id.edtRegion);
        edtDescription = findViewById(R.id.edtDescription);

        textViewBioCount = findViewById(R.id.bioCount);

        edtDescription.addTextChangedListener(new TextWatcher() {
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

                textViewBioCount.setText(String.valueOf(120-length));


                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });

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
                switch (checkedId) {
                    case R.id.radioBtnType1: {
                        applicationType = "NEW";
                        break;
                    }
                    case R.id.radioBtnType2: {
                        applicationType = "NO_NEW";
                        break;
                    }
                }
            }
        });
        radioGroupApplication.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.radioBtnOwnerType1: {
                        ownerType = "PERSONAL";
                        break;
                    }
                    case R.id.radioBtnOwnerType2: {
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
                switch (checkedId) {
                    case R.id.radioBtnReligion1: {
                        religion = "MUSLIM";
                        break;
                    }
                    case R.id.radioBtnReligion2: {
                        religion = "NON_MUSLIM";
                        break;
                    }
                }
            }
        });
        radioGroupSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.radioBtnSex1: {
                        sex = "MALE";
                        break;
                    }
                    case R.id.radioBtnSex2: {
                        sex = "FEMALE";
                        break;
                    }
                }
            }
        });
        radioGroupContactType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.radioBtnContactPhone1: {
                        phone = "";
                        additionPhone = false;
                        layPhone.setVisibility(View.GONE);
                        break;
                    }
                    case R.id.radioBtnContactPhone2: {
                        additionPhone = true;
                        layPhone.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }
        });

    }

    private void setAplicationType(String oType) {
        switch (oType) {
            case "PERSONAL": {
                layOffice.setVisibility(View.GONE);
                radioGroupApplication.check(R.id.radioBtnOwnerType1);
                break;
            }
            case "OFFICE": {
                layOffice.setVisibility(View.VISIBLE);
                radioGroupApplication.check(R.id.radioBtnOwnerType2);
                break;
            }
        }
    }

    private void preAddJob() {
        if (officeName.isEmpty() && ownerType.equals("OFFICE")) {
            edtOfficeName.setError("الرجاء كتابة اسم صحيح");
            edtOfficeName.requestFocus();
            return;
        }
        if (address.isEmpty() && ownerType.equals("OFFICE")) {
            edtAddress.setError("الرجاء كتابة اسم صحيح");
            edtAddress.requestFocus();
            return;
        }
        if (ownerName.isEmpty()) {
            edtOwnerName.setError("الرجاء كتابة اسم صحيح");
            edtOwnerName.requestFocus();
            return;
        }
        if (birthDay.isEmpty()) {
            edtBirthDay.setError("الرجاء كتابة اسم صحيح");
            edtBirthDay.requestFocus();
            return;
        }
        if (job.isEmpty()) {
            edtJob.setError("الرجاء كتابة اسم صحيح");
            edtJob.requestFocus();
            return;
        }
        if (experience.isEmpty()) {
            edtExperience.setError("الرجاء كتابة اسم صحيح");
            edtExperience.requestFocus();
            return;
        }
        if (salary.isEmpty()) {
            edtSalary.setError("الرجاء اختيار الراتب");
            edtSalary.requestFocus();
            return;
        }
        if (bill.isEmpty()) {
            edtBill.setError("الرجاء اختيار مبلغ توفير المهنة");
            edtBill.requestFocus();
            return;
        }
        if (region.isEmpty()) {
            edtRegion.setError("الرجاء كتابة اسم صحيح");
            edtRegion.requestFocus();
            return;
        }
        if (description.isEmpty()) {
            edtDescription.setError("الرجاء كتابة اسم صحيح");
            edtDescription.requestFocus();
            return;
        }
        if (additionPhone && phone.isEmpty()) {
            edtPhone.setError("الرجاء كتابة اسم صحيح");
            edtPhone.requestFocus();
            return;
        }
        if (!isValidDate(birthDay)) {
            edtBirthDay.setError("صيغة تاريخ الميلاد غير صحيحة");
            edtBirthDay.requestFocus();
            return;
        }
        if (!termAndPolicy) {
            Toast.makeText(this, "لم توافق على صحة بياناتك!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (image1.isEmpty()) {
            warningMsg("الصورة الاولى مطلوبة", false);
            return;
        }
        if (housing&&adapterHousing.getModelHousingRequest().size()<0){
            warningMsg("الرجاء تحديد العمالة المنزلية",false);
            return;
        }

        addJob();

    }

    private void init() {
        checkboxHousing = findViewById(R.id.checkboxHousing);
        checkboxHousing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    housing = true;
                    housingCard.setVisibility(View.VISIBLE);
                } else {
                    housing = false;
                    housingCard.setVisibility(View.GONE);
                }
            }
        });
        housingCard = findViewById(R.id.housingCard);
        recyclerViewHousing = findViewById(R.id.housingRec);

        ccp = findViewById(R.id.ccp);
        layPhone = findViewById(R.id.layPhone);
        imageViewFlag = findViewById(R.id.imgFlag);
        textViewDept = findViewById(R.id.txtDept);
        textViewSubDept = findViewById(R.id.txtSupDept);
        textViewDept.setText(deptName);
        textViewSubDept.setText(supDeptName);
        Glide.with(this).load(countryImage).into(imageViewFlag);


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

//                if (billType>1){
//                    switch (billType){
//                        case 2:{
//                            bill = "تكلفة اجراءات السفر";
//                            break;
//                        }
//                        case 3:{
//                            bill = "حسب الاتفاق";
//                            break;
//                        }
//                        case 4:{
//                            bill = "لاشئ";
//                            break;
//                        }
//                    }
//                }
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


        getHousingList();

    }

    private void initSpinnerSalary() {
        ArrayList<String> array = new ArrayList<>();
        array.add(getString(R.string.choose_s_));
        array.add(getString(R.string.type_no));
        array.add(getString(R.string.in_deel));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, array) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = null;
                v = super.getDropDownView(position, null, parent);
//                v.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                // If this is the selected item position
//                if (position == 0) {
//                    v.setVisibility(View.GONE);
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

            @Override
            public int getCount() {
                if (SharedPrefManager.getInstance(AddJob.this).GetAppLanguage().equals("ar"))
                array.remove("اختر");
                else {
                    array.remove("Choose");
                }
                return array.size();
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerSalary.setAdapter(adapter);
        spinnerSalary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    edtSalary.setText("");
                    edtSalary.setHint(array.get(0));
                } else {
                    edtSalary.setText(array.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    //    int billType=0;
    private void initSpinnerBill() {
        ArrayList<String> array = new ArrayList<>();
        array.add(getString(R.string.choose_s_));
        array.add(getString(R.string.type_no));
        array.add(getString(R.string.travel_fees));
        array.add(getString(R.string.in_deel));
        array.add(getString(R.string.no_thing));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, array) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = null;
                v = super.getDropDownView(position, null, parent);
//                v.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                // If this is the selected item position
//                if (position == 0) {
//                    v.setVisibility(View.GONE);
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

            @Override
            public int getCount() {
                if (SharedPrefManager.getInstance(AddJob.this).GetAppLanguage().equals("ar"))
                    array.remove("اختر");
                else {
                    array.remove("Choose");
                }
                return array.size();
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerBill.setAdapter(adapter);
        spinnerBill.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                billType = position;
                if (position == 0) {
                    edtBill.setText("");
                    edtBill.setHint(array.get(0));
                } else {
                    edtBill.setText(array.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

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

//                selectedImage = Bitmap.createScaledBitmap(selectedImage, 500, 500, false);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.JPEG, 70, bytes);


                switch (imagePicker) {
                    case 1: {
                        bitmap1 = selectedImage;
                        imageViewBtn1.setVisibility(View.GONE);
                        circleImageView1.setImageBitmap(selectedImage);
                        circleImageView1.setVisibility(View.VISIBLE);
                        image1 = getStringFromImg(selectedImage);
                        break;
                    }
                    case 2: {
                        bitmap2 = selectedImage;
                        imageViewBtn2.setVisibility(View.GONE);
                        circleImageView2.setImageBitmap(selectedImage);
                        circleImageView2.setVisibility(View.VISIBLE);
                        image2 = getStringFromImg(selectedImage);
                        break;
                    }
                    case 3: {
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

    private String getStringFromImg(Bitmap bitmap) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteStream);
        byte[] byteArray = byteStream.toByteArray();
        String baseString = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return baseString;
    }

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
                .readTimeout(60 * 5, TimeUnit.SECONDS)
                .connectTimeout(60 * 5, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.ROOT_URL)
                .client(httpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api.RetrofitAddJob service = retrofit.create(Api.RetrofitAddJob.class);

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("job_category", categoryId);
        hashMap.put("country", countryId);
        hashMap.put("job_title", job);

        if (!image1.isEmpty())
            hashMap.put("image1", image1);
        if (!image2.isEmpty())
            hashMap.put("image2", image2);
        if (!image3.isEmpty())
            hashMap.put("image3", image3);


        hashMap.put("applaction_type", applicationType);
        hashMap.put("owner_type", ownerType);
        hashMap.put("organization_name", officeName);
        hashMap.put("owner_name", ownerName);
        hashMap.put("experience", experience);
        hashMap.put("salary", salary);
        hashMap.put("bailing_money", bill);
        hashMap.put("country_of_residencey", region);
        hashMap.put("religion", religion);
        hashMap.put("sex", sex);
        hashMap.put("description", description);
        hashMap.put("custom_phone", "00" + ccp.getFullNumber() +  phone);
        hashMap.put("birthday", birthDay);
        if (housing){
            hashMap.put("housing_types", adapterHousing.getModelHousingRequest().toString());
            hashMap.put("housing", "YES");
        }else {
            hashMap.put("housing", "NO");
        }
        Call<String> call = service.putParam(hashMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("code");
                    switch (statusCode) {
                        case "200": {
                            warningMsg(getString(R.string.add_done), true);
                            break;
                        }

                        default: {
//                            Toast.makeText(getApplicationContext(), "حدث خطأ حاول مجددا", Toast.LENGTH_SHORT).show();
                            warningMsg(getString(R.string.error_try_again), false);
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

    private void getHousingList() {
        progressLay.setVisibility(View.VISIBLE);
        ArrayList<ModelHousing> housingArrayList = new ArrayList<>();
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Content-Type", "application/json;");
                        ongoing.addHeader("Accept", "application/json");
                        ongoing.addHeader("lang", SharedPrefManager.getInstance(getApplicationContext()).GetAppLanguage());
//                        String token = SharedPrefManager.getInstance(getApplicationContext()).getAppToken();
//                        ongoing.addHeader("Authorization", token);
                        return chain.proceed(ongoing.build());
                    }
                })
                .readTimeout(60 * 5, TimeUnit.SECONDS)
                .connectTimeout(60 * 5, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.ROOT_URL)
                .client(httpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api.RetrofitHousingList service = retrofit.create(Api.RetrofitHousingList.class);

        Call<String> call = service.putParam();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("code");
                    switch (statusCode) {
                        case "200": {
                            JSONArray dataHousing = object.getJSONArray("response");
                            for (int i = 0; i < dataHousing.length(); i++) {
                                JSONObject itemData = dataHousing.getJSONObject(i);
                                ModelHousing modelHousing = new ModelHousing();

                                modelHousing.setId(itemData.getString("id"));
                                modelHousing.setTransName(itemData.getString("trans_name"));

                                housingArrayList.add(modelHousing);
                            }
                            if (housingArrayList.size() > 0) {
                                initHousingAdapter(housingArrayList);

                            } else {
                                Toast.makeText(AddJob.this, "No housing data", Toast.LENGTH_SHORT).show();
                            }


                            break;
                        }

                        default: {
//                            Toast.makeText(getApplicationContext(), "حدث خطأ حاول مجددا", Toast.LENGTH_SHORT).show();
                            warningMsg(getString(R.string.error_try_again), false);
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

    private void initHousingAdapter(ArrayList<ModelHousing> list) {
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
        recyclerViewHousing.setLayoutManager(gridLayoutManager);
        adapterHousing = new AdapterHousing(this, list);
        recyclerViewHousing.setAdapter(adapterHousing);
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
        yes.setText(getString(R.string.ok));

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