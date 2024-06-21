package com.example.restaurantproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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

import com.example.restaurantproject.CategoryListActivity;
import com.example.restaurantproject.R;
import com.example.restaurantproject.RestaurantDetailActivity;
import com.example.restaurantproject.RestaurantListActivity;
import com.example.restaurantproject.RestaurantUpdateActivity;
import com.example.restaurantproject.bean.Restaurant;
import com.example.restaurantproject.repository.RestaurantRepository;

import java.util.List;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.RestaurantViewHolder>{

    private List<Restaurant> restaurants = null;
    private Context context;
    public RestaurantListAdapter(List<Restaurant> restaurants, Context context){
        this.context = context;
        this.restaurants = restaurants;
    }
    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.restaurant_list_item, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        Restaurant restaurant = restaurants.get(position);

        String id = "" + restaurant.getRestaurantId();
        // Create a SpannableString with UnderlineSpan
        SpannableString spannableString = new SpannableString(id);
        spannableString.setSpan(new UnderlineSpan(), 0, id.length(), 0);

        // Set the spannableString to the TextView
        holder.tvRestaurantId.setText(spannableString);
        holder.tvRestaurantName.setText("" + restaurant.getRestaurantName());
        holder.tvRestaurantAddress.setText("" + restaurant.getAddress());

        holder.tvRestaurantId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RestaurantDetailActivity.class);
                intent.putExtra("restaurant_id", restaurant.getRestaurantId());
                intent.putExtra("restaurant_name", restaurant.getRestaurantName());
                intent.putExtra("restaurant_description", restaurant.getRestaurantDescription());
                intent.putExtra("restaurant_email", restaurant.getEmail());
                intent.putExtra("restaurant_phone_number", restaurant.getPhoneNumber());
                intent.putExtra("restaurant_address", restaurant.getAddress());
                intent.putExtra("restaurant_image", restaurant.getRestaurantImage());
                context.startActivity(intent);
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RestaurantUpdateActivity.class);
                intent.putExtra("restaurant_id", restaurant.getRestaurantId());
                intent.putExtra("restaurant_name", restaurant.getRestaurantName());
                intent.putExtra("restaurant_description", restaurant.getRestaurantDescription());
                intent.putExtra("restaurant_email", restaurant.getEmail());
                intent.putExtra("restaurant_phone_number", restaurant.getPhoneNumber());
                intent.putExtra("restaurant_address", restaurant.getAddress());
                intent.putExtra("restaurant_image", restaurant.getRestaurantImage());
                context.startActivity(intent);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Restaurant");
                builder.setMessage("Are you sure you want to delete this Restaurant?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RestaurantRepository restaurantRepository = new RestaurantRepository(context);
                                restaurantRepository.deleteRestaurant(Integer.parseInt(id));
                                Toast.makeText(context, "Delete Restaurant successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, RestaurantListActivity.class);
                                context.startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder{
        private TextView tvRestaurantId, tvRestaurantName, tvRestaurantAddress;
        private ImageButton btnEdit, btnDelete;

        @SuppressLint("WrongViewCast")
        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRestaurantId = itemView.findViewById(R.id.tv_restaurant_id);
            tvRestaurantName = itemView.findViewById(R.id.tv_restaurant_name);
            tvRestaurantAddress = itemView.findViewById(R.id.tv_restaurant_address);
            btnEdit = itemView.findViewById(R.id.btn_edit_restaurant);
            btnDelete = itemView.findViewById(R.id.btn_delete_restaurant);
        }
    }
}
