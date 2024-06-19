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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantproject.CategoryDetailsActivity;
import com.example.restaurantproject.CategoryListActivity;
import com.example.restaurantproject.CategoryUpdateActivity;
import android.Manifest;
import com.example.restaurantproject.R;
import com.example.restaurantproject.bean.Category;
import com.example.restaurantproject.repository.CategoryRepository;

import java.util.List;

public class CategoryListAdapter  extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder>{
    private List<Category> categories = null;
    private Context context;
    public  CategoryListAdapter(List<Category> categories, Context context){
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_category_list_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);

        String id = "" + category.getCategoryId();
        // Create a SpannableString with UnderlineSpan
        SpannableString spannableString = new SpannableString(id);
        spannableString.setSpan(new UnderlineSpan(), 0, id.length(), 0);

        // Set the spannableString to the TextView
        holder.tvCategoryId.setText(spannableString);
        holder.tvCategoryName.setText("" + category.getCategoryName());

        holder.tvCategoryId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategoryDetailsActivity.class);
                intent.putExtra("category_id", category.getCategoryId());
                intent.putExtra("category_name", category.getCategoryName());
                intent.putExtra("category_description", category.getCategoryDescription());
                intent.putExtra("category_image", category.getCategoryImage());
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(intent);
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategoryUpdateActivity.class);
                intent.putExtra("category_id", category.getCategoryId());
                intent.putExtra("category_name", category.getCategoryName());
                intent.putExtra("category_description", category.getCategoryDescription());
                intent.putExtra("category_image", category.getCategoryImage());
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(intent);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Category");
                builder.setMessage("Are you sure you want to delete this category?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CategoryRepository categoryRepository = new CategoryRepository(context);
                                categoryRepository.deleteCategory(Integer.parseInt(id));
                                Toast.makeText(context, "Delete Category successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, CategoryListActivity.class);
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
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        private TextView tvCategoryId, tvCategoryName;
        private ImageButton btnEdit, btnDelete;

        @SuppressLint("WrongViewCast")
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryId = itemView.findViewById(R.id.tv_category_id);
            tvCategoryName = itemView.findViewById(R.id.tv_category_name);
            btnEdit = itemView.findViewById(R.id.btn_edit_category);
            btnDelete = itemView.findViewById(R.id.btn_delete_category);
        }
    }
}
