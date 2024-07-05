package com.example.restaurantproject.repository;

import android.content.Context;

import com.example.restaurantproject.bean.Menu;
import com.example.restaurantproject.bean.Product;
import com.example.restaurantproject.bean.Restaurant;
import com.example.restaurantproject.dao.ProductDao;
import com.example.restaurantproject.dao.RestaurantDatabase;

import java.util.List;

public class ProductRepository {
    private ProductDao productDao;

    public ProductRepository(Context context) {
        productDao = RestaurantDatabase.getInstance(context).productDao();
    }

    public void createProduct(Product product) {
        productDao.insert(product);
    }

    public void updateProduct(Product product) {
        productDao.update(product);
    }

    public Product getProduct(int productId) {
        return productDao.select(productId);
    }

    public List<Product> getAllProducts() {
        return productDao.selectAll();
    }

    public void deleteProduct(int productId) {
        productDao.delete(productId);
    }

    public void deleteAllProducts() {
        productDao.deleteAll();
    }

    public Product getByName(String name, int id) {
        return productDao.getByName(name, id);
    }

    public List<Product> searchProduct(String searchStr) { return productDao.search("%" + searchStr + "%");}
    public List<Product> getTopProducts() {
        return productDao.selectTop();
    }

    public List<Product> getProductByMenu(int menuId) { return productDao.getProductByMenu(menuId);}
}

