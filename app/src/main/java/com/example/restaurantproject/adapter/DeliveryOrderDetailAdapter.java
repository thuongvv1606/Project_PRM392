package com.example.restaurantproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantproject.R;
import com.example.restaurantproject.entity.DeliveryProductDetail;

import java.util.List;

public class DeliveryOrderDetailAdapter extends RecyclerView.Adapter<DeliveryOrderDetailAdapter.OrderViewHolder> {
    private List<DeliveryProductDetail> orders = null;
    private Context context;
    public DeliveryOrderDetailAdapter(List<DeliveryProductDetail> orders, Context context){
        this.context = context;
        this.orders = orders;
    }
    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.delivery_order_item_list, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        DeliveryProductDetail order = orders.get(position);


        holder.tvProduct.setText("" + order.getProductName());
        holder.tvPrice.setText("" + order.getPrice());
        holder.tvQuantity.setText("" + order.getQuantity());
        holder.tvTotal.setText("" + order.getTotalProductPrice());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder{
        private TextView tvProduct, tvPrice, tvQuantity, tvTotal;
        private ImageButton btnEdit, btnDelete;

        @SuppressLint("WrongViewCast")
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProduct = itemView.findViewById(R.id.tv_delivery_product);
            tvPrice = itemView.findViewById(R.id.tv_delivery_price);
            tvQuantity = itemView.findViewById(R.id.tv_deliver_quantity);
            tvTotal = itemView.findViewById(R.id.tv_delivery_total);
        }
    }
}
