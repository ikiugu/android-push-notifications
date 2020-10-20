package com.ikiugu.pushnotifications;

import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.ikiugu.pushnotifications.api.RetrofitClient;

public class BaseActivity extends AppCompatActivity {
    /**
     * Get an instance of the shared preferences editor
     */
    public SharedPreferences.Editor getSharedPrefsEditor() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.SHARED_PREFS_NAME, 0);
        return sharedPreferences.edit();
    }

    /**
     * Get shared prefs
     */
    public SharedPreferences getSharedPrefs() {
        return getApplicationContext().getSharedPreferences(Constants.SHARED_PREFS_NAME, 0);
    }

    /**
     * Get the retrofit client
     */
    public RetrofitClient getClient() {
        return RetrofitClient.getInstance();
    }
}
