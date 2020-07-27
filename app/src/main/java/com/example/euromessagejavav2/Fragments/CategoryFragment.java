package com.example.euromessagejavav2.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.euromessagejavav2.Adapters.CategoryAdapter;
import com.example.euromessagejavav2.Models.CategoryModel;
import com.example.euromessagejavav2.R;

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

public class CategoryFragment extends Fragment {

    private CategoryModel categoryModel;
    private CategoryAdapter categoryAdapter;
    private ArrayList<CategoryModel> items;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_category, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        recyclerView = v.findViewById(R.id.rv_category);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        items = new ArrayList<>();
        Bundle bundle = getArguments();

        if (bundle == null) {
            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url("https://store.therelated.com/rest/V1/categories")
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        final String myResponse = response.body().string();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Log.w("Berkay Category", myResponse);
                                    JSONObject json = new JSONObject(myResponse);
                                    JSONArray jsonArray = json.getJSONArray("children_data");
                                    Log.w("Berkay Category", jsonArray.toString());
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject child = jsonArray.getJSONObject(i);
                                        Log.w("Berkay.Category.Details", child.getString("children_data"));
                                        String name = child.getString("name");
                                        String id = child.getString("id");
                                        items.add(new CategoryModel(name, id, child.getString("children_data")));
                                    }

                                    categoryAdapter = new CategoryAdapter(getContext(), items);
                                    recyclerView.setAdapter(categoryAdapter);
                                    progressDialog.dismiss();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            });
        } else {
            final String id = bundle.getString("id");
            Log.w("Berkay.Category.Details", id);
            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url("https://store.therelated.com/rest/V1/categories")
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        final String myResponse = response.body().string();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Log.w("Berkay.Category.bundle", myResponse);
                                    JSONObject json = new JSONObject(myResponse);
                                    JSONArray jsonArray = json.getJSONArray("children_data");
                                    Log.w("Berkay.Category.bundle", jsonArray.toString());
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject child = jsonArray.getJSONObject(i);
                                        if (child.getString("id").equals(id)) {
                                            Log.w("Berkay.Category.Details", child.getString("children_data"));
                                            JSONArray jsonInside = new JSONArray(child.getString("children_data"));
                                            for (int j = 0; j < jsonInside.length(); j++) {
                                                JSONObject insideChild = jsonInside.getJSONObject(j);
                                                String name = insideChild.getString("name");
                                                String id = insideChild.getString("id");
                                                items.add(new CategoryModel(name, id, insideChild.getString("children_data")));
                                            }
                                        } else {
                                            JSONArray jsonInside = new JSONArray(child.getString("children_data"));
                                            Log.w("Berkay.Category.second", jsonInside.toString());
                                            for (int k = 0; k < jsonInside.length(); k++) {
                                                JSONObject insideChild = jsonInside.getJSONObject(k);
                                                if (insideChild.getString("id").equals(id)) {
                                                    JSONArray secondChild = new JSONArray(insideChild.getString("children_data"));
                                                    Log.w("Berkay.Category.21", secondChild.toString());
                                                    for (int x = 0; x < secondChild.length(); x++) {
                                                        JSONObject secondObject = secondChild.getJSONObject(x);
                                                        String name = secondObject.getString("name");
                                                        String id = secondObject.getString("id");
                                                        items.add(new CategoryModel(name, id, secondObject.getString("children_data")));
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    categoryAdapter = new CategoryAdapter(getContext(), items);
                                    recyclerView.setAdapter(categoryAdapter);
                                    progressDialog.dismiss();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            });
        }

        return v;
    }
}
