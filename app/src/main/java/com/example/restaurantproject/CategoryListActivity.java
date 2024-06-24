package com.example.restaurantproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantproject.adapter.CategoryListAdapter;
import com.example.restaurantproject.bean.Category;
import com.example.restaurantproject.repository.CategoryRepository;

import java.util.List;

public class CategoryListActivity extends NavigationActivity {
    private CategoryRepository categoryRepository = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setupContentLayout(R.layout.activity_category_list); // Gọi hàm này để chèn layout vào container
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        categoryRepository = new CategoryRepository(this);
        List<Category> categoryList = categoryRepository.getAllCategories();
        setCategoryList(categoryList);
        TextView txtCount = findViewById(R.id.tv_category_count);
        txtCount.setText("Found " + categoryList.size() + " category(ies)");

        Button btnCreateProduct = findViewById(R.id.btn_create_category);
        btnCreateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryListActivity.this, CategoryAddActivity.class);
                startActivity(intent);
            }
        });

        TextView searchStr = findViewById(R.id.edt_search_category);
        Button searchBtn = findViewById(R.id.btn_search_category);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Category> categoryList = categoryRepository.searchCategories(searchStr.getText().toString());
                setCategoryList(categoryList);
            }
        });
    }

    private void setCategoryList(List<Category> categoryList) {
        RecyclerView recyclerView = findViewById(R.id.category_list_recyle_view);
        CategoryListAdapter categoryListAdapter = new CategoryListAdapter(categoryList, this);
        recyclerView.setAdapter(categoryListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}