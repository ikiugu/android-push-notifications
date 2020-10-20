package com.ikiugu.pushnotifications;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.ikiugu.pushnotifications.api.RetrofitClient;
import com.ikiugu.pushnotifications.model.Notification;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagingActivity extends BaseActivity {

    TextView txtMessageSender;
    TextView txtMessageBody;
    EditText txtRecipientMessage;
    EditText txtRecipientName;
    Button btnSendMessage;
    RetrofitClient client;
    CoordinatorLayout coordinatorLayout;
    private boolean loadData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String loadDataValue = bundle.getString("load_data");
            if (!TextUtils.isEmpty(loadDataValue)) {
                loadData = Boolean.parseBoolean(loadDataValue);
            }
        }

        if (loadData) {
            Toast.makeText(this, "Load data about the message from the backend here", Toast.LENGTH_LONG).show();
        }

        txtMessageSender = findViewById(R.id.txtMessageSender);
        txtMessageBody = findViewById(R.id.txtMessageBody);
        txtRecipientName = findViewById(R.id.txtRecipientName);
        txtRecipientMessage = findViewById(R.id.txtRecipientMessage);
        btnSendMessage = findViewById(R.id.btnSendMessage);
        coordinatorLayout = findViewById(R.id.coordinator);
        client = getClient();

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = getSharedPrefs().getString(Constants.USER_TOKEN, null);
                if (token == null) {
                    Toast.makeText(MessagingActivity.this, "The token is blank", Toast.LENGTH_LONG).show();
                    return;
                }

                String recipientName = txtRecipientName.getText().toString().trim();
                String recipientMessage = txtRecipientMessage.getText().toString().trim();

                Notification notification = new Notification();
                notification.setSenderToken(token);
                notification.setRecipientUserName(recipientName);
                notification.setMessage(recipientMessage);

                client.getApi().sendMessage(notification).enqueue(new Callback<Notification>() {
                    @Override
                    public void onResponse(Call<Notification> call, Response<Notification> response) {
                        if (response.isSuccessful()) {
                            Notification notification1 = response.body();
                            if (notification1.isSuccess()) {
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, notification1.getMessage(), BaseTransientBottomBar.LENGTH_LONG);
                                snackbar.show();
                            } else {
                                Toast.makeText(MessagingActivity.this, notification1.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            try {
                                Toast.makeText(MessagingActivity.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Notification> call, Throwable t) {
                        Toast.makeText(MessagingActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}