package com.example.restaurantproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.restaurantproject.adapter.BannerAdapter;
import com.example.restaurantproject.adapter.MainCategoryAdapter;
import com.example.restaurantproject.bean.Category;
import com.example.restaurantproject.entity.AccountDTO;
import com.example.restaurantproject.repository.AccountRepository;
import com.example.restaurantproject.repository.CategoryRepository;
import com.example.restaurantproject.repository.DeliveryRepository;
import com.example.restaurantproject.repository.MenuRepository;
import com.example.restaurantproject.repository.OrderDetailsRepository;
import com.example.restaurantproject.repository.OrderRepository;
import com.example.restaurantproject.repository.ProductRepository;
import com.example.restaurantproject.repository.RestaurantRepository;
import com.example.restaurantproject.repository.RoleRepository;
import com.example.restaurantproject.ultils.session.SessionManager;

import java.util.List;

public class MainActivity extends NavigationActivity {
    private Context context = null;
    private AccountRepository accountRepository = null;
    private CategoryRepository categoryRepository = null;
    private DeliveryRepository deliveryRepository = null;
    private MenuRepository menuRepository = null;
    private OrderDetailsRepository orderDetailsRepository = null;
    private OrderRepository orderRepository = null;
    private ProductRepository productRepository = null;
    private RestaurantRepository restaurantRepository = null;
    private RoleRepository roleRepository = null;

    // Test
    private SessionManager sessionManager;
    private TextView usernameTextView, emailTextView;
    private Button logoutButton, editProfileButton, loginButton, registerButton;
    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private int[] images = {R.drawable.banner_image1, R.drawable.banner_image2, R.drawable.banner_image3};
    private int currentPage = 0;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setupContentLayout(R.layout.activity_main);
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
        restaurantRepository = new RestaurantRepository(this);
        roleRepository = new RoleRepository(this);
        context = this;
        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.dots_layout);

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
//                bookingRepository.getAllReservations();
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

        BannerAdapter bannerAdapter = new BannerAdapter(this, images);
        viewPager.setAdapter(bannerAdapter);

        addDotsIndicator(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                addDotsIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                if (currentPage == images.length) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 3000); // Chuyển đổi ảnh mỗi 3 giây
            }
        };

        List<Category> categoryList = categoryRepository.getAllCategories();
        setCategoryList(categoryList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 3000); // Khởi động chuyển đổi ảnh ban đầu
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable); // Dừng chuyển đổi ảnh khi Activity không còn hoạt động
    }

    private void addDotsIndicator(int position) {
        ImageView[] dots = new ImageView[images.length];
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageResource(R.drawable.dot_inactive);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            dotsLayout.addView(dots[i], params);
        }
        dots[position].setImageResource(R.drawable.dot_active);
    }

    private void setCategoryList(List<Category> categoryList) {
        RecyclerView recyclerView = findViewById(R.id.category_list_main);
        LinearLayoutManager horizontalLayoutManager =
                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        MainCategoryAdapter categoryListAdapter = new MainCategoryAdapter(categoryList, this);
        recyclerView.setAdapter(categoryListAdapter);
    }
}