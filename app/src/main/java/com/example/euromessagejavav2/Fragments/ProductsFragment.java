package com.example.euromessagejavav2.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.euromessagejavav2.Models.ProductModel;
import com.example.euromessagejavav2.R;
import com.example.euromessagejavav2.UserModel;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ProductsFragment extends Fragment {

    private ProductModel productModel;
    private ImageView ivProduct;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_products, container, false);

        ivProduct = v.findViewById(R.id.iv_product);


        final String appToken = getArguments().getString("token");
        Log.w("Berkay Products", appToken);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://store.therelated.com/rest/V1/products/24-MB02")
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
                    assert response.body() != null;
                    final String myResponse = response.body().string();
                    final Gson gson = new Gson();
                    JsonParser parser = new JsonParser();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.w("Berkay Products", myResponse);
                                productModel = gson.fromJson(myResponse, ProductModel.class);
                                String productUrl = productModel.getMedia_gallery_entries().get(0).getFile();
                                Log.w("Berkay Products", productUrl);
                                Picasso.get().load("https://store.therelated.com/media/catalog/product" + productUrl).into(ivProduct);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

        return v;
    }
}
