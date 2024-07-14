package com.example.restaurantproject;

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
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.restaurantproject.bean.Restaurant;
import com.example.restaurantproject.repository.RestaurantRepository;
import com.example.restaurantproject.ultils.helper.SaveImageToStorage;

import java.io.IOException;

public class RestaurantAddActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_restaurant_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar_restaurant_list);
        // Set the navigation icon click listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantAddActivity.this, RestaurantListActivity.class);
                startActivity(intent);
            }
        });

        restaurantRepository = new RestaurantRepository(this);
        saveImageToStorage = new SaveImageToStorage(this);

        txt_name = findViewById(R.id.edt_restaurant_add_name);
        txt_email = findViewById(R.id.edt_restaurant_add_email);
        txt_phone = findViewById(R.id.edt_restaurant_add_phone);
        txt_address = findViewById(R.id.edt_restaurant_add_address);
        txt_description = findViewById(R.id.edt_restaurant_add_description);
        imageView = findViewById(R.id.img_restaurant_add_image);

        // Sự kiện chọn ảnh đại diện
        imageView.setOnClickListener(v -> openImageChooser());

        Button btnAdd = findViewById(R.id.btn_add_restaurant);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Restaurant restaurant = new Restaurant();
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
                        Toast.makeText(RestaurantAddActivity.this, "Failed to save image", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    restaurant.setRestaurantImage(null); // Cập nhật ảnh đại diện cũ khi không có thay đổi
                }

                if (restaurantRepository != null){
                    Restaurant restaurantResult = restaurantRepository.checkDouble(name, txt_address.getText().toString());
                    if (restaurantResult == null) {
                        restaurantRepository.createRestaurant(restaurant);
                        Toast.makeText(RestaurantAddActivity.this, "Create new restaurant successfully",
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RestaurantAddActivity.this, RestaurantListActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(RestaurantAddActivity.this, "Restaurant repository is already exits", Toast.LENGTH_LONG).show();
                    }

                }else {
                    Toast.makeText(RestaurantAddActivity.this, "Restaurant repository is not initialized", Toast.LENGTH_LONG).show();
                }
                Intent intent = new Intent(RestaurantAddActivity.this, RestaurantListActivity.class);
                startActivity(intent);
            }
        });

        Button btnCancel = findViewById(R.id.btn_cancel_add_restaurant);
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