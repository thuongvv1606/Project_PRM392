package com.example.restaurantproject.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.restaurantproject.bean.Account;
import com.example.restaurantproject.bean.Category;
import com.example.restaurantproject.bean.Delivery;
import com.example.restaurantproject.bean.Menu;
import com.example.restaurantproject.bean.Order;
import com.example.restaurantproject.bean.OrderDetails;
import com.example.restaurantproject.bean.Product;
import com.example.restaurantproject.bean.Reservation;
import com.example.restaurantproject.bean.Restaurant;
import com.example.restaurantproject.bean.Role;

@Database(entities = {Category.class, Product.class, Role.class, Account.class, Restaurant.class,
        Order.class, OrderDetails.class, Reservation.class, Delivery.class, Menu.class}, version = 1)
public abstract class RestaurantDatabase extends RoomDatabase {

    private static final String DB_NAME = "DemoDatabase";
    private static RestaurantDatabase INSTANCE;

    // Declare abstract methods to access the DAOs
    public abstract ProductDao productDao();
    public abstract CategoryDao categoryDao();
    public abstract RoleDao roleDao();
    public abstract AccountDao accountDao();
    public abstract RestaurantDao restaurantDao();
    public abstract OrderDao orderDao();
    public abstract OrderDetailsDao orderDetailsDao();
    public abstract ReservationDao reservationDao();
    public abstract DeliveryDao deliveryDao();
    public abstract MenuDao menuDao();

    // Static method to get the instance of the database
    public static synchronized RestaurantDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RestaurantDatabase.class, DB_NAME)
                    .allowMainThreadQueries() // Allow queries on the main thread (not recommended for large applications)
                    .fallbackToDestructiveMigration() // Automatically deletes and recreates the database when the version changes
                    .enableMultiInstanceInvalidation() // Allows data synchronization between multiple instances of the database
                    .build();
        }
        return INSTANCE;
    }
}

