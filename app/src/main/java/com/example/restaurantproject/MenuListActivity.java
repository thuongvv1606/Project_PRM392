package com.example.restaurantproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantproject.adapter.MenuListAdapter;
import com.example.restaurantproject.bean.Category;
import com.example.restaurantproject.bean.Menu;
import com.example.restaurantproject.repository.CategoryRepository;
import com.example.restaurantproject.repository.MenuRepository;

import java.util.List;

public class MenuListActivity extends NavigationActivity {
    private MenuRepository menuRepository = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setupContentLayout(R.layout.activity_menu_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        menuRepository = new MenuRepository(this);
        List<Menu> menuList = menuRepository.getAllMenus();
        setMenuList(menuList);
        TextView txtCount = findViewById(R.id.tv_menu_count);
        txtCount.setText("Found " + menuList.size() + " menu(s)");

        Button btnCreateProduct = findViewById(R.id.btn_create_menu);
        btnCreateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuListActivity.this, MenuAddActivity.class);
                startActivity(intent);
            }
        });

        TextView searchStr = findViewById(R.id.edt_search_menu);
        Button searchBtn = findViewById(R.id.btn_search_menu);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Menu> menuList = menuRepository.searchMenus(searchStr.getText().toString());
                setMenuList(menuList);
            }
        });
    }

    private void setMenuList(List<Menu> menuList) {
        RecyclerView recyclerView = findViewById(R.id.menu_list_recycle_view);
        MenuListAdapter menuListAdapter = new MenuListAdapter(menuList, this);
        recyclerView.setAdapter(menuListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}