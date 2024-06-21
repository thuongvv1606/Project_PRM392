package com.example.restaurantproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.restaurantproject.bean.Account;
import com.example.restaurantproject.entity.AccountDTO;
import com.example.restaurantproject.repository.AccountRepository;
import com.example.restaurantproject.ultils.session.SessionManager;
import com.mikhaellopez.circularimageview.CircularImageView;

public class ChangePasswordActivity extends AppCompatActivity {
    // Khai báo các biến thành phần giao diện
    private EditText edtCurrentPassword, edtNewPassword, edtConfirmPassword;
    private CircularImageView profileImage;
    private TextView tvRole;
    private AccountDTO currentAccount;

    // Khai báo các biến liên quan đến quản lý phiên và repository
    private SessionManager sessionManager;
    private AccountRepository accountRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo session manager và account repository
        sessionManager = new SessionManager(this);
        accountRepository = new AccountRepository(this);

        // Khởi tạo các view
        edtCurrentPassword = findViewById(R.id.edt_current_password);
        edtNewPassword = findViewById(R.id.edt_new_password);
        edtConfirmPassword = findViewById(R.id.edt_confirm_password);
        profileImage = findViewById(R.id.profile_image);
        tvRole = findViewById(R.id.tv_role);

        // Lấy thông tin AccountDTO từ session
        currentAccount = sessionManager.getAccountFromSession();

        if (currentAccount != null) {
            tvRole.setText(currentAccount.getRoleName().toUpperCase());
            Glide.with(this).load(currentAccount.getAvatar()).into(profileImage);
        }

        // Thiết lập sự kiện cho các nút
        findViewById(R.id.btn_save_changes).setOnClickListener(v -> changePassword());
        findViewById(R.id.btn_cancel).setOnClickListener(v -> clearFields());
        findViewById(R.id.imv_back).setOnClickListener(v -> finish());
        findViewById(R.id.tv_logout).setOnClickListener(v -> logout());
    }

    private void changePassword() {
        // Lấy dữ liệu từ các trường nhập liệu
        String currentPassword = edtCurrentPassword.getText().toString();
        String newPassword = edtNewPassword.getText().toString();
        String confirmPassword = edtConfirmPassword.getText().toString();

        // Kiểm tra các điều kiện mật khẩu
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in the information", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!currentPassword.equals(currentAccount.getPassword())) {
            Toast.makeText(this, "The current password is incorrect", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPassword.length() <= 6) {
            Toast.makeText(this, "The new password must be longer than 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Confirmation password doesn't match new password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật mật khẩu mới
        Account account = new Account();
        account.setAccountId(currentAccount.getAccountId());
        account.setRoleId(currentAccount.getRoleId());
        account.setUsername(currentAccount.getUsername());
        account.setStatus(currentAccount.isStatus());
        account.setFullname(currentAccount.getFullname());
        account.setEmail(currentAccount.getEmail());
        account.setPhoneNumber(currentAccount.getPhoneNumber());
        account.setAddress(currentAccount.getAddress());
        account.setAvatar(currentAccount.getAvatar());

        account.setPassword(newPassword); // Mật khẩu mới

        // Cập nhật account trong repository
        accountRepository.updateAccount(account);

        // Cập nhật session với dữ liệu mới
        currentAccount.setPassword(newPassword); // Mật khẩu mới
        sessionManager.saveAccountToSession(currentAccount);

        // Thông báo thành công
        Toast.makeText(this, "Password change successfully", Toast.LENGTH_SHORT).show();

        // Kết thúc activity
        finish();
    }

    private void clearFields() {
        // Xóa sạch các ô nhập liệu
        edtCurrentPassword.setText("");
        edtNewPassword.setText("");
        edtConfirmPassword.setText("");
    }

    private void logout() {
        // Xóa session và điều hướng đến main activity
        sessionManager.deleteAccountFromSession();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}