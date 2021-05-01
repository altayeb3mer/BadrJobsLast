package com.example.badrjobs.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.badrjobs.R;
import com.example.badrjobs.Utils.CaptureSignatureView;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;

import java.io.ByteArrayOutputStream;

public class SignatureDraw extends AppCompatActivity implements View.OnClickListener {

    LinearLayout signatureLay;
    AppCompatButton buttonSave,buttonDel;
    ImageView imageView,imageViewClose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_draw);
        init();
    }
    CaptureSignatureView mSig;
    Bitmap signatureBitmap;
    private void init() {
        imageViewClose = findViewById(R.id.close);
        imageViewClose.setOnClickListener(this);
        buttonDel = findViewById(R.id.btnDel);
        buttonDel.setOnClickListener(this);
        buttonSave = findViewById(R.id.btn);
        signatureLay = findViewById(R.id.signatureLay);
        mSig = new CaptureSignatureView(this, null);
        signatureLay.addView(mSig, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);


        buttonSave.setOnClickListener(this);


    }

    byte[] byteArray;
    private void initScreenOfSignature() {
       byteArray = mSig.getBytes();
//        signatureBitmap = mSig.getBitmap();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn:{
                initScreenOfSignature();
                if (mSig!=null){
                    //Convert to byte array
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                    byte[] byteArray = stream.toByteArray();

                    Intent intent = new Intent();
                    intent.putExtra("bitmap",byteArray);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }else{
                    Toast.makeText(this, "الرجاء كتابة التوقيع", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.btnDel:{
                mSig.ClearCanvas();
                break;
            }
            case R.id.close:{
                onBackPressed();
                break;
            }
        }
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