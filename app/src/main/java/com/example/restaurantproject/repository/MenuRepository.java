package com.example.restaurantproject.repository;

import android.content.Context;

import com.example.restaurantproject.bean.Menu;
import com.example.restaurantproject.dao.MenuDao;
import com.example.restaurantproject.dao.RestaurantDatabase;

import java.util.List;

public class MenuRepository {
    private MenuDao menuDao;

    public MenuRepository(Context context) {
        menuDao = RestaurantDatabase.getInstance(context).menuDao();
    }

    public void createMenu(Menu menu) {
        menuDao.insert(menu);
    }

    public void updateMenu(Menu menu) {
        menuDao.update(menu);
    }

    public Menu getMenu(int menuId) {
        return menuDao.select(menuId);
    }

    public List<Menu> getAllMenus() {
        return menuDao.selectAll();
    }

    public void deleteMenu(int menuId) {
        menuDao.delete(menuId);
    }

    public void deleteAllMenus() {
        menuDao.deleteAll();
    }
}

