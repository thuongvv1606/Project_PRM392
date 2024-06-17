package com.example.restaurantproject.ultils.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.restaurantproject.MainActivity;
import com.example.restaurantproject.entity.AccountDTO;
import com.google.gson.Gson;

public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_ACCOUNT = "account";

    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private Gson gson;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    // Lưu thông tin tài khoản vào Session
    public void saveAccountToSession(AccountDTO accountDTO) {
        String jsonAccount = gson.toJson(accountDTO);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_ACCOUNT, jsonAccount);
        editor.apply();
    }

    // Lấy thông tin tài khoản từ Session
    public AccountDTO getAccountFromSession() {
        String jsonAccount = sharedPreferences.getString(KEY_ACCOUNT, null);
        if (jsonAccount != null) {
            return gson.fromJson(jsonAccount, AccountDTO.class);
        }
        return null;
    }

    // Kiểm tra xem người dùng đã đăng nhập hay chưa
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Xóa Session khi người dùng đăng xuất
    public void deleteAccountFromSession() {
        editor.clear();
        editor.commit();
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
