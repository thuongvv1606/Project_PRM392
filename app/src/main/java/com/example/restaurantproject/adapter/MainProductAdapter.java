package com.example.restaurantproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.restaurantproject.R;
import com.example.restaurantproject.bean.Product;
import com.example.restaurantproject.bean.Restaurant;

import java.util.List;

public class MainProductAdapter extends RecyclerView.Adapter<MainProductAdapter.ProductViewHolder>{
    private List<Product> products = null;
    private Context context;
    public  MainProductAdapter(List<Product> products, Context context){
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public MainProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_product_item, parent, false);
        return new MainProductAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainProductAdapter.ProductViewHolder holder, int position) {
        Product product = products.get(position);

        String id = "" + product.getProductId();

        // Set the spannableString to the TextView
        holder.tvProductName.setText("" + product.getProductName());
        holder.tvProductPrice.setText("" + product.getPrice() + " VND");
        if (product.getProductImage() != null && !product.getProductImage().isEmpty()) {
            Glide.with(context).load(product.getProductImage()).into(holder.tvProductImage);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{
        private TextView tvProductName, tvProductPrice;
        private ImageView tvProductImage;

        @SuppressLint("WrongViewCast")
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.product_name);
            tvProductPrice = itemView.findViewById(R.id.product_price);
            tvProductImage = itemView.findViewById(R.id.product_image);
        }
    }
}
