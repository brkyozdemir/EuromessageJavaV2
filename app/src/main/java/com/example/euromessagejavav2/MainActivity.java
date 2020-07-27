package com.example.euromessagejavav2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.euromessagejavav2.Fragments.CategoryFragment;
import com.example.euromessagejavav2.Fragments.ProductsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
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

import static com.example.euromessagejavav2.Constants.APP_ALIAS;
import static com.example.euromessagejavav2.Constants.GET_CUSTOMER;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private EuroMobileManager euroMobileManager;
    private static final int FIRST_ITEM_CAROUSEL = 0;
    private String userId;
    private String userEmail;
    private UserModel userModel;
    private String pushToken;
    private Bundle userBundle;
    private UserAreaActivity userAreaActivity;
    private String appToken;
    private CategoryFragment categoryFragment;
    private TextView tvDrawerEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        Intent intent = getIntent();
        appToken = intent.getStringExtra("token");
        Log.w("Berkay", appToken);
        String email = intent.getStringExtra("username");

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        userBundle = new Bundle();
        userAreaActivity = new UserAreaActivity();
        categoryFragment = new CategoryFragment();
        tvDrawerEmail = headerView.findViewById(R.id.tv_drawerEmail);


        initializeEuroMessage();
        setUI();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            userBundle.putString("token", appToken.split("\"")[1]);
            userAreaActivity.setArguments(userBundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    userAreaActivity).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        tvDrawerEmail.setText(email);

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
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.w("Berkay Main", myResponse);
                                userModel = gson.fromJson(myResponse, UserModel.class);
                                JSONObject json = new JSONObject(myResponse);
                                userId = json.get("id").toString();
                                userEmail = json.get("email").toString();
                                Log.w("Berkay Main", json.get("id").toString());
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


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.bottomNav_home:
                            userBundle.putString("token", appToken.split("\"")[1]);
                            userAreaActivity.setArguments(userBundle);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    userAreaActivity).commit();
                            break;
                        case R.id.bottomNav_category:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    categoryFragment).commit();
                            break;
                        case R.id.bottomNav_comingSoon:
                            Toast.makeText(getApplicationContext(), "Coming Soon...", Toast.LENGTH_SHORT).show();
                            break;
                    }

                    return true;
                }
            };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getExtras() != null) {
            euroMobileManager.reportRead(intent.getExtras());
            notificationUrl(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getExtras() != null && euroMobileManager.getNotification(getIntent()) != null) {
            euroMobileManager.reportRead(getIntent().getExtras());
            notificationUrl(getIntent());
        }
    }

    public void notificationUrl(Intent intent) {
        if (euroMobileManager.getNotification(intent) != null) {
            Log.w("Berkay Main", euroMobileManager.getNotification(intent).getUrl());
        }

        if (euroMobileManager.getCarousels(intent) != null) {
            Log.w("Berkay Main", euroMobileManager.getCarousels(intent).get(FIRST_ITEM_CAROUSEL).getUrl());
        }
    }

    //
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                userBundle.putString("token", appToken.split("\"")[1]);
                userAreaActivity.setArguments(userBundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        userAreaActivity).commit();
                break;
            case R.id.nav_categories:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CategoryFragment()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setUI() {
        setReleaseName();
        checkTokenStatus();
    }

    public void setReleaseName() {
        String libVersionName = com.google.firebase.BuildConfig.VERSION_NAME;
        Log.w("Berkay Main", "App version: " + BuildConfig.VERSION_NAME + " EM SDK version:" + libVersionName);
    }

    public void initializeEuroMessage() {
        euroMobileManager = EuroMobileManager.init(APP_ALIAS, this);
        euroMobileManager.registerToFCM(getApplicationContext());
    }

    public void checkTokenStatus() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Berkay Main", "Token Failed");
                            showToast("Token Failed");
                            return;
                        }

                        pushToken = task.getResult().getToken();
                        Log.w("Berkay Main", "Token Success");
                        Log.w("Berkay Main", pushToken);
                        EuroMobileManager.getInstance().subscribe(pushToken, getApplicationContext());
                    }
                });
    }

    public void showToast(final String Text) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),
                        Text, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    public void switchContent(int id, Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(id, fragment, fragment.toString());
        ft.addToBackStack(null);
        ft.commit();
    }
}
