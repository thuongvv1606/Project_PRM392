package com.example.restaurantproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantproject.OrderDetailsActivity;
import com.example.restaurantproject.R;
import com.example.restaurantproject.bean.Order;
import com.example.restaurantproject.bean.OrderDetails;
import com.example.restaurantproject.bean.Product;
import com.example.restaurantproject.entity.ProductOrderDTO;
import com.example.restaurantproject.repository.OrderDetailsRepository;
import com.example.restaurantproject.repository.OrderRepository;
import com.example.restaurantproject.repository.ProductRepository;

import org.w3c.dom.Text;

import java.util.List;

public class OrderDetailItemAdapter extends RecyclerView.Adapter<OrderDetailItemAdapter.OrderItemListViewHolder>{
    private List<OrderDetails> detailss;
    private Context context;
    private OnDataChangeListener onDataChangeListener;

    public OrderDetailItemAdapter(List<OrderDetails> detailss, Context context, OnDataChangeListener onDataChangeListener) {
        this.detailss = detailss;
        this.context = context;
        this.onDataChangeListener = onDataChangeListener;
    }

    public interface OnDataChangeListener {
        void onDataChanged();
    }
    @NonNull
    @Override
    public OrderDetailItemAdapter.OrderItemListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item_list, parent, false);
        return new OrderDetailItemAdapter.OrderItemListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailItemAdapter.OrderItemListViewHolder holder, int position) {
        OrderDetails details = detailss.get(position);

        holder.tvSTT.setText(position + 1 + "");
        ProductRepository productRepository = new ProductRepository(context);
        Product product = productRepository.getProduct(details.getProductId());
        holder.tvProductName.setText(product.getProductName());
        holder.tvPrice.setText(details.getPrice() + "");
        holder.tvNumber.setText(details.getQuantity() + "");

        OrderRepository orderRepository = new OrderRepository(context);
        OrderDetailsRepository orderDetailsRepository = new OrderDetailsRepository(context);

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 int quantity = Integer.parseInt(holder.tvNumber.getText().toString());
                 if (quantity > 1) {
                     holder.tvNumber.setText(quantity - 1 + "");
                     details.setQuantity(quantity - 1);
                     orderDetailsRepository.updateOrderDetails(details);
                     Order order = orderRepository.getOrder(details.getOrderId());
                     double totalPrice = order.getTotalPrice();
                     order.setTotalPrice(totalPrice - details.getPrice());
                     orderRepository.updateOrder(order);

                     if (onDataChangeListener != null) {
                         onDataChangeListener.onDataChanged();
                     }
                 }
            }
        });

        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(holder.tvNumber.getText().toString());
                holder.tvNumber.setText(quantity + 1 + "");
                details.setQuantity(quantity + 1);
                orderDetailsRepository.updateOrderDetails(details);
                Order order = orderRepository.getOrder(details.getOrderId());
                double totalPrice = order.getTotalPrice();
                order.setTotalPrice(totalPrice + details.getPrice());
                orderRepository.updateOrder(order);

                if (onDataChangeListener != null) {
                    onDataChangeListener.onDataChanged();
                }
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order order = orderRepository.getOrder(details.getOrderId());
                double totalPrice = order.getTotalPrice();
                order.setTotalPrice(totalPrice - details.getPrice() * details.getQuantity());
                orderRepository.updateOrder(order);

                orderDetailsRepository.deleteById(details.getOrderId(), details.getProductId());

                if (onDataChangeListener != null) {
                    onDataChangeListener.onDataChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount()  {
        return detailss.size();
    }

    public class OrderItemListViewHolder extends RecyclerView.ViewHolder{
        private TextView tvSTT, tvProductName, tvPrice, tvNumber;
        private Button btnMinus, btnPlus, btnDelete;

        @SuppressLint("WrongViewCast")
        public OrderItemListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSTT = itemView.findViewById(R.id.tv_stt);
            tvProductName = itemView.findViewById(R.id.tv_product_name_inOrder);
            tvPrice = itemView.findViewById(R.id.tv_price_view_order);
            tvNumber = itemView.findViewById(R.id.tv_number);
            btnMinus = itemView.findViewById(R.id.btn_minus);
            btnPlus = itemView.findViewById(R.id.btn_plus);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
