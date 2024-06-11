package com.example.restaurantproject.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.restaurantproject.bean.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Category category);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Category category);

    @Query("SELECT * FROM Category WHERE category_id = :categoryId")
    Category select(int categoryId);

    @Query("SELECT * FROM Category")
    List<Category> selectAll();

    @Query("DELETE FROM Category")
    void deleteAll();

    @Query("DELETE FROM Category WHERE category_id = :categoryId")
    void delete(int categoryId);
}

