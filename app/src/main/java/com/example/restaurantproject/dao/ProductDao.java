package com.example.restaurantproject.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.restaurantproject.bean.Product;

import java.util.List;

@Dao
public interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Product product);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Product product);

    @Query("SELECT * FROM Product WHERE product_id = :productId")
    Product select(int productId);

    @Query("SELECT * FROM Product")
    List<Product> selectAll();

    @Query("DELETE FROM Product")
    void deleteAll();

    @Query("DELETE FROM Product WHERE product_id = :productId")
    void delete(int productId);
}

