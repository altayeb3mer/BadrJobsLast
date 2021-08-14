package bader.cutShort.badrjobs;

import android.app.Application;
import android.content.res.Configuration;
import android.widget.Toast;

import bader.cutShort.badrjobs.Activity.ChattingActivity;
import bader.cutShort.badrjobs.Activity.CustomProfileActivity;
import bader.cutShort.badrjobs.Fragment.FragmentChat;

import com.example.badrjobs.R;
import com.franmontiel.localechanger.LocaleChanger;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.PhoneAuthProvider;

import org.pmw.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import sdk.chat.app.firebase.ChatSDKFirebase;
import sdk.chat.core.events.EventType;
import sdk.chat.core.events.NetworkEvent;
import sdk.chat.core.session.ChatSDK;
import sdk.chat.firebase.adapter.module.FirebaseModule;
import sdk.chat.firebase.push.FirebasePushModule;
import sdk.chat.firebase.ui.FirebaseUIModule;
import sdk.chat.firebase.upload.FirebaseUploadModule;
import sdk.chat.message.audio.AudioMessageModule;
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
            ChatSDK.config().setPushNotificationImageDefaultResourceId(R.mipmap.ic_launcher);
            ChatSDK.builder()
                    .setPushNotificationColor(R.color.colorPrimary).setPushNotificationAction(getString(R.string.app_name)).build();


            ChatSDK.config().setInboundPushHandlingEnabled(false);








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
