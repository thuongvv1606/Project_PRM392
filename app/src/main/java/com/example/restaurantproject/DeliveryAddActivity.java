package com.example.restaurantproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.restaurantproject.adapter.OrderListAdapter;
import com.example.restaurantproject.adapter.ProductListAdapter;
import com.example.restaurantproject.bean.Account;
import com.example.restaurantproject.bean.Delivery;
import com.example.restaurantproject.bean.Order;
import com.example.restaurantproject.bean.OrderDetails;
import com.example.restaurantproject.bean.Product;
import com.example.restaurantproject.bean.Restaurant;
import com.example.restaurantproject.repository.AccountRepository;
import com.example.restaurantproject.repository.DeliveryRepository;
import com.example.restaurantproject.repository.OrderDetailsRepository;
import com.example.restaurantproject.repository.OrderRepository;
import com.example.restaurantproject.repository.ProductRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeliveryAddActivity extends AppCompatActivity {
    private OrderRepository orderRepository = null;
    private OrderDetailsRepository orderDetailsRepository = null;
    private AccountRepository accountRepository = null;
    private DeliveryRepository deliveryRepository = null;
    private ArrayAdapter<String> orderAdapter;
    private Map<String, Integer> orderMap = new HashMap<>();
    private Spinner spinner;
    private TextView txt_customer, txt_price;
    private EditText txt_address, txt_note;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delivery_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar_delivery_list);
        // Set the navigation icon click listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeliveryAddActivity.this, DeliveryListActivity.class);
                startActivity(intent);
            }
        });

        orderRepository = new OrderRepository(this);
        orderDetailsRepository = new OrderDetailsRepository(this);
        accountRepository = new AccountRepository(this);
        deliveryRepository = new DeliveryRepository(this);
        txt_address = findViewById(R.id.txt_address);
        txt_customer = findViewById(R.id.txt_customer_name);
        txt_price = findViewById(R.id.txt_total_price);
        txt_note = findViewById(R.id.txt_note);
        spinner = findViewById(R.id.spinner_delivery_order);

        populateOrderSpinner();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Lấy item được chọn
                String selectedItem = parent.getItemAtPosition(position).toString();
                int selectedOrderId = orderMap.get(selectedItem);

                if (selectedOrderId != -1) {
                    Order order = orderRepository.getOrder(selectedOrderId);
                    Account account = accountRepository.getAccount(order.getCustomerId());
                    List<OrderDetails> orderDetails = orderDetailsRepository.selectAllByOrderId(selectedOrderId);
                    setOrderList(orderDetails);

                    txt_address.setText(order.getAddress());
                    txt_customer.setText(account.getFullname());
                    txt_price.setText(order.getTotalPrice() + "");
                }
                else {
                    setOrderList(new ArrayList<>());
                    txt_address.setText("");
                    txt_customer.setText("");
                    txt_price.setText("0");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btnAdd = findViewById(R.id.btn_add_delivery);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedOrderName = (String) spinner.getSelectedItem();
                int selectedOrderId = orderMap.get(selectedOrderName);
                if (selectedOrderId == -1) {
                    Toast.makeText(DeliveryAddActivity.this, "Please select an order.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String address = txt_address.getText().toString();
                if (address.trim().isEmpty()) {
                    txt_address.setError("Address can not be empty!");
                    return;
                }

                Delivery delivery = new Delivery();
                delivery.setOrderId(selectedOrderId);
                delivery.setStatus(2);
                Account account = accountRepository.getDelivery();
                if (account != null) {
                    delivery.setDelivererId(account.getAccountId());
                }
                delivery.setNote(txt_note.getText().toString());
                deliveryRepository.createDelivery(delivery);

                Order order1 = orderRepository.getOrder(selectedOrderId);
                order1.setAddress(address);
                order1.setStatus(5);
                orderRepository.updateOrder(order1);

                Toast.makeText(DeliveryAddActivity.this, "Create delivery successfully!", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(DeliveryAddActivity.this, DeliveryListActivity.class);
                startActivity(intent1);
            }
        });

        Button btnCancel = findViewById(R.id.btn_cancel_add_delivery);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(DeliveryAddActivity.this, DeliveryListActivity.class);
                startActivity(intent1);
            }
        });
    }

    private void populateOrderSpinner() {
        // Fetch restaurant data
        List<Order> orderList = orderRepository.getAllNotDeliveredOrder();
        List<String> orderNames = new ArrayList<>();

        orderNames.add("----- Choose order -----");
        orderMap.put("----- Choose order -----", -1);

        // Populate the map and the list of names
        for (Order order : orderList) {
            orderNames.add("Order " + order.getOrderId());
            orderMap.put("Order " + order.getOrderId(), order.getOrderId());
        }

        // Create an adapter for the spinner
        orderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, orderNames);
        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(orderAdapter);
    }

    private void setOrderList(List<OrderDetails> orderList) {
        RecyclerView recyclerView = findViewById(R.id.list_dish_ordered_recycler_view);
        OrderDetailsProductListAdapter orderDetailItemAdapter = new OrderDetailsProductListAdapter(orderList, this);
        recyclerView.setAdapter(orderDetailItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}