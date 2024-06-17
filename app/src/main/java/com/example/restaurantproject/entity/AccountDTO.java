package com.example.restaurantproject.entity;

public class AccountDTO {
    private int accountId;
    private String username;
    private String password;
    private String fullname;
    private String email;
    private String phoneNumber;
    private String address;
    private int roleId;
    private Integer restaurantId;
    private boolean status;
    private String avatar;
    private String roleName;
    private String restaurantName;

    public AccountDTO() {
    }

    public AccountDTO(int accountId, String username, String password, String fullname, String email, String phoneNumber, String address, int roleId, int restaurantId, boolean status, String avatar, String roleName, String restaurantName) {
        this.accountId = accountId;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.roleId = roleId;
        this.restaurantId = restaurantId;
        this.status = status;
        this.avatar = avatar;
        this.roleName = roleName;
        this.restaurantName = restaurantName;
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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
