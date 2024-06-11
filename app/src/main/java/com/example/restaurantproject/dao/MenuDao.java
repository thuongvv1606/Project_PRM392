package com.example.restaurantproject.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.restaurantproject.bean.Menu;

import java.util.List;

@Dao
public interface MenuDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Menu menu);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Menu menu);

    @Query("SELECT * FROM Menu WHERE menu_id = :menuId")
    Menu select(int menuId);

    @Query("SELECT * FROM Menu")
    List<Menu> selectAll();

    @Query("DELETE FROM Menu")
    void deleteAll();

    @Query("DELETE FROM Menu WHERE menu_id = :menuId")
    void delete(int menuId);
}

