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
import com.example.restaurantproject.bean.Menu;
import com.example.restaurantproject.bean.Product;
import com.example.restaurantproject.bean.Restaurant;
import com.example.restaurantproject.repository.CategoryRepository;
import com.example.restaurantproject.repository.MenuRepository;
import com.example.restaurantproject.repository.ProductRepository;
import com.example.restaurantproject.repository.RestaurantRepository;

public class ProductDetailsActivity extends AppCompatActivity {
    private TextView txt_name, txt_description, txt_category, txt_menu, txt_price;
    private ImageView txt_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar = findViewById(R.id.toolbar_product_list);
        // Set the navigation icon click listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailsActivity.this, ProductListActivity.class);
                startActivity(intent);
            }
        });

        txt_name = findViewById(R.id.txt_product_name);
        txt_category = findViewById(R.id.txt_category_name);
        txt_menu = findViewById(R.id.txt_menu_name);
        txt_price = findViewById(R.id.txt_product_price);
        txt_image = findViewById(R.id.txt_product_image);
        txt_description = findViewById(R.id.txt_product_description);

        Intent intent = getIntent();
        int id = intent.getIntExtra("product_id", -1);
        ProductRepository productRepository = new ProductRepository(this);
        Product product = productRepository.getProduct(id);
        txt_name.setText(product.getProductName());
        txt_price.setText("" + product.getPrice());
        txt_description.setText(product.getProductDescription());

        CategoryRepository categoryRepository = new CategoryRepository(this);
        Category category = categoryRepository.getCategory(product.getCategoryId());
        String categoryName = category != null ? category.getCategoryName() : "Unknown";

        txt_category.setText(categoryName);

        MenuRepository menuRepository = new MenuRepository(this);
        Menu menu = menuRepository.getMenu(product.getMenuId());
        String menuName = menu != null ? menu.getMenuName() : "Unknown";

        txt_menu.setText(menuName);

        if (product.getProductName() != null) {
            Glide.with(this).load(product.getProductImage()).into(txt_image);
        }
        Button toUpdateBtn = findViewById(R.id.btn_toupdate_product);
        toUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailsActivity.this, CategoryUpdateActivity.class);
                intent.putExtra("product_id", id);
                startActivity(intent);
            }
        });

        Button toListBtn = findViewById(R.id.btn_tolist_product);
        toListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailsActivity.this, ProductListActivity.class);
                startActivity(intent);
            }
        });
    }
}