package com.example.euromessagejavav2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.euromessagejavav2.Constants.REGISTER_URL;
import static com.example.euromessagejavav2.LoginActivity.JSON;

public class RegisterActivity extends AppCompatActivity {

    private EditText mName, mSurname, mEmail, mPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mName = findViewById(R.id.et_name);
        mSurname = findViewById(R.id.et_surname);
        mEmail = findViewById(R.id.et_reg_email);
        mPassword = findViewById(R.id.et_reg_password);
        Button mRegister = findViewById(R.id.btn_register);
        TextView mBack = findViewById(R.id.backToLogin);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = mEmail.getText().toString();
                String Name = mName.getText().toString();
                String Surname = mSurname.getText().toString();
                String Password = mPassword.getText().toString();

                new RegisterUser().execute(Email, Password, Name, Surname);
            }
        });
    }

    public class RegisterUser extends AsyncTask<String, Void, String> {
        public Request request;

        @Override
        protected String doInBackground(String... strings) {
            String Email = strings[0];
            String Password = strings[1];
            String FirstName = strings[2];
            String LastName = strings[3];

            OkHttpClient okHttpClient = new OkHttpClient();
            JSONObject customer = new JSONObject();
            JSONObject json = new JSONObject();
            try {
                customer.put("email", Email);
                customer.put("firstname", FirstName);
                customer.put("lastname", LastName);
                json.put("customer", customer);
                json.put("password", Password);
                RequestBody body = RequestBody.create(JSON, json.toString());
                request = new Request.Builder()
                        .url(REGISTER_URL)
                        .post(body)
                        .build();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
                System.out.println(json.toString());
                System.out.println(response.message());
                if (response.message().equals("OK")) {
                    Intent i = new Intent(RegisterActivity.this,
                            LoginActivity.class);
                    i.putExtra("username", Email);
                    startActivity(i);
                    finish();
                    showToast("Register Successful!");
                } else {
                    showToast(response.body().string());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void showToast(final String Text) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this,
                        Text, Toast.LENGTH_LONG).show();
            }
        });
    }
}
