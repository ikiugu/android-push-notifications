package com.ikiugu.pushnotifications;

import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    /**
     * Get an instance of the shared preferences editor
     *
     * @return -> editor
     */
    public SharedPreferences.Editor getSharedPrefsEditor() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.SHARED_PREFS_NAME, 0);
        return sharedPreferences.edit();
    }

    /**
     * Get shared prefs
     *
     * @return -> Shared preferences
     */
    public SharedPreferences getSharedPrefs() {
        return getApplicationContext().getSharedPreferences(Constants.SHARED_PREFS_NAME, 0);
    }
}
