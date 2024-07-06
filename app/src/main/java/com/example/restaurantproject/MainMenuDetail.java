package com.example.restaurantproject;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.restaurantproject.bean.Menu;
import com.example.restaurantproject.bean.Product;
import com.example.restaurantproject.bean.Restaurant;
import com.example.restaurantproject.repository.MenuRepository;
import com.example.restaurantproject.repository.ProductRepository;
import com.example.restaurantproject.repository.RestaurantRepository;

import java.util.List;

public class MainMenuDetail extends NavigationActivity {
    private MenuRepository menuRepository = null;
    private ProductRepository productRepository = null;
    private RestaurantRepository restaurantRepository = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setupContentLayout(R.layout.activity_main_menu_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        menuRepository = new MenuRepository(this);
        productRepository = new ProductRepository(this);
        restaurantRepository = new RestaurantRepository(this);

        Intent intent = getIntent();
        int id = intent.getIntExtra("Menu_ID", -1);
        Menu menu = menuRepository.getMenu(id);
        TextView txt_name = findViewById(R.id.menu_name_detail);
        txt_name.setText(menu.getMenuName());
        TextView txt_description = findViewById(R.id.menu_description_detail);
        txt_description.setText(menu.getMenuDescription());
        ImageView txt_image = findViewById(R.id.menu_image_detail);
        if (menu.getMenuImage() != null) {
            Glide.with(this).load(menu.getMenuImage()).into(txt_image);
        }

        Restaurant restaurant = restaurantRepository.getRestaurant(menu.getRestaurantId());
        TextView r_name = findViewById(R.id.r_menu_name_detail);
        r_name.setText(restaurant.getRestaurantName());
        TextView r_address = findViewById(R.id.r_menu_address_detail);
        r_address.setText(restaurant.getAddress());
        TextView r_description = findViewById(R.id.r_menu_description_detail);
        r_description.setText(restaurant.getRestaurantDescription());
        ImageView r_image = findViewById(R.id.r_menu_image_detail);
        if (restaurant.getRestaurantImage() != null) {
            Glide.with(this).load(restaurant.getRestaurantImage()).into(r_image);
        }

        List<Menu> menuList = menuRepository.getMenusInRestaurant(menu.getRestaurantId(), id);
        setMenuList(menuList);
        TextView txtCountMenu = findViewById(R.id.tv_menu_count);
        txtCountMenu.setText("Found " + menuList.size() + " menu(s)");

        List<Product> productList = productRepository.getProductsInMenu(id);
        setProductList(productList);
        TextView txtCountProduct = findViewById(R.id.tv_product_count);
        txtCountProduct.setText("Found " + productList.size() + " product(s)");
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