package com.example.restaurantproject;

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

import android.Manifest;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.restaurantproject.bean.Category;
import com.example.restaurantproject.repository.CategoryRepository;

import java.io.IOException;
import java.io.InputStream;

public class CategoryDetailsActivity extends AppCompatActivity {
    private TextView txt_name, txt_description;
    private ImageView txt_image;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txt_name = findViewById(R.id.txt_category_name);
        txt_image = findViewById(R.id.txt_category_image);
        txt_description = findViewById(R.id.txt_category_description);

        Intent intent = getIntent();
        int id = intent.getIntExtra("category_id", -1);
        CategoryRepository categoryRepository = new CategoryRepository(this);
        Category category = categoryRepository.getCategory(id);
        txt_name.setText(category.getCategoryName());
        txt_description.setText(category.getCategoryDescription());

        if (category.getCategoryImage() != null) {
            imageUri = Uri.parse(category.getCategoryImage());
            Glide.with(this).load(imageUri).into(txt_image);
        }

        Button toUpdateBtn = findViewById(R.id.btn_toupdate_category);
        toUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryDetailsActivity.this, CategoryUpdateActivity.class);
                intent.putExtra("category_id", id);
                startActivity(intent);
            }
        });

        Button toListBtn = findViewById(R.id.btn_tolist_category);
        toListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryDetailsActivity.this, CategoryListActivity.class);
                startActivity(intent);
            }
        });
    }
}