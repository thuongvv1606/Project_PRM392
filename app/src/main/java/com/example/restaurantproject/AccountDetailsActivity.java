package com.example.restaurantproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

public class AccountDetailsActivity extends AppCompatActivity {

    // Khai báo các biến cho các thành phần UI
    private CircularImageView detailAccountImage;
    private TextView tvUserName, tvFullName, tvEmail, tvPhone, tvAddress, tvRole, tvStatus;
    private TextView btnGoToUpdate, btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Liên kết các thành phần UI với các biến
        tvUserName = findViewById(R.id.tv_username);
        tvEmail = findViewById(R.id.tv_email);
        tvFullName = findViewById(R.id.tv_fullname);
        tvPhone = findViewById(R.id.tv_phone);
        tvAddress = findViewById(R.id.tv_address);
        tvRole = findViewById(R.id.tv_role);
        tvStatus = findViewById(R.id.tv_status);
        detailAccountImage = findViewById(R.id.detail_account_image);

        btnGoToUpdate = findViewById(R.id.btn_update);
        btnCancel = findViewById(R.id.btn_cancel);

        // Nhận Intent và lấy các dữ liệu được truyền từ adapter khi click id
        Intent intent = getIntent();

        // Assuming you have serialized data or extras from categoryList
        String username = intent.getStringExtra("username");
        String email = intent.getStringExtra("email");
        String fullname = intent.getStringExtra("fullname");
        String phone = intent.getStringExtra("phoneNumber");
        String address = intent.getStringExtra("address");
        String role = intent.getStringExtra("roleName");
        String avatar = intent.getStringExtra("avatar");
        boolean status = intent.getBooleanExtra("status", false);

        int accountId = intent.getIntExtra("accountId", -1);
        String password = intent.getStringExtra("password");
        int roleId = intent.getIntExtra("roleId", -1);
        int restaurantId = intent.getIntExtra("restaurantId", -1);
        String restaurantName = intent.getStringExtra("restaurantName");


        if (avatar != null && !avatar.isEmpty()) {
            Glide.with(this).load(avatar).into(detailAccountImage);
        }

        // Gán dữ liệu cho các TextView
        tvUserName.setText(username);
        tvEmail.setText(email);
        tvFullName.setText(fullname);
        tvPhone.setText(phone);
        tvAddress.setText(address);
        tvRole.setText(role);
        tvStatus.setText(status ? "active" : "locked");


        // Thiết lập sự kiện cho nút GoToUpdate
        btnGoToUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountDetailsActivity.this, AccountUpdateActivity.class);
                intent.putExtra("accountId", accountId);
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                intent.putExtra("fullname", fullname);
                intent.putExtra("email", email);
                intent.putExtra("phoneNumber", phone);
                intent.putExtra("address", address);
                intent.putExtra("roleId", roleId);
                intent.putExtra("restaurantId", restaurantId);
                intent.putExtra("status", status);
                intent.putExtra("avatar", avatar);
                intent.putExtra("roleName", role);
                intent.putExtra("restaurantName", restaurantName);
                startActivity(intent);
            }
        });

        // Thiết lập sự kiện cho nút Cancel
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            } // Dong activity
        });
    }
}