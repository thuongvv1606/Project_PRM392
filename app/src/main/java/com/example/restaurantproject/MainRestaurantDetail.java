package com.example.restaurantproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.restaurantproject.adapter.MainMenuAdapter;
import com.example.restaurantproject.adapter.MainProductAdapter;
import com.example.restaurantproject.adapter.MainRestaurantAdapter;
import com.example.restaurantproject.bean.Menu;
import com.example.restaurantproject.bean.Product;
import com.example.restaurantproject.bean.Restaurant;
import com.example.restaurantproject.repository.MenuRepository;
import com.example.restaurantproject.repository.OrderDetailsRepository;
import com.example.restaurantproject.repository.OrderRepository;
import com.example.restaurantproject.repository.ProductRepository;
import com.example.restaurantproject.repository.RestaurantRepository;
import com.example.restaurantproject.ultils.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class MainRestaurantDetail extends NavigationActivity {
    private MenuRepository menuRepository = null;
    private ProductRepository productRepository = null;
    private RestaurantRepository restaurantRepository = null;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setupContentLayout(R.layout.activity_main_restaurant_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        menuRepository = new MenuRepository(this);
        productRepository = new ProductRepository(this);
        restaurantRepository = new RestaurantRepository(this);
        sessionManager = new SessionManager(this);

        Intent intent = getIntent();
        int id = intent.getIntExtra("Restaurant_ID", -1);
        Restaurant restaurant = restaurantRepository.getRestaurant(id);
        TextView txt_name = findViewById(R.id.restaurant_name_detail);
        txt_name.setText(restaurant.getRestaurantName());
        TextView txt_description = findViewById(R.id.restaurant_description_detail);
        txt_description.setText(restaurant.getRestaurantDescription());
        TextView txt_address = findViewById(R.id.restaurant_address_detail);
        txt_address.setText(restaurant.getAddress());
        TextView txt_email = findViewById(R.id.restaurant_email_detail);
        txt_email.setText(restaurant.getEmail());
        TextView txt_phone = findViewById(R.id.restaurant_phone_detail);
        txt_phone.setText(restaurant.getPhoneNumber());
        ImageView txt_image = findViewById(R.id.restaurant_image_detail);
        if (restaurant.getRestaurantImage() != null) {
            Glide.with(this).load(restaurant.getRestaurantImage()).into(txt_image);
        }

        List<Restaurant> restaurantList = restaurantRepository.getOtherRestaurants(restaurant.getRestaurantId());
        setRestaurantList(restaurantList);
        TextView txtCountRestaurant = findViewById(R.id.tv_restaurant_count);
        txtCountRestaurant.setText("Found " + restaurantList.size() + " restaurant(s)");

        List<Menu> menuList = menuRepository.getMenusInRestaurant(restaurant.getRestaurantId(),-1);
        setMenuList(menuList);
        TextView txtCountMenu = findViewById(R.id.tv_menu_count);
        txtCountMenu.setText("Found " + menuList.size() + " menu(s)");

        List<Product> productList = new ArrayList<>();
        if (menuList.size() > 0) {
            for (Menu menu: menuList) {
                List<Product> products = productRepository.getProductsInMenu(menu.getMenuId(), -1);
                productList.addAll(products);
            }
        }
        setProductList(productList);
        TextView txtCountProduct = findViewById(R.id.tv_product_count);
        txtCountProduct.setText("Found " + productList.size() + " product(s)");

        ImageView ic_edit = findViewById(R.id.iv_edit_icon);
        if (sessionManager.isLoggedIn() && (sessionManager.getAccountFromSession().getRoleId() == 1 ||
                ((sessionManager.getAccountFromSession().getRoleId() == 2 || sessionManager.getAccountFromSession().getRoleId() == 3)
                        && sessionManager.getAccountFromSession().getRestaurantId() == restaurant.getRestaurantId())))
        {
            ic_edit.setVisibility(View.VISIBLE);
        }
        ic_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainRestaurantDetail.this, RestaurantDetailActivity.class);
                intent.putExtra("restaurant_id", restaurant.getRestaurantId());
                startActivity(intent);
            }
        });
    }

    private void setRestaurantList(List<Restaurant> restaurantList) {
        RecyclerView recyclerView = findViewById(R.id.restaurant_list_main);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        MainRestaurantAdapter mainMenuAdapter = new MainRestaurantAdapter(restaurantList, this);
        recyclerView.setAdapter(mainMenuAdapter);
    }

    private void setMenuList(List<Menu> menuList) {
        RecyclerView recyclerView = findViewById(R.id.menu_list_main);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        MainMenuAdapter mainMenuAdapter = new MainMenuAdapter(menuList, this);
        recyclerView.setAdapter(mainMenuAdapter);
    }

    private void setProductList(List<Product> productList) {
        RecyclerView recyclerView = findViewById(R.id.product_list_main);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        MainProductAdapter mainProductAdapter = new MainProductAdapter(productList, this);
        recyclerView.setAdapter(mainProductAdapter);
    }
}