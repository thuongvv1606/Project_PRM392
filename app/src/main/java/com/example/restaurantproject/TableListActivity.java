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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantproject.adapter.CategoryListAdapter;
import com.example.restaurantproject.adapter.TableListAdapter;
import com.example.restaurantproject.bean.Category;
import com.example.restaurantproject.bean.Table;
import com.example.restaurantproject.repository.CategoryRepository;
import com.example.restaurantproject.repository.TableRepository;

import java.util.List;

public class TableListActivity extends NavigationActivity {
    private TableRepository tableRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setupContentLayout(R.layout.activity_table_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tableRepository = new TableRepository(this);

        List<Table> tableList = tableRepository.getAllTables();
        setTableList(tableList);
        TextView txtCount = findViewById(R.id.tv_table_count);
        txtCount.setText("Found " + tableList.size() + " tables");

        Button btnCreateTable = findViewById(R.id.btn_create_table);
        btnCreateTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TableListActivity.this, TableAddActivity.class);
                startActivity(intent);
            }
        });

//        TextView searchStr = findViewById(R.id.edt_search_category);
//        Button searchBtn = findViewById(R.id.btn_search_category);
//        searchBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                List<Category> categoryList = categoryRepository.searchCategories(searchStr.getText().toString());
//                setCategoryList(categoryList);
//            }
//        });
    }
    private void setTableList(List<Table> tableList) {
        RecyclerView recyclerView = findViewById(R.id.table_list_recyle_view);
        TableListAdapter tableListAdapter = new TableListAdapter(tableList, this);
        recyclerView.setAdapter(tableListAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }
}