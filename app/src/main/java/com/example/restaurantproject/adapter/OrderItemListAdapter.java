package com.example.restaurantproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantproject.R;
import com.example.restaurantproject.bean.Order;
import com.example.restaurantproject.bean.OrderDetails;
import com.example.restaurantproject.bean.Product;
import com.example.restaurantproject.entity.ProductOrderDTO;
import com.example.restaurantproject.repository.CategoryRepository;
import com.example.restaurantproject.repository.OrderDetailsRepository;
import com.example.restaurantproject.repository.OrderRepository;

import java.util.HashMap;
import java.util.List;

public class OrderItemListAdapter extends RecyclerView.Adapter<OrderItemListAdapter.OrderItemListViewHolder>{
    private List<ProductOrderDTO> products;
    private Context context;
    public OrderItemListAdapter(List<ProductOrderDTO> products, Context context) {
        this.products = products;
        this.context = context;
    }
    @NonNull
    @Override
    public OrderItemListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item_list, parent, false);
        return new OrderItemListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemListViewHolder holder, int position) {
        ProductOrderDTO product = products.get(position);

        holder.tvSTT.setText(position + 1 + "");
        holder.tvProductName.setText(product.getProductName());
        holder.tvPrice.setText(product.getPrice() + "");
        holder.tvNumber.setText(product.getNumber() + "");
    }

    @Override
    public int getItemCount()  {
        return products.size();
    }

    public class OrderItemListViewHolder extends RecyclerView.ViewHolder{
        private TextView tvSTT, tvProductName, tvPrice, tvNumber;

        @SuppressLint("WrongViewCast")
        public OrderItemListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSTT = itemView.findViewById(R.id.tv_stt);
            tvProductName = itemView.findViewById(R.id.tv_product_name_inOrder);
            tvPrice = itemView.findViewById(R.id.tv_price_view_order);
            tvNumber = itemView.findViewById(R.id.tv_number);
        }
    }
}
