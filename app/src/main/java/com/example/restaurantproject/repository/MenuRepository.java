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

    public Menu getByName(String name, int id) {
        return menuDao.getByName(name, id);
    }
    public List<Menu> getAllMenus() {
        return menuDao.selectAll();
    }
    public List<Menu> searchMenus(String searchStr) { return menuDao.search("%" + searchStr + "%");}
    public void deleteMenu(int menuId) {
        menuDao.delete(menuId);
    }

    public void deleteAllMenus() {
        menuDao.deleteAll();
    }
}

