package com.example.restaurantproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.restaurantproject.bean.Category;
import com.example.restaurantproject.repository.CategoryRepository;
import com.example.restaurantproject.repository.ProductRepository;
import com.example.restaurantproject.ultils.helper.SaveImageToStorage;

import java.io.IOException;

public class CategoryAddActivity extends AppCompatActivity {
    private ImageView imageView;
    private CategoryRepository categoryRepository = null;
    private EditText edtName, edtDescription;

    private Uri imageUri;

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
                            imageView.setImageBitmap(bitmap);
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
        setContentView(R.layout.activity_category_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar_category_list);
        // Set the navigation icon click listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryAddActivity.this, CategoryListActivity.class);
                startActivity(intent);
            }
        });

        categoryRepository = new CategoryRepository(this);
        saveImageToStorage = new SaveImageToStorage(this);
        edtName = findViewById(R.id.edt_category_name);
        edtDescription = findViewById(R.id.edt_category_description);
        imageView = findViewById(R.id.edt_category_image);

        // Sự kiện chọn ảnh đại diện
        imageView.setOnClickListener(v -> openImageChooser());

        Button addBtn = findViewById(R.id.btn_add_category);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category category = new Category();
                String name = edtName.getText().toString();
                if (name.trim().isEmpty()) {
                    edtName.setError("Name cannot be empty!");
                    return;
                }
                if (categoryRepository.getByName(name.trim(), 0) != null) {
                    edtName.setError("This name existed!");
                    return;
                }
                category.setCategoryName(name);
                category.setCategoryDescription(edtDescription.getText().toString());
                if (imageUri != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        String imagePath = saveImageToStorage.saveImageToStorage(bitmap);
                        category.setCategoryImage(imagePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(CategoryAddActivity.this, "Failed to save image", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    category.setCategoryImage(null);
                }

                if (categoryRepository != null) {
                    categoryRepository.createCategory(category);
                    Toast.makeText(CategoryAddActivity.this, "Add category successfully",
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CategoryAddActivity.this, CategoryListActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(CategoryAddActivity.this, "Category repository is not initialized", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button cancelBtn = findViewById(R.id.btn_cancel_add_category);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void openImageChooser() {
        // Mở activity để chọn hình ảnh từ bộ nhớ ngoài
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imageChooserLauncher.launch(intent);
    }
}