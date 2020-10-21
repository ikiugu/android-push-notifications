package com.ikiugu.pushnotifications;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceFragmentCompat;

import com.ikiugu.pushnotifications.api.RetrofitClient;
import com.ikiugu.pushnotifications.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }


        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (sharedPreferences.contains(key)) {
                updateSetting(sharedPreferences.getBoolean(key, false));
            }
        }

        private void updateSetting(boolean value) {
            RetrofitClient client = RetrofitClient.getInstance();
            String userName = getContext()
                    .getApplicationContext().getSharedPreferences(Constants.SHARED_PREFS_NAME, 0).getString(Constants.USER_NAME, null);
            User user = new User();
            user.setNotifications(value);
            user.setUserName(userName);
            client.getApi().handleNotifications(user).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        User user1 = response.body();
                        if (user1.isSuccess()) {
                            if (value) {
                                Toast.makeText(getContext(), "Notifications back on. Re-subscribe to any topics you want", Toast.LENGTH_SHORT).show();
                            } else {
                                SharedPreferences.Editor editor = getContext().getApplicationContext().getSharedPreferences(Constants.SHARED_PREFS_NAME, 0).edit();
                                editor.putBoolean(Constants.SUBSCRIBED, false);
                                editor.apply();
                                Toast.makeText(getContext(), "Notifications are off", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), user1.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}