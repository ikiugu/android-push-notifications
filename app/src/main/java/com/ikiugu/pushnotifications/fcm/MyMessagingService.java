package com.ikiugu.pushnotifications.fcm;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ikiugu.pushnotifications.Constants;
import com.ikiugu.pushnotifications.MainActivity;
import com.ikiugu.pushnotifications.R;
import com.ikiugu.pushnotifications.api.RetrofitClient;

public class MyMessagingService extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "123";
    RetrofitClient client;

    public MyMessagingService() {
        this.client = RetrofitClient.getInstance();
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences(Constants.SHARED_PREFS_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.USER_TOKEN, s);
        editor.apply();

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

    }

    private void sendNotification(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("user_name", remoteMessage.getData().get("user_name"));
        intent.putExtra("user_id", remoteMessage.getData().get("user_id"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) // set more content than the one liner
                .setContentIntent(pendingIntent)
                .setShowWhen(true) // hide timestamp
                .setAutoCancel(true); // dismiss when tapped
        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, builder.build());
    }
}