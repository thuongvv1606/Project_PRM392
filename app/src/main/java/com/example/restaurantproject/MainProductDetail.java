package com.example.restaurantproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.restaurantproject.adapter.MainProductAdapter;
import com.example.restaurantproject.bean.Category;
import com.example.restaurantproject.bean.Menu;
import com.example.restaurantproject.bean.Order;
import com.example.restaurantproject.bean.OrderDetails;
import com.example.restaurantproject.bean.Product;
import com.example.restaurantproject.bean.Restaurant;
import com.example.restaurantproject.repository.CategoryRepository;
import com.example.restaurantproject.repository.MenuRepository;
import com.example.restaurantproject.repository.OrderDetailsRepository;
import com.example.restaurantproject.repository.OrderRepository;
import com.example.restaurantproject.repository.ProductRepository;
import com.example.restaurantproject.repository.RestaurantRepository;
import com.example.restaurantproject.ultils.session.SessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainProductDetail extends NavigationActivity {
    private OrderDetailsRepository orderDetailsRepository = null;
    private OrderRepository orderRepository = null;
    private CategoryRepository categoryRepository = null;
    private MenuRepository menuRepository = null;
    private ProductRepository productRepository = null;
    private RestaurantRepository restaurantRepository = null;
    private SessionManager sessionManager;
    private int quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setupContentLayout(R.layout.activity_main_product_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        categoryRepository = new CategoryRepository(this);
        menuRepository = new MenuRepository(this);
        productRepository = new ProductRepository(this);
        restaurantRepository = new RestaurantRepository(this);
        orderRepository = new OrderRepository(this);
        orderDetailsRepository = new OrderDetailsRepository(this);

        sessionManager = new SessionManager(this);

        Intent intent = getIntent();
        int id = intent.getIntExtra("Product_ID", -1);
        Product product = productRepository.getProduct(id);
        TextView txt_name = findViewById(R.id.product_name_detail);
        txt_name.setText(product.getProductName());
        TextView txt_description = findViewById(R.id.product_description_detail);
        txt_description.setText(product.getProductDescription());
        TextView txt_price = findViewById(R.id.product_price_detail);
        txt_price.setText(product.getPrice() + " (VND)");
        ImageView txt_image = findViewById(R.id.product_image_detail);
        if (product.getProductImage() != null) {
            Glide.with(this).load(product.getProductImage()).into(txt_image);
        }

        Category category = categoryRepository.getCategory(product.getCategoryId());
        TextView txt_category = findViewById(R.id.product_category_detail);
        txt_category.setText(category.getCategoryName());

        Menu menu = menuRepository.getMenu(product.getMenuId());
        TextView menu_name = findViewById(R.id.product_menu_name_detail);
        menu_name.setText(menu.getMenuName());
        TextView menu_description = findViewById(R.id.product_menu_description_detail);
        menu_description.setText(menu.getMenuName());
        ImageView menu_image = findViewById(R.id.product_menu_image_detail);
        if (menu.getMenuImage() != null) {
            Glide.with(this).load(menu.getMenuImage()).into(menu_image);
        }

        Restaurant restaurant = restaurantRepository.getRestaurant(menu.getRestaurantId());
        TextView menu_restaurant = findViewById(R.id.product_menu_restaurant_detail);
        menu_restaurant.setText("From " + restaurant.getRestaurantName() + " - " + restaurant.getAddress());

        List<Product> productList = productRepository.getProductsInMenu(product.getMenuId(), product.getProductId());
        setProductList(productList);
        TextView txtCountProduct = findViewById(R.id.tv_product_count);
        txtCountProduct.setText("Found " + productList.size() + " product(s)");

        EditText txt_quantity = findViewById(R.id.product_quantity_detail);
        quantity = Integer.parseInt(txt_quantity.getText().toString());
        Button minusBtn = findViewById(R.id.btn_minus);
        Button plusBtn = findViewById(R.id.btn_plus);
        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) {
                    quantity--;
                }
                txt_quantity.setText("" + quantity);
            }
        });
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                txt_quantity.setText("" + quantity);
            }
        });

        LinearLayoutCompat linearLayoutCompat = findViewById(R.id.from_menu_detail);
        linearLayoutCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainProductDetail.this, MainMenuDetail.class);
                intent1.putExtra("Menu_ID", menu.getMenuId());
                startActivity(intent1);
            }
        });
        txt_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainProductDetail.this, MainCategoryList.class);
                intent2.putExtra("Cate_ID", category.getCategoryId());
                startActivity(intent2);
            }
        });

        ImageView ic_edit = findViewById(R.id.iv_edit_icon);
        if (sessionManager.isLoggedIn() && (sessionManager.getAccountFromSession().getRoleId() == 1 ||
                ((sessionManager.getAccountFromSession().getRoleId() == 2 || sessionManager.getAccountFromSession().getRoleId() == 3)
                && sessionManager.getAccountFromSession().getRestaurantId() == restaurant.getRestaurantId())))
        {
            ic_edit.setVisibility(View.VISIBLE);
        }
        ic_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainProductDetail.this, ProductDetailsActivity.class);
                intent.putExtra("product_id", product.getProductId());
                startActivity(intent);
            }
        });

        Button btnAddCart = findViewById(R.id.add_to_cart);
        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sessionManager.isLoggedIn()) {
                    Intent intent = new Intent(MainProductDetail.this, UserLoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(MainProductDetail.this, "You must login before order!", Toast.LENGTH_SHORT).show();
                }

                Order order = orderRepository.selectUserNewest(sessionManager.getAccountFromSession().getAccountId());
                if (order == null) {
                    Calendar calendar = Calendar.getInstance();
                    Date date = calendar.getTime();

                    // Format date
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String currentDate = dateFormat.format(date);
                    order = new Order(0.0, currentDate, sessionManager.getAccountFromSession().getAccountId(), 1, true);
                    orderRepository.createOrder(order);
                    order = orderRepository.selectUserNewest(sessionManager.getAccountFromSession().getAccountId());
                }

                OrderDetails orderDetails = orderDetailsRepository.selectAllByOrderIdAndProductId(order.getOrderId(), product.getProductId());
                if (orderDetails == null) {
                    OrderDetails od = new OrderDetails(product.getProductId(), order.getOrderId(), product.getPrice(), quantity);
                    orderDetailsRepository.createOrderDetails(od);
                } else {
                    int quantity1 = orderDetails.getQuantity() + quantity;
                    orderDetails.setQuantity(quantity1);
                    orderDetailsRepository.updateOrderDetails(orderDetails);
                }

                double totalPrice = order.getTotalPrice() + product.getPrice() * quantity;
                order.setTotalPrice(totalPrice);
                orderRepository.updateOrder(order);

                txt_quantity.setText("" + 1);

                Toast.makeText(MainProductDetail.this, "Add to cart product " + product.getProductName() + " successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setProductList(List<Product> productList) {
        RecyclerView recyclerView = findViewById(R.id.product_list_main);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        MainProductAdapter mainProductAdapter = new MainProductAdapter(productList, this);
        recyclerView.setAdapter(mainProductAdapter);
    }
}