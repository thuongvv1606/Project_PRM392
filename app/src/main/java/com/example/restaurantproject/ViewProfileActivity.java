package com.example.restaurantproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.restaurantproject.entity.AccountDTO;
import com.example.restaurantproject.ultils.session.SessionManager;
import com.mikhaellopez.circularimageview.CircularImageView;

public class ViewProfileActivity extends AppCompatActivity {

    private TextView tvFullName, tvEmail, tvPhone, tvAddress, tvRole;
    private TextView tvLogout, tvEditProfile, tvChangePass;
    private CircularImageView profileImage;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sessionManager = new SessionManager(this);

        // Find views
        tvFullName = findViewById(R.id.tv_fullname);
        tvEmail = findViewById(R.id.tv_email);
        tvPhone = findViewById(R.id.tv_phone);
        tvAddress = findViewById(R.id.tv_address);
        tvRole = findViewById(R.id.tv_role);
        profileImage = findViewById(R.id.profile_image);

        // view click
        tvLogout = findViewById(R.id.tv_logout);
        tvEditProfile = findViewById(R.id.tv_editProfile);
        tvChangePass = findViewById(R.id.tv_changePassword);

        ImageView backArrow = findViewById(R.id.imv_back);

        // Get accountDTO data from session
        updateProfileData();

        // Back arrow click listener
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to the previous activity
                finish();
            }
        });

        // Logout button click listener
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear session and navigate to main activity
                sessionManager.deleteAccountFromSession();
                Intent intent = new Intent(ViewProfileActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        // EditProfile button click listener
        tvEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewProfileActivity.this, UpdateProfileActivity.class);
                startActivity(intent);
            }
        });

        // ChangePassword button click listener
        tvChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewProfileActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void onResume() {
        super.onResume();
        updateProfileData();
    }

    private void updateProfileData() {
        AccountDTO account = sessionManager.getAccountFromSession();
        if (account != null) {
            tvFullName.setText(account.getFullname());
            tvEmail.setText(account.getEmail());
            tvPhone.setText(account.getPhoneNumber());
            tvAddress.setText(account.getAddress());
            tvRole.setText(account.getRoleName().toUpperCase());

            Glide.with(this).load(account.getAvatar()).into(profileImage);

        }
    }
}