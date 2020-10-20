package com.ikiugu.pushnotifications;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(BaseActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
