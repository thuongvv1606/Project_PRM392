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
    @Query("SELECT * FROM Menu WHERE menu_name = :name AND menu_id != :id")
    Menu getByName(String name, int id);
    @Query("SELECT * FROM Menu WHERE restaurant_id = :id")
    List<Menu> getByRestaurant(int id);
    @Query("SELECT * FROM Menu")
    List<Menu> selectAll();
    @Query("SELECT * FROM Menu WHERE menu_name LIKE '%' || :searchStr || '%' OR restaurant_id LIKE '%' || :searchStr || '%'")
    List<Menu> search(String searchStr);
    @Query("DELETE FROM Menu")
    void deleteAll();

    @Query("DELETE FROM Menu WHERE menu_id = :menuId")
    void delete(int menuId);
}

