package com.example.restaurantproject.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.restaurantproject.bean.Delivery;
import com.example.restaurantproject.entity.DeliveryDTO;
import com.example.restaurantproject.entity.DeliveryOrderInfo;
import com.example.restaurantproject.entity.DeliveryProductDetail;

import java.util.List;

@Dao
public interface DeliveryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Delivery delivery);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Delivery delivery);

    @Query("SELECT * FROM Delivery WHERE delivery_id = :deliveryId")
    Delivery select(int deliveryId);

    @Query("SELECT * FROM Delivery")
    List<Delivery> selectAll();

    @Query("DELETE FROM Delivery")
    void deleteAll();

    @Query("DELETE FROM Delivery WHERE delivery_id = :deliveryId")
    void delete(int deliveryId);

    @Query("SELECT \n" +
            "    d.delivery_id AS deliveryId,\n" +
            "    da.fullname AS delivererName,\n" +
            "    d.status AS deliveryStatus,\n" +
            "    o.order_date AS orderDate\n" +
            "FROM \n" +
            "    delivery d\n" +
            "    JOIN account da ON d.deliverer_id = da.account_id\n" +
            "    JOIN [order] o ON d.order_id = o.order_id\n")
    List<DeliveryDTO> selectAllDelivery();

    @Query("SELECT \n" +
            "    d.delivery_id AS deliveryId,\n" +
            "    da.fullname AS delivererName,\n" +
            "    d.status AS deliveryStatus,\n" +
            "    o.order_date AS orderDate\n" +
            "FROM \n" +
            "    delivery d\n" +
            "    JOIN account da ON d.deliverer_id = da.account_id\n" +
            "    JOIN [order] o ON d.order_id = o.order_id\n" +
            "WHERE \n" +
            "    da.fullname LIKE '%' || :search || '%'")
    List<DeliveryDTO> selectAllDelivery(String search);

    @Query("SELECT \n" +
            "    o.order_id AS orderId,\n" +
            "    a.fullname AS customerName,\n" +
            "    o.address AS customerAddress,\n" +
            "    a.phone_number AS customerPhoneNumber,\n" +
            "    d.delivery_id AS deliveryId,\n" +
            "    d.status AS deliveryStatus,\n" +
            "    da.fullname AS delivererName,\n" +
            "    da.phone_number AS delivererPhoneNumber,\n" +
            "    r.restaurant_name AS restaurantName\n" +
            "FROM \n" +
            "    Delivery d\n" +
            "    JOIN [Order] o ON d.order_id = o.order_id\n" +
            "    JOIN Account a ON o.customer_id = a.account_id\n" +
            "    JOIN Account da ON d.deliverer_id = da.account_id\n" +
            "    LEFT JOIN Restaurant r ON a.restaurant_id = r.restaurant_id\n" +
            "WHERE \n" +
            "    d.delivery_id = :orderId")
    DeliveryOrderInfo getOrderInfoByDeliveryId(int orderId);



    @Query("SELECT \n" +
            "    p.product_name AS productName,\n" +
            "    od.quantity,\n" +
            "    od.price,\n" +
            "    (od.price * od.quantity) AS totalProductPrice\n" +
            "FROM \n" +
            "    Delivery d\n" +
            "    JOIN [Order] o ON d.order_id = o.order_id\n" +
            "    JOIN OrderDetails od ON o.order_id = od.order_id\n" +
            "    JOIN Product p ON od.product_id = p.product_id\n" +
            "WHERE \n" +
            "    d.delivery_id = :orderId")
    List<DeliveryProductDetail> ProductDetails (int orderId);
}

