package com.example.st1ch.xmppclient.logic;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;

import com.example.st1ch.xmppclient.R;
import com.example.st1ch.xmppclient.chat.activity.ChatActivity;

/**
 * Created by st1ch on 24.10.15.
 */
public class NotificatonClass {

    public static final int NEW_MESSAGE_ID = 1;

    private Context context;

    public NotificatonClass(Context context){
        this.context = context;
    }

    public void getNotify(int notifyId){
        Intent notificationIntent = null;
        String title = "";
        String text = "";
        String ticker = "";
        int NOTIFY_ID = -1;
        switch (notifyId){
            case NEW_MESSAGE_ID:
                notificationIntent = new Intent(context, ChatActivity.class);
                title = "Notification";
                text = "Received new message";
                ticker = "New message";
                NOTIFY_ID = NEW_MESSAGE_ID;
                break;

        }
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);


        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
                .setTicker(ticker).setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL);

        Notification notification = builder.build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFY_ID, notification);


    }

}
