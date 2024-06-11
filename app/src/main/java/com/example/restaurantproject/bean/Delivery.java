package com.example.restaurantproject.bean;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Delivery",
        foreignKeys = {
                @ForeignKey(entity = Order.class,
                        parentColumns = "order_id",
                        childColumns = "order_id",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Account.class,
                        parentColumns = "account_id",
                        childColumns = "deliverer_id",
                        onDelete = ForeignKey.CASCADE)
        })
public class Delivery {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "delivery_id")
    private int deliveryId;

    @ColumnInfo(name = "order_id")
    private int orderId;

    @ColumnInfo(name = "deliverer_id")
    private int delivererId;

    @ColumnInfo(name = "status")
    private int status;

    @ColumnInfo(name = "note")
    private String note;

    public int getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getDelivererId() {
        return delivererId;
    }

    public void setDelivererId(int delivererId) {
        this.delivererId = delivererId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    // Getters and setters
    // ...
}

