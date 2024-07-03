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

import com.example.restaurantproject.MenuDetailsActivity;
import com.example.restaurantproject.MenuListActivity;
import com.example.restaurantproject.MenuUpdateActivity;
import com.example.restaurantproject.ProductDetailsActivity;
import com.example.restaurantproject.ProductListActivity;
import com.example.restaurantproject.ProductUpdateActivity;
import com.example.restaurantproject.R;
import com.example.restaurantproject.bean.Category;
import com.example.restaurantproject.bean.Menu;
import com.example.restaurantproject.bean.Product;
import com.example.restaurantproject.bean.Restaurant;
import com.example.restaurantproject.repository.CategoryRepository;
import com.example.restaurantproject.repository.MenuRepository;
import com.example.restaurantproject.repository.ProductRepository;
import com.example.restaurantproject.repository.RestaurantRepository;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductViewHolder> {

    private List<Product> products;
    private Context context;
    private CategoryRepository categoryRepository;

    public ProductListAdapter(List<Product> products, Context context) {
        this.products = products;
        this.context = context;
        this.categoryRepository = new CategoryRepository(context);  // Initialize repository
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_list_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);

        String id = "" + product.getProductId();
        String productName = product.getProductName();
        int categoryId = product.getCategoryId();

        // Fetch restaurant name using restaurantId
        Category category = categoryRepository.getCategory(categoryId);
        String categoryName = category != null ? category.getCategoryName() : "Unknown";

        // Create a SpannableString with UnderlineSpan
        SpannableString spannableString = new SpannableString(id);
        spannableString.setSpan(new UnderlineSpan(), 0, id.length(), 0);

        // Set the values to the TextViews
        holder.tvProductId.setText(spannableString);
        holder.tvProudctName.setText(productName);
        holder.tvCategoryName.setText(categoryName);

        // Set click listeners
        holder.tvProductId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("product_id", product.getProductId());
                context.startActivity(intent);
            }
        });

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductUpdateActivity.class);
            intent.putExtra("product_id", product.getProductId());
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            ProductRepository productRepository = new ProductRepository(context);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete Product");
            builder.setMessage("Are you sure you want to delete product " + product.getProductName() + "?")
                    .setPositiveButton("OK", (dialog, which) -> {
                        productRepository.deleteProduct(product.getProductId());
                        Toast.makeText(context, "Product deleted successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, ProductListActivity.class);
                        context.startActivity(intent);
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{
        private TextView tvProductId, tvCategoryName, tvProudctName;
        private ImageButton btnEdit, btnDelete;

        @SuppressLint("WrongViewCast")
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductId = itemView.findViewById(R.id.tv_product_id);
            tvProudctName = itemView.findViewById(R.id.tv_product_name);
            tvCategoryName = itemView.findViewById(R.id.tv_category_id);
            btnEdit = itemView.findViewById(R.id.btn_edit_product);
            btnDelete = itemView.findViewById(R.id.btn_delete_product);
        }
    }
}
