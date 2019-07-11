package com.example.notificationtest;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        Log.i("Man","Token = "+s);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getNotification()!=null){
            String title=remoteMessage.getNotification().getTitle();
            String text=remoteMessage.getNotification().getBody();

            NotificationHelper.displayNotification(getApplicationContext(),title,text);

        }
    }
}
