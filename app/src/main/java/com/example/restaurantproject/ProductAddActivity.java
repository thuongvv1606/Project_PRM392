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

import com.example.restaurantproject.bean.Category;
import com.example.restaurantproject.bean.Menu;
import com.example.restaurantproject.bean.Product;
import com.example.restaurantproject.bean.Restaurant;
import com.example.restaurantproject.repository.CategoryRepository;
import com.example.restaurantproject.repository.MenuRepository;
import com.example.restaurantproject.repository.ProductRepository;
import com.example.restaurantproject.repository.RestaurantRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductAddActivity extends NavigationActivity {
    private ImageView imageView;
    private ProductRepository productRepository = null;
    private CategoryRepository categoryRepository;
    private MenuRepository menuRepository;
    private EditText edtName, edtDescription, edtPrice;
    private Spinner spinCategory, spinMenu;
    private Uri imageUri;
    private ArrayAdapter<String> categoryAdapter;
    private ArrayAdapter<String> menuAdapter;
    private Map<String, Integer> categoryMap = new HashMap<>();
    private Map<String, Integer> menuMap = new HashMap<>();

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
        setContentView(R.layout.activity_product_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar_product_list);
        // Set the navigation icon click listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductAddActivity.this, ProductListActivity.class);
                startActivity(intent);
            }
        });
        // Initialize repositories
        productRepository = new ProductRepository(this);
        categoryRepository = new CategoryRepository(this);
        menuRepository = new MenuRepository(this);

        // Bind views
        edtName = findViewById(R.id.edt_product_name);
        edtDescription = findViewById(R.id.edt_product_description);
        edtPrice = findViewById(R.id.edt_product_price);
        imageView = findViewById(R.id.edt_product_image);
        spinCategory = findViewById(R.id.spinner_category_id);
        spinMenu = findViewById(R.id.spinner_menu_id);

        // Set up image chooser
        imageView.setOnClickListener(v -> openImageChooser());

        // Fetch category and populate spinner
        populateCategorySpinner();

        // Fetch menu and populate spinner
        populateMenuSpinner();

        // Set up button click listener
        Button addBtn = findViewById(R.id.btn_add_product);
        addBtn.setOnClickListener(v -> addProduct());

        Button cancelBtn = findViewById(R.id.btn_cancel_add_product);
        cancelBtn.setOnClickListener(v -> finish());
    }

    private void populateCategorySpinner() {
        // Fetch Category data
        List<Category> categoryList = categoryRepository.getAllCategories();
        List<String> categoryNames = new ArrayList<>();

        // Populate the map and the list of names
        for (Category category : categoryList) {
            categoryNames.add(category.getCategoryName());
            categoryMap.put(category.getCategoryName(), category.getCategoryId());
        }

        // Create an adapter for the spinner
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCategory.setAdapter(categoryAdapter);
    }

    private void populateMenuSpinner() {
        // Fetch menu data
        List<Menu> menuList  = menuRepository.getAllMenus();
        List<String> menuNames = new ArrayList<>();

        // Populate the map and the list of names
        for (Menu menu : menuList) {
            menuNames.add(menu.getMenuName());
            menuMap.put(menu.getMenuName(), menu.getMenuId());
        }

        // Create an adapter for the spinner
        menuAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, menuNames);
        menuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinMenu.setAdapter(menuAdapter);
    }


    private void addProduct() {
        String name = edtName.getText().toString();
        String priceString = edtPrice.getText().toString();
        String description = edtDescription.getText().toString();

        if (name.trim().isEmpty()) {
            edtName.setError("Name cannot be empty!");
            return;
        }

        // Retrieve selected category ID
        String selectedCategoryName = (String) spinCategory.getSelectedItem();
        if (selectedCategoryName == null || selectedCategoryName.isEmpty()) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrieve selected menu ID
        String selectedMenuName = (String) spinMenu.getSelectedItem();
        if (selectedMenuName == null || selectedMenuName.isEmpty()) {
            Toast.makeText(this, "Please select a menu", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedCategoryId = categoryMap.get(selectedCategoryName);
        int selectedMenuId = menuMap.get(selectedMenuName);
        double price;
        try {
            price = Double.parseDouble(priceString);
        } catch (NumberFormatException e) {
            edtPrice.setError("Invalid price format!");
            return;
        }



        // Create a new Menu object
        Product product = new Product();
        product.setProductName(name);
        product.setProductDescription(description);
        product.setPrice(price);
        product.setCategoryId(selectedCategoryId);
        product.setMenuId(selectedMenuId);

        if (imageUri != null) {
            product.setProductImage(imageUri.toString());
        } else {
            product.setProductImage(null);
        }

        // Add menu to database
        if (productRepository != null) {
            productRepository.createProduct(product);
            Toast.makeText(this, "Product added successfully", Toast.LENGTH_LONG).show();
            startActivity(new Intent(ProductAddActivity.this, ProductListActivity.class));
        } else {
            Toast.makeText(this, "Product repository is not initialized", Toast.LENGTH_LONG).show();
        }
    }

    private void openImageChooser() {
        // Mở activity để chọn hình ảnh từ bộ nhớ ngoài
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imageChooserLauncher.launch(intent);
    }}