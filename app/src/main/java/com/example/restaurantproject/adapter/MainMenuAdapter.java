package com.example.restaurantproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.restaurantproject.R;
import com.example.restaurantproject.bean.Category;
import com.example.restaurantproject.bean.Menu;
import com.example.restaurantproject.bean.Product;

import java.util.List;

public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.MenuViewHolder>{
    private List<Menu> menus = null;
    private Context context;
    public  MainMenuAdapter(List<Menu> menus, Context context){
        this.context = context;
        this.menus = menus;
    }

    @NonNull
    @Override
    public MainMenuAdapter.MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_menu_item, parent, false);
        return new MainMenuAdapter.MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainMenuAdapter.MenuViewHolder holder, int position) {
        Menu menu = menus.get(position);

        String id = "" + menu.getMenuId();

        // Set the spannableString to the TextView
        holder.tvMenuName.setText("" + menu.getMenuName());
        if (menu.getMenuImage() != null && !menu.getMenuImage().isEmpty()) {
            Glide.with(context).load(menu.getMenuImage()).into(holder.tvMenuImage);
        }
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder{
        private TextView tvMenuName;
        private ImageView tvMenuImage;

        @SuppressLint("WrongViewCast")
        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMenuName = itemView.findViewById(R.id.menu_name_main);
            tvMenuImage = itemView.findViewById(R.id.menu_image_main);
        }
    }
}
