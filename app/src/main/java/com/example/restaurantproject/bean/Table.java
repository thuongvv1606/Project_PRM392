package com.example.restaurantproject.bean;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "Table",
        foreignKeys = @ForeignKey(entity = Restaurant.class,
                parentColumns = "restaurant_id",
                childColumns = "restaurant_id",
                onDelete = ForeignKey.CASCADE)
)
public class Table {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "table_id")
    private int tableId;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "seat_number")
    private int seatNum;
    @ColumnInfo(name = "isVIP")
    private boolean isVIP ;
    @ColumnInfo(name = "status")
    private int status ;
    @ColumnInfo(name = "restaurant_id")
    private int restaurantId;

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(int seatNum) {
        this.seatNum = seatNum;
    }

    public boolean isVIP() {
        return isVIP;
    }

    public void setVIP(boolean VIP) {
        isVIP = VIP;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Table() {
    }

    public Table(String name, int seatNum, boolean isVIP, int status, int restaurantId) {
        this.name = name;
        this.seatNum = seatNum;
        this.isVIP = isVIP;
        this.status = status;
        this.restaurantId = restaurantId;
    }
}
