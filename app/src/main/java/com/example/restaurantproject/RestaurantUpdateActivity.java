package com.example.restaurantproject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.restaurantproject.bean.Restaurant;
import com.example.restaurantproject.repository.RestaurantRepository;

public class RestaurantUpdateActivity extends AppCompatActivity {

    private RestaurantRepository restaurantRepository = null;
    private TextView txt_name, txt_description, txt_phone, txt_address, txt_email;
    private ImageView iv_image;
    private String selectedImageName;
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

        txt_name = findViewById(R.id.edt_restaurant_update_name);
        txt_email = findViewById(R.id.edt_restaurant_update_email);
        txt_phone = findViewById(R.id.edt_restaurant_update_phone);
        txt_address = findViewById(R.id.edt_restaurant_update_address);
        txt_description = findViewById(R.id.edt_restaurant_update_description);
        iv_image = findViewById(R.id.img_update_restaurant);

        Intent intent = getIntent();
        int id = intent.getIntExtra("restaurant_id", -1);
        String name = intent.getStringExtra("restaurant_name");
        txt_name.setText(name);
        String email = intent.getStringExtra("restaurant_email");
        txt_email.setText(email);
        String phone = intent.getStringExtra("restaurant_phone_number");
        txt_phone.setText(phone);
        String address = intent.getStringExtra("restaurant_address");
        txt_address.setText(address);
        String description = intent.getStringExtra("restaurant_description");
        txt_description.setText(description);
        String image = intent.getStringExtra("restaurant_image");
        if (!image.isEmpty()) {
            @SuppressLint("DiscouragedApi") int imageResId = getResources().getIdentifier(image, "drawable", getPackageName());
            if (imageResId != 0) {
                iv_image.setImageResource(imageResId);
            } else {
                iv_image.setImageResource(R.drawable.logo);
            }
        } else {
            iv_image.setImageResource(R.drawable.editbutton);
        }

        Button btnSelectImg = findViewById(R.id.btn_select_image_update_restaurant);
        btnSelectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageSelectionDialog();
            }
        });

        restaurantRepository = new RestaurantRepository(this);
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
                restaurant.setRestaurantImage(selectedImageName);
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

    private void showImageSelectionDialog() {
        // Array of image names without file extensions
        String[] imageNames = {"kfc", "lotte", "mcdonalds"}; // Replace with your actual image names

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image");
        builder.setItems(imageNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedImageName = imageNames[which];
                setImage(selectedImageName);
                dialog.dismiss();
            }
        });

        AlertDialog imageDialog = builder.create();
        imageDialog.show();
    }



    private void setImage(String imageName) {
        int imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());
        if (imageResId != 0) {
            iv_image.setImageResource(imageResId);
        } else {
            Toast.makeText(this, "Image resource not found", Toast.LENGTH_SHORT).show();
        }
    }
}