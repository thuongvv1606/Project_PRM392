package com.example.restaurantproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantproject.R;
import com.example.restaurantproject.bean.Order;
import com.example.restaurantproject.bean.OrderDetails;
import com.example.restaurantproject.bean.Product;
import com.example.restaurantproject.repository.OrderDetailsRepository;
import com.example.restaurantproject.repository.OrderRepository;
import com.example.restaurantproject.repository.ProductRepository;

import java.util.List;

public class OrderDetailsProductListAdapter extends RecyclerView.Adapter<OrderDetailsProductListAdapter.OrderItemListViewHolder>{
    private List<OrderDetails> detailss;
    private Context context;
    public OrderDetailsProductListAdapter(List<OrderDetails> detailss, Context context) {
        this.detailss = detailss;
        this.context = context;
    }
    @NonNull
    @Override
    public OrderDetailsProductListAdapter.OrderItemListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_details_product_list_item, parent, false);
        return new OrderDetailsProductListAdapter.OrderItemListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsProductListAdapter.OrderItemListViewHolder holder, int position) {
        OrderDetails details = detailss.get(position);

        holder.tvSTT.setText(position + 1 + "");
        ProductRepository productRepository = new ProductRepository(context);
        Product product = productRepository.getProduct(details.getProductId());
        holder.tvProductName.setText(product.getProductName());
        holder.tvPrice.setText(details.getPrice() + "");
        holder.tvNumber.setText(details.getQuantity() + "");
    }

    @Override
    public int getItemCount()  {
        return detailss.size();
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
