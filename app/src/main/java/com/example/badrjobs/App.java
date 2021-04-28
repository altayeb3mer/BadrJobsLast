package com.example.badrjobs;

import android.app.Application;
import android.content.res.Configuration;

import com.franmontiel.localechanger.LocaleChanger;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;



import static com.google.firebase.auth.EmailAuthProvider.*;

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
//        try {
//            ChatSDKFirebase.quickStart(this,"pre_1","altayeb3mer",true);//todo google map key
//        }catch (Exception e){
//            e.printStackTrace();
//        }

//        try {
//            ChatSDK.builder()
//                    .setGoogleMaps("Your Google Static Maps API key")
//                    .setPublicChatRoomLifetimeMinutes(TimeUnit.HOURS.toMinutes(24))
//                    .build()
//
//                    // Add the Firebase network adapter module
//                    .addModule(
//                            FirebaseModule.builder()
//                                    .setFirebaseRootPath("pre_1")
//                                    .setDevelopmentModeEnabled(true)
//                                    .build()
//                    )
//
//                    // Add the UI module
//                    .addModule(UIModule.builder()
//                            .setPublicRoomCreationEnabled(true)
//                            .setPublicRoomsEnabled(true)
//                            .build()
//                    )
//
//                    // Add modules to handle file uploads, push notifications
//                    .addModule(FirebaseUploadModule.shared())
//                    .addModule(FirebasePushModule.shared())
//
//                    // Enable Firebase UI with phone and email auth
//                    .addModule(FirebaseUIModule.builder()
//                            .setProviders(PROVIDER_ID, PhoneAuthProvider.PROVIDER_ID)
//                            .build()
//                    )
//
//                    // Activate
//                    .build()
//                    .activate(this);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        //language
        List<Locale> localeList = new ArrayList<>();
        localeList.add(new Locale("ar"));
        localeList.add(new Locale("en"));

        LocaleChanger.initialize(getApplicationContext(),localeList);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleChanger.onConfigurationChanged();
    }
}
