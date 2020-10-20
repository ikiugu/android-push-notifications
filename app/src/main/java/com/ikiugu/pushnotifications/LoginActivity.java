package com.ikiugu.pushnotifications;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class LoginActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "1";
    Button btnCreateUser;
    Button btnSendPush;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // hooks
        btnCreateUser = findViewById(R.id.btnCreateUser);
        btnSendPush = findViewById(R.id.btnSendPush);

        btnSendPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final NotificationCompat.Builder builder = buildFirstNotification();
                final NotificationCompat.Builder builder2 = buildSecondNotification();

                // use the notification manager to display the notification
                final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                // comment this line out if you need to display the notification after a set time below
                notificationManager.notify(1, builder.build());

                // uncomment the code block below to set a timer to enable the notification to show when the app is in the background
                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        notificationManager.notify(1, builder.build());
                    }
                }, 5000);*/

                // uncomment the code block below to update the notification
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        notificationManager.notify(1, builder2.build());
                    }
                }, 5000);
            }
        });
    }

    private NotificationCompat.Builder buildFirstNotification() {
        // build the notification tap action
        Intent intent = new Intent(getApplicationContext(), SplashScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        // build the notification

        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Sample Title")
                .setContentText("Sample body")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true) // set this to remove it when tapped
                .setShowWhen(false) // disable the timestamp with false
                .setTimeoutAfter(5000) // this automatically dismisses the notification
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

    private NotificationCompat.Builder buildSecondNotification() {
        // build the notification tap action
        Intent intent = new Intent(getApplicationContext(), SplashScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        // build the notification

        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Updated Title")
                .setContentText("Updated body")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true) // set this to remove it when tapped
                .setShowWhen(false) // disable the timestamp with false
                .setTimeoutAfter(5000) // this automatically dismisses the notification
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }
}