package com.safademirel.quizgame.Utilities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.safademirel.quizgame.Activities.Application;
import com.safademirel.quizgame.Activities.Main;
import com.safademirel.quizgame.R;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by filipp on 5/23/2016.
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            showNotification(remoteMessage.getNotification().getBody());
        }

        if (remoteMessage.getData().get("message") != null)
        {
            showNotification(remoteMessage.getData().get("message"));
        }
    }

    private void showNotification(String message) {

        Intent newActivity = new Intent(this, Main.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,newActivity,PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("Bil Kazan")
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0,builder.build());
    }


}
