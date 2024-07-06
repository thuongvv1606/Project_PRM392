package com.example.restaurantproject;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.restaurantproject.bean.Menu;
import com.example.restaurantproject.bean.Restaurant;
import com.example.restaurantproject.repository.MenuRepository;
import com.example.restaurantproject.repository.RestaurantRepository;
import com.example.restaurantproject.ultils.helper.SaveImageToStorage;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MenuUpdateActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private MenuRepository menuRepository = null;
    private TextView txt_name, txt_description;
    private ImageView txt_image;
    private Spinner spinRestaurant;
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
        setContentView(R.layout.activity_menu_update);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar_menu_list);
        // Set the navigation icon click listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuUpdateActivity.this, MenuListActivity.class);
                startActivity(intent);
            }
        });

        menuRepository = new MenuRepository(this);
        saveImageToStorage = new SaveImageToStorage(this);
        txt_name = findViewById(R.id.edit_menu_name);
        txt_image = findViewById(R.id.edit_menu_image);
        txt_description = findViewById(R.id.edit_menu_description);
        spinRestaurant = findViewById(R.id.edit_spinner_restaurant_id);

        Intent intent = getIntent();
        int id = intent.getIntExtra("menu_id", -1);
        Menu menu = menuRepository.getMenu(id);

        // Load list of restaurants
        RestaurantRepository restaurantRepository = new RestaurantRepository(this);
        List<Restaurant> restaurants = restaurantRepository.getAllRestaurants();
        List<String> restaurantNames = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            restaurantNames.add(restaurant.getRestaurantName());
        }

        // Set up the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, restaurantNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinRestaurant.setAdapter(adapter);

        // Select the current restaurant in the spinner
        String currentRestaurantName = restaurantRepository.getRestaurant(menu.getRestaurantId()).getRestaurantName();
        int restaurantPosition = adapter.getPosition(currentRestaurantName);
        spinRestaurant.setSelection(restaurantPosition);

        txt_name.setText(menu.getMenuName());
        txt_description.setText(menu.getMenuDescription());
        // Sự kiện chọn ảnh đại diện
        txt_image.setOnClickListener(v -> openImageChooser());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }

        // Load ảnh đại diện từ URL
        if (menu.getMenuImage()  != null && !menu.getMenuImage().isEmpty()) {
            if (menu.getMenuImage().startsWith("content://")) {
                Glide.with(this).load(Uri.parse(menu.getMenuImage())).into(txt_image);
            } else {
                Glide.with(this).load(menu.getMenuImage()).into(txt_image);
            }
        }

        Button updateBtn = findViewById(R.id.btn_update_menu);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Menu menu1 = new Menu();
                menu1.setMenuId(id);
                String name = txt_name.getText().toString();
                if (name.trim().isEmpty()) {
                    txt_name.setError("Name cannot be empty!");
                    return;
                }
                if (menuRepository.getByName(name.trim(), id) != null) {
                    txt_name.setError("This name existed!");
                    return;
                }
                menu1.setMenuName(name);
                menu1.setMenuDescription(txt_description.getText().toString());

                if (imageUri != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        String imagePath = saveImageToStorage.saveImageToStorage(bitmap);
                        menu1.setMenuImage(imagePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(MenuUpdateActivity.this, "Failed to save image", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    menu1.setMenuImage(menu.getMenuImage());
                }

                // Update restaurantId based on spinner selection
                int selectedPosition = spinRestaurant.getSelectedItemPosition();
                Restaurant selectedRestaurant = restaurants.get(selectedPosition);
                menu1.setRestaurantId(selectedRestaurant.getRestaurantId());

                if (menuRepository != null) {
                    menuRepository.updateMenu(menu1);
                    Toast.makeText(MenuUpdateActivity.this, "Update menu successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MenuUpdateActivity.this, MenuListActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MenuUpdateActivity.this, "Menu repository is not initialized", Toast.LENGTH_LONG).show();
                }
                Intent intent = new Intent(MenuUpdateActivity.this, MenuListActivity.class);
                startActivity(intent);
            }
        });

        Button toListBtn = findViewById(R.id.btn_cancel_update_menu);
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
