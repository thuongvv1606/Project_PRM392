package com.example.restaurantproject.repository;

import android.content.Context;

import com.example.restaurantproject.bean.Category;
import com.example.restaurantproject.dao.CategoryDao;
import com.example.restaurantproject.dao.RestaurantDatabase;

import java.util.List;

public class CategoryRepository {
    private CategoryDao categoryDao;

    public CategoryRepository(Context context) {
        categoryDao = RestaurantDatabase.getInstance(context).categoryDao();
    }

    public void createCategory(Category category) {
        categoryDao.insert(category);
    }

    public void updateCategory(Category category) {
        categoryDao.update(category);
    }

    public Category getCategory(int categoryId) {
        return categoryDao.select(categoryId);
    }

    public Category getByName(String name, int id) {
        return categoryDao.getByName(name, id);
    }

    public List<Category> getAllCategories() {
        return categoryDao.selectAll();
    }

    public List<Category> searchCategories(String searchStr) { return categoryDao.search("%" + searchStr + "%");}

    public void deleteCategory(int categoryId) { categoryDao.delete(categoryId); }

    public void deleteAllCategories() { categoryDao.deleteAll(); }
}

