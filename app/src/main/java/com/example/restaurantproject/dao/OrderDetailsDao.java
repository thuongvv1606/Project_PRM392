package com.example.restaurantproject.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.restaurantproject.bean.OrderDetails;

import java.util.List;

@Dao
public interface OrderDetailsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(OrderDetails orderDetails);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(OrderDetails orderDetails);

//    @Query("SELECT * FROM OrderDetails WHERE orderdetails_id = :orderDetailsId")
//    OrderDetails select(int orderDetailsId);

    @Query("SELECT * FROM OrderDetails")
    List<OrderDetails> selectAll();

    @Query("DELETE FROM OrderDetails")
    void deleteAll();

//    @Query("DELETE FROM OrderDetails WHERE orderdetails_id = :orderDetailsId")
//    void delete(int orderDetailsId);
}

