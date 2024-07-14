package com.example.restaurantproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantproject.adapter.DeliveryListAdapter;
import com.example.restaurantproject.entity.DeliveryDTO;
import com.example.restaurantproject.repository.DeliveryRepository;

import java.util.List;

public class DeliveryListActivity extends NavigationActivity {

    private DeliveryRepository repository = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setupContentLayout(R.layout.activity_delivery_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        repository = new DeliveryRepository(this);
        List<DeliveryDTO> deliveryDTOS = repository.getAllDelivery();
        setList(deliveryDTOS);
        TextView txtCount = findViewById(R.id.tv_delivery_count);
        txtCount.setText("Found " + repository.getAllDelivery().size() + " delivery(ies)");

//        Button btnCreateProduct = findViewById(R.id.b);
//        btnCreateProduct.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(CategoryListActivity.this, CategoryAddActivity.class);
//                startActivity(intent);
//            }
//        });

        EditText searchStr = findViewById(R.id.edt_search_delivery);
        Button searchBtn = findViewById(R.id.btn_search_delivery);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchStr.getText().toString().isEmpty()) {
                    setList(deliveryDTOS);
                }
                List<DeliveryDTO> list = repository.getAllDeliverySearch(searchStr.getText().toString());
                setList(list);
            }
        });
    }

    private void setList(List<DeliveryDTO> list) {
        RecyclerView recyclerView = findViewById(R.id.delivery_list_recyle_view);
        DeliveryListAdapter listAdapter = new DeliveryListAdapter(list, this);
        recyclerView.setAdapter(listAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}