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
import com.example.restaurantproject.bean.Restaurant;
import com.example.restaurantproject.bean.Role;
import com.example.restaurantproject.bean.Table;

import java.util.ArrayList;
import java.util.List;

@Database(entities = {Category.class, Product.class, Role.class, Account.class, Restaurant.class,
        Order.class, OrderDetails.class, Delivery.class, Menu.class, Table.class}, version = 12)
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
    public abstract DeliveryDao deliveryDao();
    public abstract MenuDao menuDao();
    public abstract TableDao tableDao();

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
                accounts.add(new Account("Deliver1", "1234567", "Deliver 1", "deliver1@gmail.com","0912123345","HaNoi",5, true, "https://www.iconpacks.net/icons/2/free-user-icon-3297-thumb.png"));
                accounts.add(new Account("Deliver2", "1234567", "Deliver 2", "deliver2@gmail.com","0912123345","HaNoi",5, true, "https://www.iconpacks.net/icons/2/free-user-icon-3297-thumb.png"));
                accounts.add(new Account("Deliver3", "1234567", "Deliver 3", "deliver3@gmail.com","0912123345","HaNoi",5, true, "https://www.iconpacks.net/icons/2/free-user-icon-3297-thumb.png"));
                accounts.add(new Account("Deliver4", "1234567", "Deliver 4", "deliver4@gmail.com","0912123345","HaNoi",5, true, "https://www.iconpacks.net/icons/2/free-user-icon-3297-thumb.png"));
                accounts.add(new Account("Deliver5", "1234567", "Deliver 5", "deliver5@gmail.com","0912123345","HaNoi",5, true, "https://www.iconpacks.net/icons/2/free-user-icon-3297-thumb.png"));
                for (Account a : accounts) {
                    accountDao.insert(a);
                }

                RestaurantDao restaurantDao = database.restaurantDao();
                List<Restaurant> restaurants = new ArrayList<>();

                restaurants.add(new Restaurant("KFC", "KFC stands for \"Kentucky Fried Chicken.\" " +
                        "It is a globally renowned fast-food restaurant chain " +
                        "specializing in fried chicken and various chicken-related " +
                        "products.", "kfc@gmail.com", "0123456789", "HCM",
                        "data:image/jpeg;base64,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"));

                restaurants.add(new Restaurant("McDonald's", "McDonald's is one of the world's largest and most well-known fast-food restaurant chains.",
                        "mcdonalds@gmail.com", "0123456789", "HaNoi",
                        "https://s7d1.scene7.com/is/image/mcdonalds/crown_point_mcdonalds-RR-EDIT_001:hero-desktop?resmode=sharp2"));

                restaurants.add(new Restaurant("Lotte", "Lotte is a well-known South Korean conglomerate that also operates a chain of fast-food restaurants.",
                        "lotte@gmail.com", "0123456789", "DaNang",
                        "https://vir.com.vn/stores/news_dataimages/luuhuong/042021/16/17/in_article/3841_Lotteria.jpg"));
                for (Restaurant r : restaurants) {
                    restaurantDao.insert(r);
                }

                CategoryDao cateDao = database.categoryDao();
                List<Category> categories = new ArrayList<>();
                categories.add(new Category("Appetizers", "Appetizer (or starter) is a small dish served before the main course of a meal.", "https://www.onceuponapumpkinrd.com/wp-content/uploads/2020/11/pumpkin-appetizer-puffs-scaled.jpg"));
                categories.add(new Category("Breakfast", "Breakfast is the first meal of the day, typically eaten in the morning.", "https://www.kateskitchenkc.com/wp-content/uploads/2023/10/traditional-full-american-breakfast-eggs-pancakes-with-bacon-and-toast.jpg_s1024x1024wisk20cz03ui5Oqyz8Ys_pG0bVWsgoz_v_E5Oct4x-0C-sAjME.jpg"));
                categories.add(new Category("Desserts", "Dessert is a sweet course typically served at the end of a meal.", "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ab/Desserts.jpg/800px-Desserts.jpg"));
                categories.add(new Category("Drinks", "Drink (or beverage) is any liquid intended for human consumption.", "https://www.acouplecooks.com/wp-content/uploads/2021/06/Strawberry-Water-006.jpg"));
                categories.add(new Category("Lunch", "Lunch is the meal eaten in the middle of the day, typically between breakfast and dinner.", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSbyYlwAjSh5kKfR_EgB_H6BmZEN9xYqsDzIQ&s"));
                categories.add(new Category("Mains", "Main course (or main dish) is the primary and most substantial dish of a meal, typically following the appetizer and preceding the dessert.", "https://www.southernliving.com/thmb/TiGXc2JsEJpsioS_TA-LDzRFPEc=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/27531_ST_TENDERLOINS_13-7442687e434e4c4c95609ff0262773a2.jpg"));
                categories.add(new Category("Speacials", "A special refers to a dish or menu item that is offered for a limited time or is unique in some way compared to the regular menu items.", "https://static.toiimg.com/thumb/msid-79891769,width-960,height-1280,resizemode-6.cms"));
                categories.add(new Category("Combos", "A food combo, also known as a meal deal, is a package that includes multiple food items sold together at a discounted price.", "https://www.portablepress.com/wp-content/uploads/2017/10/Fast-food-combo-meal-1.jpg"));
                for (Category c : categories) {
                    cateDao.insert(c);
                }

                MenuDao menuDao = database.menuDao();
                List<Menu> menus = new ArrayList<>();
                menus.add(new Menu("Đồ ăn", "Đồ ăn", "https://congthanh.vn/uploads/images/menu%20nh%C3%A0%20h%C3%A0ng%20sang%20tr%E1%BB%8Dng/menu-nha-hang-sang-trong30-min.jpg", 1));
                menus.add(new Menu("Đồ uống", "Đồ uống", "https://bizweb.dktcdn.net/thumb/1024x1024/100/438/465/products/z3514167835312-f4655cad5695442ab0a33b5b9bcb2e72.jpg?v=1655969268750", 1));
                menus.add(new Menu("Menu KFC", "", "https://danhgiasao.com/wp-content/uploads/2024/01/menu-hap-dan-cua-ga-ran-kfc-danhgiasao.jpg", 1));
                menus.add(new Menu("Menu McDonals", "", "https://i.pinimg.com/736x/df/dc/0b/dfdc0b8c50c95c2a3ee86e21ae0f22b7.jpg", 2));
                menus.add(new Menu("Menu Lotteria", "", "https://aeonmall-hadong.com.vn/wp-content/uploads/2020/04/lotteria-4-360x573.jpg", 3));

                for (Menu m : menus) {
                    menuDao.insert(m);
                }

                ProductDao productDao = database.productDao();
                List<Product> products = new ArrayList<>();

                products.add(new Product("Bánh", "Bánh", 100000, "https://cdn.tgdd.vn/Files/2021/07/28/1371385/2-cach-lam-banh-donut-nuong-va-chien-ngon-don-gian-tai-nha-202206031611571875.jpg", 3, 1));
                products.add(new Product("Phở", "Phở", 60000, "https://cdn.tgdd.vn/Files/2022/01/25/1412805/cach-nau-pho-bo-nam-dinh-chuan-vi-thom-ngon-nhu-hang-quan-202201250230038502.jpg", 6, 1));
                products.add(new Product("Bún", "Bún", 50000, "https://bepxua.vn/wp-content/uploads/2020/12/bun-bo-hue2.jpg", 6, 1));
                products.add(new Product("Mì", "Mì", 30000, "https://cdn.tgdd.vn/Files/2019/10/23/1211482/cac-loai-mi-tom-duoc-ua-chuong-nhat-viet-nam-202206041405267719.jpeg", 6, 1));
                products.add(new Product("Cơm", "Cơm", 120000, "https://cdn.tgdd.vn/2021/07/CookProduct/2-1200x676-2-1200x676-3.jpg", 6, 1));
                products.add(new Product("Nước lọc", "Nước", 10000, "https://web.hn.ss.bfcplatform.vn/mkmart/product/202201/3686905571-1674132732-m.jpg", 4, 2));
                products.add(new Product("Nước Coca", "Nước", 20000, "https://www.coca-cola.com/content/dam/onexp/vn/home-image/coca-cola/Coca-Cola_OT%20320ml_VN-EX_Desktop.png", 4, 2));
                products.add(new Product("Nước Pesi", "Nước", 20000, "https://minhcaumart.vn/media/com_eshop/products/8934588012228%201.jpg", 4, 2));
                products.add(new Product("Nước ép", "Nước", 50000, "https://vinut.vn/wp-content/uploads/2024/03/nuoc-ep-trai-cay.jpeg", 4, 2));
                products.add(new Product("Combo Fried Chicken", "- 2 pieces of chicken\\n- 1 fries (M) / 1 coleslaw (M) & 1 mashed potato\\n- 1 Pepsi (M)", 87000, "https://static.kfcvietnam.com.vn/images/items/lg/D-CBO-CHICKEN-2.jpg?v=LWkXxL", 8, 3));
                products.add(new Product("Combo Shrimp Burger", "- 1 shrimp burger\\n- 1 fries (M)\\n- 1 Pepsi (M)", 67000, "https://static.kfcvietnam.com.vn/images/items/lg/D-CBO-B-Shrimp.jpg?v=LWkXxL", 8, 3));
                products.add(new Product("Box Meal Pasta Cob", "- 1 Spaghetti with Fried Chicken\\n- 3 Chicken Nuggets\\n- 1 Pepsi (L)", 97000, "https://static.kfcvietnam.com.vn/images/items/lg/Pasta-Nuggets.jpg?v=LWnGY3", 8, 3));
                products.add(new Product("Combo 3A", "- 1 portion of chicken nuggets\\n- 2 servings of fries\\n- 5 pieces of chicken\\n- 3 Cokes", 279000, "https://mcdonalds.vn/uploads/2018/food/Combo/xcombo3A.png.pagespeed.ic.oKw89faUi5.webp", 8, 4));
                products.add(new Product("Combo 2A", "- 2 pieces of chicken\\n- 2 burgers\\n- 1 Coke", 131000, "https://mcdonalds.vn/uploads/2018/food/Combo/x02,281,29.png.pagespeed.ic.WyEPwqCChI.webp", 8, 4));
                products.add(new Product("Matcha Latte", "", 30000, "https://www.lotteria.vn/media/catalog/product/l/_/l_coffee_matcha_latte-16.png", 4, 5));
                products.add(new Product("Combo Burger Bulgogi", "- 01 Bulgogi Burger\\n- 01 Fries (M)", 82000, "https://www.lotteria.vn/media/catalog/product/c/o/combo_bulgogi_6.png", 8, 5));
                products.add(new Product("Pie Chicken", "", 44000, "https://www.lotteria.vn/media/catalog/product/g/_/g_pie_1.png", 7, 5));
                products.add(new Product("Apple Pie", "", 20000, "https://www.lotteria.vn/media/catalog/product/l/s/ls0007_5.png", 3, 5));
                products.add(new Product("Kimchi-filled Fried Bread + Milkis", "- 1 Kimchi-filled Fried Bread\\n- 1 Milkis", 55000, "https://www.lotteria.vn/media/catalog/product/d/e/dessert_b_nh_chi_n_kim_chi_milkis.png", 3, 5));
                products.add(new Product("L4 Set", "- 04 Fried Chicken Pieces\\n- 01 Bulgogi Burger", 280000, "https://www.lotteria.vn/media/catalog/product/p/a/pack_l4_set_2_.png", 8, 5));
                products.add(new Product("Value Burger Double Double", "- 01 Double Double Burger\\n- 01 Fried Chicken Piece", 119000, "https://www.lotteria.vn/media/catalog/product/v/a/value_d-double.png", 8, 5));
                products.add(new Product("Combo Roast Chicken", "- 1 Flava Roast Chicken Leg\\n- 1 Seed Salad\\n- 1 Large Lipton Tea", 117000, "https://www.lotteria.vn/media/catalog/product/d/e/dessert_b_nh_chi_n_kim_chi_milkis.png", 8, 3));
                products.add(new Product("Combo Teriyaki", "- 1 Teriyaki Rice\\n- 1 Seaweed Soup\\n- 1 Large Pepsi", 69000, "https://static.kfcvietnam.com.vn/images/items/lg/D-CBO-Rice-Teri.jpg?v=LWnGY3", 8, 3));
                products.add(new Product("Milo", "110 Kcal", 22000, "https://mcdonalds.vn/uploads/2018/food/beverage/xmilo.png.pagespeed.ic.owUoRzA7-9.webp", 4, 4));
                products.add(new Product("Special Cheese Beef Burger", "", 56000, "https://mcdonalds.vn/uploads/2018/food/burgers/xcheesedlx_bb.png.pagespeed.ic.T9fdYoxRFN.webp", 6, 4));
                for (Product p : products) {
                    productDao.insert(p);
                }

                TableDao tableDao = database.tableDao();
                List<Table> tables = new ArrayList<>();
                tables.add(new Table("Table 1", 6, "", 1, 1));
                tables.add(new Table("Table 2", 6, "", 1, 1));
                tables.add(new Table("Table 3", 6, "", 1, 1));
                tables.add(new Table("Table 4", 6, "", 1, 1));
                tables.add(new Table("Table 5", 6, "", 1, 1));
                tables.add(new Table("Table 6", 6, "", 1, 1));
                for (Table t : tables){
                    tableDao.insert(t);
                }

            }).start();
        }
    };
}

