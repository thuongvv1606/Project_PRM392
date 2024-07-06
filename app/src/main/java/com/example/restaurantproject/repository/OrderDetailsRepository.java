package com.example.restaurantproject.repository;

import android.content.Context;

import androidx.room.Query;

import com.example.restaurantproject.bean.OrderDetails;
import com.example.restaurantproject.dao.OrderDetailsDao;
import com.example.restaurantproject.dao.RestaurantDatabase;

import java.util.List;

public class OrderDetailsRepository {
    private OrderDetailsDao orderDetailsDao;

    public OrderDetailsRepository(Context context) {
        orderDetailsDao = RestaurantDatabase.getInstance(context).orderDetailsDao();
    }

    public void createOrderDetails(OrderDetails orderDetails) {
        orderDetailsDao.insert(orderDetails);
    }

    public void updateOrderDetails(OrderDetails orderDetails) {
        orderDetailsDao.update(orderDetails);
    }

//    public OrderDetails getOrderDetails(int orderDetailsId) {
//        return orderDetailsDao.select(orderDetailsId);
//    }

    public List<OrderDetails> getAllOrderDetails() {
        return orderDetailsDao.selectAll();
    }

//    public void deleteOrderDetails(int orderDetailsId) {
//        orderDetailsDao.delete(orderDetailsId);
//    }

    public void deleteAllOrderDetails() {
        orderDetailsDao.deleteAll();
    }
    public OrderDetails selectAllByOrderIdAndProductId(int oid, int pid) {
        return orderDetailsDao.selectAllByOrderIdAndProductId(oid, pid);
    }
    public List<OrderDetails> selectAllByOrderId(int oid) {
        return orderDetailsDao.selectAllByOrderId(oid);
    }
}

