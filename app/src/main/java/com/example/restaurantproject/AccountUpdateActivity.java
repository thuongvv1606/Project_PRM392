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

import java.io.IOException;
import java.util.List;

public class AccountUpdateActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 100;
    private static final int REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE = 101;

    private ImageView updateAccountImage;
    private EditText edtUsername, edtPassword, edtEmail, edtFullname, edtPhone, edtAddress;
    private Spinner spinnerRole;
    private TextView btnUpdateAccount, btnCancel;
    private Uri imageUri;
    private RoleRepository roleRepository;
    private AccountRepository accountRepository;

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

        roleRepository = new RoleRepository(this);
        accountRepository = new AccountRepository(this);


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

        roles = roleRepository.getAllRoles();
        // Tạo Adapter tùy chỉnh và đặt cho Spinner
        roleSpinnerAdapter = new RoleSpinnerAdapter(this, roles);
        spinnerRole.setAdapter(roleSpinnerAdapter);

        // Chọn vai trò dựa trên roleId
        selectRoleByRoleId(roleId);


        // Sự kiện khi click vào ảnh để chọn hình mới
        updateAccountImage.setOnClickListener(v -> chooseImage());

        // Sự kiện khi click vào nút cập nhật tài khoản
        btnUpdateAccount.setOnClickListener(v -> updateAccount());

        // Sự kiện khi click vào nút hủy bỏ
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


    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imageChooserLauncher.launch(intent);
    }

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


        if (usernameEnter.isEmpty() || passwordEnter.isEmpty() || emailEnter.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields (*) required.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra độ dài của username
        if (usernameEnter.length() < 6) {
            Toast.makeText(this, "Username must be at least 6 characters long", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!usernameEnter.matches("[a-zA-Z0-9]+")) {
            Toast.makeText(this, "Username can only contain letters and numbers", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra email không được trùng
        if (!usernameEnter.equals(username) && accountRepository.checkUsernameExists(usernameEnter)) {
            Toast.makeText(AccountUpdateActivity.this, "This username is already in use", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra độ dài của password
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

        // Update account logic (save to database)

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
        newAccount.setAvatar(imageUri != null ? imageUri.toString() : avatar);

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

    private boolean isValidEmail(String emailAddress) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches();
    }
}