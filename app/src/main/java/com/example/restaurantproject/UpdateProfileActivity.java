package com.example.restaurantproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.restaurantproject.bean.Account;
import com.example.restaurantproject.entity.AccountDTO;
import com.example.restaurantproject.repository.AccountRepository;
import com.example.restaurantproject.ultils.helper.SaveImageToStorage;
import com.example.restaurantproject.ultils.session.SessionManager;

import java.io.IOException;

public class UpdateProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    // Khai báo các biến thành phần giao diện
    private ImageView editProfileImage;
    private TextView tvRole;
    private EditText edtFullName, edtEmail, edtPhone, edtAddress;

    private Uri imageUri;
    private AccountDTO currentAccount;

    // Khai báo các biến liên quan đến quản lý phiên và repository
    private SessionManager sessionManager;
    private AccountRepository accountRepository;

    private SaveImageToStorage saveImageToStorage;

    // Biến launcher để chọn hình ảnh từ bộ nhớ ngoài và xử lý kết quả trả về
    private final ActivityResultLauncher<Intent> imageChooserLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    // Nếu hình ảnh được chọn thành công, lấy URI của hình ảnh
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        imageUri = selectedImageUri;
                        try {
                            // Load hình ảnh được chọn lên ImageView
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            editProfileImage.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sessionManager = new SessionManager(this);
        accountRepository = new AccountRepository(this);

        saveImageToStorage = new SaveImageToStorage(this);

        // Khởi tạo các view
        tvRole = findViewById(R.id.tv_role);
        editProfileImage = findViewById(R.id.edit_profile_image);
        edtFullName = findViewById(R.id.edt_fullname);
        edtEmail = findViewById(R.id.edt_email);
        edtPhone = findViewById(R.id.edt_phone);
        edtAddress = findViewById(R.id.edt_address);

        // Đưa dữ liệu ban đầu vào form
        loadProfileData();

        // Thiết lập sự kiện cho các nút
        findViewById(R.id.btn_save_changes).setOnClickListener(v -> saveProfileData());
        findViewById(R.id.btn_cancel).setOnClickListener(v -> loadProfileData());
        findViewById(R.id.imv_back).setOnClickListener(v -> finish());
        findViewById(R.id.tv_logout).setOnClickListener(v -> logout());

        // Sự kiện chọn ảnh đại diện
        editProfileImage.setOnClickListener(v -> openImageChooser());
    }

    private void loadProfileData() {
        // Lấy thông tin AccountDTO từ session
        currentAccount = sessionManager.getAccountFromSession();
        if (currentAccount != null) {
            // Push dữ liệu lên giao diện
            edtFullName.setText(currentAccount.getFullname());
            edtEmail.setText(currentAccount.getEmail());
            edtPhone.setText(currentAccount.getPhoneNumber());
            edtAddress.setText(currentAccount.getAddress());
            tvRole.setText(currentAccount.getRoleName().toUpperCase());

            // Load ảnh đại diện từ URL

            if (currentAccount.getAvatar() != null && !currentAccount.getAvatar().isEmpty()) {
                if (currentAccount.getAvatar().startsWith("content://")) {
                    Glide.with(this).load(Uri.parse(currentAccount.getAvatar())).into(editProfileImage);
                } else {
                    Glide.with(this).load(currentAccount.getAvatar()).into(editProfileImage);
                }
            }

        } else {
            Toast.makeText(this, "No account data found", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveProfileData() {
        // Lấy dữ liệu cập nhật
        String fullName = edtFullName.getText().toString();
        String email = edtEmail.getText().toString();
        String phone = edtPhone.getText().toString();
        String address = edtAddress.getText().toString();

        // Kiểm tra các trường không được để trống
        if (email.isEmpty()) {
            Toast.makeText(UpdateProfileActivity.this, "Please enter an email in the field", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra định dạng email
        if (!isValidEmail(email)) {
            Toast.makeText(UpdateProfileActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra email không được trùng
        if (!email.equals(currentAccount.getEmail()) && accountRepository.checkEmailExists(email)) {
            Toast.makeText(UpdateProfileActivity.this, "This email is already in use", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentAccount != null) {
            // Chuyển đổi từ AccountDTO sang Account
            Account account = new Account();
            account.setAccountId(currentAccount.getAccountId());
            account.setRoleId(currentAccount.getRoleId());
            account.setPassword(currentAccount.getPassword());
            account.setUsername(currentAccount.getUsername());
            account.setStatus(currentAccount.isStatus());

            // Cập nhật thông tin mới
            account.setFullname(fullName);
            account.setEmail(email);
            account.setPhoneNumber(phone);
            account.setAddress(address);

            if (imageUri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    String imagePath = saveImageToStorage.saveImageToStorage(bitmap);
                    account.setAvatar(imagePath); // Cập nhật URI ảnh đại diện mới
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                account.setAvatar(currentAccount.getAvatar()); // Cập nhật ảnh đại diện cũ khi không có thay đổi
            }

            // Cập nhật account trong DB
            accountRepository.updateAccount(account);

            // Cập nhật session với dữ liệu mới (tuỳ chọn)
            currentAccount.setFullname(fullName);
            currentAccount.setEmail(email);
            currentAccount.setPhoneNumber(phone);
            currentAccount.setAddress(address);
            if (imageUri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    String imagePath = saveImageToStorage.saveImageToStorage(bitmap);
                    currentAccount.setAvatar(imagePath);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                currentAccount.setAvatar(currentAccount.getAvatar());
            }
            sessionManager.saveAccountToSession(currentAccount);

            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

            finish();
        } else {
            Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
        }
    }

    private void openImageChooser() {
        // Mở activity để chọn hình ảnh từ bộ nhớ ngoài
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imageChooserLauncher.launch(intent);
    }

    private void logout() {
        // Xóa session và điều hướng đến main activity
        sessionManager.deleteAccountFromSession();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // Kiem tra dinh dang email
    private boolean isValidEmail(String emailAddress) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches();
    }
}