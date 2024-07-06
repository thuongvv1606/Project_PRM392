package com.example.restaurantproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.restaurantproject.R;
import com.example.restaurantproject.adapter.MainCategoryAdapter;
import com.example.restaurantproject.adapter.MainProductAdapter;
import com.example.restaurantproject.bean.Category;
import com.example.restaurantproject.bean.Product;
import com.example.restaurantproject.repository.CategoryRepository;
import com.example.restaurantproject.repository.DeliveryRepository;
import com.example.restaurantproject.repository.MenuRepository;
import com.example.restaurantproject.repository.OrderDetailsRepository;
import com.example.restaurantproject.repository.OrderRepository;
import com.example.restaurantproject.repository.ProductRepository;
import com.example.restaurantproject.ultils.session.SessionManager;

import java.util.List;

public class MainCategoryList extends NavigationActivity {
    private CategoryRepository categoryRepository = null;
    private ProductRepository productRepository = null;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setupContentLayout(R.layout.activity_main_category_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        categoryRepository = new CategoryRepository(this);
        productRepository = new ProductRepository(this);
        sessionManager = new SessionManager(this);

        List<Category> categoryList = categoryRepository.getAllCategories();
        setCategoryList(categoryList);

        Intent intent = getIntent();
        int id = intent.getIntExtra("Cate_ID", -1);

        Category category = categoryRepository.getCategory(id);
        ImageView txt_image = findViewById(R.id.category_image_main);
        if (category.getCategoryImage() != null) {
            Glide.with(this).load(category.getCategoryImage()).into(txt_image);
        }
        TextView txt_name = findViewById(R.id.category_name_main);
        txt_name.setText(category.getCategoryName());
        TextView txt_description = findViewById(R.id.category_description_main);
        txt_description.setText(category.getCategoryDescription());

        List<Product> productList = productRepository.getAllProductsInCategory(id);
        setProductList(productList);
        TextView txtCountProduct = findViewById(R.id.tv_product_count);
        txtCountProduct.setVisibility(View.VISIBLE);
        txtCountProduct.setText("Found " + productList.size() + " product(s)");

        ImageView ic_edit = findViewById(R.id.iv_edit_icon);
        if (sessionManager.isLoggedIn() && !(sessionManager.getAccountFromSession().getRoleId() == 4 ||
            sessionManager.getAccountFromSession().getRoleId() == 5))
        {
            ic_edit.setVisibility(View.VISIBLE);
        }
        ic_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainCategoryList.this, CategoryUpdateActivity.class);
                intent1.putExtra("category_id", id);
                startActivity(intent1);
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

    private void setCategoryList(List<Category> categoryList) {
        RecyclerView recyclerView = findViewById(R.id.category_list_main);
        LinearLayoutManager horizontalLayoutManager =
                new LinearLayoutManager(MainCategoryList.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        MainCategoryAdapter categoryListAdapter = new MainCategoryAdapter(categoryList, this);
        recyclerView.setAdapter(categoryListAdapter);
    }
}