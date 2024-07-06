package com.example.restaurantproject.repository;

import android.content.Context;

import com.example.restaurantproject.bean.Account;
import com.example.restaurantproject.dao.AccountDao;
import com.example.restaurantproject.dao.RestaurantDatabase;
import com.example.restaurantproject.entity.AccountDTO;

import java.util.List;
import java.util.UUID;

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

    public AccountDTO checkLogin(String username, String password) {
        AccountDTO accountDTO = accountDao.checkLogin(username, password);
        return accountDTO;
    }

    public long register(Account account) {
        // Kiểm tra username và email đã tồn tại chưa
        if (accountDao.checkUsernameExists(account.getUsername()) > 0) {
            return -1; // Đã tồn tại username
        } else if (accountDao.checkEmailExists(account.getEmail()) > 0) {
            return -2; // Đã tồn tại email
        }

        // Nếu chưa tồn tại, thêm mới tài khoản
        return accountDao.insert(account);
    }

    public String resetPassword(String email) {
        // Thực hiện logic để tạo mật khẩu mới
        String newPassword = generateNewPassword();

        // Cập nhật mật khẩu của tài khoản trong DB
        Account account = accountDao.findAccountByEmail(email);
        if (account != null) {
            account.setPassword(newPassword);
            accountDao.update(account);
        }

        return newPassword;
    }

    public List<AccountDTO> getAllAccountDTOs() {
        return accountDao.selectAllAccountDTO();
    }

    public List<AccountDTO> searchAccounts(String searchStr) {
        return accountDao.searchAccountByUserName("%" + searchStr + "%");
    }

    public void updateAccountStatus(int accountId, boolean newStatus) {
        Account account = accountDao.select(accountId);
        if (account != null) {
            account.setStatus(newStatus);
            accountDao.update(account);
        }
    }

    private String generateNewPassword() {
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0, 8);
    }

    public boolean checkEmailExists(String email) {
        int count = accountDao.checkEmailExists(email);
        return count > 0;
    }

    public boolean checkUsernameExists(String username) {
        int count = accountDao.checkUsernameExists(username);
        return count > 0;
    }

    public Account getDelivery() {
        return accountDao.selectRandomWithRoleId5();
    }
}

