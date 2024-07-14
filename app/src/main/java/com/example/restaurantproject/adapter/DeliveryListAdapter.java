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

import com.example.restaurantproject.DeliveryDetailActivity;
import com.example.restaurantproject.DeliveryListActivity;
import com.example.restaurantproject.R;
import com.example.restaurantproject.RestaurantDetailActivity;
import com.example.restaurantproject.RestaurantListActivity;
import com.example.restaurantproject.entity.DeliveryDTO;
import com.example.restaurantproject.repository.DeliveryRepository;
import com.example.restaurantproject.repository.RestaurantRepository;

import java.util.List;

public class DeliveryListAdapter extends RecyclerView.Adapter<DeliveryListAdapter.RestaurantViewHolder>{
    private List<DeliveryDTO> deliveries= null;
    private Context context;
    public DeliveryListAdapter(List<DeliveryDTO> deliveries, Context context){
        this.context = context;
        this.deliveries = deliveries;
    }

    @NonNull
    @Override
    public DeliveryListAdapter.RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.delivery_item_list, parent, false);
        return new DeliveryListAdapter.RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryListAdapter.RestaurantViewHolder holder, int position) {
        DeliveryDTO delivery = deliveries.get(position);

        String id = "" + delivery.getDeliveryId();
        // Create a SpannableString with UnderlineSpan
        SpannableString spannableString = new SpannableString(id);
        spannableString.setSpan(new UnderlineSpan(), 0, id.length(), 0);

        // Set the spannableString to the TextView
        holder.tvDeliveryId.setText(spannableString);
        holder.tvDeliverName.setText("" + delivery.getDelivererName());
        holder.tvDeliverydate.setText("" + delivery.getOrderDate());
        int status = delivery.getDeliveryStatus();
        if (status == 1) {
            holder.tvDeliveryStatus.setText("Not yet");
        }
        if (status == 2) {
            holder.tvDeliveryStatus.setText("Ongoing");
        }
        if (status == 3) {
            holder.tvDeliveryStatus.setText("Done");
        }
        if (status == 4) {
            holder.tvDeliveryStatus.setText("Cancel");
        }
        holder.tvDeliveryId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DeliveryDetailActivity.class);
                intent.putExtra("Deliver_ID", delivery.getDeliveryId());
                context.startActivity(intent);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Delivery");
                builder.setMessage("Are you sure you want to delete this Delivery?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DeliveryRepository deliveryRepository = new DeliveryRepository(context);
                                deliveryRepository.deleteDelivery(delivery.getDeliveryId());
                                Toast.makeText(context, "Delete Delivery successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, DeliveryListActivity.class);
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
        return deliveries.size();
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder{
        private TextView tvDeliverName, tvDeliveryId, tvDeliveryStatus, tvDeliverydate;
        private ImageButton  btnDelete;
        @SuppressLint("WrongViewCast")
        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDeliveryId = itemView.findViewById(R.id.tv_delivery_id);
            tvDeliverName = itemView.findViewById(R.id.tv_deliver_name);
            tvDeliverydate = itemView.findViewById(R.id.tv_delivery_date);
            tvDeliveryStatus = itemView.findViewById(R.id.tv_delivery_status);
            btnDelete = itemView.findViewById(R.id.btn_delete_delivery);
        }
    }
}
