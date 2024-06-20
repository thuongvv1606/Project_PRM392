package com.example.restaurantproject.dao;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

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

import java.util.ArrayList;
import java.util.List;

@Database(entities = {Category.class, Product.class, Role.class, Account.class, Restaurant.class,
        Order.class, OrderDetails.class, Reservation.class, Delivery.class, Menu.class}, version = 6)
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
                    .addCallback(roomDatabaseCallback) // Thêm callback để chèn dữ liệu mẫu
                    .build();
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // Đây là nơi bạn sẽ thêm dữ liệu vào cơ sở dữ liệu khi nó được tạo lần đầu tiên.
            new Thread(() -> {
                // Lấy đối tượng cơ sở dữ liệu
                RestaurantDatabase database = INSTANCE;


                RoleDao roleDao = database.roleDao();
                List<Role> roles = new ArrayList<>();
                roles.add(new Role("Admin", "Administrator with full access"));
                roles.add(new Role("Restaurant staff", "Staff working in the restaurant"));
                roles.add(new Role("Waiter", "Server responsible for serving customers"));
                roles.add(new Role("Customer", "Regular customer of the restaurant"));
                roles.add(new Role("Deliverer", "Delivery person responsible for delivering orders"));
                for (Role c : roles) {
                    roleDao.insert(c);
                }

                AccountDao accountDao = database.accountDao();
                List<Account> accounts = new ArrayList<>();
                accounts.add(new Account("admin", "1234567", "Admin", "admin@gmail.com","0123456789","FPT",1, true, "https://www.iconpacks.net/icons/2/free-user-icon-3297-thumb.png"));
                accounts.add(new Account("thuong", "1234567", "Vu Thuong", "thuongvv16@gmail.com","0888160699","FPT",4, true, "https://www.iconpacks.net/icons/2/free-user-icon-3297-thumb.png"));
                accounts.add(new Account("tiendat320", "1234567", "Nguyễn Tiến Đạt", "tiendat320@gmail.com","0912123345","FPT",4, true, "https://www.iconpacks.net/icons/2/free-user-icon-3297-thumb.png"));
                for (Account a : accounts) {
                    accountDao.insert(a);
                }


            }).start();
        }
    };
}

