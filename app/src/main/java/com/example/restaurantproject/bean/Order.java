package com.example.restaurantproject.bean;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Order",
        foreignKeys = {
                @ForeignKey(entity = Account.class,
                        parentColumns = "account_id",
                        childColumns = "customer_id",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Table.class,
                        parentColumns = "table_id",
                        childColumns = "table_id",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class Order {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "order_id")
    private int orderId;

    @ColumnInfo(name = "totalprice")
    private Double totalPrice;

    @ColumnInfo(name = "order_date")
    private String orderDate;

    @ColumnInfo(name = "customer_id")
    private Integer customerId;

    @ColumnInfo(name = "status")
    private Integer status;

    @ColumnInfo(name = "payment")
    private Boolean payment;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "table_id")
    private Integer tableID;

    @ColumnInfo(name = "no_of_people")
    private Integer noOfPeople;

    @ColumnInfo(name = "reservation_date")
    private String reservationDate;

    @ColumnInfo(name = "note")
    private String note;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isPayment() {
        return payment;
    }

    public void setPayment(boolean payment) {
        this.payment = payment;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTableID() {
        return tableID;
    }

    public void setTableID(int tableID) {
        this.tableID = tableID;
    }

    public int getNoOfPeople() {
        return noOfPeople;
    }

    public void setNoOfPeople(int noOfPeople) {
        this.noOfPeople = noOfPeople;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
