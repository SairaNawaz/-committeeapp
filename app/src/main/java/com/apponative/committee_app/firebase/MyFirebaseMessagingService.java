package com.apponative.committee_app.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.apponative.committee_app.CommitteeApplication;
import com.apponative.committee_app.R;
import com.apponative.committee_app.datamodles.Notification;
import com.apponative.committee_app.ui.MainActivity;
import com.apponative.committee_app.utils.CommitteeCallBack;
import com.apponative.committee_app.utils.Constants;
import com.apponative.committee_app.utils.LocalStorageUtility;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private NotificationManager mNotificationManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            sendNotification(remoteMessage, getApplicationContext());
        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {

            //    sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), remoteMessage.getFrom(), "");
        }
    }

    public void sendNotification(RemoteMessage message, Context context) {

        Notification notification = new com.apponative.committee_app.datamodles.Notification(message.getData()
                .get("title").toString()
                , message.getData().get("message").toString()
                , message.getData().get("sentFrom").toString()
                , message.getData().get("cid").toString()
                , Constants.NTYPE.valueOf(message.getData().get("type")));

        mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(context, MainActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent1.putExtra("Notification", new Gson().toJson(notification));
        PendingIntent contentIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        String tickerText = String.format(Locale.getDefault(), "%s: %s", "Committee App", message.getData().get("message"));

        android.support.v4.app.NotificationCompat.Builder mBuilder =
                new android.support.v4.app.NotificationCompat.Builder(context)
                        .setDefaults(android.app.Notification.DEFAULT_ALL)
                        .setTicker(tickerText)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(message.getData().get("title"))
                        .setStyle(new android.support.v4.app.NotificationCompat.BigTextStyle()
                                .bigText(message.getData().get("message")))
                        .setContentText(message.getData().get("message")).setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);

        try {
            ArrayList<com.apponative.committee_app.datamodles.Notification> remoteMessages = new ArrayList<>();
            remoteMessages.add(notification);

            if (LocalStorageUtility.getFromSharedPref(getApplicationContext(), Constants.NOTIFICATIONSLIST) != null) {
                remoteMessages.addAll((Collection<? extends Notification>) new Gson().fromJson(LocalStorageUtility.getFromSharedPref(getApplicationContext(), Constants.NOTIFICATIONSLIST)
                        , new TypeToken<ArrayList<Notification>>() {
                }.getType()));
                LocalStorageUtility.saveToSharedPref(getApplicationContext(), Constants.NOTIFICATIONSLIST, new Gson().toJson(remoteMessages));
            } else {
                LocalStorageUtility.saveToSharedPref(getApplicationContext(), Constants.NOTIFICATIONSLIST, new Gson().toJson(remoteMessages));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (CommitteeApplication.isActive() && CommitteeApplication.getCurrentActivity() != null) {
            CommitteeCallBack.NotificationCallBack notificationCallBack = (CommitteeCallBack.NotificationCallBack) CommitteeApplication.getCurrentActivity();
            notificationCallBack.OnNotificationReceived();
        }
        mNotificationManager.notify(0, mBuilder.build());

    }

    void sendNotification(String title, String body, String from, String type) {

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.putExtra("Notification", type);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        android.app.Notification n = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(type)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)
                .build();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);
    }


    @Override
    public void onMessageSent(String msgId) {
        super.onMessageSent(msgId);
    }

    @Override
    public void onSendError(String msgId, Exception e) {
        super.onSendError(msgId, e);
    }
}
