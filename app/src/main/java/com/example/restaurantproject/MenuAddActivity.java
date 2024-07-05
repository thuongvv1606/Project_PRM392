package com.example.restaurantproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.restaurantproject.bean.Menu;
import com.example.restaurantproject.bean.Restaurant;
import com.example.restaurantproject.repository.MenuRepository;
import com.example.restaurantproject.repository.RestaurantRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuAddActivity extends NavigationActivity {
    private ImageView imageView;
    private MenuRepository menuRepository = null;
    private RestaurantRepository restaurantRepository;
    private EditText edtName, edtDescription;
    private Spinner spinRestaurant;
    private Uri imageUri;
    private ArrayAdapter<String> restaurantAdapter;
    private Map<String, Integer> restaurantMap = new HashMap<>();

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
        setContentView(R.layout.activity_menu_add);
        EdgeToEdge.enable(this);
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
                Intent intent = new Intent(MenuAddActivity.this, MenuListActivity.class);
                startActivity(intent);
            }
        });

        // Initialize repositories
        menuRepository = new MenuRepository(this);
        restaurantRepository = new RestaurantRepository(this);

        // Bind views
        edtName = findViewById(R.id.edt_menu_name);
        edtDescription = findViewById(R.id.edt_menu_description);
        imageView = findViewById(R.id.edt_menu_image);
        spinRestaurant = findViewById(R.id.spinner_restaurant_id);

        // Set up image chooser
        imageView.setOnClickListener(v -> openImageChooser());

        // Fetch restaurants and populate spinner
        populateRestaurantSpinner();

        // Set up button click listener
        Button addBtn = findViewById(R.id.btn_add_menu);
        addBtn.setOnClickListener(v -> addMenu());

        Button cancelBtn = findViewById(R.id.btn_cancel_add_menu);
        cancelBtn.setOnClickListener(v -> finish());
    }

    private void populateRestaurantSpinner() {
        // Fetch restaurant data
        List<Restaurant> restaurantList = restaurantRepository.getAllRestaurants();
        List<String> restaurantNames = new ArrayList<>();

        // Populate the map and the list of names
        for (Restaurant restaurant : restaurantList) {
            restaurantNames.add(restaurant.getRestaurantName());
            restaurantMap.put(restaurant.getRestaurantName(), restaurant.getRestaurantId());
        }

        // Create an adapter for the spinner
        restaurantAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, restaurantNames);
        restaurantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinRestaurant.setAdapter(restaurantAdapter);
    }

    private void addMenu() {
        String name = edtName.getText().toString();
        String description = edtDescription.getText().toString();

        if (name.trim().isEmpty()) {
            edtName.setError("Name cannot be empty!");
            return;
        }

        // Retrieve selected restaurant ID
        String selectedRestaurantName = (String) spinRestaurant.getSelectedItem();
        if (selectedRestaurantName == null || selectedRestaurantName.isEmpty()) {
            Toast.makeText(this, "Please select a restaurant", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedRestaurantId = restaurantMap.get(selectedRestaurantName);

        // Create a new Menu object
        Menu menu = new Menu();
        menu.setMenuName(name);
        menu.setMenuDescription(description);
        menu.setRestaurantId(selectedRestaurantId);

        if (imageUri != null) {
            menu.setMenuImage(imageUri.toString());
        } else {
            menu.setMenuImage(null);
        }

        // Add menu to database
        if (menuRepository != null) {
            menuRepository.createMenu(menu);
            Toast.makeText(this, "Menu added successfully", Toast.LENGTH_LONG).show();
            startActivity(new Intent(MenuAddActivity.this, MenuListActivity.class));
        } else {
            Toast.makeText(this, "Menu repository is not initialized", Toast.LENGTH_LONG).show();
        }
    }

    private void openImageChooser() {
        // Mở activity để chọn hình ảnh từ bộ nhớ ngoài
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imageChooserLauncher.launch(intent);
    }}

