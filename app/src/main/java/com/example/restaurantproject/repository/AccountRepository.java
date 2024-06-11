package com.example.restaurantproject.repository;

import android.content.Context;

import com.example.restaurantproject.bean.Account;
import com.example.restaurantproject.dao.AccountDao;
import com.example.restaurantproject.dao.RestaurantDatabase;

import java.util.List;

public class AccountRepository {
    private AccountDao accountDao;

    public AccountRepository(Context context) {
        accountDao = RestaurantDatabase.getInstance(context).accountDao();
    }

    public void createAccount(Account account) {
        accountDao.insert(account);
    }

    public void updateAccount(Account account) {
        accountDao.update(account);
    }

    public Account getAccount(int accountId) {
        return accountDao.select(accountId);
    }

    public List<Account> getAllAccounts() {
        return accountDao.selectAll();
    }

    public void deleteAccount(int accountId) {
        accountDao.delete(accountId);
    }

    public void deleteAllAccounts() {
        accountDao.deleteAll();
    }
}

