package com.example.restaurantproject.repository;

import android.content.Context;

import com.example.restaurantproject.bean.Restaurant;
import com.example.restaurantproject.dao.RestaurantDao;
import com.example.restaurantproject.dao.RestaurantDatabase;

import java.util.List;

public class RestaurantRepository {
    private RestaurantDao restaurantDao;

    public RestaurantRepository(Context context) {
        restaurantDao = RestaurantDatabase.getInstance(context).restaurantDao();
    }

    public void createRestaurant(Restaurant restaurant) {
        restaurantDao.insert(restaurant);
    }

    public void updateRestaurant(Restaurant restaurant) {
        restaurantDao.update(restaurant);
    }

    public Restaurant getRestaurant(int restaurantId) {
        return restaurantDao.select(restaurantId);
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantDao.selectAll();
    }

    public void deleteRestaurant(int restaurantId) {
        restaurantDao.delete(restaurantId);
    }

    public void deleteAllRestaurants() {
        restaurantDao.deleteAll();
    }
}

