package com.example.euromessagejavav2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.euromessagejavav2.Constants.LOGIN_URL;

public class LoginActivity extends AppCompatActivity {
    private Button signIn;
    private EditText mUsername, mPassword;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private TextView mRegister;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signIn = findViewById(R.id.signInBtn);
        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mRegister = findViewById(R.id.register);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        mUsername.setText(username);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = mUsername.getText().toString();
                String Password = mPassword.getText().toString();
                new LoginUser().execute(Email, Password);
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    OkHttpClient client = new OkHttpClient();
    public class LoginUser extends AsyncTask<String, Void, String> {
        public Request request;

        @Override
        protected String doInBackground(String... strings) {
            String Email = strings[0];
            String Password = strings[1];

            OkHttpClient okHttpClient = new OkHttpClient();
            JSONObject json = new JSONObject();
            try {
                json.put("username", Email);
                json.put("password", Password);
                RequestBody body = RequestBody.create(JSON, json.toString());
                request = new Request.Builder()
                        .url(LOGIN_URL)
                        .post(body)
                        .build();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Response response;
            try{
                response = okHttpClient.newCall(request).execute();
                System.out.println(json.toString());
                if(response.message().equals("OK")){
                    Intent i = new Intent(LoginActivity.this,
                            MainActivity.class);
                    i.putExtra("token", response.body().string());
                    i.putExtra("username", Email);
                    startActivity(i);
                    finish();
                    showToast("Login Successful!");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    public void showToast(final String Text){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this,
                        Text, Toast.LENGTH_LONG).show();
            }
        });
    }
}
