package com.example.restaurantproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
import com.google.android.material.navigation.NavigationView;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
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
    private SessionManager sessionManager;
    private MenuItem itemAccounts, itemRestaurants, itemCategories, itemMenus,
            itemProducts, itemOrders, itemReservations, itemDeliveries,
            itemOrder, itemReservation,
            itemLogin, itemRegister, itemProfile, itemLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        DrawerLayout drawer = findViewById(R.id.navigation);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, drawer, toolbar,
                        R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        if (navigationView != null) {
            updateNavigationMenu(navigationView);
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    private void updateNavigationMenu(NavigationView navigationView) {
        Menu menu = navigationView.getMenu();
        sessionManager = new SessionManager(this);

        // Tùy chỉnh hiển thị của MenuItem dựa vào điều kiện
        itemAccounts = menu.findItem(R.id.nav_accounts);
        itemRestaurants = menu.findItem(R.id.nav_restaurants);
        itemCategories = menu.findItem(R.id.nav_categories);
        itemMenus = menu.findItem(R.id.nav_menus);
        itemProducts = menu.findItem(R.id.nav_products);
        itemOrders = menu.findItem(R.id.nav_orders);
        itemReservations = menu.findItem(R.id.nav_reservations);
        itemDeliveries = menu.findItem(R.id.nav_deliveries);
        itemOrder = menu.findItem(R.id.nav_order);
        itemReservation = menu.findItem(R.id.nav_reservation);
        itemLogin = menu.findItem(R.id.nav_login);
        itemLogout = menu.findItem(R.id.nav_logout);
        itemProfile = menu.findItem(R.id.nav_profile);
        itemRegister = menu.findItem(R.id.nav_register);

        if (sessionManager.isLoggedIn())
        {
            itemLogin.setVisible(false);
            itemRegister.setVisible(false);

            if (sessionManager.getAccountFromSession().getRoleId() == 1) {
                itemAccounts.setVisible(true);
                itemRestaurants.setVisible(true);
            }
            if (sessionManager.getAccountFromSession().getRoleId() == 1 ||
                    sessionManager.getAccountFromSession().getRoleId() == 2 ||
                    sessionManager.getAccountFromSession().getRoleId() == 3) {
                itemCategories.setVisible(true);
                itemMenus.setVisible(true);
                itemProducts.setVisible(true);
            }
            if (sessionManager.getAccountFromSession().getRoleId() != 4) {
                itemDeliveries.setVisible(true);
            }
            itemOrders.setVisible(true);
            itemReservations.setVisible(true);

            itemOrder.setVisible(false);
            itemReservation.setVisible(false);
        }
        else
        {
            itemLogout.setVisible(false);
            itemProfile.setVisible(false);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = findViewById(R.id.navigation);
        if (item.getItemId() == R.id.nav_home) {
            Intent intent = new Intent(NavigationActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.nav_accounts) {
            Intent intent = new Intent(NavigationActivity.this, AccountListActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.nav_categories) {
            Intent intent = new Intent(NavigationActivity.this, CategoryListActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.nav_restaurants) {
            Intent intent = new Intent(NavigationActivity.this, RestaurantListActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.nav_menus) {
            Intent intent = new Intent(NavigationActivity.this, MenuListActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.nav_products) {
            Intent intent = new Intent(NavigationActivity.this, ProductListActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.nav_orders) {
            Intent intent = new Intent(NavigationActivity.this, OrderListActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.nav_reservations) {
            Intent intent = new Intent(NavigationActivity.this, TableListActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.nav_deliveries) {
            Intent intent = new Intent(NavigationActivity.this, OrderDeliveryActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.nav_order) {
            Intent intent = new Intent(NavigationActivity.this, UserLoginActivity.class);
            startActivity(intent);
            displayToast("You must login first to do this action!");
            return true;
        } else if (item.getItemId() == R.id.nav_reservation) {
            Intent intent = new Intent(NavigationActivity.this, UserLoginActivity.class);
            startActivity(intent);
            displayToast("You must login first to do this action!");
            return true;
        } else if (item.getItemId() == R.id.nav_login) {
            Intent intent = new Intent(NavigationActivity.this, UserLoginActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.nav_register) {
            Intent intent = new Intent(NavigationActivity.this, UserRegisterActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.nav_profile) {
            drawer.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(NavigationActivity.this, ViewProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.nav_logout) {
            drawer.closeDrawer(GravityCompat.START);
            // Logout user
            sessionManager.deleteAccountFromSession();
            // Reload MainActivity to reflect changes
            Intent intent = new Intent(NavigationActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (item.getItemId() == R.id.nav_create_db) {
            accountRepository.getAllAccounts();
            categoryRepository.getAllCategories();
            deliveryRepository.getAll();
            menuRepository.getAllMenus();
            orderRepository.getAllOrders();
            orderDetailsRepository.getAllOrderDetails();
            productRepository.getAllProducts();
            restaurantRepository.getAllRestaurants();
            roleRepository.getAllRoles();
            displayToast("Create database successfully!");
            drawer.closeDrawer(GravityCompat.START);
            return true;
        } else if (item.getItemId() == R.id.nav_delete_db) {
            context.deleteDatabase("DemoDatabase");
            displayToast("Delete database successfully!");
            drawer.closeDrawer(GravityCompat.START);
            return true;
        } else {
            return false;
        }
    }

    private void displayToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    protected void setupContentLayout(int layoutResID) {
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup container = findViewById(R.id.container);
        inflater.inflate(layoutResID, container, true);
    }
}