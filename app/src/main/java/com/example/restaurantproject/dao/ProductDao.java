package com.example.restaurantproject.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.restaurantproject.bean.Menu;
import com.example.restaurantproject.bean.Product;

import java.util.List;

@Dao
public interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Product product);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Product product);

    @Query("SELECT * FROM Product WHERE product_id = :productId")
    Product select(int productId);

    @Query("SELECT * FROM Product")
    List<Product> selectAll();
    @Query("SELECT * FROM Product P WHERE P.category_id = :id")
    List<Product> selectAllInCategory(int id);
    @Query("SELECT * FROM Product P WHERE P.menu_id = :menuId AND P.product_id != :productId")
    List<Product> selectAllInMenu(int menuId, int productId);
    @Query("SELECT * FROM Product LIMIT 8")
    List<Product> selectTop();

    @Query("DELETE FROM Product")
    void deleteAll();

    @Query("DELETE FROM Product WHERE product_id = :productId")
    void delete(int productId);

    @Query("SELECT * FROM Product WHERE product_name = :name AND product_id != :id")
    Product getByName(String name, int id);

    @Query("SELECT * FROM Product WHERE product_name LIKE '%' || :searchStr || '%'")
    List<Product> search(String searchStr);

    @Query("SELECT * FROM Product WHERE menu_id = :menuId")
    List<Product> getProductByMenu(int menuId);
}

