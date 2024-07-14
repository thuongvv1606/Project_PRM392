package com.example.restaurantproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantproject.adapter.DeliveryListAdapter;
import com.example.restaurantproject.adapter.DeliveryOrderDetailAdapter;
import com.example.restaurantproject.bean.Category;
import com.example.restaurantproject.entity.DeliveryDTO;
import com.example.restaurantproject.entity.DeliveryOrderInfo;
import com.example.restaurantproject.entity.DeliveryProductDetail;
import com.example.restaurantproject.repository.CategoryRepository;
import com.example.restaurantproject.repository.DeliveryRepository;
import com.example.restaurantproject.repository.RestaurantRepository;

import java.util.List;

public class DeliveryDetailActivity extends AppCompatActivity {

    private TextView tv_order_id, tvCustomerName, tvCustomerPhone, tvCustomerAddress, tvTotal, tvDeliveryID, tvDeliveryName, tvDeliveryStatus, tvDeliveryPhone, tvRestaurantName;
    private ImageView imageView;
    private Uri imageUri;
    private DeliveryRepository repository = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delivery_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        repository = new DeliveryRepository(this);
        Toolbar toolbar = findViewById(R.id.toolbar_delivery_list);
        // Set the navigation icon click listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeliveryDetailActivity.this, DeliveryListActivity.class);
                startActivity(intent);
            }
        });

        tv_order_id = findViewById(R.id.tv_order_id);
        tvCustomerName = findViewById(R.id.tv_customer_name);
        tvCustomerAddress = findViewById(R.id.tv_customer_address);
        tvCustomerPhone = findViewById(R.id.tv_customer_phone);
        tvDeliveryID = findViewById(R.id.tv_delivery_id);
        tvDeliveryName = findViewById(R.id.tv_deliverer_name);
        tvDeliveryStatus = findViewById(R.id.tv_delivery_status);
        tvDeliveryPhone = findViewById(R.id.tv_deliverer_phone);


        Intent intent = getIntent();
        int id = intent.getIntExtra("Deliver_ID", -1);
        DeliveryOrderInfo deliveryOrderInfo = repository.orderInfo(id);
        tv_order_id.setText("" + deliveryOrderInfo.getOrderId());
        tvCustomerName.setText("" + deliveryOrderInfo.getCustomerName());
        tvCustomerAddress.setText("" + deliveryOrderInfo.getCustomerAddress());
        tvCustomerPhone.setText("" + deliveryOrderInfo.getCustomerPhoneNumber());
        tvDeliveryID.setText("" + deliveryOrderInfo.getDeliveryId());
        tvDeliveryName.setText("" + deliveryOrderInfo.getDelivererName());
        tvDeliveryPhone.setText("" + deliveryOrderInfo.getDelivererPhoneNumber());
        int status = deliveryOrderInfo.getDeliveryStatus();
        if (status == 1) {
            tvDeliveryStatus.setText("Not yet");
        }
        if (status == 2) {
            tvDeliveryStatus.setText("Ongoing");
        }
        if (status == 3) {
            tvDeliveryStatus.setText(" Done");
        }
        if (status == 4) {
            tvDeliveryStatus.setText("Cancel");
        }

        List<DeliveryProductDetail> list = repository.productDetail(id);
        setList(list);

        double sum = 0;
        for (DeliveryProductDetail deliveryProductDetail : list) {
            sum += deliveryProductDetail.getPrice() * deliveryProductDetail.getQuantity();
        }
        tvTotal = findViewById(R.id.tv_total);
        tvTotal.setText("" + sum);



    }

    private void setList(List<DeliveryProductDetail> list) {
                RecyclerView recyclerView = findViewById(R.id.delivery_order_recyle_view);
        DeliveryOrderDetailAdapter listAdapter = new DeliveryOrderDetailAdapter(list, this);
        recyclerView.setAdapter(listAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}