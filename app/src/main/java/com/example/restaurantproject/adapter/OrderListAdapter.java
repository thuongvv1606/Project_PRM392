package com.example.restaurantproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantproject.OrderDetailsActivity;
import com.example.restaurantproject.OrderListActivity;
import com.example.restaurantproject.OrderUpdateActivity;
import com.example.restaurantproject.R;
import com.example.restaurantproject.bean.Account;
import com.example.restaurantproject.bean.Order;
import com.example.restaurantproject.entity.OrderDTO;
import com.example.restaurantproject.repository.AccountRepository;
import com.example.restaurantproject.repository.OrderRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderViewHolder> {
    private List<Order> orders = null;
    private Context context;
    public  OrderListAdapter(List<Order> orders, Context context){
        this.context = context;
        this.orders = orders;
    }
    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_list_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        AccountRepository accountRepository = new AccountRepository(context);

        String id = "" + order.getOrderId();
        // Create a SpannableString with UnderlineSpan
        SpannableString spannableString = new SpannableString(id);
        spannableString.setSpan(new UnderlineSpan(), 0, id.length(), 0);

        // Set the spannableString to the TextView
        holder.tvOrderID.setText(spannableString);
        Account customer = accountRepository.getAccount(order.getCustomerId());
        holder.tvCustomerName.setText("" + customer.getFullname());
        holder.tvOrderDate.setText("" + order.getOrderDate());
        String status = "Pending";
        if (order.getStatus() == 2) {
            status = "Confirmed";
        } else if (order.getStatus() == 3) {
            status = "In preparation";
        } else if (order.getStatus() == 4) {
            status = "Ready";
        } else if (order.getStatus() == 5) {
            status = "Out for delivery";
        } else if (order.getStatus() == 6) {
            status = "Completed";
        } else if (order.getStatus() == 7) {
            status = "Canceled";
        } else if (order.getStatus() == 8) {
            status = "Refunded";
        }
        holder.tvStatus.setText(status);

        holder.tvOrderID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderDetailsActivity.class);
                intent.putExtra("order_id", order.getOrderId());
                context.startActivity(intent);
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderUpdateActivity.class);
                intent.putExtra("order_id", order.getOrderId());
                context.startActivity(intent);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderRepository orderRepository = new OrderRepository(context);
                Order orderDelete = orderRepository.getOrder(Integer.parseInt(id));
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Order");
                builder.setMessage("Are you sure you want to delete order " + orderDelete.getOrderId() + "?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                orderRepository.deleteOrder(Integer.parseInt(id));
                                Toast.makeText(context, "Delete Order successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, OrderListActivity.class);
                                context.startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder{
        private TextView tvOrderID, tvCustomerName, tvOrderDate, tvStatus;
        private ImageButton btnEdit, btnDelete;

        @SuppressLint("WrongViewCast")
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderID = itemView.findViewById(R.id.tv_order_id);
            tvCustomerName = itemView.findViewById(R.id.tv_customer_name);
            tvOrderDate = itemView.findViewById(R.id.tv_order_date);
            tvStatus = itemView.findViewById(R.id.tv_status);
            btnEdit = itemView.findViewById(R.id.btn_edit_order);
            btnDelete = itemView.findViewById(R.id.btn_delete_order);
        }
    }
}
