package com.example.restaurantproject.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.restaurantproject.bean.Role;

import java.util.List;

@Dao
public interface RoleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Role role);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Role role);

    @Query("SELECT * FROM Role WHERE role_id = :roleId")
    Role select(int roleId);

    @Query("SELECT * FROM Role")
    List<Role> selectAll();

    @Query("DELETE FROM Role")
    void deleteAll();

    @Query("DELETE FROM Role WHERE role_id = :roleId")
    void delete(int roleId);
}

