package com.example.badrjobs;

import android.app.Application;
import android.content.res.Configuration;
import android.graphics.drawable.Icon;
import android.widget.Toast;

import com.example.badrjobs.Activity.ChattingActivity;
import com.example.badrjobs.Activity.CustomProfileActivity;
import com.example.badrjobs.Fragment.FragmentChat;
import com.franmontiel.localechanger.LocaleChanger;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome;

import org.pmw.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import sdk.chat.core.events.EventType;
import sdk.chat.core.events.NetworkEvent;
import sdk.chat.core.module.Module;
import sdk.chat.core.session.ChatSDK;
import sdk.chat.core.utils.ProfileOption;
import sdk.chat.firebase.adapter.module.FirebaseModule;
import sdk.chat.firebase.push.FirebasePushModule;
import sdk.chat.firebase.ui.FirebaseUIModule;
import sdk.chat.firebase.upload.FirebaseUploadModule;
import sdk.chat.message.audio.AudioMessageModule;
import sdk.chat.message.audio.BaseAudioMessageHandler;
import sdk.chat.ui.ChatSDKUI;
import sdk.chat.ui.activities.ProfileActivity;
import sdk.chat.ui.icons.Icons;
import sdk.chat.ui.module.UIModule;

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();



        try {
//            ChatSDKFirebase.quickStart(this, "bre1", "bbbb", true);

            try {
                ChatSDK.builder()
                        .setGoogleMaps("bbb")
                        .setPublicChatRoomLifetimeMinutes(TimeUnit.HOURS.toMinutes(24))
                        .build()


                        // Add the Firebase network adapter module
                        .addModule(
                                FirebaseModule.builder()
                                        .setFirebaseRootPath("bre1")
                                        .setDevelopmentModeEnabled(true)
                                        .build()
                        )

                        // Add the UI module
                        .addModule(UIModule.builder()
                                .setPublicRoomCreationEnabled(true)
                                .setPublicRoomsEnabled(true)
                                .build()
                        )
                        .addModule(UIModule.builder().setLocationMessagesEnabled(false).build())
//                        .addModule(AudioMessageModule.shared())


                        // Add modules to handle file uploads, push notifications
                        .addModule(FirebaseUploadModule.shared())
                        .addModule(FirebasePushModule.shared())
//                        .addModule(AudioMessageModule.shared())

                        // Enable Firebase UI with phone and email auth
                        .addModule(FirebaseUIModule.builder()
                                .setProviders(EmailAuthProvider.PROVIDER_ID, PhoneAuthProvider.PROVIDER_ID)
                                .build()
                        )

                        // Activate
                        .build()
                        .activate(this);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //config
            ChatSDK.ui().setProfileActivity(CustomProfileActivity.class);
            ChatSDK.ui().setChatActivity(ChattingActivity.class);
//            ChatSDK.ui().setProfileFragmentProvider();
            ChatSDK.ui().setPrivateThreadsFragment(new FragmentChat());
            UIModule.config().overrideTheme();
            UIModule.config().setMessageForwardingEnabled(false);
            AudioMessageModule.shared().activate(getApplicationContext());
            ChatSDK.config().logoDrawableResourceID = R.mipmap.ic_launcher;
            ChatSDK.builder()
                    .setPushNotificationColor(R.color.colorPrimary)
                    .setPushNotificationImageDefaultResourceId(R.mipmap.ic_launcher).setPushNotificationAction(getString(R.string.app_name)).build();



            //message event
            Predicate<NetworkEvent> filter = NetworkEvent.filterType(EventType.MessageAdded, EventType.MessageRemoved);

            Disposable d = ChatSDK.events()
                    .source()
                    .filter(filter)
                    .subscribe(networkEvent -> {

                        // Handle Event Here
                        if (networkEvent.getMessage() != null) {
                            Logger.debug(networkEvent.getMessage().getText());
                            Toast.makeText(getApplicationContext(), networkEvent.getMessage().getText(), Toast.LENGTH_SHORT).show();
                        }

                    });

// Stop listening
            d.dispose();






        } catch (Exception e) {
            e.printStackTrace();
        }


        List<Locale> localeList = new ArrayList<>();
        localeList.add(new Locale("ar"));
        localeList.add(new Locale("en"));

        LocaleChanger.initialize(getApplicationContext(), localeList);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleChanger.onConfigurationChanged();
    }
}
