package com.example.restaurantproject.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.restaurantproject.bean.Order;

import java.util.List;

@Dao
public interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Order order);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Order order);

    @Query("SELECT * FROM `Order` WHERE order_id = :orderId")
    Order select(int orderId);

    @Query("SELECT * FROM `Order`")
    List<Order> selectAll();

    @Query("DELETE FROM `Order`")
    void deleteAll();

    @Query("DELETE FROM `Order` WHERE order_id = :orderId")
    void delete(int orderId);
}

