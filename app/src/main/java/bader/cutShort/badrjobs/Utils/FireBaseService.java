package bader.cutShort.badrjobs.Utils;

import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tot.badges.IconBadgeNumManager;

import sdk.chat.core.notifications.MessagingService;
import sdk.chat.core.push.BaseBroadcastHandler;


public class FireBaseService extends FirebaseMessagingService  {
    private LocalBroadcastManager broadcaster;
    BaseBroadcastHandler bbc;

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.e("newToken", token);
        broadcaster = LocalBroadcastManager.getInstance(this);
        SharedPrefManager.getInstance(getApplicationContext()).storeFcmToken(token);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            SharedPrefManager.getInstance(getApplicationContext()).putHasNotification(true);
            sendIntent();
            new IconBadgeNumManager().setIconBadgeNum(getApplication(), null, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onMessageReceived(remoteMessage);
    }


    private void sendIntent() {
        Intent intent = new Intent("MyData");
        intent.putExtra("someData", "someData");
        broadcaster.sendBroadcast(intent);
    }


}



