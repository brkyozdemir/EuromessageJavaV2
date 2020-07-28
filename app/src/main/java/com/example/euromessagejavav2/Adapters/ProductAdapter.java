package com.example.euromessagejavav2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.euromessagejavav2.Models.ProductModel;
import com.example.euromessagejavav2.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<ProductModel> data;

    public ProductAdapter(Context context, List<ProductModel> data){
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.card_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ProductModel model = data.get(position);
        String imageUrl = model.getImageUrl();
        String title = model.getName();
        String description = model.getPrice();

        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((75 - 60) + 1) + 60;

        holder.textTitle.setText(title);
        holder.textDescription.setText(randomNum + " TL");
        Picasso.get().load("https://store.therelated.com/media/catalog/product"+imageUrl).fit().centerInside().into(holder.productImage);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textTitle, textDescription;
        ImageView productImage;

        public ViewHolder(View itemView){
            super(itemView);
            textTitle = itemView.findViewById(R.id.tv_title_product);
            textDescription = itemView.findViewById(R.id.tv_description_product);
            productImage = itemView.findViewById(R.id.iv_for_product);
        }
    }
}
