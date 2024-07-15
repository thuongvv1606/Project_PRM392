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

    @Query("SELECT O.* FROM `Order` O JOIN Account A ON O.customer_id = A.account_id " +
            "WHERE ((O.customer_id != :uid AND O.status != 1) OR O.customer_id == :uid) " +
            "AND A.fullname like :searchStr ORDER BY order_date DESC")
    List<Order> getOrders(int uid, String searchStr);

    @Query("SELECT O.* FROM `Order` O JOIN Account A ON O.customer_id = A.account_id " +
            "WHERE O.customer_id = :uid and A.fullname like :searchStr ORDER BY order_date DESC")
    List<Order> getOrdersOfAccount(int uid, String searchStr);

    @Query("SELECT * FROM `Order` WHERE order_id NOT IN (SELECT order_id FROM Delivery) AND status <= 3")
    List<Order> getNoDeliveredOrders();

    //@Query("SELECT * FROM `Order` O WHERE O.customer_id = :uid AND status = 1")
    //List<OrderDTO> search(String searchStr);
}

