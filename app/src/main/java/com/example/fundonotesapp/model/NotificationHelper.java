package com.example.fundonotesapp.model;

import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.fundonotesapp.R;
import com.example.fundonotesapp.view.MainActivity;

public class NotificationHelper extends ContextWrapper {
    private NotificationManager mManager = null;
    public static final String channelID = "reminderID";
    public static final String channelName = "Fundoo Reminder";
    Context base;

    public NotificationHelper(Context base) {
        super(base);
        this.base = base;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        getManager().createNotificationChannel(channel);
    }

    public NotificationCompat.Builder getChannelNotification(String title, String text) {
        Intent intent = new Intent(base, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(base);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_baseline_event_note_24)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
    }

    public NotificationManager getManager() {

        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return mManager;
    }

}
