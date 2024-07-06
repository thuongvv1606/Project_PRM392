package com.example.restaurantproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.restaurantproject.MainMenuDetail;
import com.example.restaurantproject.MainProductDetail;
import com.example.restaurantproject.MenuListActivity;
import com.example.restaurantproject.R;
import com.example.restaurantproject.UserLoginActivity;
import com.example.restaurantproject.bean.Order;
import com.example.restaurantproject.bean.OrderDetails;
import com.example.restaurantproject.bean.Product;
import com.example.restaurantproject.bean.Restaurant;
import com.example.restaurantproject.repository.MenuRepository;
import com.example.restaurantproject.repository.OrderDetailsRepository;
import com.example.restaurantproject.repository.OrderRepository;
import com.example.restaurantproject.ultils.session.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainProductAdapter extends RecyclerView.Adapter<MainProductAdapter.ProductViewHolder>{
    private SessionManager sessionManager;
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

        int id = product.getProductId();

        // Set the spannableString to the TextView
        holder.tvProductName.setText("" + product.getProductName());
        holder.tvProductPrice.setText("" + product.getPrice() + " VND");
        if (product.getProductImage() != null && !product.getProductImage().isEmpty()) {
            Glide.with(context).load(product.getProductImage()).into(holder.tvProductImage);
        }

        holder.btProductDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainProductDetail.class);
                intent.putExtra("Product_ID", id);
                context.startActivity(intent);
            }
        });

        holder.btProductDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainProductDetail.class);
                intent.putExtra("Product_ID", id);
                context.startActivity(intent);
            }
        });

        holder.btOrder.setOnClickListener(v -> {
            sessionManager = new SessionManager(context);

            if (!sessionManager.isLoggedIn()) {
                Intent intent = new Intent(context, UserLoginActivity.class);
                context.startActivity(intent);
                Toast.makeText(context, "You must login before order!", Toast.LENGTH_SHORT).show();
            }
            OrderRepository orderRepository = new OrderRepository(context);
            OrderDetailsRepository orderDetailsRepository = new OrderDetailsRepository(context);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Order");
            builder.setMessage("Are you sure you want to add to cart product " + product.getProductName() + "?")
                    .setPositiveButton("OK", (dialog, which) -> {
                        Order order = orderRepository.selectUserNewest(sessionManager.getAccountFromSession().getAccountId());
                        if (order == null) {
                            Calendar calendar = Calendar.getInstance();
                            Date date = calendar.getTime();

                            // Format date
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                            String currentDate = dateFormat.format(date);
                            order = new Order(0.0, currentDate, sessionManager.getAccountFromSession().getAccountId(), 1, true);
                            orderRepository.createOrder(order);
                            order = orderRepository.selectUserNewest(sessionManager.getAccountFromSession().getAccountId());
                        }

                        OrderDetails orderDetails = orderDetailsRepository.selectAllByOrderIdAndProductId(order.getOrderId(), product.getProductId());
                        if (orderDetails == null) {
                            OrderDetails od = new OrderDetails(product.getProductId(), order.getOrderId(), product.getPrice(), 1);
                            orderDetailsRepository.createOrderDetails(od);
                        } else {
                            int quantity = orderDetails.getQuantity() + 1;
                            orderDetails.setQuantity(quantity);
                            orderDetailsRepository.updateOrderDetails(orderDetails);
                        }

                        double totalPrice = order.getTotalPrice() + product.getPrice();
                        order.setTotalPrice(totalPrice);
                        orderRepository.updateOrder(order);

                        Toast.makeText(context, "Add to cart product " + product.getProductName() + " successfully!", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{
        private TextView tvProductName, tvProductPrice;
        private ImageView tvProductImage;
        private Button btProductDetail, btOrder;

        @SuppressLint("WrongViewCast")
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.product_name);
            tvProductPrice = itemView.findViewById(R.id.product_price);
            tvProductImage = itemView.findViewById(R.id.product_image);
            btProductDetail = itemView.findViewById(R.id.btn_product_details);
            btOrder = itemView.findViewById(R.id.btn_product_order);
        }
    }
}
