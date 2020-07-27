package com.example.euromessagejavav2;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import static com.example.euromessagejavav2.Constants.APP_ALIAS;
import static com.example.euromessagejavav2.Constants.GET_CUSTOMER;


public class UserAreaActivity extends Fragment {
    private EuroMobileManager euroMobileManager;
    private String userId;
    private String userEmail;
    private UserModel userModel;
    public static String APP_ALIAS = Constants.APP_ALIAS;
    private static final int FIRST_ITEM_CAROUSEL = 0;
    private String pushToken;
    private ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_userarea, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        final TextView token = view.findViewById(R.id.token);
        final TextView tUsername = view.findViewById(R.id.areaUsername);
        TextView tvSlider = view.findViewById(R.id.tv_slider);

        token.setText(userEmail);
        tUsername.setText(userId);

        Animation animBlink = AnimationUtils.loadAnimation(getActivity(), R.anim.blink);
        tvSlider.startAnimation(animBlink);

        final String appToken = getArguments().getString("token");
        Log.w("Berkay UserArea", appToken);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(GET_CUSTOMER)
                .addHeader("Authorization", "Bearer " + appToken)
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.w("Berkay UserArea", myResponse);
                                userModel = gson.fromJson(myResponse, UserModel.class);
                                JSONObject json = new JSONObject(myResponse);
                                userId = json.get("id").toString();
                                userEmail = json.get("email").toString();
                                Log.w("Berkay UserArea", json.get("id").toString());
                                token.setText(appToken);
                                tUsername.setText(userModel.getId());
                                progressDialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

        return view;
    }

}
