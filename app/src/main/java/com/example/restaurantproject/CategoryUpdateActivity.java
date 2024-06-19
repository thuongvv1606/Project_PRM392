package com.example.restaurantproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.restaurantproject.bean.Category;
import com.example.restaurantproject.repository.CategoryRepository;

import java.io.IOException;
import java.io.InputStream;

public class CategoryUpdateActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private CategoryRepository categoryRepository = null;
    private TextView txt_id, txt_name, txt_description;
    private ImageView txt_image;
    private Uri imageUri;
    private static final int REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE = 101;

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

        categoryRepository = new CategoryRepository(this);

        txt_id = findViewById(R.id.edit_category_id);
        txt_name = findViewById(R.id.edit_category_name);
        txt_image = findViewById(R.id.edit_category_image);
        txt_description = findViewById(R.id.edit_category_description);

        Intent intent = getIntent();
        int id = intent.getIntExtra("category_id", -1);
        txt_id.setText(String.valueOf(id));
        String name = intent.getStringExtra("category_name");
        txt_name.setText(name);
        String description = intent.getStringExtra("category_description");
        txt_description.setText(description);
        String imageUriString = intent.getStringExtra("category_image");
        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            loadImageFromUri(imageUri);
        }

        Button btnSelectImage = findViewById(R.id.button_select_image_update_category);

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        Button updateBtn = findViewById(R.id.btn_update_category);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category category = new Category();
                category.setCategoryId(id);
                String name = txt_name.getText().toString();
                if (name.trim().isEmpty()) {
                    txt_name.setError("Name cannot be empty!");
                    return;
                }
                if (categoryRepository.getByName(name.trim(), id) != null) {
                    txt_name.setError("This name existed!");
                    return;
                }
                category.setCategoryName(name);
                category.setCategoryDescription(txt_description.getText().toString());
                if (imageUriString != null) {
                    category.setCategoryImage(imageUriString);
                } else {
                    category.setCategoryImage("");
                }

                if (categoryRepository != null) {
                    categoryRepository.updateCategory(category);
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
                Intent intent = new Intent(CategoryUpdateActivity.this, CategoryListActivity.class);
                startActivity(intent);
            }
        });
    }



    @SuppressLint("WrongConstant")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            final int takeFlags = data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            getContentResolver().takePersistableUriPermission(imageUri, takeFlags);

            txt_image.setImageURI(imageUri);
        }
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
}