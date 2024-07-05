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
import com.example.restaurantproject.bean.Category;
import com.example.restaurantproject.bean.Menu;
import com.example.restaurantproject.bean.Product;
import com.example.restaurantproject.bean.Restaurant;
import com.example.restaurantproject.repository.CategoryRepository;
import com.example.restaurantproject.repository.MenuRepository;
import com.example.restaurantproject.repository.ProductRepository;
import com.example.restaurantproject.repository.RestaurantRepository;
import com.example.restaurantproject.ultils.helper.SaveImageToStorage;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ProductUpdateActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private ProductRepository productRepository = null;
    private TextView txt_name, txt_description, txt_price;
    private ImageView txt_image;
    private Spinner spinCategory, spinMenu;
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
        setContentView(R.layout.activity_product_update);
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
                Intent intent = new Intent(ProductUpdateActivity.this, ProductListActivity.class);
                startActivity(intent);
            }
        });

        productRepository = new ProductRepository(this);
        saveImageToStorage = new SaveImageToStorage(this);
        txt_name = findViewById(R.id.edit_product_name);
        txt_image = findViewById(R.id.edit_product_image);
        txt_description = findViewById(R.id.edit_product_description);
        txt_price = findViewById(R.id.edit_product_price);
        spinCategory = findViewById(R.id.edit_spinner_category_id);
        spinMenu = findViewById(R.id.edit_spinner_menu_id);

        Intent intent = getIntent();
        int id = intent.getIntExtra("product_id", -1);
        Product product = productRepository.getProduct(id);

        // Load list of category
        CategoryRepository categoryRepository = new CategoryRepository(this);
        List<Category> categories = categoryRepository.getAllCategories();
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categories) {
            categoryNames.add(category.getCategoryName());
        }

        // Load list of menu
        MenuRepository menuRepository = new MenuRepository(this);
        List<Menu> menus = menuRepository.getAllMenus();
        List<String> menuNames = new ArrayList<>();
        for (Menu menu : menus) {
            menuNames.add(menu.getMenuName());
        }

        // Set up the spinner
        ArrayAdapter<String> adapterc = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        adapterc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCategory.setAdapter(adapterc);

        ArrayAdapter<String> adapterm = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, menuNames);
        adapterm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinMenu.setAdapter(adapterm);



        // Select the current restaurant in the spinner
        String currentCategoryName = categoryRepository.getCategory(product.getCategoryId()).getCategoryName();
        int categoryPosition = adapterc.getPosition(currentCategoryName);
        spinCategory.setSelection(categoryPosition);

        String currentMenuName = menuRepository.getMenu(product.getMenuId()).getMenuName();
        int menuPosition = adapterm.getPosition(currentMenuName);
        spinCategory.setSelection(menuPosition);

        txt_name.setText(product.getProductName());
        txt_description.setText(product.getProductDescription());
        txt_price.setText("" + product.getPrice());
        // Sự kiện chọn ảnh đại diện
        txt_image.setOnClickListener(v -> openImageChooser());

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }

        // Load ảnh đại diện từ URL
        if (product.getProductImage()  != null && !product.getProductImage().isEmpty()) {
            if (product.getProductImage().startsWith("content://")) {
                Glide.with(this).load(Uri.parse(product.getProductImage())).into(txt_image);
            } else {
                Glide.with(this).load(product.getProductImage()).into(txt_image);
            }
        }

        Button updateBtn = findViewById(R.id.btn_update_product);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = new Product();
                product.setMenuId(id);
                String name = txt_name.getText().toString();
                if (name.trim().isEmpty()) {
                    txt_name.setError("Name cannot be empty!");
                    return;
                }
                if (productRepository.getByName(name.trim(), id) != null) {
                    txt_name.setError("This name existed!");
                    return;
                }
                product.setProductName(name);

                String priceText = txt_price.getText().toString();
                // Initialize the price variable
                double price = Double.parseDouble(priceText);
                product.setPrice(price);
                product.setProductDescription(txt_description.getText().toString());

                if (imageUri != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        String imagePath = saveImageToStorage.saveImageToStorage(bitmap);
                        product.setProductImage(imagePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(ProductUpdateActivity.this, "Failed to save image", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    product.setProductImage(null);
                }

                // Update categoryId based on spinner selection
                int selectedPositionC = spinCategory.getSelectedItemPosition();
                Category selectedCategory = categories.get(selectedPositionC);
                product.setCategoryId(selectedCategory.getCategoryId());

                int selectedPositionM = spinMenu.getSelectedItemPosition();
                Menu selectedMenu = menus.get(selectedPositionM);
                product.setMenuId(selectedMenu.getMenuId());

                if (productRepository != null) {
                    productRepository.updateProduct(product);
                    Toast.makeText(ProductUpdateActivity.this, "Update product successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ProductUpdateActivity.this, ProductListActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ProductUpdateActivity.this, "Product repository is not initialized", Toast.LENGTH_LONG).show();
                }
                Intent intent = new Intent(ProductUpdateActivity.this, ProductListActivity.class);
                startActivity(intent);
            }
        });

        Button toListBtn = findViewById(R.id.btn_cancel_update_product);
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
