package com.example.restaurantproject.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.restaurantproject.bean.Order;
import com.example.restaurantproject.entity.OrderDTO;

import java.util.List;

@Dao
public interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Order order);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Order order);

    @Query("SELECT " +
            "*" +
            " FROM `Order`" +
            " ORDER BY order_id DESC" +
            " LIMIT 1")
    Order selectNewest();

    @Query("SELECT * FROM `Order` O WHERE O.customer_id = :uid AND status = 1")
    Order selectUserNewest(int uid);

    @Query("SELECT " +
            "`Order`.*, fullname" +
            " FROM `Order` " +
            "JOIN `Account` ON customer_id = account_id " +
            "WHERE order_id = :orderId")
    OrderDTO select(int orderId);

    @Query("SELECT * FROM `Order` WHERE order_id = :orderId")
    Order getById(int orderId);

    @Query("SELECT " +
            "`Order`.*, fullname" +
            " FROM `Order` " +
            " LEFT JOIN `Account` ON customer_id = account_id ")
    List<OrderDTO> selectAll();

    @Query("DELETE FROM `Order`")
    void deleteAll();

    @Query("DELETE FROM `Order` WHERE order_id = :orderId")
    void delete(int orderId);

    @Query("SELECT * FROM `Order` WHERE (customer_id != :uid AND status != 1) OR customer_id == :uid ORDER BY order_date DESC")
    List<Order> selectOrders(int uid);

    @Query("SELECT * FROM `Order` WHERE customer_id = :uid ORDER BY order_date DESC")
    List<Order> selectOrdersOfAccount(int uid);

    //@Query("SELECT * FROM `Order` O WHERE O.customer_id = :uid AND status = 1")
    //List<OrderDTO> search(String searchStr);
}

