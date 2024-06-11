package com.example.restaurantproject.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.restaurantproject.bean.Account;

import java.util.List;

@Dao
public interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Account account);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Account account);

    @Query("SELECT * FROM Account WHERE account_id = :accountId")
    Account select(int accountId);

    @Query("SELECT * FROM Account")
    List<Account> selectAll();

    @Query("DELETE FROM Account")
    void deleteAll();

    @Query("DELETE FROM Account WHERE account_id = :accountId")
    void delete(int accountId);
}

