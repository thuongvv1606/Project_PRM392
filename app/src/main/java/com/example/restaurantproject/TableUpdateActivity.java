package com.example.restaurantproject;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import com.example.restaurantproject.bean.Restaurant;
import com.example.restaurantproject.bean.Table;
import com.example.restaurantproject.repository.CategoryRepository;
import com.example.restaurantproject.repository.MenuRepository;
import com.example.restaurantproject.repository.ProductRepository;
import com.example.restaurantproject.repository.RestaurantRepository;
import com.example.restaurantproject.repository.TableRepository;
import com.example.restaurantproject.ultils.constant.Common;
import com.example.restaurantproject.ultils.helper.SaveImageToStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TableUpdateActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private TableRepository tableRepository = null;
    private SaveImageToStorage saveImageToStorage;
    private TextView txt_name, txt_num_seat;
    private Spinner spnStatus, spnRestaurant;
    private ImageView imageView;
    private Uri imageUri;
    private RestaurantRepository restaurantRepository;
    private List<Restaurant> restaurants;

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
        setContentView(R.layout.activity_table_update);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar_table_list);
        // Set the navigation icon click listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TableUpdateActivity.this, TableListActivity.class);
                startActivity(intent);
            }
        });

        tableRepository = new TableRepository(this);
        restaurantRepository = new RestaurantRepository(this);

        txt_name = findViewById(R.id.edt_table_add_name);
        imageView = findViewById(R.id.edt_table_image);
        spnStatus = findViewById(R.id.spn_table_status);
        spnRestaurant = findViewById(R.id.spn_table_restaurant_add);
        txt_num_seat = findViewById(R.id.edt_table_number_seat);

        Intent intent = getIntent();
        int id = intent.getIntExtra("table_id", -1);
        Table table = tableRepository.getTable(id);

        // Load list of category
        restaurants = restaurantRepository.getAllRestaurants();
        List<String> restaurantNames = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            restaurantNames.add(restaurant.getRestaurantName());
        }

        // Load list of status
        List<String> status = Common.tableStatus;

        // Set up the spinner
        ArrayAdapter<String> adapterc = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, restaurantNames);
        adapterc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnRestaurant.setAdapter(adapterc);

        ArrayAdapter<String> adapterm = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, status);
        adapterm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnStatus.setAdapter(adapterm);

        // Select the current restaurant in the spinner
        String currentRestaurant = restaurantRepository.getRestaurant(table.getRestaurantId()).getRestaurantName();
        int categoryPosition = adapterc.getPosition(currentRestaurant);
        spnRestaurant.setSelection(categoryPosition);

        spnStatus.setSelection(table.getStatus()-1);

        txt_name.setText(table.getName());
        txt_num_seat.setText(table.getSeatNum()+"");

        // Sự kiện chọn ảnh đại diện
        imageView.setOnClickListener(v -> openImageChooser());

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }

        if (table.getTableImage() != null && !table.getTableImage().isEmpty()) {
            imageUri = Uri.parse(table.getTableImage());
            Glide.with(this).load(table.getTableImage()).into(imageView);
        }

        Button toUpdateBtn = findViewById(R.id.btn_update_table);
        toUpdateBtn.setOnClickListener(v -> updateMenu());

        Button toCancelBtn = findViewById(R.id.btn_cancel_table);
        toCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateMenu() {
        String name = txt_name.getText().toString();
        String numberSeat = txt_num_seat.getText().toString();

        if (name.trim().isEmpty()) {
            txt_name.setError("Name cannot be empty!");
            return;
        }
        if (numberSeat.trim().isEmpty()) {
            txt_num_seat.setError("Number of Seat cannot be empty!");
            return;
        }else if (!numberSeat.matches("^[0-9]+$")){
            txt_num_seat.setError("Number of Seat must be a number!");
            return;
        }

        // Update categoryId based on spinner selection
        int selectedPositionR = spnRestaurant.getSelectedItemPosition();
        Restaurant selectedRestaurant = restaurants.get(selectedPositionR);

        int selectedPositionS = spnStatus.getSelectedItemPosition();

        // Create a new Menu object
        Table table = new Table();
        table.setName(name);
        table.setSeatNum(Integer.parseInt(numberSeat));
        table.setRestaurantId(selectedRestaurant.getRestaurantId());
        table.setStatus(selectedPositionS + 1);

        if (imageUri != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                String imagePath = saveImageToStorage.saveImageToStorage(bitmap);
                table.setTableImage(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(TableUpdateActivity.this, "Failed to save image", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            table.setTableImage("");
        }

        // Add menu to database
        if (tableRepository != null) {
            tableRepository.createTable(table);
            Toast.makeText(this, "Table updated successfully", Toast.LENGTH_LONG).show();
            startActivity(new Intent(TableUpdateActivity.this, TableListActivity.class));
        } else {
            Toast.makeText(this, "Table repository is not initialized", Toast.LENGTH_LONG).show();
        }
    }

    private void openImageChooser() {
        // Mở activity để chọn hình ảnh từ bộ nhớ ngoài
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imageChooserLauncher.launch(intent);
    }
}