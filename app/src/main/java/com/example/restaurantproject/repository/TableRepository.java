package com.example.restaurantproject.repository;

import android.content.Context;

import com.example.restaurantproject.bean.Table;
import com.example.restaurantproject.dao.RestaurantDatabase;
import com.example.restaurantproject.dao.TableDao;

import java.util.List;

public class TableRepository {
    private TableDao tableDao;

    public TableRepository(Context context) {
        tableDao = RestaurantDatabase.getInstance(context).tableDao();
    }

    public void createTable(Table table) {
        tableDao.insert(table);
    }

    public void updateTable(Table table) {
        tableDao.update(table);
    }
    public void updateTableStatus(int tableId, int status) {
        tableDao.updateStatus(tableId, status);
    }

    public Table getTable(int tableId) {
        return tableDao.select(tableId);
    }
    public List<Table> getTableByRestaurant(int restaurantId) {
        return tableDao.selectByRestaurant(restaurantId);
    }

    public List<Table> getAllTables() {
        return tableDao.selectAll();
    }

    public void deleteTable(int tableId) {
        tableDao.delete(tableId);
    }

    public void deleteAllTables() {
        tableDao.deleteAll();
    }
}
