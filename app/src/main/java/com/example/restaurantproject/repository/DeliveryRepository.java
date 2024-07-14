package com.example.restaurantproject.repository;

import android.content.Context;

import com.example.restaurantproject.bean.Delivery;
import com.example.restaurantproject.dao.DeliveryDao;
import com.example.restaurantproject.dao.RestaurantDatabase;
import com.example.restaurantproject.entity.DeliveryDTO;
import com.example.restaurantproject.entity.DeliveryOrderInfo;
import com.example.restaurantproject.entity.DeliveryProductDetail;

import java.util.List;

public class DeliveryRepository {
    private DeliveryDao deliveryDao;

    public DeliveryRepository(Context context) {
        deliveryDao = RestaurantDatabase.getInstance(context).deliveryDao();
    }

    public void createDelivery(Delivery delivery) {
        deliveryDao.insert(delivery);
    }

    public void updateDelivery(Delivery delivery) {
        deliveryDao.update(delivery);
    }

    public Delivery getDelivery(int deliveryId) {
        return deliveryDao.select(deliveryId);
    }

    public List<Delivery> getAll() {
        return deliveryDao.selectAll();
    }
    public List<DeliveryDTO> getAllDelivery() {
        return deliveryDao.selectAllDelivery();
    }

    public List<DeliveryDTO> getAllDeliverySearch(String search) {
        return deliveryDao.selectAllDelivery(search);
    }

    public void deleteDelivery(int deliveryId) {
        deliveryDao.delete(deliveryId);
    }

    public void deleteAllDeliveries() {
        deliveryDao.deleteAll();
    }
    public DeliveryOrderInfo orderInfo(int orderId) {
        return deliveryDao.getOrderInfoByDeliveryId(orderId);
    }

    public List<DeliveryProductDetail> productDetail(int orderId) {
        return deliveryDao.ProductDetails(orderId);
    }

}

