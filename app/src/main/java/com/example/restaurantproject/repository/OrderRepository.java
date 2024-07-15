package com.example.restaurantproject.repository;

import android.content.Context;

import com.example.restaurantproject.bean.Order;
import com.example.restaurantproject.dao.OrderDao;
import com.example.restaurantproject.dao.RestaurantDatabase;
import com.example.restaurantproject.entity.OrderDTO;

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
    public Order getById(int orderId) {
        return orderDao.getById(orderId);
    }
    public Order getOrderNewest() {
        return orderDao.selectNewest();
    }

    public List<OrderDTO> getAllOrders() {
        return orderDao.selectAll();
    }

    public List<Order> getAllNotDeliveredOrder() {
        return orderDao.getNoDeliveredOrders();
    }

    public List<Order> getAll(int uid, String searchStr) {
        return orderDao.getOrders(uid, searchStr);
    }

    public List<Order> getAllOfAccount(int uid, String searchStr) {
        return orderDao.getOrdersOfAccount(uid, searchStr);
    }
//    public List<OrderDTO> searchOrder(String searchStr) {
//        return orderDao.search(searchStr);
//    }

    public void deleteOrder(int orderId) {
        orderDao.delete(orderId);
    }

    public void deleteAllOrders() {
        orderDao.deleteAll();
    }
    public Order selectUserNewest(int uid) {
        return orderDao.selectUserNewest(uid);
    }
}

