package com.example.badrjobs;

import android.app.Application;
import android.content.res.Configuration;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;

import com.example.badrjobs.Activity.ChattingActivity;
import com.example.badrjobs.ChatClasses.MainCustomChatList;
import com.example.badrjobs.Fragment.FragmentChat;
import com.example.badrjobs.Utils.SharedPrefManager;
import com.franmontiel.localechanger.LocaleChanger;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import sdk.chat.app.firebase.ChatSDKFirebase;
import sdk.chat.core.session.ChatSDK;
import sdk.chat.core.types.AccountDetails;
import sdk.chat.firebase.adapter.module.FirebaseModule;
import sdk.chat.firebase.push.FirebasePushModule;
import sdk.chat.firebase.ui.FirebaseUIModule;
import sdk.chat.firebase.upload.FirebaseUploadModule;
import sdk.chat.ui.ChatSDKUI;
import sdk.chat.ui.icons.Icons;
import sdk.chat.ui.module.UIModule;

import static com.google.firebase.auth.EmailAuthProvider.*;

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        try {
            ChatSDKFirebase.quickStart(this,"bre1","bbbb",true);
            //configure
            ChatSDK.ui().setChatActivity(ChattingActivity.class);
            ChatSDK.ui().setPrivateThreadsFragment(new FragmentChat());
            UIModule.config().overrideTheme();
            UIModule.config().setMessageForwardingEnabled(false);






//            ChatSDK.ui().setMainActivity(MainCustomChatList.class);
//            ChatSDKUI.setPrivateThreadsFragment(new);
//            ChatSDKUI.setPrivateThreadsFragment(new FragmentChat());
//            try {
//                ChatSDK.builder()
//                        .setGoogleMaps("Your Google Static Maps API key")
//                        .setPublicChatRoomLifetimeMinutes(TimeUnit.HOURS.toMinutes(24))
//                        .build()
//
//
//
//                        // Add the Firebase network adapter module
//                        .addModule(
//                                FirebaseModule.builder()
//                                        .setFirebaseRootPath("pre_1")
//                                        .setDevelopmentModeEnabled(true)
//                                        .build()
//                        )
//
//                        // Add the UI module
//                        .addModule(UIModule.builder()
//                                .setPublicRoomCreationEnabled(true)
//                                .setPublicRoomsEnabled(true)
//                                .build()
//                        )
//
//                        // Add modules to handle file uploads, push notifications
//                        .addModule(FirebaseUploadModule.shared())
//                        .addModule(FirebasePushModule.shared())
//
//                        // Enable Firebase UI with phone and email auth
//                        .addModule(FirebaseUIModule.builder()
//                                .setProviders(EmailAuthProvider.PROVIDER_ID, PhoneAuthProvider.PROVIDER_ID)
//                                .build()
//                        )
//
//                        // Activate
//                        .build()
//                        .activate(this);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        String fcmToken = SharedPrefManager.getInstance(this).getFireBaseToken();
//        if (!fcmToken.isEmpty()){
//            AccountDetails details = AccountDetails.token(fcmToken);
//            ChatSDK.auth().authenticate(details).subscribe(new Action() {
//                @Override
//                public void run() throws Exception {
//                    Toast.makeText(App.this, "تم تفعيل الدردشة", Toast.LENGTH_SHORT).show();
//                }
//            }, new Consumer<Throwable>() {
//                @Override
//                public void accept(Throwable throwable) throws Exception {
//                    Toast.makeText(App.this, "خطأ في تفعيل الدردشة", Toast.LENGTH_SHORT).show();
//
//                }
//            });
//        }


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
