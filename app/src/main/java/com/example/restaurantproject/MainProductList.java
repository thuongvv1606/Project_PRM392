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

import com.example.restaurantproject.adapter.MainProductAdapter;
import com.example.restaurantproject.bean.Category;
import com.example.restaurantproject.bean.Menu;
import com.example.restaurantproject.bean.Product;
import com.example.restaurantproject.bean.Restaurant;
import com.example.restaurantproject.repository.ProductRepository;

import java.util.List;

public class MainProductList extends NavigationActivity {
    private ProductRepository productRepository = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setupContentLayout(R.layout.activity_main_product_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        productRepository = new ProductRepository(this);

        List<Product> productList = productRepository.getAllProducts();
        setProductList(productList);
        TextView txtCountProduct = findViewById(R.id.tv_product_count);
        txtCountProduct.setText("Found " + productList.size() + " product(s)");

        Button searchBtn = findViewById(R.id.btn_home_search);
        EditText searchEdt = findViewById(R.id.edt_home_search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Product> productList = productRepository.searchProduct(searchEdt.getText().toString());
                setProductList(productList);
                TextView txtCountProduct = findViewById(R.id.tv_product_count);
                txtCountProduct.setText("Found " + productList.size() + " product(s)");
            }
        });
    }

    private void setProductList(List<Product> productList) {
        RecyclerView recyclerView = findViewById(R.id.product_list_main);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        MainProductAdapter mainProductAdapter = new MainProductAdapter(productList, this);
        recyclerView.setAdapter(mainProductAdapter);
    }
}