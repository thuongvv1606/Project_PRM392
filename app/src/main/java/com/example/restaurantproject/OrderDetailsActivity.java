package com.example.restaurantproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantproject.adapter.MenuListAdapter;
import com.example.restaurantproject.adapter.OrderDetailItemAdapter;
import com.example.restaurantproject.adapter.OrderDetailsProductListAdapter;
import com.example.restaurantproject.adapter.OrderItemListAdapter;
import com.example.restaurantproject.bean.Account;
import com.example.restaurantproject.bean.Menu;
import com.example.restaurantproject.bean.Order;
import com.example.restaurantproject.bean.OrderDetails;
import com.example.restaurantproject.bean.Product;
import com.example.restaurantproject.bean.Restaurant;
import com.example.restaurantproject.bean.Table;
import com.example.restaurantproject.entity.ProductOrderDTO;
import com.example.restaurantproject.repository.AccountRepository;
import com.example.restaurantproject.repository.OrderDetailsRepository;
import com.example.restaurantproject.repository.OrderRepository;
import com.example.restaurantproject.repository.RestaurantRepository;
import com.example.restaurantproject.repository.TableRepository;
import com.example.restaurantproject.ultils.session.SessionManager;

import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity {
    private TextView txt_customer, txt_table, txt_price, txt_address, txt_note, txt_status;
    private Button btnUpdate, btnList;
    private OrderRepository orderRepository = null;
    private AccountRepository accountRepository = null;
    private TableRepository tableRepository = null;
    private OrderDetailsRepository orderDetailsRepository = null;
    private SessionManager sessionManager = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar_back_order_main);
        // Set the navigation icon click listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailsActivity.this, OrderListActivity.class);
                startActivity(intent);
            }
        });

        txt_customer = findViewById(R.id.txt_customer_name);
        txt_table = findViewById(R.id.txt_restaurant_no);
        txt_price = findViewById(R.id.view_total_price);
        txt_note = findViewById(R.id.edt_order_note);
        txt_status = findViewById(R.id.txt_status);
        txt_address = findViewById(R.id.txt_address);

        orderRepository = new OrderRepository(this);
        accountRepository = new AccountRepository(this);
        tableRepository = new TableRepository(this);
        orderDetailsRepository = new OrderDetailsRepository(this);
        sessionManager = new SessionManager(this);

        Intent intent = getIntent();
        int id = intent.getIntExtra("order_id", -1);
        Order order = orderRepository.getOrder(id);
        Account account = accountRepository.getAccount(order.getCustomerId());
        if (order.getTableID() != null) {
            Table table = tableRepository.getTable(order.getTableID());
            txt_table.setText(table.getName());
        } else {
            txt_table.setText("No table reservation");
        }

        txt_customer.setText(account.getFullname());
        txt_price.setText("" + order.getTotalPrice());
        txt_note.setText(order.getNote());
        txt_address.setText(order.getAddress());
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
        txt_status.setText(status);
        List<OrderDetails> detailsList = orderDetailsRepository.selectAllByOrderId(id);
        setOrderDetailsList(detailsList);

        Button btnCheckout = findViewById(R.id.btn_checkout);

        if (sessionManager.getAccountFromSession().getAccountId() == order.getCustomerId()
            && order.getStatus() == 1) {
            btnCheckout.setVisibility(View.VISIBLE);
        }
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.setStatus(2);
                orderRepository.updateOrder(order);
                Toast.makeText(OrderDetailsActivity.this, "Checkout order successfully!", Toast.LENGTH_LONG).show();
                Intent intent1 = new Intent(OrderDetailsActivity.this, OrderListActivity.class);
                startActivity(intent1);
            }
        });

        btnUpdate = findViewById(R.id.btn_go_order_update);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(OrderDetailsActivity.this, OrderUpdateActivity.class);
                intent2.putExtra("order_id", id);
                startActivity(intent2);
            }
        });

        btnList = findViewById(R.id.btn_back_order_list);
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(OrderDetailsActivity.this, OrderListActivity.class);
                startActivity(intent1);
            }
        });
    }

    private void setOrderDetailsList(List<OrderDetails> detailsList) {
        RecyclerView recyclerView = findViewById(R.id.list_dish_ordered_recycler_view);
        OrderDetailsProductListAdapter orderItemListAdapter = new OrderDetailsProductListAdapter(detailsList, this);
        recyclerView.setAdapter(orderItemListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}