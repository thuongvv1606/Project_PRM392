package com.example.restaurantproject;

import android.content.Intent;
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
import com.example.restaurantproject.bean.Restaurant;
import com.example.restaurantproject.bean.Table;
import com.example.restaurantproject.repository.CategoryRepository;
import com.example.restaurantproject.repository.RestaurantRepository;
import com.example.restaurantproject.repository.TableRepository;
import com.example.restaurantproject.ultils.constant.Common;

public class TableDetailActivity extends AppCompatActivity {
    private TextView txt_name, txt_status, txt_restaurant, txt_num_seat;
    private ImageView txt_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_table_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar_table_list_update);
        // Set the navigation icon click listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TableDetailActivity.this, TableListActivity.class);
                startActivity(intent);
            }
        });

        txt_name = findViewById(R.id.tv_table_add_name);
        txt_image = findViewById(R.id.iv_table_image);
        txt_status = findViewById(R.id.tv_table_status);
        txt_restaurant = findViewById(R.id.tv_table_restaurant_add);
        txt_num_seat = findViewById(R.id.tv_table_number_seat);

        Intent intent = getIntent();
        int id = intent.getIntExtra("table_id", -1);
        TableRepository tableRepository = new TableRepository(this);
        Table table = tableRepository.getTable(id);

        txt_name.setText(table.getName());
        RestaurantRepository restaurantRepository = new RestaurantRepository(this);
        Restaurant restaurant = restaurantRepository.getRestaurant(table.getRestaurantId());
        txt_restaurant.setText(restaurant.getRestaurantName());
        txt_num_seat.setText(table.getSeatNum()+"");
        txt_status.setText(Common.tableStatus.get(table.getStatus()-1));

        if (table.getTableImage() != null || table.getTableImage() != "") {
            Glide.with(this).load(table.getTableImage()).into(txt_image);
        }

        Button toUpdateBtn = findViewById(R.id.btn_update_table);
        toUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TableDetailActivity.this, TableUpdateActivity.class);
                intent.putExtra("table_id", id);
                startActivity(intent);
            }
        });
    }
}