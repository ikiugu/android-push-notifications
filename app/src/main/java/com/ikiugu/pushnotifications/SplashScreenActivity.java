package com.ikiugu.pushnotifications;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends BaseActivity {

    public static final long SPLASH_SCREEN_DELAY_TIME = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPrefs();

                Intent intent = null;
                if (sharedPreferences.getBoolean(Constants.LOGGED_IN, false)) {
                    // meaning user has been created and we have logged in
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                } else {
                    // meaning no user has been created and we have not logged in
                    intent = new Intent(getApplicationContext(), LoginActivity.class);

                }
                startActivity(intent);

                finish();

            }
        }, SPLASH_SCREEN_DELAY_TIME);
    }
}