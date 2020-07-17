package com.example.euromessagejavav2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.BuildConfig;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import euromsg.com.euromobileandroid.EuroMobileManager;
import euromsg.com.euromobileandroid.enums.GsmPermit;

public class MainActivity extends AppCompatActivity {

    private static final int FIRST_ITEM_CAROUSEL = 0;
    private static EuroMobileManager euroMobileManager;
    public static String APP_ALIAS = Constants.APP_ALIAS;
    private String token;
    private Button btnSync;
    private TextView tvTokenMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSync = findViewById(R.id.btn_sync);
        tvTokenMessage = findViewById(R.id.tv_token_message);

        initializeEuroMessage();
        setUI();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getExtras() != null) {
            euroMobileManager.reportRead(intent.getExtras());
            notificationUrl(intent);
        }
    }

    private void notificationUrl(Intent intent) {
        if (euroMobileManager.getNotification(intent) != null) {
            Log.w("Euromessage", euroMobileManager.getNotification(intent).getUrl());
        }

        if (euroMobileManager.getCarousels(intent) != null) {
            Log.w("Euromessage", euroMobileManager.getCarousels(intent).get(FIRST_ITEM_CAROUSEL).getUrl());
        }

        euroMobileManager.removeIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getExtras() != null && euroMobileManager.getNotification(getIntent()) != null) {
            euroMobileManager.reportRead(getIntent().getExtras());
            notificationUrl(getIntent());
        }
    }

    public void initializeEuroMessage() {
        euroMobileManager = EuroMobileManager.init(APP_ALIAS, this);
        euroMobileManager.registerToFCM(getBaseContext());

    }

    private void setUI() {
        setReleaseName();
        checkTokenStatus();
        sync();
    }

    private void sync() {
        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                euroMobileManager.setGsmPermit(GsmPermit.ACTIVE, getApplicationContext());
                euroMobileManager.setEmail("fuko95@fuko95.com", getApplicationContext());
                euroMobileManager.setEuroUserId("56", getApplicationContext());
                euroMobileManager.sync(getApplicationContext());
            }
        });
    }

    private void checkTokenStatus() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Euromessage", "Token Failed");
                            return;
                        }

                        token = task.getResult().getToken();
                        Log.w("Euromessage", "Token Success");
                        tvTokenMessage.setText(token);

                        EuroMobileManager.getInstance().subscribe(token, getApplicationContext());
                    }
                });
    }

    public void setReleaseName() {
        String libVersionName = BuildConfig.VERSION_NAME;
        Log.w("Euromessage", "App version: " + BuildConfig.VERSION_NAME + " EM SDK version:" + libVersionName);

    }
}