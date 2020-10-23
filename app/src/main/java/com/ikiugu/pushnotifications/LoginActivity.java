package com.ikiugu.pushnotifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.ikiugu.pushnotifications.api.RetrofitClient;
import com.ikiugu.pushnotifications.model.User;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    public static final String CHANNEL_ID = "notifications_channel";
    EditText editTextUserName;
    Button btnLogin;
    Button btnCreateUser;
    Button btnSendPush;
    RetrofitClient client;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // hooks
        btnCreateUser = findViewById(R.id.btnCreateUser);
        btnSendPush = findViewById(R.id.btnSendPush);
        editTextUserName = findViewById(R.id.editTextUserName);
        btnLogin = findViewById(R.id.btnLogin);
        client = getClient();
        coordinatorLayout = findViewById(R.id.coordinator);

        createNotificationChannel();


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

        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userToken = getSharedPrefs().getString(Constants.USER_TOKEN, null);
                if (userToken == null) {
                    Toast.makeText(LoginActivity.this, "Token is null", Toast.LENGTH_SHORT).show();
                    return;
                }
                User user = new User();
                user.setUserToken(userToken);

                client.getApi().handleUser(user).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            User user = response.body();
                            if (user.isSuccess()) {
                                // save the info from the server to the api
                                saveSharedPrefs(user);

                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Account created successfully", BaseTransientBottomBar.LENGTH_INDEFINITE);
                                snackbar.setAction("Ok", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // navigate to the home screen
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                });

                                snackbar.show();

                            } else {
                                Toast.makeText(LoginActivity.this, user.getErrorMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            try {
                                Toast.makeText(LoginActivity.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = editTextUserName.getText().toString().trim();
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(LoginActivity.this, "Please enter a user name", Toast.LENGTH_SHORT).show();
                    return;
                }

                String token = getSharedPrefs().getString(Constants.USER_TOKEN, null);
                if (TextUtils.isEmpty(token)) {
                    Toast.makeText(LoginActivity.this, "No token found, please clear data and try again", Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = new User();
                user.setUserName(userName);
                user.setUserToken(token);

                client.getApi().handleUser(user).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            User user = response.body();
                            if (user.isSuccess()) {
                                // save the info from the server to the api
                                saveSharedPrefs(user);

                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Welcome back", BaseTransientBottomBar.LENGTH_INDEFINITE);
                                snackbar.setAction("Ok", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // navigate to the home screen
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                });

                                snackbar.show();
                            } else {
                                Toast.makeText(LoginActivity.this, user.getErrorMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            try {
                                Toast.makeText(LoginActivity.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void saveSharedPrefs(User user) {
        SharedPreferences.Editor editor = getSharedPrefsEditor();
        editor.putBoolean(Constants.LOGGED_IN, true);
        editor.putString(Constants.USER_NAME, user.getUserName());
        editor.putString(Constants.USER_TOKEN, user.getUserToken());
        editor.commit();
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
                //.setShowWhen(false) // disable the timestamp with false
                //.setTimeoutAfter(5000) // this automatically dismisses the notification
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

    private NotificationCompat.Builder buildSecondNotification() {
        // build the notification tap action
        Intent intent = new Intent(getApplicationContext(), SplashScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        // build the notificationsendMessage

        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Updated Title")
                .setContentText("Updated body")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true) // set this to remove it when tapped
                //.setShowWhen(false) // disable the timestamp with false
                //.setTimeoutAfter(5000) // this automatically dismisses the notification
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}