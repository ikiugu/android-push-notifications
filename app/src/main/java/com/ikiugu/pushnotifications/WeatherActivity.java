package com.ikiugu.pushnotifications;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.ikiugu.pushnotifications.api.RetrofitClient;
import com.ikiugu.pushnotifications.model.Subscription;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends BaseActivity {

    CheckBox subscribeCheckBox;
    RetrofitClient client;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        subscribeCheckBox = findViewById(R.id.checkBoxSubscribe);
        coordinatorLayout = findViewById(R.id.coordinator);
        client = getClient();

        boolean subscribed = getSharedPrefs().getBoolean(Constants.SUBSCRIBED, false);
        subscribeCheckBox.setChecked(subscribed);

        subscribeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleSubscription(isChecked);
            }
        });
    }

    private void handleSubscription(boolean subscribe) {
        String userName = getSharedPrefs().getString(Constants.USER_NAME, null);
        if (userName == null) {
            Toast.makeText(this, "User name is null", Toast.LENGTH_SHORT).show();
            return;
        }

        Subscription subscription = new Subscription();
        subscription.setSubscribe(subscribe);
        subscription.setUserName(userName);
        SharedPreferences.Editor editor = getSharedPrefsEditor();

        client.getApi().subscribeToWeather(subscription).enqueue(new Callback<Subscription>() {
            @Override
            public void onResponse(Call<Subscription> call, Response<Subscription> response) {
                if (response.isSuccessful()) {
                    Subscription sub = response.body();
                    if (sub.isSuccess()) {
                        Snackbar snackbar = null;
                        if (subscribe) {
                            snackbar = Snackbar.make(coordinatorLayout, "You have subscribed successfully", BaseTransientBottomBar.LENGTH_LONG);
                        } else {
                            snackbar = Snackbar.make(coordinatorLayout, "You have un-subscribed successfully", BaseTransientBottomBar.LENGTH_LONG);
                        }
                        editor.putBoolean(Constants.SUBSCRIBED, subscribe);
                        editor.apply();

                        snackbar.show();
                    } else {
                        Toast.makeText(WeatherActivity.this, sub.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        Toast.makeText(WeatherActivity.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Subscription> call, Throwable t) {
                Toast.makeText(WeatherActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}