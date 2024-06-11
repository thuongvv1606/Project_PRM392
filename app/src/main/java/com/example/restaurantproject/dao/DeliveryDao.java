package com.example.restaurantproject.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.restaurantproject.bean.Delivery;

import java.util.List;

@Dao
public interface DeliveryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Delivery delivery);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Delivery delivery);

    @Query("SELECT * FROM Delivery WHERE delivery_id = :deliveryId")
    Delivery select(int deliveryId);

    @Query("SELECT * FROM Delivery")
    List<Delivery> selectAll();

    @Query("DELETE FROM Delivery")
    void deleteAll();

    @Query("DELETE FROM Delivery WHERE delivery_id = :deliveryId")
    void delete(int deliveryId);
}

