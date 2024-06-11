package com.example.restaurantproject.bean;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Reservation",
        foreignKeys = @ForeignKey(entity = Account.class,
                parentColumns = "account_id",
                childColumns = "account_id",
                onDelete = ForeignKey.CASCADE))
public class Reservation {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "reservation_id")
    private int reservationId;

    @ColumnInfo(name = "account_id")
    private int accountId;

    @ColumnInfo(name = "order_date")
    private String orderDate;

    @ColumnInfo(name = "no_of_people")
    private int noOfPeople;

    @ColumnInfo(name = "reservation_date")
    private String reservationDate;

    @ColumnInfo(name = "note")
    private String note;

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
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

    // Getters and setters
    // ...
}
