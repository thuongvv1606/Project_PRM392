package com.example.restaurantproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

import com.example.restaurantproject.adapter.OrderDetailItemAdapter;
import com.example.restaurantproject.adapter.OrderDetailsProductListAdapter;
import com.example.restaurantproject.bean.Account;
import com.example.restaurantproject.bean.Category;
import com.example.restaurantproject.bean.Menu;
import com.example.restaurantproject.bean.Order;
import com.example.restaurantproject.bean.OrderDetails;
import com.example.restaurantproject.bean.Table;
import com.example.restaurantproject.repository.AccountRepository;
import com.example.restaurantproject.repository.MenuRepository;
import com.example.restaurantproject.repository.OrderDetailsRepository;
import com.example.restaurantproject.repository.OrderRepository;
import com.example.restaurantproject.repository.TableRepository;

import java.util.ArrayList;
import java.util.List;

public class OrderUpdateActivity extends AppCompatActivity implements OrderDetailItemAdapter.OnDataChangeListener
{
    private TextView txt_customer, txt_table, txt_price, txt_address, txt_note;
    private Spinner spinner;
    private Button btnUpdate, btnList;
    private OrderRepository orderRepository = null;
    private AccountRepository accountRepository = null;
    private TableRepository tableRepository = null;
    private OrderDetailsRepository orderDetailsRepository = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_update);
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
                Intent intent = new Intent(OrderUpdateActivity.this, OrderListActivity.class);
                startActivity(intent);
            }
        });

        txt_customer = findViewById(R.id.txt_customer_name);
        txt_table = findViewById(R.id.txt_restaurant_no);
        txt_price = findViewById(R.id.view_total_price);
        txt_note = findViewById(R.id.edt_order_note);
        txt_address = findViewById(R.id.txt_address);
        spinner = findViewById(R.id.edit_spinner_status);

        orderRepository = new OrderRepository(this);
        accountRepository = new AccountRepository(this);
        tableRepository = new TableRepository(this);
        orderDetailsRepository = new OrderDetailsRepository(this);

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
        txt_note.setText("" + order.getNote());
        txt_address.setText(order.getAddress());

        List<String> statusList = new ArrayList<>();
        statusList.add("Pending");
        statusList.add("Confirmed");
        statusList.add("In preparation");
        statusList.add("Ready");
        statusList.add("Out for delivery");
        statusList.add("Completed");
        statusList.add("Canceled");
        statusList.add("Refunded");
        // Set up the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        // Select the current status in the spinner
        int currentStatus = order.getStatus();
        spinner.setSelection(currentStatus - 1);
        List<OrderDetails> detailsList = orderDetailsRepository.selectAllByOrderId(id);
        Button addMoreBtn = findViewById(R.id.btn_add_more_order_item);
        if (order.getStatus() != 1) {
            setOrderDetailsList(detailsList);
        } else {
            setOrderDetailsConfirmList(detailsList);
            addMoreBtn.setVisibility(View.VISIBLE);
        }

        addMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(OrderUpdateActivity.this, MainProductList.class);
                startActivity(intent1);
            }
        });

        btnUpdate = findViewById(R.id.btn_update_order);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update status based on spinner selection
                int selectedPositionC = spinner.getSelectedItemPosition();
                order.setStatus(selectedPositionC + 1);
                Order order1 = orderRepository.getOrder(id);
                order.setTotalPrice(order1.getTotalPrice());
                order.setAddress(txt_address.getText().toString());
                orderRepository.updateOrder(order);
                Toast.makeText(OrderUpdateActivity.this, "Update order successfully!", Toast.LENGTH_LONG).show();
                Intent intent2 = new Intent(OrderUpdateActivity.this, OrderListActivity.class);
                startActivity(intent2);
            }
        });

        btnList = findViewById(R.id.btn_cancel_order);
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(OrderUpdateActivity.this, OrderListActivity.class);
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

    private void setOrderDetailsConfirmList(List<OrderDetails> detailsList) {
        RecyclerView recyclerView = findViewById(R.id.list_dish_ordered_recycler_view);
        OrderDetailItemAdapter orderItemListAdapter = new OrderDetailItemAdapter(detailsList, this, this);
        recyclerView.setAdapter(orderItemListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onDataChanged() {
        orderRepository = new OrderRepository(this);

        Intent intent = getIntent();
        int id = intent.getIntExtra("order_id", -1);
        Order order = orderRepository.getOrder(id);

        txt_price.setText("" + order.getTotalPrice());

        List<OrderDetails> detailsList = orderDetailsRepository.selectAllByOrderId(id);
        if (order.getStatus() != 1) {
            setOrderDetailsList(detailsList);
        } else {
            setOrderDetailsConfirmList(detailsList);
        }
    }
}