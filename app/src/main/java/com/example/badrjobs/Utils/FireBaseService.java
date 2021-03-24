package com.example.badrjobs.Utils;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FireBaseService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.e("newToken", token);
//Add your token in your sharepreferences.
        if (SharedPrefManager.getInstance(getApplicationContext()).getFcmToken().isEmpty()) {
            SharedPrefManager.getInstance(getApplicationContext()).storeFcmToken(token);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }

}
