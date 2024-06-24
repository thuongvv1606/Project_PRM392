package com.example.restaurantproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.restaurantproject.bean.Restaurant;
import com.example.restaurantproject.repository.RestaurantRepository;

public class RestaurantDetailActivity extends AppCompatActivity {

    private TextView txt_name, txt_description, txt_phone, txt_address, txt_email;
    private ImageView imageView;
    private Uri imageUri;
    private RestaurantRepository restaurantRepository = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurant_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        restaurantRepository = new RestaurantRepository(this);
        txt_name = findViewById(R.id.txt_restaurant_detail_name);
        txt_email = findViewById(R.id.txt_restaurant_detail_email);
        txt_phone = findViewById(R.id.txt_restaurant_detail_phone);
        txt_address = findViewById(R.id.txt_restaurant_detail_address);
        txt_description = findViewById(R.id.txt_restaurant_detail_description);
        imageView = findViewById(R.id.img_restaurant_detail_image);

        Intent intent = getIntent();
        int id = intent.getIntExtra("restaurant_id", -1);
        Restaurant restaurant = restaurantRepository.getRestaurant(id);
        txt_name.setText(restaurant.getRestaurantName());
        txt_email.setText(restaurant.getEmail());
        txt_phone.setText(restaurant.getPhoneNumber());
        txt_address.setText(restaurant.getAddress());
        txt_description.setText(restaurant.getRestaurantDescription());

        // Load ảnh đại diện từ URL
        if (restaurant.getRestaurantImage()  != null && !restaurant.getRestaurantImage() .isEmpty()) {
            if (restaurant.getRestaurantImage() .startsWith("content://")) {
                Glide.with(this).load(Uri.parse(restaurant.getRestaurantImage())).into(imageView);
            } else {
                Glide.with(this).load(restaurant.getRestaurantImage()).into(imageView);
            }
        }

        Button btnGotoUpdate = findViewById(R.id.btn_goto_update_restaurant);
        btnGotoUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantDetailActivity.this, RestaurantUpdateActivity.class);
                intent.putExtra("restaurant_id", id);
                startActivity(intent);
            }
        });

        Button btnGotoList = findViewById(R.id.btn_goto_list_restaurant);
        btnGotoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}