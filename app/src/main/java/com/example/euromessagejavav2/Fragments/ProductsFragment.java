package com.example.euromessagejavav2.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.euromessagejavav2.Adapters.ProductAdapter;
import com.example.euromessagejavav2.Models.CategoryModel;
import com.example.euromessagejavav2.Models.ProductModel;
import com.example.euromessagejavav2.R;
import com.example.euromessagejavav2.UserModel;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ProductsFragment extends Fragment {

    private ProductModel productModel;
    private ProductAdapter productAdapter;
    private ArrayList<ProductModel> items;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private TextView tvEmptyPage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_products, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        tvEmptyPage = v.findViewById(R.id.tv_emptyPage);

        recyclerView = v.findViewById(R.id.rv_products);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        items = new ArrayList<>();

        final String itemId = getArguments().getString("selected_item");
        Log.w("Berkay.Products", itemId);


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://store.therelated.com/rest/V1/categories/" + itemId + "/products")
//                .addHeader("Authorization", "Bearer " + appToken)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    final String myResponse = response.body().string();
                    final Gson gson = new Gson();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.w("Berkay.Products", myResponse);
                                JSONArray jsonArray = new JSONArray(myResponse);
                                if(jsonArray.length() < 1){
                                    tvEmptyPage.setVisibility(View.VISIBLE);
                                    progressDialog.dismiss();
                                }
                                Log.w("Berkay.Products1", jsonArray.toString());
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject child = jsonArray.getJSONObject(i);
                                    String sku = child.getString("sku");
                                    OkHttpClient client = new OkHttpClient();
                                    Request request = new Request.Builder()
                                            .url("https://store.therelated.com/rest/V1/products/"+sku)
                                            .build();
                                    client.newCall(request).enqueue(new Callback() {
                                        @Override
                                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                            e.printStackTrace();
                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            if (response.isSuccessful()) {
                                                assert response.body() != null;
                                                final String myResponse = response.body().string();
                                                final Gson gson = new Gson();
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            productModel = gson.fromJson(myResponse, ProductModel.class);
                                                            Log.w("Berkay.Products.req", myResponse);
                                                            Log.w("Berkay.Products.req", productModel.toString());
//                                                            JSONArray jsonArray = new JSONArray(myResponse);
//                                                            Log.w("Berkay.Products.req", jsonArray.toString());
                                                            Log.w("Berkay.Product.image",productModel.getMedia_gallery_entries().get(0).getFile());
                                                            String name = productModel.getName();
                                                            String price = productModel.getPrice();
                                                            String imageUrl = productModel.getMedia_gallery_entries().get(0).getFile();
                                                            items.add(new ProductModel(name, price, imageUrl));
                                                            productAdapter = new ProductAdapter(getContext(), items);
                                                            recyclerView.setAdapter(productAdapter);
                                                            progressDialog.dismiss();
                                                        }
                                                        catch (Exception e){
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });

                                }



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
