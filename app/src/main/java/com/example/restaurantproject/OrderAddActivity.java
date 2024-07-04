package com.example.restaurantproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantproject.adapter.OrderItemListAdapter;
import com.example.restaurantproject.adapter.ProductOrderAdapter;
import com.example.restaurantproject.bean.Order;
import com.example.restaurantproject.bean.OrderDetails;
import com.example.restaurantproject.bean.Product;
import com.example.restaurantproject.bean.Restaurant;
import com.example.restaurantproject.bean.Table;
import com.example.restaurantproject.entity.ProductOrderDTO;
import com.example.restaurantproject.repository.OrderDetailsRepository;
import com.example.restaurantproject.repository.OrderRepository;
import com.example.restaurantproject.repository.ProductRepository;
import com.example.restaurantproject.repository.RestaurantRepository;
import com.example.restaurantproject.repository.TableRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderAddActivity extends AppCompatActivity {
    private EditText edtCustomerName, edtNote;
    private Spinner spnRestaurant, spnTable;
    private RecyclerView rcvOrderItem;
    private List<Product> lstproduct;
    private TextView tvTotalPrice;
    private Button btnAddMore, btnOrder, btnCancel;
    private RestaurantRepository restaurantRepository;
    private TableRepository tableRepository;
    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private OrderDetailsRepository orderDetailsRepository;
    private ArrayAdapter<String> restaurantAdapter;
    private ArrayAdapter<String> tableAdapter;
    private Map<String, Integer> restaurantMap = new HashMap<>();
    private Map<String, Integer> tableMap = new HashMap<>();
    private HashMap<Integer, Integer> orderItemList;
    private List<ProductOrderDTO> orderDTOList;
    private ActivityResultLauncher activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if(o.getResultCode() == 2){
                        Intent data = o.getData();
                        orderItemList = (HashMap<Integer, Integer>) data.getSerializableExtra("orderItemList");
                        viewListOrderItem();
                    }
                }
            }
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_add);
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
                Intent intent = new Intent(OrderAddActivity.this, OrderAddMainActivity.class);
                startActivity(intent);
            }
        });

        orderItemList = new HashMap<>();
        orderDTOList = new ArrayList<>();
        // Initialize repositories
        restaurantRepository = new RestaurantRepository(this);
        tableRepository = new TableRepository(this);
        productRepository = new ProductRepository(this);
        orderRepository = new OrderRepository(this);
        orderDetailsRepository = new OrderDetailsRepository(this);

        edtCustomerName = findViewById(R.id.edt_customer_name);
        edtNote = findViewById(R.id.edt_order_note);

        spnRestaurant = findViewById(R.id.spn_restaurant);
        populateRestaurantSpinner();

        spnTable = findViewById(R.id.spn_number_table);
        populateTableSpinner();

        viewListOrderItem();

        tvTotalPrice = findViewById(R.id.view_total_price);

        // Set up button click listener
        btnAddMore = findViewById(R.id.btn_add_more_order_item);
        btnAddMore.setOnClickListener(v -> addMoreItem());
        
        btnOrder = findViewById(R.id.btn_add_order);
        btnOrder.setOnClickListener(v -> addOrder());

        btnCancel = findViewById(R.id.btn_cancel_add_order);
        btnCancel.setOnClickListener(v -> finish());
    }
    private double getTotalPrice(){
        double total = 0;
        for (ProductOrderDTO item : orderDTOList) {
            total += item.getNumber() * item.getPrice();
        }
        return total;
    }
    @Override
    protected void onResume() {
        super.onResume();
        tvTotalPrice.setText(getTotalPrice()+"");
    }
    private void viewListOrderItem() {
        rcvOrderItem = findViewById(R.id.list_dish_ordered_recycler_view);
        if (orderItemList != null){
            orderItemList.forEach((productId, quantity) -> {
                Product p = productRepository.getProduct(productId);
                orderDTOList.add(new ProductOrderDTO(p, quantity));
            });
        }
        OrderItemListAdapter orderItemListAdapter = new OrderItemListAdapter(orderDTOList, this);
        rcvOrderItem.setAdapter(orderItemListAdapter);
        rcvOrderItem.setLayoutManager(new LinearLayoutManager(this));
    }

    private int getRestaurantId(){
        String selectedRestaurantName = (String) spnRestaurant.getSelectedItem();
        if (selectedRestaurantName == null || selectedRestaurantName.isEmpty()) {
            Toast.makeText(this, "Please select a restaurant", Toast.LENGTH_SHORT).show();
            return 0;
        }

        return restaurantMap.get(selectedRestaurantName);
    }
    private void addMoreItem() {
        int selectedRestaurantId = getRestaurantId();
        Intent intent = new Intent(OrderAddActivity.this, MenuOrderActivity.class);
        intent.putExtra("restaurant_id", selectedRestaurantId);
        if (orderItemList != null || !orderItemList.isEmpty()){
            intent.putExtra("orderItemList", orderItemList);
        }
        activityResultLauncher.launch(intent);
    }

    private void addOrder() {
        Order order = new Order();

        order.setTotalPrice(getTotalPrice());
        order.setOrderDate(LocalDateTime.now()+"");
        order.setCustomerId(1);
        order.setStatus(1);
        order.setPayment(false);
        order.setAddress("HaNoi");
        order.setTableID(1);
        order.setNoOfPeople(3);
        order.setReservationDate(LocalDateTime.now()+"");
        order.setNote(edtNote.getText().toString());

        if (orderRepository != null && orderDetailsRepository != null){
            orderRepository.createOrder(order);
            int orderId = orderRepository.getOrderNewest().getOrderId();
            for (ProductOrderDTO item:orderDTOList) {
                OrderDetails orderDetails = new OrderDetails();

                orderDetails.setOrderId(orderId);
                orderDetails.setProductId(item.getProductId());
                orderDetails.setPrice(item.getPrice());
                orderDetails.setQuantity(item.getNumber());

                orderDetailsRepository.createOrderDetails(orderDetails);
            }
            Toast.makeText(OrderAddActivity.this, "Add category successfully",
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(OrderAddActivity.this, OrderListActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(OrderAddActivity.this, "Order/OrderDetail repository is not initialized", Toast.LENGTH_LONG).show();
        }
    }

    private void populateTableSpinner() {
        int selectedRestaurantId = getRestaurantId();

        // Fetch table data
        List<Table> tableList = tableRepository.getTableByRestaurant(selectedRestaurantId);
        List<String> tableNames = new ArrayList<>();

        // Populate the map and the list of names
        for (Table item : tableList) {
            tableNames.add(item.getName());
            tableMap.put(item.getName(), item.getRestaurantId());
        }

        // Create an adapter for the spinner
        tableAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tableNames);
        tableAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTable.setAdapter(tableAdapter);
    }

    private void populateRestaurantSpinner() {
        // Fetch restaurant data
        List<Restaurant> restaurantList = restaurantRepository.getAllRestaurants();
        List<String> restaurantNames = new ArrayList<>();

        // Populate the map and the list of names
        for (Restaurant restaurant : restaurantList) {
            restaurantNames.add(restaurant.getRestaurantName());
            restaurantMap.put(restaurant.getRestaurantName(), restaurant.getRestaurantId());
        }

        // Create an adapter for the spinner
        restaurantAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, restaurantNames);
        restaurantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnRestaurant.setAdapter(restaurantAdapter);
    }
}