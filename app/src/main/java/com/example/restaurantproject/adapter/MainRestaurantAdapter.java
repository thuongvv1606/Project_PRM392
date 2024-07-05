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
import com.example.restaurantproject.bean.Restaurant;

import java.util.List;

public class MainRestaurantAdapter extends RecyclerView.Adapter<MainRestaurantAdapter.RestaurantViewHolder>{
    private List<Restaurant> restaurants = null;
    private Context context;
    public  MainRestaurantAdapter(List<Restaurant> restaurants, Context context){
        this.context = context;
        this.restaurants = restaurants;
    }

    @NonNull
    @Override
    public MainRestaurantAdapter.RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_category_item, parent, false);
        return new MainRestaurantAdapter.RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainRestaurantAdapter.RestaurantViewHolder holder, int position) {
        Restaurant restaurant = restaurants.get(position);

        String id = "" + restaurant.getRestaurantId();

        // Set the spannableString to the TextView
        holder.tvRestaurantName.setText("" + restaurant.getRestaurantName());
        if (restaurant.getRestaurantImage() != null && !restaurant.getRestaurantImage().isEmpty()) {
            Glide.with(context).load(restaurant.getRestaurantImage()).into(holder.tvRestaurantImage);
        }

        holder.tvRestaurantImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.tvRestaurantName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder{
        private TextView tvRestaurantName;
        private ImageView tvRestaurantImage;

        @SuppressLint("WrongViewCast")
        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRestaurantName = itemView.findViewById(R.id.category_name);
            tvRestaurantImage = itemView.findViewById(R.id.category_image);
        }
    }
}
