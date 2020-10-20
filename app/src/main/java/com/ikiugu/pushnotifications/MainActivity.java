package com.ikiugu.pushnotifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button navigateToMessaging;
    Button navigateToWeather;
    Button navigateToForex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigateToMessaging = findViewById(R.id.btnMessaging);
        navigateToWeather = findViewById(R.id.btnWeather);
        navigateToForex = findViewById(R.id.btnForex);

        navigateToMessaging.setOnClickListener(this);
        navigateToWeather.setOnClickListener(this);
        navigateToForex.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMessaging:
                navigate("messaging");
                break;
           /* case R.id.btnWeather:
                navigate("weather");
                break;
            case R.id.btnForex:
                navigate("forex");
                break;*/
        }
    }

    private void navigate(String screen) {
        Intent intent = null;
        if (screen.equalsIgnoreCase("messaging")) {
            intent = new Intent(MainActivity.this, MessagingActivity.class);
        }

        startActivity(intent);
    }
}