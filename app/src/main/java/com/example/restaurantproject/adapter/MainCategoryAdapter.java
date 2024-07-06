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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.restaurantproject.CategoryDetailsActivity;
import com.example.restaurantproject.CategoryListActivity;
import com.example.restaurantproject.CategoryUpdateActivity;
import com.example.restaurantproject.MainCategoryList;
import com.example.restaurantproject.R;
import com.example.restaurantproject.bean.Category;
import com.example.restaurantproject.repository.CategoryRepository;

import java.util.List;

public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.CategoryViewHolder>{
        private List<Category> categories = null;
        private Context context;
        public  MainCategoryAdapter(List<Category> categories, Context context){
            this.context = context;
            this.categories = categories;
        }

        @NonNull
        @Override
        public MainCategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.main_category_item, parent, false);
            return new MainCategoryAdapter.CategoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
            Category category = categories.get(position);

            int id = category.getCategoryId();

            // Set the spannableString to the TextView
            holder.tvCategoryName.setText("" + category.getCategoryName());
            if (category.getCategoryImage() != null && !category.getCategoryImage().isEmpty()) {
                Glide.with(context).load(category.getCategoryImage()).into(holder.tvCategoryImage);
            }

            holder.tvCategoryImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    directToMainCategoryListView(id);
                }
            });

            holder.tvCategoryName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    directToMainCategoryListView(id);
                }
            });
        }

        public void directToMainCategoryListView(int id) {
            Intent intent = new Intent(context, MainCategoryList.class);
            intent.putExtra("Cate_ID", id);
            context.startActivity(intent);
        }

        @Override
        public int getItemCount() {
            return categories.size();
        }

        public class CategoryViewHolder extends RecyclerView.ViewHolder{
            private TextView tvCategoryName;
            private ImageView tvCategoryImage;

            @SuppressLint("WrongViewCast")
            public CategoryViewHolder(@NonNull View itemView) {
                super(itemView);
                tvCategoryName = itemView.findViewById(R.id.category_name);
                tvCategoryImage = itemView.findViewById(R.id.category_image);
            }
        }
}
