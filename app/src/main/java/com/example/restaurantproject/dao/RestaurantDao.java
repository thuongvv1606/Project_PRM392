package com.example.restaurantproject.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.restaurantproject.bean.Restaurant;

import java.util.List;

@Dao
public interface RestaurantDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Restaurant restaurant);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Restaurant restaurant);

    @Query("SELECT * FROM Restaurant WHERE restaurant_id = :restaurantId")
    Restaurant select(int restaurantId);

    @Query("SELECT * FROM Restaurant")
    List<Restaurant> selectAll();

    @Query("DELETE FROM Restaurant")
    void deleteAll();

    @Query("DELETE FROM Restaurant WHERE restaurant_id = :restaurantId")
    void delete(int restaurantId);
}
