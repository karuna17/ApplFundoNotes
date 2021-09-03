package com.example.fundonotesapp.model;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        NotificationHelper notificationHelper = new NotificationHelper(this);
        NotificationCompat.Builder channelNotification = notificationHelper.getChannelNotification(
                remoteMessage.getNotification().getTitle().toString(),
                remoteMessage.getNotification().getBody().toString());
        notificationHelper.getManager().notify(0, channelNotification.build());
    }

}
