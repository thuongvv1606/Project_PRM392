package com.example.restaurantproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.restaurantproject.MenuOrderActivity;
import com.example.restaurantproject.R;
import com.example.restaurantproject.bean.Product;

import java.util.HashMap;
import java.util.List;

public class ProductOrderAdapter extends RecyclerView.Adapter<ProductOrderAdapter.ProductOrderViewHolder>{
    private Context context;
    private List<Product> productList;
    private OnProductAddListener onProductAddListener;
    public ProductOrderAdapter(Context context, List<Product> productList, OnProductAddListener onProductAddListener) {
        this.context = context;
        this.productList = productList;
        this.onProductAddListener = onProductAddListener;
    }
    @NonNull
    @Override
    public ProductOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_product_menu_order, parent, false);
        return new ProductOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductOrderViewHolder holder, int position) {
        Product product = productList.get(position);
        if (product.getProductImage() != null) {
            Glide.with(context).load(product.getProductImage()).into(holder.imageFood);
        }
        holder.textProductName.setText(product.getProductName());
        holder.textPrice.setText(product.getPrice()+" VNĐ");

        holder.btnAdd.setOnClickListener(v -> {
            if (onProductAddListener != null) {
                onProductAddListener.onProductAdded(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductOrderViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageFood;
        private TextView textProductName, textPrice;
        private Button btnAdd;
        public ProductOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageFood = itemView.findViewById(R.id.image_product);
            textProductName = itemView.findViewById(R.id.text_product_name_menu);
            textPrice = itemView.findViewById(R.id.text_price_menu);
            btnAdd = itemView.findViewById(R.id.btn_add_order_item);
        }
    }
    public interface OnProductAddListener {
        void onProductAdded(Product Products);
    }
}
