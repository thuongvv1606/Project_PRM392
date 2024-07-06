package com.example.restaurantproject.bean;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "OrderDetails",
        foreignKeys = {
                @ForeignKey(entity = Product.class,
                        parentColumns = "product_id",
                        childColumns = "product_id",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Order.class,
                        parentColumns = "order_id",
                        childColumns = "order_id",
                        onDelete = ForeignKey.CASCADE)
        },
        primaryKeys = { "product_id", "order_id" }
)
public class OrderDetails {
//    @PrimaryKey(autoGenerate = true)
//    @NonNull
//    @ColumnInfo(name = "orderdetails_id")
//    private int orderDetailsId;

    @ColumnInfo(name = "product_id")
    private int productId;

    @ColumnInfo(name = "order_id")
    private int orderId;

    @ColumnInfo(name = "price")
    private double price;

    @ColumnInfo(name = "quantity")
    private int quantity;

    // Getters and setters
    // ...

//    public int getOrderDetailsId() {
//        return orderDetailsId;
//    }
//
//    public void setOrderDetailsId(int orderDetailsId) {
//        this.orderDetailsId = orderDetailsId;
//    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public OrderDetails() {
    }

    public OrderDetails(int productId, int orderId, double price, int quantity) {
        this.productId = productId;
        this.orderId = orderId;
        this.price = price;
        this.quantity = quantity;
    }
}
