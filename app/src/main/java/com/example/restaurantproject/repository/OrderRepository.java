package com.example.restaurantproject.repository;

import android.content.Context;

import com.example.restaurantproject.bean.Order;
import com.example.restaurantproject.dao.OrderDao;
import com.example.restaurantproject.dao.RestaurantDatabase;

import java.util.List;

public class OrderRepository {
    private OrderDao orderDao;

    public OrderRepository(Context context) {
        orderDao = RestaurantDatabase.getInstance(context).orderDao();
    }

    public void createOrder(Order order) {
        orderDao.insert(order);
    }

    public void updateOrder(Order order) {
        orderDao.update(order);
    }

    public Order getOrder(int orderId) {
        return orderDao.select(orderId);
    }

    public List<Order> getAllOrders() {
        return orderDao.selectAll();
    }

    public void deleteOrder(int orderId) {
        orderDao.delete(orderId);
    }

    public void deleteAllOrders() {
        orderDao.deleteAll();
    }
}

