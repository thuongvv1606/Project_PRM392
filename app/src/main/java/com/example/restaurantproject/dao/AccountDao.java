package com.example.restaurantproject.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.restaurantproject.bean.Account;
import com.example.restaurantproject.entity.AccountDTO;

import java.util.List;

@Dao
public interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Account account);

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

    // Login

    @Query("SELECT a.account_id AS accountId, a.username AS username, a.password AS password, " +
            "a.fullname AS fullname, a.email AS email, a.phone_number AS phoneNumber, " +
            "a.address AS address, a.role_id AS roleId, a.restaurant_id AS restaurantId, " +
            "a.status AS status, a.avatar AS avatar, r.role_name AS roleName, rt.restaurant_name AS restaurantName " +
            "FROM Account a " +
            "JOIN Role r ON a.role_id = r.role_id " +
            "LEFT JOIN Restaurant rt ON a.restaurant_id = rt.restaurant_id " +
            "WHERE a.username = :username AND a.password = :password")
    AccountDTO checkLogin(String username, String password);



    // Register

    @Query("SELECT COUNT(*) FROM Account WHERE username = :username")
    int checkUsernameExists(String username);

    @Query("SELECT COUNT(*) FROM Account WHERE email = :email")
    int checkEmailExists(String email);

    // Forgot Pass
    @Query("SELECT * FROM Account WHERE email = :email")
    Account findAccountByEmail(String email);


    // AccountList -> List<AccountDTO>
    @Query("SELECT a.account_id AS accountId, a.username AS username, a.password AS password, " +
            "a.fullname AS fullname, a.email AS email, a.phone_number AS phoneNumber, " +
            "a.address AS address, a.role_id AS roleId, a.restaurant_id AS restaurantId, " +
            "a.status AS status, a.avatar AS avatar, r.role_name AS roleName, rt.restaurant_name AS restaurantName " +
            "FROM Account a " +
            "JOIN Role r ON a.role_id = r.role_id " +
            "LEFT JOIN Restaurant rt ON a.restaurant_id = rt.restaurant_id")
    List<AccountDTO> selectAllAccountDTO();

    @Query("SELECT a.account_id AS accountId, a.username AS username, a.password AS password, " +
            "a.fullname AS fullname, a.email AS email, a.phone_number AS phoneNumber, " +
            "a.address AS address, a.role_id AS roleId, a.restaurant_id AS restaurantId, " +
            "a.status AS status, a.avatar AS avatar, r.role_name AS roleName, rt.restaurant_name AS restaurantName " +
            "FROM Account a " +
            "JOIN Role r ON a.role_id = r.role_id " +
            "LEFT JOIN Restaurant rt ON a.restaurant_id = rt.restaurant_id " +
            "WHERE username like :searchStr")
    List<AccountDTO> searchAccountByUserName(String searchStr);
}

