package com.example.restaurantproject.entity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.restaurantproject.bean.Product;

import java.util.HashMap;

public class OrderViewModel extends ViewModel {
    private MutableLiveData<HashMap<Integer, Integer>> orderMap = new MutableLiveData<>(new HashMap<>());

    public void setOrderMap(HashMap<Integer, Integer> orderMap) {
        this.orderMap.setValue(orderMap);
    }

    public LiveData<HashMap<Integer, Integer>> getOrderMap() {
        return orderMap;
    }

    public void addProduct(Product product) {
        HashMap<Integer, Integer> currentOrderMap = orderMap.getValue();
        if (currentOrderMap != null) {
            currentOrderMap.put(product.getProductId(), currentOrderMap.getOrDefault(product.getProductId(), 0) + 1);
            orderMap.setValue(currentOrderMap);
        }
    }
}