package com.example.restaurantproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantproject.adapter.RestaurantListAdapter;
import com.example.restaurantproject.bean.Restaurant;
import com.example.restaurantproject.repository.RestaurantRepository;

import java.util.List;

public class RestaurantListActivity extends AppCompatActivity {

    private RestaurantListAdapter restaurantListAdapter = null;
    private RestaurantRepository restaurantRepository = null;
    private List<Restaurant> restaurantList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurant_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        restaurantRepository = new RestaurantRepository(this);
        restaurantList = restaurantRepository.getAllRestaurants();
        setRestaurantList(restaurantList);
        TextView txtCount = findViewById(R.id.tv_restaurant_count);
        txtCount.setText("Found " + restaurantList.size() + " restaurant(ies)");

        Button btnCreate = findViewById(R.id.btn_create_restaurant);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantListActivity.this, RestaurantAddActivity.class);
                startActivity(intent);
            }
        });

        TextView searchStr = findViewById(R.id.edt_search_restaurant);
        Button searchBtn = findViewById(R.id.btn_search_restaurant);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = searchStr.getText().toString().trim();
                if (!searchQuery.isEmpty()) {
                    restaurantList = restaurantRepository.getAllBySearch(searchQuery);
                } else {
                    restaurantList = restaurantRepository.getAllRestaurants();
                }
                setRestaurantList(restaurantList);
                TextView txtCount = findViewById(R.id.tv_restaurant_count);
                txtCount.setText("Found " + restaurantList.size() + " restaurant(ies)");
            }
        });
    }

    private void setRestaurantList(List<Restaurant> restaurantList) {
        RecyclerView recyclerView = findViewById(R.id.restaurant_list_recycle_view);
        restaurantListAdapter = new RestaurantListAdapter(restaurantList, this);
        recyclerView.setAdapter(restaurantListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}