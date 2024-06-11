package com.example.restaurantproject.repository;

import android.content.Context;

import com.example.restaurantproject.bean.Role;
import com.example.restaurantproject.dao.RestaurantDatabase;
import com.example.restaurantproject.dao.RoleDao;

import java.util.List;

import android.content.Context;
import java.util.List;

public class RoleRepository {
    private RoleDao roleDao;

    public RoleRepository(Context context) {
        roleDao = RestaurantDatabase.getInstance(context).roleDao();
    }

    public void createRole(Role role) {
        roleDao.insert(role);
    }

    public void updateRole(Role role) {
        roleDao.update(role);
    }

    public Role getRole(int roleId) {
        return roleDao.select(roleId);
    }

    public List<Role> getAllRoles() {
        return roleDao.selectAll();
    }

    public void deleteRole(int roleId) {
        roleDao.delete(roleId);
    }

    public void deleteAllRoles() {
        roleDao.deleteAll();
    }
}


