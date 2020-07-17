package com.example.euromessagejavav2;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.BuildConfig;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import euromsg.com.euromobileandroid.EuroMobileManager;
import euromsg.com.euromobileandroid.enums.GsmPermit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.euromessagejavav2.Constants.GET_CUSTOMER;

public class UserAreaActivity extends AppCompatActivity {
    private TextView token;
    private EuroMobileManager euroMobileManager;
    private String userId;
    private String userEmail;
    private UserModel userModel;
    public static String APP_ALIAS = Constants.APP_ALIAS;
    private static final int FIRST_ITEM_CAROUSEL = 0;
    private String pushToken;
    private Animation animFadeIn, animBlink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userarea);

        token = findViewById(R.id.token);
        TextView tUsername = findViewById(R.id.areaUsername);
        TextView tvSlider = findViewById(R.id.tv_slider);

        animBlink = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);
        tvSlider.startAnimation(animBlink);

        initializeEuroMessage();
        setUI();

        Intent intent = getIntent();
        String appToken = intent.getStringExtra("token");
        String username = intent.getStringExtra("username");

        Log.w("Berkay", "Bearer " + appToken.split("\"")[1]);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(GET_CUSTOMER)
                .addHeader("Authorization", "Bearer " + appToken.split("\"")[1])
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    final Gson gson = new Gson();
                    JsonParser parser = new JsonParser();
                    final JsonElement json = parser.parse(response.body().toString());
                    UserAreaActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.w("Berkay", myResponse);
                                userModel = gson.fromJson(myResponse, UserModel.class);
                                JSONObject json = new JSONObject(myResponse);
                                userId = json.get("id").toString();
                                userEmail = json.get("email").toString();
                                Log.w("Berkay", json.get("id").toString());
                                euroMobileManager.setGsmPermit(GsmPermit.ACTIVE, getApplicationContext());
                                euroMobileManager.setEmail(userModel.getEmail(), getApplicationContext());
                                euroMobileManager.setEuroUserId(userModel.getId(), getApplicationContext());
                                euroMobileManager.sync(getApplicationContext());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

        token.setText(appToken.split("\"")[1]);
        tUsername.setText(username);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent.getExtras() != null){
            euroMobileManager.reportRead(intent.getExtras());
            notificationUrl(intent);
        }
    }

    public void notificationUrl(Intent intent){
        if(euroMobileManager.getNotification(intent) != null) {
            Log.w("Berkay", euroMobileManager.getNotification(intent).getUrl());
        }

        if(euroMobileManager.getCarousels(intent) != null) {
            Log.w("Berkay", euroMobileManager.getCarousels(intent).get(FIRST_ITEM_CAROUSEL).getUrl());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getIntent().getExtras() != null && euroMobileManager.getNotification(getIntent()) != null){
            euroMobileManager.reportRead(getIntent().getExtras());
            notificationUrl(getIntent());
        }
    }

    public void initializeEuroMessage(){
        euroMobileManager = EuroMobileManager.init(APP_ALIAS, this);
        euroMobileManager.registerToFCM(getBaseContext());
    }

    public void setUI(){
        setReleaseName();
        checkTokenStatus();
    }


    public void checkTokenStatus(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(!task.isSuccessful()){
                            Log.w("Berkay", "Token Failed");
                            showToast("Token Failed");
                            return;
                        }

                        pushToken = task.getResult().getToken();
                        Log.w("Berkay", "Token Success");
                        Log.w("Berkay", pushToken);
                        token.setText(pushToken);
                        EuroMobileManager.getInstance().subscribe(pushToken, getApplicationContext());
                    }
                });
    }

    public void setReleaseName(){
        String libVersionName = BuildConfig.VERSION_NAME;
        Log.w("Berkay", "App version: " + BuildConfig.VERSION_NAME + " EM SDK version:" + libVersionName);
    }

    public void showToast(final String Text) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(UserAreaActivity.this,
                        Text, Toast.LENGTH_LONG).show();
            }
        });
    }
}
