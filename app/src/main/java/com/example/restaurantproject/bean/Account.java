package com.example.restaurantproject.bean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Account",
        foreignKeys = {
                @ForeignKey(entity = Role.class,
                        parentColumns = "role_id",
                        childColumns = "role_id",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Restaurant.class,
                        parentColumns = "restaurant_id",
                        childColumns = "restaurant_id",
                        onDelete = ForeignKey.CASCADE)
        })
public class Account {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo (name = "account_id")
    private int accountId;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "fullname")
    private String fullname;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "phone_number")
    private String phoneNumber;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "role_id")
    private int roleId;

    @ColumnInfo(name = "restaurant_id")
    private int restaurantId;

    @ColumnInfo(name = "status")
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }
}
