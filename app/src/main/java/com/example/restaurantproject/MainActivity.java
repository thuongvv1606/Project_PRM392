package com.example.restaurantproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.restaurantproject.bean.Delivery;
import com.example.restaurantproject.repository.AccountRepository;
import com.example.restaurantproject.repository.CategoryRepository;
import com.example.restaurantproject.repository.DeliveryRepository;
import com.example.restaurantproject.repository.MenuRepository;
import com.example.restaurantproject.repository.OrderDetailsRepository;
import com.example.restaurantproject.repository.OrderRepository;
import com.example.restaurantproject.repository.ProductRepository;
import com.example.restaurantproject.repository.ReservationRepository;
import com.example.restaurantproject.repository.RestaurantRepository;
import com.example.restaurantproject.repository.RoleRepository;

public class MainActivity extends AppCompatActivity {

    private AccountRepository accountRepository = null;
    private CategoryRepository categoryRepository = null;
    private DeliveryRepository deliveryRepository = null;
    private MenuRepository menuRepository = null;
    private OrderDetailsRepository orderDetailsRepository = null;
    private OrderRepository orderRepository = null;
    private ProductRepository productRepository = null;
    private ReservationRepository reservationRepository = null;
    private RestaurantRepository restaurantRepository = null;
    private RoleRepository roleRepository = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        accountRepository = new AccountRepository(this);
        categoryRepository = new CategoryRepository(this);
        deliveryRepository = new DeliveryRepository(this);
        menuRepository = new MenuRepository(this);
        orderRepository = new OrderRepository(this);
        orderDetailsRepository = new OrderDetailsRepository(this);
        productRepository = new ProductRepository(this);
        reservationRepository = new ReservationRepository(this);
        restaurantRepository = new RestaurantRepository(this);
        roleRepository = new RoleRepository(this);

        Button database = findViewById(R.id.database);
        database.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountRepository.getAllAccounts();
                categoryRepository.getAllCategories();
                deliveryRepository.getAllDeliveries();
                menuRepository.getAllMenus();
                orderRepository.getAllOrders();
                orderDetailsRepository.getAllOrderDetails();
                productRepository.getAllProducts();
                reservationRepository.getAllReservations();
                restaurantRepository.getAllRestaurants();
                roleRepository.getAllRoles();
                Toast.makeText(MainActivity.this,"Database successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
}