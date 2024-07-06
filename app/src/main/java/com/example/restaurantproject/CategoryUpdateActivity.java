package com.example.restaurantproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.restaurantproject.bean.Category;
import com.example.restaurantproject.repository.CategoryRepository;
import com.example.restaurantproject.ultils.helper.SaveImageToStorage;

import java.io.IOException;
import java.io.InputStream;

public class CategoryUpdateActivity extends AppCompatActivity {
    private CategoryRepository categoryRepository = null;
    private TextView txt_name, txt_description;
    private ImageView txt_image;
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
                            txt_image.setImageBitmap(bitmap);
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
        setContentView(R.layout.activity_category_update);
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
                Intent intent = new Intent(CategoryUpdateActivity.this, CategoryListActivity.class);
                startActivity(intent);
            }
        });

        categoryRepository = new CategoryRepository(this);
        saveImageToStorage = new SaveImageToStorage(this);
        txt_name = findViewById(R.id.edit_category_name);
        txt_image = findViewById(R.id.edit_category_image);
        txt_description = findViewById(R.id.edit_category_description);

        Intent intent = getIntent();
        int id = intent.getIntExtra("category_id", -1);
        Category category = categoryRepository.getCategory(id);
        txt_name.setText(category.getCategoryName());
        txt_description.setText(category.getCategoryDescription());
        // Sự kiện chọn ảnh đại diện
        txt_image.setOnClickListener(v -> openImageChooser());

        // Load ảnh đại diện từ URL
        if (category.getCategoryImage()  != null && !category.getCategoryImage().isEmpty()) {
            if (category.getCategoryImage().startsWith("content://")) {
                Glide.with(this).load(Uri.parse(category.getCategoryImage())).into(txt_image);
            } else {
                Glide.with(this).load(category.getCategoryImage()).into(txt_image);
            }
        }

        Button updateBtn = findViewById(R.id.btn_update_category);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category category1 = new Category();
                category1.setCategoryId(id);
                String name = txt_name.getText().toString();
                if (name.trim().isEmpty()) {
                    txt_name.setError("Name cannot be empty!");
                    return;
                }
                if (categoryRepository.getByName(name.trim(), id) != null) {
                    txt_name.setError("This name existed!");
                    return;
                }
                category1.setCategoryName(name);
                category1.setCategoryDescription(txt_description.getText().toString());

                if (imageUri != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        String imagePath = saveImageToStorage.saveImageToStorage(bitmap);
                        category1.setCategoryImage(imagePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(CategoryUpdateActivity.this, "Failed to save image", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    category1.setCategoryImage(category.getCategoryImage());
                }

                if (categoryRepository != null) {
                    categoryRepository.updateCategory(category1);
                    Toast.makeText(CategoryUpdateActivity.this, "Update category successfully",
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CategoryUpdateActivity.this, CategoryListActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(CategoryUpdateActivity.this, "Category repository is not initialized", Toast.LENGTH_LONG).show();
                }
                Intent intent = new Intent(CategoryUpdateActivity.this, CategoryListActivity.class);
                startActivity(intent);
            }
        });

        Button toListBtn = findViewById(R.id.btn_cancel_update_category);
        toListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadImageFromUri(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            txt_image.setImageBitmap(bitmap);
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openImageChooser() {
        // Mở activity để chọn hình ảnh từ bộ nhớ ngoài
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imageChooserLauncher.launch(intent);
    }
}