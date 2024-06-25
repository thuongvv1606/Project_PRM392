package com.example.restaurantproject;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.example.restaurantproject.adapter.RoleSpinnerAdapter;
import com.example.restaurantproject.bean.Account;
import com.example.restaurantproject.bean.Role;
import com.example.restaurantproject.repository.AccountRepository;
import com.example.restaurantproject.repository.RoleRepository;
import com.example.restaurantproject.ultils.helper.SaveImageToStorage;

import java.io.IOException;
import java.util.List;

public class AccountUpdateActivity extends AppCompatActivity {

    // Khai báo các biến cho các thành phần UI
    private ImageView updateAccountImage;
    private EditText edtUsername, edtPassword, edtEmail, edtFullname, edtPhone, edtAddress;
    private Spinner spinnerRole;
    private TextView btnUpdateAccount, btnCancel;
    private Uri imageUri;
    private RoleRepository roleRepository;
    private AccountRepository accountRepository;
    private SaveImageToStorage saveImageToStorage;

    private RoleSpinnerAdapter roleSpinnerAdapter;
    private List<Role> roles;

    private int accountId, roleId, restaurantId;
    private String username, password, fullname, email, phoneNumber, address, avatar, roleName, restaurantName;
    private boolean status;

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
                            updateAccountImage.setImageBitmap(bitmap);
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
        setContentView(R.layout.activity_account_update);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo các thành phần giao diện
        updateAccountImage = findViewById(R.id.update_account_image);
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        edtEmail = findViewById(R.id.edt_email);
        edtFullname = findViewById(R.id.edt_fullname);
        edtPhone = findViewById(R.id.edt_phone);
        edtAddress = findViewById(R.id.edt_address);
        spinnerRole = findViewById(R.id.spinner_role);
        btnUpdateAccount = findViewById(R.id.btn_update_account);
        btnCancel = findViewById(R.id.btn_cancel);

        // Khởi tạo repository
        roleRepository = new RoleRepository(this);
        accountRepository = new AccountRepository(this);

        saveImageToStorage = new SaveImageToStorage(this);


        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();

        accountId = intent.getIntExtra("accountId", -1);
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        fullname = intent.getStringExtra("fullname");
        email = intent.getStringExtra("email");
        phoneNumber = intent.getStringExtra("phoneNumber");
        address = intent.getStringExtra("address");
        avatar = intent.getStringExtra("avatar");
        roleId = intent.getIntExtra("roleId", -1);
        restaurantId = intent.getIntExtra("restaurantId", -1);
        status = intent.getBooleanExtra("status", true);
        roleName = intent.getStringExtra("roleName");
        restaurantName = intent.getStringExtra("restaurantName");

        // Hiển thị dữ liệu lên các view
        edtUsername.setText(username);
        edtPassword.setText(password);
        edtFullname.setText(fullname);
        edtEmail.setText(email);
        edtPhone.setText(phoneNumber);
        edtAddress.setText(address);

        // Hiển thị avatar bằng Glide
        if (avatar != null && !avatar.isEmpty()) {
            if (avatar.startsWith("content://")) {
                Glide.with(this).load(Uri.parse(avatar)).into(updateAccountImage);
            } else {
                Glide.with(this).load(avatar).into(updateAccountImage);
            }
        }

        // Lấy danh sách các roles từ repository
        roles = roleRepository.getAllRoles();
        // Tạo Adapter tùy chỉnh và đặt cho Spinner
        roleSpinnerAdapter = new RoleSpinnerAdapter(this, roles);
        spinnerRole.setAdapter(roleSpinnerAdapter);

        // Chọn vai trò dựa trên roleId
        selectRoleByRoleId(roleId);


        // Sự kiện khi click vào ảnh để chọn hình mới
        updateAccountImage.setOnClickListener(v -> chooseImage());

        // Sự kiện khi click vào nút update account
        btnUpdateAccount.setOnClickListener(v -> updateAccount());

        // Sự kiện khi click vào nút cancel
        btnCancel.setOnClickListener(v -> finish());
        btnCancel.setOnClickListener(v -> finish());
    }

    // Phương thức chọn vai trò theo roleId
    private void selectRoleByRoleId(int roleId) {
        for (int i = 0; i < roles.size(); i++) {
            if (roles.get(i).getRoleId() == roleId) {
                spinnerRole.setSelection(i);
                break;
            }
        }
    }

    // Phương thức chọn hình ảnh từ bộ nhớ ngoài
    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imageChooserLauncher.launch(intent);
    }

    // Phương thức cập nhật thông tin tài khoản
    private void updateAccount() {
        String usernameEnter = edtUsername.getText().toString().trim();
        String passwordEnter = edtPassword.getText().toString().trim();
        String emailEnter = edtEmail.getText().toString().trim();
        String fullnameEnter = edtFullname.getText().toString().trim();
        String phoneEnter = edtPhone.getText().toString().trim();
        String addressEnter = edtAddress.getText().toString().trim();
        // Lấy đối tượng Role được chọn từ Spinner
        Role selectedRole = (Role) spinnerRole.getSelectedItem();

        if (selectedRole == null) {
            Toast.makeText(this, "Please select a role.", Toast.LENGTH_SHORT).show();
            return;
        }
        int roleIdEnter = selectedRole.getRoleId();


        // Kiểm tra các trường bắt buộc
        if (usernameEnter.isEmpty() || passwordEnter.isEmpty() || emailEnter.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields (*) required.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra độ dài của username >= 6
        if (usernameEnter.length() < 6) {
            Toast.makeText(this, "Username must be at least 6 characters long", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra ký tự hợp lệ của username bao gom chu va so
        if (!usernameEnter.matches("[a-zA-Z0-9]+")) {
            Toast.makeText(this, "Username can only contain letters and numbers", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra username không được trùng
        if (!usernameEnter.equals(username) && accountRepository.checkUsernameExists(usernameEnter)) {
            Toast.makeText(AccountUpdateActivity.this, "This username is already in use", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra độ dài của password >=7
        if (passwordEnter.length() <= 6) {
            Toast.makeText(this, "Password must be longer than 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra định dạng email
        if (!isValidEmail(emailEnter)) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra email không được trùng
        if (!emailEnter.equals(email) && accountRepository.checkEmailExists(emailEnter)) {
            Toast.makeText(AccountUpdateActivity.this, "This email is already in use", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật thông tin tài khoản vào db

        Account newAccount = new Account();
        newAccount.setAccountId(accountId);
        newAccount.setEmail(emailEnter);
        newAccount.setUsername(usernameEnter);
        newAccount.setPassword(passwordEnter);
        newAccount.setRoleId(roleIdEnter);
        newAccount.setRestaurantId(restaurantId == 0 ? null : restaurantId);
        newAccount.setStatus(status);
        newAccount.setFullname(fullnameEnter);
        newAccount.setPhoneNumber(phoneEnter);
        newAccount.setAddress(addressEnter);

        if (imageUri != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                String imagePath = saveImageToStorage.saveImageToStorage(bitmap);
                newAccount.setAvatar(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            newAccount.setAvatar(avatar);
        }

        // Gọi phương thức cập nhật từ repository
        try {
            accountRepository.updateAccount(newAccount);
            Toast.makeText(this, "Update account successful", Toast.LENGTH_SHORT).show();

            // Chuyển sang màn hình danh sách tài khoản
            Intent intent = new Intent(AccountUpdateActivity.this, AccountListActivity.class);
            startActivity(intent);
            finish();
        } catch (SQLiteConstraintException e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    // kiểm tra định dạng email
    private boolean isValidEmail(String emailAddress) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches();
    }
}