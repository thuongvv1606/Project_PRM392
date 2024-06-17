package com.example.restaurantproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.restaurantproject.bean.Delivery;
import com.example.restaurantproject.entity.AccountDTO;
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
import com.example.restaurantproject.ultils.session.SessionManager;

public class MainActivity extends AppCompatActivity {
    private Context context = null;
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

    // Test
    private SessionManager sessionManager;
    private TextView usernameTextView, emailTextView;
    private Button logoutButton, editProfileButton, loginButton, registerButton;

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
        context = this;

        Button database = findViewById(R.id.database);
        Button deleteDatabase = findViewById(R.id.deletedatabase);
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

        deleteDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.deleteDatabase("DemoDatabase");
                Toast.makeText(MainActivity.this,"Delete Database successfully", Toast.LENGTH_SHORT).show();
            }
        });

        sessionManager = new SessionManager(this);
        // Find views
        usernameTextView = findViewById(R.id.usernameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        logoutButton = findViewById(R.id.logoutButton);
        editProfileButton = findViewById(R.id.editProfileButton);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        // Check if user is logged in
        if (sessionManager.isLoggedIn()) {
            // User is logged in, show user info and logout/edit buttons
            AccountDTO loggedInAccount = sessionManager.getAccountFromSession();
            if (loggedInAccount != null) {
                usernameTextView.setText(loggedInAccount.getUsername());
                emailTextView.setText(loggedInAccount.getEmail());
            }
            usernameTextView.setVisibility(View.VISIBLE);
            emailTextView.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.VISIBLE);
            editProfileButton.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
            registerButton.setVisibility(View.GONE);
        } else {
            // User is not logged in, hide user info and logout/edit buttons
            usernameTextView.setVisibility(View.GONE);
            emailTextView.setVisibility(View.GONE);
            logoutButton.setVisibility(View.GONE);
            editProfileButton.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
            registerButton.setVisibility(View.VISIBLE);
        }

        // Set click listeners
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logout user
                sessionManager.deleteAccountFromSession();
                // Reload MainActivity to reflect changes
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ViewProfileActivity
                Intent intent = new Intent(MainActivity.this, ViewProfileActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to LoginActivity
                Intent intent = new Intent(MainActivity.this, UserLoginActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to RegisterActivity
                Intent intent = new Intent(MainActivity.this, UserRegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}