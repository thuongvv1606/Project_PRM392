package com.example.restaurantproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantproject.R;
import com.example.restaurantproject.adapter.CategoryListAdapter;
import com.example.restaurantproject.adapter.OrderListAdapter;
import com.example.restaurantproject.bean.Category;
import com.example.restaurantproject.bean.Order;
import com.example.restaurantproject.entity.OrderDTO;
import com.example.restaurantproject.repository.OrderRepository;
import com.example.restaurantproject.ultils.session.SessionManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class OrderListActivity extends NavigationActivity {
    private OrderRepository orderRepository = null;
    private SessionManager sessionManager = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentLayout(R.layout.activity_order_list);

        orderRepository = new OrderRepository(this);
        sessionManager = new SessionManager(this);

        List<Order> orderList = new ArrayList<>();
        if (sessionManager.getAccountFromSession().getRoleId() != 4
            || sessionManager.getAccountFromSession().getRoleId() != 5) {
            orderList = orderRepository.getAll(sessionManager.getAccountFromSession().getAccountId(), "%%");
        }
        else if (sessionManager.getAccountFromSession().getRoleId() == 4) {
            orderList = orderRepository.getAllOfAccount(sessionManager.getAccountFromSession().getAccountId(), "%%");
        }
        setOrderList(orderList);
        TextView txtCount = findViewById(R.id.tv_order_count);
        if (orderList.size() == 0) txtCount.setText("Not found any Orders");
        else if (orderList.size() == 0) {
            txtCount.setText("Found 1 order");
        }
        else txtCount.setText("Found " + orderList.size() + " orders");

//        Button btnCreateProduct = findViewById(R.id.btn_create_order);
//        btnCreateProduct.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(OrderListActivity.this, OrderAddActivity.class);
//                startActivity(intent);
//            }
//        });

        TextView searchStr = findViewById(R.id.edt_search_order);
        Button searchBtn = findViewById(R.id.btn_search_order);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Order> orders = new ArrayList<>();
                if (sessionManager.getAccountFromSession().getRoleId() != 4
                        || sessionManager.getAccountFromSession().getRoleId() != 5) {
                    orders = orderRepository.getAll(sessionManager.getAccountFromSession().getAccountId(), "%" + searchStr.getText().toString() + "%");
                }
                else if (sessionManager.getAccountFromSession().getRoleId() == 4) {
                    orders = orderRepository.getAllOfAccount(sessionManager.getAccountFromSession().getAccountId(), "%" + searchStr.getText().toString() + "%");
                }
                setOrderList(orders);
            }
        });

        Button reservaionButton = findViewById(R.id.btn_create_reservation);
        reservaionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderListActivity.this, ReservationActivity.class);
                startActivity(intent);
            }
        });
    }
    private void setOrderList(List<Order> orderList) {
        RecyclerView recyclerView = findViewById(R.id.order_list_recyle_view);
        OrderListAdapter orderListAdapter = new OrderListAdapter(orderList, this);
        recyclerView.setAdapter(orderListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}