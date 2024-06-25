package com.example.restaurantproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.restaurantproject.R;

public class BannerAdapter extends PagerAdapter {
    private Context context;
    private int[] images; // Mảng chứa các ID của hình ảnh banner

    // Constructor để khởi tạo Adapter với Context và mảng hình ảnh
    public BannerAdapter(Context context, int[] images) {
        this.context = context;
        this.images = images;
    }

    // Trả về số lượng phần tử trong dữ liệu (số lượng hình ảnh)
    @Override
    public int getCount() {
        return images.length;
    }

    // Tạo view cho item tại vị trí cho trước
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        // Inflate layout cho item banner
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_banner, container, false);

        // Tìm ImageView trong layout được inflate
        ImageView imageView = view.findViewById(R.id.banner_image);

        // Set hình ảnh cho ImageView dựa vào vị trí
        imageView.setImageResource(images[position]);

        // Thêm view đã inflate vào ViewPager container
        container.addView(view);

        // Trả về view đã inflate làm object
        return view;
    }

    // Xóa item khỏi container tại vị trí cho trước
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    // Kiểm tra xem view có liên kết với object cho trước hay không
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
