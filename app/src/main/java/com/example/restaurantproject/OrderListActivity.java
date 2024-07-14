package com.example.restaurantproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
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

import java.util.List;

public class OrderListActivity extends NavigationActivity {

    private OrderRepository orderRepository = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentLayout(R.layout.activity_order_list);

        orderRepository = new OrderRepository(this);
        List<OrderDTO> orderList = orderRepository.getAllOrders();
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

//        TextView searchStr = findViewById(R.id.edt_search_order);
//        Button searchBtn = findViewById(R.id.btn_search_order);
//        searchBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                List<Order> orders = orderRepository.(searchStr.getText().toString());
//                setCategoryList(categoryList);
//            }
//        });
    }
    private void setOrderList(List<OrderDTO> orderList) {
        RecyclerView recyclerView = findViewById(R.id.order_list_recyle_view);
        OrderListAdapter orderListAdapter = new OrderListAdapter(orderList, this);
        recyclerView.setAdapter(orderListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}