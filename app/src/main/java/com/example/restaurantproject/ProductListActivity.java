package com.example.restaurantproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantproject.adapter.ProductListAdapter;
import com.example.restaurantproject.bean.Product;
import com.example.restaurantproject.repository.ProductRepository;

import java.util.List;


public class ProductListActivity extends NavigationActivity  {
    private ProductRepository productRepository = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setupContentLayout(R.layout.activity_product_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        productRepository = new ProductRepository(this);
        List<Product> productList = productRepository.getAllProducts();
        setProductList(productList);
        TextView txtCount = findViewById(R.id.tv_product_count);
        txtCount.setText("Found " + productList.size() + " product(s)");

        Button btnCreateProduct = findViewById(R.id.btn_create_product);
        btnCreateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductListActivity.this, ProductAddActivity.class);
                startActivity(intent);
            }
        });

        TextView searchStr = findViewById(R.id.edt_search_product);
        Button searchBtn = findViewById(R.id.btn_search_product);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Product> productList = productRepository.searchProduct(searchStr.getText().toString());
                setProductList(productList);
            }
        });
    }

    private void setProductList(List<Product> productList) {
        RecyclerView recyclerView = findViewById(R.id.product_list_recycle_view);
        ProductListAdapter productListAdapter = new ProductListAdapter(productList, this);
        recyclerView.setAdapter(productListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}