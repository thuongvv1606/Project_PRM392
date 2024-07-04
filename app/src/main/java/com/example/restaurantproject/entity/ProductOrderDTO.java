package com.example.restaurantproject.entity;

import com.example.restaurantproject.bean.Product;

public class ProductOrderDTO extends Product {
    private int number;

    public ProductOrderDTO(Product product, int number) {
        this.setProductId(product.getProductId());
        this.setProductName(product.getProductName());
        this.setProductDescription(product.getProductDescription());
        this.setProductImage(product.getProductImage());
        this.setPrice(product.getPrice());
        this.setCategoryId(product.getCategoryId());
        this.setMenuId(product.getMenuId());
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
