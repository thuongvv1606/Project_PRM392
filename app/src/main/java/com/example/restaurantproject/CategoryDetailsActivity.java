package com.example.restaurantproject;

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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.Manifest;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

public class CategoryDetailsActivity extends AppCompatActivity {

    private TextView txt_id, txt_name, txt_description;
    private ImageView txt_image;
    private static final int REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE = 101;

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

        txt_id = findViewById(R.id.txt_category_id);
        txt_name = findViewById(R.id.txt_category_name);
        txt_image = findViewById(R.id.txt_category_image);
        txt_description = findViewById(R.id.txt_category_description);

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

        Button toUpdateBtn = findViewById(R.id.btn_toupdate_category);
        toUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryDetailsActivity.this, CategoryUpdateActivity.class);
                intent.putExtra("category_id", id);
                intent.putExtra("category_name", name);
                intent.putExtra("category_description", description);
                intent.putExtra("category_image", imageUriString);
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