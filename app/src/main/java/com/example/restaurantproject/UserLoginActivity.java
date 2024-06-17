package com.example.restaurantproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.restaurantproject.entity.AccountDTO;
import com.example.restaurantproject.repository.AccountRepository;
import com.example.restaurantproject.ultils.session.SessionManager;

public class UserLoginActivity extends AppCompatActivity {

    private EditText username, password;
    private Button loginButton;
    private TextView tvRegister, tvForgotPass;

    private AccountRepository accountRepository;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_login);

        username = findViewById(R.id.edt_username);
        password = findViewById(R.id.edt_password);
        loginButton = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_not_have_account_register);
        tvForgotPass = findViewById(R.id.tv_forgot_password);

        accountRepository = new AccountRepository(this);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredUsername = username.getText().toString().trim();
                String enteredPassword = password.getText().toString().trim();

                // Kiểm tra các trường không được để trống
                if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                    Toast.makeText(UserLoginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra tài khoản và mật khẩu
                AccountDTO loggedInAccount = accountRepository.checkLogin(enteredUsername, enteredPassword);

                if (loggedInAccount != null) {
                    if (loggedInAccount.isStatus()) {
                        // Lưu thông tin tài khoản vào Session
                        SessionManager sessionManager = new SessionManager(UserLoginActivity.this);
                        sessionManager.saveAccountToSession(loggedInAccount);

                        // Đăng nhập thành công
                        Toast.makeText(UserLoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Tài khoản bị khóa (status = false)
                        Toast.makeText(UserLoginActivity.this, "Account is locked", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Sai thông tin đăng nhập
                    Toast.makeText(UserLoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang màn hình đăng ký (RegisterActivity)
                Intent intent = new Intent(UserLoginActivity.this, UserRegisterActivity.class);
                startActivity(intent);
            }
        });

        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserLoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}