package com.example.restaurantproject;

import android.annotation.SuppressLint;
import android.content.Intent;
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

public class RestaurantDetailActivity extends AppCompatActivity {

    private TextView txt_name, txt_description, txt_phone, txt_address, txt_email;
    private ImageView iv_image;
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

        txt_name = findViewById(R.id.txt_restaurant_detail_name);
        txt_email = findViewById(R.id.txt_restaurant_detail_email);
        txt_phone = findViewById(R.id.txt_restaurant_detail_phone);
        txt_address = findViewById(R.id.txt_restaurant_detail_address);
        txt_description = findViewById(R.id.txt_restaurant_detail_description);
        iv_image = findViewById(R.id.img_restaurant_detail_image);

        Intent intent = getIntent();
        int id = intent.getIntExtra("restaurant_id", -1);
        String name = intent.getStringExtra("restaurant_name");
        txt_name.setText(name);
        String email = intent.getStringExtra("restaurant_email");
        txt_email.setText(email);
        String phone = intent.getStringExtra("restaurant_phone_number");
        txt_phone.setText(phone);
        String address = intent.getStringExtra("restaurant_address");
        txt_address.setText(address);
        String description = intent.getStringExtra("restaurant_description");
        txt_description.setText(description);
        String image = intent.getStringExtra("restaurant_image");
        if (!image.isEmpty()) {
            @SuppressLint("DiscouragedApi") int imageResId = getResources().getIdentifier(image, "drawable", getPackageName());
            if (imageResId != 0) {
                iv_image.setImageResource(imageResId);
            } else {
                iv_image.setImageResource(R.drawable.img_error);
            }
        } else {
            iv_image.setImageResource(R.drawable.img_error);
        }

        Button btnGotoUpdate = findViewById(R.id.btn_goto_update_restaurant);
        btnGotoUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantDetailActivity.this, RestaurantUpdateActivity.class);
                intent.putExtra("restaurant_id", id);
                intent.putExtra("restaurant_name", name);
                intent.putExtra("restaurant_description", description);
                intent.putExtra("restaurant_email", email);
                intent.putExtra("restaurant_phone_number", phone);
                intent.putExtra("restaurant_address", address);
                intent.putExtra("restaurant_image", image);
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