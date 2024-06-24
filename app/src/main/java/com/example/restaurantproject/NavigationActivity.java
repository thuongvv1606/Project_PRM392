package com.example.restaurantproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

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
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = findViewById(R.id.navigation);
        if (item.getItemId() == R.id.nav_home) {
            drawer.closeDrawer(GravityCompat.START);
            displayToast("Go to homepage successfully!");
            return true;
        } else if (item.getItemId() == R.id.nav_info) {
            drawer.closeDrawer(GravityCompat.START);
            displayToast("Go to about us page successfully!");
            return true;
        } else if (item.getItemId() == R.id.nav_order) {
            drawer.closeDrawer(GravityCompat.START);
            displayToast("Go to order page successfully!");
            return true;
        } else if (item.getItemId() == R.id.nav_reservation) {
            drawer.closeDrawer(GravityCompat.START);
            displayToast("Go to reservation page successfully!");
            return true;
        } else if (item.getItemId() == R.id.nav_login) {
            drawer.closeDrawer(GravityCompat.START);
            displayToast("Go to login page successfully!");
            Intent intent = new Intent(NavigationActivity.this, UserLoginActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.nav_register) {
            drawer.closeDrawer(GravityCompat.START);
            displayToast("Go to register page successfully!");
            Intent intent = new Intent(NavigationActivity.this, UserRegisterActivity.class);
            startActivity(intent);
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