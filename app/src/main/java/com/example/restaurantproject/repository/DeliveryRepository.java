package com.example.restaurantproject.repository;

import android.content.Context;

import com.example.restaurantproject.bean.Delivery;
import com.example.restaurantproject.dao.DeliveryDao;
import com.example.restaurantproject.dao.RestaurantDatabase;

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

    public List<Delivery> getAllDeliveries() {
        return deliveryDao.selectAll();
    }

    public void deleteDelivery(int deliveryId) {
        deliveryDao.delete(deliveryId);
    }

    public void deleteAllDeliveries() {
        deliveryDao.deleteAll();
    }
}

