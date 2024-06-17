package com.example.restaurantproject;

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

import com.example.restaurantproject.bean.Account;
import com.example.restaurantproject.repository.AccountRepository;

public class UserRegisterActivity extends AppCompatActivity {

    private EditText email, username, password;
    private Button registerButton;
    private TextView tvLogin;

    private AccountRepository accountRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_register);
        email = findViewById(R.id.edt_email);
        username = findViewById(R.id.edt_username);
        password = findViewById(R.id.edt_password);
        registerButton = findViewById(R.id.btn_register);
        tvLogin = findViewById(R.id.tv_have_account_login);

        accountRepository = new AccountRepository(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredEmail = email.getText().toString().trim();
                String enteredUsername = username.getText().toString().trim();
                String enteredPassword = password.getText().toString().trim();

                // Kiểm tra các trường không được để trống
                if (enteredEmail.isEmpty() || enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                    Toast.makeText(UserRegisterActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra độ dài của username
                if (enteredUsername.length() < 6) {
                    Toast.makeText(UserRegisterActivity.this, "Username must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!enteredUsername.matches("[a-zA-Z0-9]+")) {
                    Toast.makeText(UserRegisterActivity.this, "Username can only contain letters and numbers", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra độ dài của password
                if (enteredPassword.length() <= 6) {
                    Toast.makeText(UserRegisterActivity.this, "Password must be longer than 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra định dạng email
                if (!isValidEmail(enteredEmail)) {
                    Toast.makeText(UserRegisterActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Thực hiện đăng ký tài khoản
                Account newAccount = new Account();
                newAccount.setEmail(enteredEmail);
                newAccount.setUsername(enteredUsername);
                newAccount.setPassword(enteredPassword);
                newAccount.setRoleId(2);
                newAccount.setRestaurantId(1);
                newAccount.setStatus(true);
                newAccount.setFullname(enteredUsername);
                newAccount.setAvatar("https://www.iconpacks.net/icons/2/free-user-icon-3297-thumb.png");

                long accountId = accountRepository.register(newAccount);

                if (accountId == -1) {
                    Toast.makeText(UserRegisterActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                } else if (accountId == -2) {
                    Toast.makeText(UserRegisterActivity.this, "Email already exists", Toast.LENGTH_SHORT).show();
                } else if (accountId > 0) {
                    Toast.makeText(UserRegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    // Chuyển sang màn hình đăng nhập (UserLoginActivity)
                    Intent intent = new Intent(UserRegisterActivity.this, UserLoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(UserRegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserRegisterActivity.this, UserLoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private boolean isValidEmail(String emailAddress) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches();
    }
}
