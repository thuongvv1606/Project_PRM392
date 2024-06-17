package com.example.restaurantproject.bean;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Menu",
        foreignKeys = @ForeignKey(entity = Restaurant.class,
                parentColumns = "restaurant_id",
                childColumns = "restaurant_id",
                onDelete = ForeignKey.CASCADE))
public class Menu {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "menu_id")
    private int menuId;

    @ColumnInfo(name = "menu_name")
    private String menuName;

    @ColumnInfo(name = "menu_description")
    private String menuDescription;

    @ColumnInfo(name = "menu_image")
    private String menuImage;

    @ColumnInfo(name = "restaurant_id")
    private int restaurantId;

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuDescription() {
        return menuDescription;
    }

    public void setMenuDescription(String menuDescription) {
        this.menuDescription = menuDescription;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getMenuImage() {
        return menuImage;
    }

    public void setMenuImage(String menuImage) {
        this.menuImage = menuImage;
    }
}
