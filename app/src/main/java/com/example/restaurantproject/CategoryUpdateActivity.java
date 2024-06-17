package com.example.restaurantproject;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // Show an explanation to the user asynchronously
            new AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("This permission is needed to load images from your device")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Request the permission again
                            ActivityCompat.requestPermissions(CategoryUpdateActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();
        } else {
            // Request the permission
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE);
        } else {
            loadImageFromIntent();
        }

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
                if (imageUri != null) {
                    category.setCategoryImage(imageUri.toString());
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

    private void loadImageFromIntent() {
        Intent intent = getIntent();
        String image = intent.getStringExtra("category_image");
        Uri imageUri = Uri.parse(image);
        txt_image.setImageURI(imageUri);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadImageFromIntent();
            } else {
                Toast.makeText(this, "Permission denied to read external storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            txt_image.setImageURI(imageUri);
        }
    }
}