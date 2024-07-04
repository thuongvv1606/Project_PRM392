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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantproject.adapter.MenuListAdapter;
import com.example.restaurantproject.adapter.MenuOrderListAdapter;
import com.example.restaurantproject.adapter.ProductOrderAdapter;
import com.example.restaurantproject.bean.Menu;
import com.example.restaurantproject.bean.Product;
import com.example.restaurantproject.entity.OrderViewModel;
import com.example.restaurantproject.repository.MenuRepository;
import com.example.restaurantproject.repository.ProductRepository;

import java.util.HashMap;
import java.util.List;

public class MenuOrderActivity extends AppCompatActivity implements ProductOrderAdapter.OnProductAddListener {
    private MenuRepository menuRepository = null;
    private OrderViewModel orderViewModel;
    private HashMap<Integer, Integer> orderItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        Intent intent = getIntent();
        int restaurant_id = intent.getIntExtra("restaurant_id", -1);
        orderItemList = (HashMap<Integer, Integer>) intent.getSerializableExtra("orderItemList");
        orderViewModel.setOrderMap(orderItemList);

        menuRepository = new MenuRepository(this);
        List<Menu> menuList = menuRepository.getMenusByRestaurant(restaurant_id);
        setMenuList(menuList);

        TextView tvNumItem = findViewById(R.id.toolbar_text_view_number_items);
        orderViewModel.getOrderMap().observe(this, orderMap -> {
            orderItemList = orderMap;
            tvNumItem.setText(orderItemList.size() + " items");
        });

        Button btnOrder = findViewById(R.id.btn_order_in_menu);
        btnOrder.setOnClickListener(v -> {
            intent.putExtra("orderItemList", orderItemList);
            setResult(2, intent);
            finish();
        });
    }

    private void setMenuList(List<Menu> menuList) {
        RecyclerView recyclerView = findViewById(R.id.list_menu_recycler_view);
        MenuOrderListAdapter menuListAdapter = new MenuOrderListAdapter(menuList, this, this);
        recyclerView.setAdapter(menuListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onProductAdded(Product product) {
        orderViewModel.addProduct(product);
    }
}