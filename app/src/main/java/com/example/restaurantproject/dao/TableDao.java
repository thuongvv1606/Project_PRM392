package com.example.restaurantproject.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.restaurantproject.bean.Role;
import com.example.restaurantproject.bean.Table;

import java.util.List;

@Dao
public interface TableDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Table table);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Table table);

    @Query("SELECT * FROM `Table` WHERE table_id = :tableId")
    Table select(int tableId);
    @Query("SELECT * FROM `Table` WHERE restaurant_id = :restaurantId and status = 1")
    List<Table> selectByRestaurant(int restaurantId);

    @Query("SELECT * FROM `Table`")
    List<Table> selectAll();

    @Query("DELETE FROM `Table`")
    void deleteAll();

    @Query("DELETE FROM `Table` WHERE table_id = :tableId")
    void delete(int tableId);

    @Query("Update `Table` SET status = :status where table_id = :tableId")
    void updateStatus(int tableId, int status);
}
