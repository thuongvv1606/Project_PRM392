package com.example.restaurantproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.restaurantproject.bean.Restaurant;
import com.example.restaurantproject.repository.RestaurantRepository;
import com.example.restaurantproject.ultils.helper.SaveImageToStorage;

import java.io.IOException;

public class RestaurantUpdateActivity extends AppCompatActivity {

    private RestaurantRepository restaurantRepository = null;
    private TextView txt_name, txt_description, txt_phone, txt_address, txt_email;
    private ImageView imageView;
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
        setContentView(R.layout.activity_restaurant_update);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        restaurantRepository = new RestaurantRepository(this);
        saveImageToStorage = new SaveImageToStorage(this);

        txt_name = findViewById(R.id.edt_restaurant_update_name);
        txt_email = findViewById(R.id.edt_restaurant_update_email);
        txt_phone = findViewById(R.id.edt_restaurant_update_phone);
        txt_address = findViewById(R.id.edt_restaurant_update_address);
        txt_description = findViewById(R.id.edt_restaurant_update_description);
        imageView = findViewById(R.id.img_update_restaurant);
        // Sự kiện chọn ảnh đại diện
        imageView.setOnClickListener(v -> openImageChooser());

        Intent intent = getIntent();
        int id = intent.getIntExtra("restaurant_id", -1);
        Restaurant restaurant = restaurantRepository.getRestaurant(id);
        txt_name.setText(restaurant.getRestaurantName());
        txt_email.setText(restaurant.getEmail());
        txt_phone.setText(restaurant.getPhoneNumber());
        txt_address.setText(restaurant.getAddress());
        txt_description.setText(restaurant.getRestaurantDescription());

        // Load ảnh đại diện từ URL
        if (restaurant.getRestaurantImage() != null && !restaurant.getRestaurantImage().isEmpty()) {
            if (restaurant.getRestaurantImage() .startsWith("content://")) {
                Glide.with(this).load(Uri.parse(restaurant.getRestaurantImage())).into(imageView);
            } else {
                Glide.with(this).load(restaurant.getRestaurantImage()).into(imageView);
            }
        }


        Button btnUpdate = findViewById(R.id.btn_update_restaurant);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Restaurant restaurant = new Restaurant();
                restaurant.setRestaurantId(id);
                String name = txt_name.getText().toString();
                if (name.trim().isEmpty()) {
                    txt_name.setError("Name cannot be empty!");
                    return;
                }
                restaurant.setRestaurantName(name);
                restaurant.setEmail(txt_email.getText().toString());
                restaurant.setPhoneNumber(txt_phone.getText().toString());
                restaurant.setAddress(txt_address.getText().toString());
                restaurant.setRestaurantDescription(txt_description.getText().toString());

                if (imageUri != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        String imagePath = saveImageToStorage.saveImageToStorage(bitmap);
                        restaurant.setRestaurantImage(imagePath); // Cập nhật URI ảnh đại diện mới
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(RestaurantUpdateActivity.this, "Failed to save image", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    restaurant.setRestaurantImage(restaurant.getRestaurantImage()); // Cập nhật ảnh đại diện cũ khi không có thay đổi
                }

                if (restaurantRepository != null){
                    restaurantRepository.updateRestaurant(restaurant);
                    Toast.makeText(RestaurantUpdateActivity.this, "Update restaurant successfully",
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RestaurantUpdateActivity.this, RestaurantListActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(RestaurantUpdateActivity.this, "Restaurant repository is not initialized", Toast.LENGTH_LONG).show();
                }
                Intent intent = new Intent(RestaurantUpdateActivity.this, RestaurantListActivity.class);
                startActivity(intent);

            }
        });

        Button btnCancel = findViewById(R.id.btn_cancel_update_restaurant);
        btnCancel.setOnClickListener(new View.OnClickListener() {
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