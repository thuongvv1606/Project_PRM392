package com.example.restaurantproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.restaurantproject.bean.Category;
import com.example.restaurantproject.bean.Menu;
import com.example.restaurantproject.bean.Restaurant;
import com.example.restaurantproject.repository.CategoryRepository;
import com.example.restaurantproject.repository.MenuRepository;
import com.example.restaurantproject.repository.RestaurantRepository;

public class MenuDetailsActivity extends AppCompatActivity {
    private TextView txt_name, txt_description, txt_restaurant;
    private ImageView txt_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar = findViewById(R.id.toolbar_menu_list);
        // Set the navigation icon click listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuDetailsActivity.this, MenuListActivity.class);
                startActivity(intent);
            }
        });

        txt_name = findViewById(R.id.txt_menu_name);
        txt_restaurant = findViewById(R.id.txt_restaurant_name);
        txt_image = findViewById(R.id.txt_menu_image);
        txt_description = findViewById(R.id.txt_menu_description);

        Intent intent = getIntent();
        int id = intent.getIntExtra("menu_id", -1);
        MenuRepository menuRepository = new MenuRepository(this);
        Menu menu = menuRepository.getMenu(id);
        txt_name.setText(menu.getMenuName());
        txt_description.setText(menu.getMenuDescription());

        RestaurantRepository restaurantRepository = new RestaurantRepository(this);
        Restaurant restaurant = restaurantRepository.getRestaurant(menu.getRestaurantId());
        String restaurantName = restaurant != null ? restaurant.getRestaurantName() : "Unknown";

        txt_restaurant.setText(restaurantName);

        if (menu.getMenuImage() != null) {
            Glide.with(this).load(menu.getMenuImage()).into(txt_image);
        }

        Button toUpdateBtn = findViewById(R.id.btn_toupdate_menu);
        toUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuDetailsActivity.this, MenuUpdateActivity.class);
                intent.putExtra("menu_id", id);
                startActivity(intent);
            }
        });
        Button toListBtn = findViewById(R.id.btn_tolist_menu);
        toListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuDetailsActivity.this, MenuListActivity.class);
                startActivity(intent);
            }
        });
    }
}