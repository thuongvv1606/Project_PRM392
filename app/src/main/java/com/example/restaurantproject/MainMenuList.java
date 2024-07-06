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

import com.example.restaurantproject.adapter.MainMenuAdapter;
import com.example.restaurantproject.bean.Category;
import com.example.restaurantproject.bean.Menu;
import com.example.restaurantproject.bean.Product;
import com.example.restaurantproject.bean.Restaurant;
import com.example.restaurantproject.repository.MenuRepository;

import java.util.List;

public class MainMenuList extends NavigationActivity {
    private MenuRepository menuRepository = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setupContentLayout(R.layout.activity_main_menu_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        menuRepository = new MenuRepository(this);

        List<Menu> menuList = menuRepository.getAllMenus();
        setMenuList(menuList);
        TextView txtCountMenu = findViewById(R.id.tv_menu_count);
        txtCountMenu.setText("Found " + menuList.size() + " menu(s)");

        Button searchBtn = findViewById(R.id.btn_home_search);
        EditText searchEdt = findViewById(R.id.edt_home_search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Menu> menuList = menuRepository.searchMenus(searchEdt.getText().toString());
                setMenuList(menuList);
                TextView txtCountMenu = findViewById(R.id.tv_menu_count);
                txtCountMenu.setText("Found " + menuList.size() + " menu(s)");
            }
        });
    }

    private void setMenuList(List<Menu> menuList) {
        RecyclerView recyclerView = findViewById(R.id.menu_list_main);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        MainMenuAdapter mainMenuAdapter = new MainMenuAdapter(menuList, this);
        recyclerView.setAdapter(mainMenuAdapter);
    }
}