package com.example.restaurantproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantproject.MenuDetailsActivity;
import com.example.restaurantproject.MenuListActivity;
import com.example.restaurantproject.MenuOrderActivity;
import com.example.restaurantproject.MenuUpdateActivity;
import com.example.restaurantproject.R;
import com.example.restaurantproject.bean.Menu;
import com.example.restaurantproject.bean.Product;
import com.example.restaurantproject.bean.Restaurant;
import com.example.restaurantproject.repository.MenuRepository;
import com.example.restaurantproject.repository.ProductRepository;
import com.example.restaurantproject.repository.RestaurantRepository;

import java.util.HashMap;
import java.util.List;

public class MenuOrderListAdapter extends RecyclerView.Adapter<MenuOrderListAdapter.MenuOrderViewHolder>{
    private List<Menu> menus;
    private Context context;
    private ProductRepository productRepository;
    private ProductOrderAdapter.OnProductAddListener onProductAddListener;
    public MenuOrderListAdapter(List<Menu> menus, Context context, ProductOrderAdapter.OnProductAddListener onProductAddListener) {
        this.menus = menus;
        this.context = context;
        this.productRepository = new ProductRepository(context);  // Initialize repository
        this.onProductAddListener = onProductAddListener;
    }


    @NonNull
    @Override
    public MenuOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_menu_order, parent, false);
        return new MenuOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuOrderViewHolder holder, int position) {
        Menu menu = menus.get(position);

        String menuName = menu.getMenuName();
        holder.tvMenuName.setText("Menu: "+menuName);

        GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
        holder.productRecyclerView.setLayoutManager(layoutManager);
        ProductOrderAdapter foodAdapter = new ProductOrderAdapter(context, productRepository.getProductByMenu(menu.getMenuId()), onProductAddListener);
        holder.productRecyclerView.setAdapter(foodAdapter);
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }

    public class MenuOrderViewHolder extends RecyclerView.ViewHolder{
        TextView tvMenuName;
        RecyclerView productRecyclerView;

        @SuppressLint("WrongViewCast")
        public MenuOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMenuName = itemView.findViewById(R.id.tv_menu_name_lstor);
            productRecyclerView = itemView.findViewById(R.id.list_product_on_menu_recycler_view);
        }
    }
}
