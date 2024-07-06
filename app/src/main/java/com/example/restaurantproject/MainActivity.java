package com.example.restaurantproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.restaurantproject.adapter.BannerAdapter;
import com.example.restaurantproject.adapter.MainCategoryAdapter;
import com.example.restaurantproject.adapter.MainMenuAdapter;
import com.example.restaurantproject.adapter.MainProductAdapter;
import com.example.restaurantproject.adapter.MainRestaurantAdapter;
import com.example.restaurantproject.bean.Category;
import com.example.restaurantproject.entity.AccountDTO;
import com.example.restaurantproject.bean.Menu;
import com.example.restaurantproject.bean.Product;
import com.example.restaurantproject.bean.Restaurant;
import com.example.restaurantproject.repository.AccountRepository;
import com.example.restaurantproject.repository.CategoryRepository;
import com.example.restaurantproject.repository.DeliveryRepository;
import com.example.restaurantproject.repository.MenuRepository;
import com.example.restaurantproject.repository.OrderDetailsRepository;
import com.example.restaurantproject.repository.OrderRepository;
import com.example.restaurantproject.repository.ProductRepository;
import com.example.restaurantproject.repository.RestaurantRepository;
import com.example.restaurantproject.repository.RoleRepository;
import com.example.restaurantproject.ultils.session.SessionManager;

import java.util.List;

public class MainActivity extends NavigationActivity {
    private Context context = null;
    private AccountRepository accountRepository = null;
    private CategoryRepository categoryRepository = null;
    private DeliveryRepository deliveryRepository = null;
    private MenuRepository menuRepository = null;
    private OrderDetailsRepository orderDetailsRepository = null;
    private OrderRepository orderRepository = null;
    private ProductRepository productRepository = null;
    private RestaurantRepository restaurantRepository = null;
    private RoleRepository roleRepository = null;

    // Test
    private SessionManager sessionManager;

    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private int[] images = {R.drawable.banner_image1, R.drawable.banner_image2, R.drawable.banner_image3};
    private int currentPage = 0;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setupContentLayout(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        accountRepository = new AccountRepository(this);
        categoryRepository = new CategoryRepository(this);
        deliveryRepository = new DeliveryRepository(this);
        menuRepository = new MenuRepository(this);
        orderRepository = new OrderRepository(this);
        orderDetailsRepository = new OrderDetailsRepository(this);
        productRepository = new ProductRepository(this);
        restaurantRepository = new RestaurantRepository(this);
        roleRepository = new RoleRepository(this);
        context = this;
        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.dots_layout);

        sessionManager = new SessionManager(this);

        BannerAdapter bannerAdapter = new BannerAdapter(this, images);
        viewPager.setAdapter(bannerAdapter);

        addDotsIndicator(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                addDotsIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                if (currentPage == images.length) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 3000); // Chuyển đổi ảnh mỗi 3 giây
            }
        };

        List<Category> categoryList = categoryRepository.getAllCategories();
        setCategoryList(categoryList);

        List<Restaurant> restaurantList = restaurantRepository.getTopRestaurants();
        setRestaurantList(restaurantList);

        List<Menu> menuList = menuRepository.getTopMenus();
        setMenuList(menuList);

        List<Product> productList = productRepository.getTopProducts();
        setProductList(productList);

        Button searchBtn = findViewById(R.id.btn_home_search);
        EditText searchEdt = findViewById(R.id.edt_home_search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Category> categoryList = categoryRepository.searchCategories(searchEdt.getText().toString());
                setCategoryList(categoryList);
                TextView txtCountCategory = findViewById(R.id.tv_category_count);
                txtCountCategory.setVisibility(View.VISIBLE);
                txtCountCategory.setText("Found " + categoryList.size() + " category(ies)");

                List<Restaurant> restaurantList = restaurantRepository.getAllBySearch(searchEdt.getText().toString());
                setRestaurantList(restaurantList);
                TextView txtCountRestaurant = findViewById(R.id.tv_restaurant_count);
                txtCountRestaurant.setVisibility(View.VISIBLE);
                txtCountRestaurant.setText("Found " + restaurantList.size() + " restaurant(s)");

                List<Menu> menuList = menuRepository.searchMenus(searchEdt.getText().toString());
                setMenuList(menuList);
                TextView txtCountMenu = findViewById(R.id.tv_menu_count);
                txtCountMenu.setVisibility(View.VISIBLE);
                txtCountMenu.setText("Found " + menuList.size() + " menu(s)");

                List<Product> productList = productRepository.searchProduct(searchEdt.getText().toString());
                setProductList(productList);
                TextView txtCountProduct = findViewById(R.id.tv_product_count);
                txtCountProduct.setVisibility(View.VISIBLE);
                txtCountProduct.setText("Found " + productList.size() + " product(s)");
            }
        });

        TextView seeMoreRestaunrants = findViewById(R.id.tv_restaurant_see_more);
        seeMoreRestaunrants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRestaunrant = new Intent(MainActivity.this, MainRestaurantList.class);
                startActivity(intentRestaunrant);
            }
        });

        TextView seeMoreMenus = findViewById(R.id.tv_menu_see_more);
        seeMoreMenus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMenu = new Intent(MainActivity.this, MainMenuList.class);
                startActivity(intentMenu);
            }
        });

        TextView seeMoreProducts = findViewById(R.id.tv_product_see_more);
        seeMoreProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentProduct = new Intent(MainActivity.this, MainProductList.class);
                startActivity(intentProduct);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 3000); // Khởi động chuyển đổi ảnh ban đầu
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable); // Dừng chuyển đổi ảnh khi Activity không còn hoạt động
    }

    private void addDotsIndicator(int position) {
        ImageView[] dots = new ImageView[images.length];
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageResource(R.drawable.dot_inactive);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            dotsLayout.addView(dots[i], params);
        }
        dots[position].setImageResource(R.drawable.dot_active);
    }

    private void setCategoryList(List<Category> categoryList) {
        RecyclerView recyclerView = findViewById(R.id.category_list_main);
        LinearLayoutManager horizontalLayoutManager =
                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        MainCategoryAdapter categoryListAdapter = new MainCategoryAdapter(categoryList, this);
        recyclerView.setAdapter(categoryListAdapter);
    }

    private void setRestaurantList(List<Restaurant> restaurantList) {
        RecyclerView recyclerView = findViewById(R.id.restaurant_list_main);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        MainRestaurantAdapter mainMenuAdapter = new MainRestaurantAdapter(restaurantList, this);
        recyclerView.setAdapter(mainMenuAdapter);
    }

    private void setMenuList(List<Menu> menuList) {
        RecyclerView recyclerView = findViewById(R.id.menu_list_main);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        MainMenuAdapter mainMenuAdapter = new MainMenuAdapter(menuList, this);
        recyclerView.setAdapter(mainMenuAdapter);
    }

    private void setProductList(List<Product> productList) {
        RecyclerView recyclerView = findViewById(R.id.product_list_main);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        MainProductAdapter mainProductAdapter = new MainProductAdapter(productList, this);
        recyclerView.setAdapter(mainProductAdapter);
    }
}