package com.example.restaurantproject.bean;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Role")
public class Role {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "role_id")
    private int roleId;

    @ColumnInfo(name = "role_name")
    private String roleName;

    @ColumnInfo(name = "role_description")
    private String roleDescription;

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

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }
}