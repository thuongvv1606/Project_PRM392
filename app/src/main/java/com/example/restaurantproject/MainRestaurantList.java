package com.example.restaurantproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantproject.adapter.MainRestaurantAdapter;
import com.example.restaurantproject.bean.Category;
import com.example.restaurantproject.bean.Menu;
import com.example.restaurantproject.bean.Product;
import com.example.restaurantproject.bean.Restaurant;
import com.example.restaurantproject.repository.ProductRepository;
import com.example.restaurantproject.repository.RestaurantRepository;

import java.util.List;

public class MainRestaurantList extends NavigationActivity {
    private RestaurantRepository restaurantRepository = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setupContentLayout(R.layout.activity_main_restaurant_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        restaurantRepository = new RestaurantRepository(this);

        List<Restaurant> restaurantList = restaurantRepository.getAllRestaurants();
        setRestaurantList(restaurantList);
        TextView txtCountRestaurant = findViewById(R.id.tv_restaurant_count);
        txtCountRestaurant.setVisibility(View.VISIBLE);
        txtCountRestaurant.setText("Found " + restaurantList.size() + " restaurant(s)");

        Button searchBtn = findViewById(R.id.btn_home_search);
        EditText searchEdt = findViewById(R.id.edt_home_search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Restaurant> restaurantList = restaurantRepository.getAllBySearch(searchEdt.getText().toString());
                setRestaurantList(restaurantList);
                TextView txtCountRestaurant = findViewById(R.id.tv_restaurant_count);
                txtCountRestaurant.setText("Found " + restaurantList.size() + " restaurant(s)");
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
}