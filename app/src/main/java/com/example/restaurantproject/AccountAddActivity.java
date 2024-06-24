package com.example.restaurantproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
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

import com.example.restaurantproject.adapter.RoleSpinnerAdapter;
import com.example.restaurantproject.bean.Account;
import com.example.restaurantproject.bean.Role;
import com.example.restaurantproject.repository.AccountRepository;
import com.example.restaurantproject.repository.RoleRepository;
import com.example.restaurantproject.ultils.helper.SaveImageToStorage;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;
import java.util.List;

public class AccountAddActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private CircularImageView addAccountImage;
    private EditText edtUsername, edtPassword, edtEmail, edtFullname, edtPhone, edtAddress;
    private Spinner spinnerRole;
    private TextView btnAddAccount, btnCancel;
    private Uri imageUri;
    private RoleRepository roleRepository;
    private AccountRepository accountRepository;
    private SaveImageToStorage saveImageToStorage;

    private RoleSpinnerAdapter roleSpinnerAdapter;
    private List<Role> roles;

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
                            addAccountImage.setImageBitmap(bitmap);
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
        setContentView(R.layout.activity_account_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo các view
        addAccountImage = findViewById(R.id.add_account_image);
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        edtEmail = findViewById(R.id.edt_email);
        edtFullname = findViewById(R.id.edt_fullname);
        edtPhone = findViewById(R.id.edt_phone);
        edtAddress = findViewById(R.id.edt_address);
        spinnerRole = findViewById(R.id.spinner_role);
        btnAddAccount = findViewById(R.id.btn_add_account);
        btnCancel = findViewById(R.id.btn_cancel);

        roleRepository = new RoleRepository(this);
        accountRepository = new AccountRepository(this);
        saveImageToStorage = new SaveImageToStorage(this);

        // Sự kiện chọn ảnh đại diện
        addAccountImage.setOnClickListener(v -> openImageChooser());

        // Sự kiện khi nhấn nút "Add Account"
        btnAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAccount();
            }
        });

        // Sự kiện khi nhấn nút "Cancel"
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            } // Close Activity
        });


        // Load danh sách vai trò vào spinner
        roles = roleRepository.getAllRoles();
        // Tạo Adapter tùy chỉnh và đặt cho Spinner
        roleSpinnerAdapter = new RoleSpinnerAdapter(this, roles);
        spinnerRole.setAdapter(roleSpinnerAdapter);
    }

    // Mở activity để chọn hình ảnh từ bộ nhớ ngoài
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imageChooserLauncher.launch(intent);
    }


    // FUnction thêm tài khoản mới
    private void addAccount() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String fullname = edtFullname.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        // Lấy đối tượng Role được chọn từ Spinner
        Role selectedRole = (Role) spinnerRole.getSelectedItem();
        if (selectedRole == null) {
            Toast.makeText(this, "Please select a role.", Toast.LENGTH_SHORT).show();
            return;
        }
        int roleId = selectedRole.getRoleId();


        // Kiểm tra các trường bắt buộc
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields (*) required.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra độ dài của username >= 6
        if (username.length() < 6) {
            Toast.makeText(this, "Username must be at least 6 characters long", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra ký tự hợp lệ của username bao gom chu va so
        if (!username.matches("[a-zA-Z0-9]+")) {
            Toast.makeText(this, "Username can only contain letters and numbers", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra độ dài của password >=7
        if (password.length() <= 6) {
            Toast.makeText(this, "Password must be longer than 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra định dạng email
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng Account mới và gán giá trị
        Account newAccount = new Account();
        newAccount.setEmail(email);
        newAccount.setUsername(username);
        newAccount.setPassword(password);
        newAccount.setRoleId(roleId);
        newAccount.setRestaurantId(null);
        newAccount.setStatus(true);
        newAccount.setFullname(fullname);
        newAccount.setPhoneNumber(phone);
        newAccount.setAddress(address);
        if (!fullname.isEmpty()) {
            newAccount.setFullname(fullname);
        } else {
            newAccount.setFullname(username); // Nếu fullname trống thì gán username làm fullname
        }

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
            newAccount.setAvatar("https://www.iconpacks.net/icons/2/free-user-icon-3297-thumb.png");
        }

        // Lưu tài khoản mới vào cơ sở dữ liệu
        long accountId = accountRepository.register(newAccount);

        // Kiểm tra kết quả add tài khoản
        if (accountId == -1) {
            Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
        } else if (accountId == -2) {
            Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show();
        } else if (accountId > 0) {
            Toast.makeText(this, "Add account successful", Toast.LENGTH_SHORT).show();
            // Chuyển sang màn hình đăng nhập (UserLoginActivity)
            Intent intent = new Intent(AccountAddActivity.this, AccountListActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(AccountAddActivity.this, "Add account failed", Toast.LENGTH_SHORT).show();
        }

    }

    // kiểm tra định dạng email hợp lệ
    private boolean isValidEmail(String emailAddress) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches();
    }
}