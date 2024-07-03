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
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantproject.CategoryDetailsActivity;
import com.example.restaurantproject.MenuDetailsActivity;
import com.example.restaurantproject.MenuListActivity;
import com.example.restaurantproject.MenuUpdateActivity;
import com.example.restaurantproject.R;
import com.example.restaurantproject.RestaurantDetailActivity;
import com.example.restaurantproject.RestaurantUpdateActivity;
import com.example.restaurantproject.bean.Menu;
import com.example.restaurantproject.bean.Restaurant;
import com.example.restaurantproject.repository.MenuRepository;
import com.example.restaurantproject.repository.RestaurantRepository;

import java.util.List;

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.MenuViewHolder> {
    private List<Menu> menus;
    private Context context;
    private RestaurantRepository restaurantRepository;

    public MenuListAdapter(List<Menu> menus, Context context) {
        this.menus = menus;
        this.context = context;
        this.restaurantRepository = new RestaurantRepository(context);  // Initialize repository
    }


    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_list_item, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        Menu menu = menus.get(position);

        String id = "" + menu.getMenuId();
        String menuName = menu.getMenuName();
        int restaurantId = menu.getRestaurantId();

        // Fetch restaurant name using restaurantId
        Restaurant restaurant = restaurantRepository.getRestaurant(restaurantId);
        String restaurantName = restaurant != null ? restaurant.getRestaurantName() : "Unknown";

        // Create a SpannableString with UnderlineSpan
        SpannableString spannableString = new SpannableString(id);
        spannableString.setSpan(new UnderlineSpan(), 0, id.length(), 0);

        // Set the values to the TextViews
        holder.tvMenuId.setText(spannableString);
        holder.tvMenuName.setText(menuName);
        holder.tvRestaurantName.setText(restaurantName);

        // Set click listeners
        holder.tvMenuId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MenuDetailsActivity.class);
                intent.putExtra("menu_id", menu.getMenuId());
                context.startActivity(intent);
            }
        });

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, MenuUpdateActivity.class);
            intent.putExtra("menu_id", menu.getMenuId());
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            MenuRepository menuRepository = new MenuRepository(context);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete Menu");
            builder.setMessage("Are you sure you want to delete menu " + menu.getMenuName() + "?")
                    .setPositiveButton("OK", (dialog, which) -> {
                        menuRepository.deleteMenu(menu.getMenuId());
                        Toast.makeText(context, "Menu deleted successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, MenuListActivity.class);
                        context.startActivity(intent);
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }


    public class MenuViewHolder extends RecyclerView.ViewHolder{
        private TextView tvMenuId, tvRestaurantName, tvMenuName;
        private ImageButton btnEdit, btnDelete;

        @SuppressLint("WrongViewCast")
        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMenuId = itemView.findViewById(R.id.tv_menu_id);
            tvMenuName = itemView.findViewById(R.id.tv_menu_name);
            tvRestaurantName = itemView.findViewById(R.id.tv_restaurant_id);
            btnEdit = itemView.findViewById(R.id.btn_edit_menu);
            btnDelete = itemView.findViewById(R.id.btn_delete_menu);
        }
    }
}
