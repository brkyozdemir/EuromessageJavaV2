package com.example.euromessagejavav2.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.euromessagejavav2.Fragments.CategoryFragment;
import com.example.euromessagejavav2.Fragments.ProductsFragment;
import com.example.euromessagejavav2.MainActivity;
import com.example.euromessagejavav2.Models.CategoryModel;
import com.example.euromessagejavav2.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context context;
    private List<CategoryModel> data;

    public CategoryAdapter(Context context, List<CategoryModel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.category_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CategoryModel model = data.get(position);
        String title = model.getName();
        holder.textTitle.setText(title);
        Log.w("Berkay.Category.Adapter", model.getChildren_data());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONArray jsonArray = new JSONArray(model.getChildren_data());
                    if (jsonArray.length() > 0) {
                        Bundle bundle = new Bundle();
                        bundle.putString("id", model.getId());
                        CategoryFragment categoryFragment = new CategoryFragment();
                        categoryFragment.setArguments(bundle);
                        ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, categoryFragment).commit();
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("selected_item", model.getId());
                        bundle.putString("children_data", model.getChildren_data());
                        ProductsFragment productsFragment = new ProductsFragment();
                        productsFragment.setArguments(bundle);
                        ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                productsFragment).commit();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textTitle;
        CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            textTitle = itemView.findViewById(R.id.tv_title_category);
        }
    }
}
