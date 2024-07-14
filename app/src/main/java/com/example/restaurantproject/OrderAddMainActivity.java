package com.example.restaurantproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OrderAddMainActivity extends AppCompatActivity {
    private Button btnOrder, btnOrderDelivery, btnReservaiton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_add_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar_back_page);
        // Set the navigation icon click listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderAddMainActivity.this, OrderListActivity.class);
                startActivity(intent);
            }
        });

        btnOrder = findViewById(R.id.btn_order);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderAddMainActivity.this, OrderAddActivity.class);
                startActivity(intent);
            }
        });
        btnReservaiton = findViewById(R.id.btn_reservation);
        btnReservaiton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderAddMainActivity.this, ReservationActivity.class);
                startActivity(intent);
            }
        });

        btnOrderDelivery = findViewById(R.id.btn_order_delivery);

        btnOrderDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderAddMainActivity.this, OrderDeliveryActivity.class);
                startActivity(intent);
            }

        });
    }
}