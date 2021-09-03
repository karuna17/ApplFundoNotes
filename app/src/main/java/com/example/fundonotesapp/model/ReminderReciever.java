package com.example.fundonotesapp.model;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.NotificationCompat;

public class ReminderReciever extends BroadcastReceiver {
    public static final String SHARED_PREF = "com.example.fundonotesapp.notification";
    public static final String NOTIFICATION_ID = "id";

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        String title = intent.getStringExtra("title");
        String text = intent.getStringExtra("text");

        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder channelNotification = notificationHelper.getChannelNotification(title, text);
        int notificationId = getNotificationId(sharedPreferences);

        notificationId += 1;
        notificationHelper.getManager().notify(notificationId, channelNotification.build());
        setNotificationId(sharedPreferences, notificationId);

    }

    public void setNotificationId(SharedPreferences sharedPreferences, int notificationId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(NOTIFICATION_ID, notificationId);
        editor.apply();
    }

    public int getNotificationId(SharedPreferences sharedPreferences) {
        return sharedPreferences.getInt(NOTIFICATION_ID, 0);
    }
}
