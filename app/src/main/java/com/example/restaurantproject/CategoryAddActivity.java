package com.example.restaurantproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.restaurantproject.bean.Category;
import com.example.restaurantproject.repository.CategoryRepository;
import com.example.restaurantproject.repository.ProductRepository;

public class CategoryAddActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private CategoryRepository categoryRepository = null;
    private EditText edtName, edtDescription;
    private Uri imageUri;

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

        categoryRepository = new CategoryRepository(this);
        edtName = findViewById(R.id.edt_category_name);
        edtDescription = findViewById(R.id.edt_category_description);
        Button btnSelectImage = findViewById(R.id.btn_select_image_add_category);
        imageView = findViewById(R.id.edt_category_image);

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

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
                    category.setCategoryImage(imageUri.toString());
                } else {
                    category.setCategoryImage("");
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
                Intent intent = new Intent(CategoryAddActivity.this, CategoryListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            imageView.setVisibility(View.VISIBLE); // Make ImageView visible after image selection
        }
    }

}