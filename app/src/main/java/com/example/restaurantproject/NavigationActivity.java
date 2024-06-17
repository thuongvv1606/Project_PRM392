package com.example.restaurantproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_navigation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.navigation), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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

        NavigationView navigationView = (NavigationView)
                findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = findViewById(R.id.navigation);
        // Handle navigation view item clicks here.
        if (item.getItemId() == R.id.nav_home) {
            // Redirect to homepage (for now display a toast).
            drawer.closeDrawer(GravityCompat.START);
            displayToast("Go to homepage successfully!");
            return true;
        } else if (item.getItemId() == R.id.nav_info) {
            // Redirect to about us page (for now display a toast).
            drawer.closeDrawer(GravityCompat.START);
            displayToast("Go to about us page successfully!");
            return true;
        } else if (item.getItemId() == R.id.nav_order) {
            // Redirect to order page (for now display a toast).
            drawer.closeDrawer(GravityCompat.START);
            displayToast("Go to order page successfully!");
            return true;
        } else if (item.getItemId() == R.id.nav_reservation) {
            // Redirect to reservation page (for now display a toast).
            drawer.closeDrawer(GravityCompat.START);
            displayToast("Go to reservation page successfully!");
            return true;
        } else if (item.getItemId() == R.id.nav_login) {
            // Redirect to login page (for now display a toast).
            drawer.closeDrawer(GravityCompat.START);
            displayToast("Go to login page successfully!");
            Intent intent = new Intent(NavigationActivity.this, UserLoginActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.nav_register) {
            // Redirect to register page (for now display a toast).
            drawer.closeDrawer(GravityCompat.START);
            displayToast("Go to register page successfully!");
            Intent intent = new Intent(NavigationActivity.this, UserRegisterActivity.class);
            startActivity(intent);
            return true;
        } else {
            return false;
        }
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
    private void displayToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }
}