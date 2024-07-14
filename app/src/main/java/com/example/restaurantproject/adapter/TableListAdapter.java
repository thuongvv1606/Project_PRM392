package com.example.restaurantproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantproject.CategoryDetailsActivity;
import com.example.restaurantproject.R;
import com.example.restaurantproject.TableDetailActivity;
import com.example.restaurantproject.bean.Category;
import com.example.restaurantproject.bean.Table;
import com.example.restaurantproject.ultils.constant.Common;

import java.util.List;

public class TableListAdapter extends RecyclerView.Adapter<TableListAdapter.TableViewHolder>{
    private List<Table> tables = null;
    private Context context;
    public  TableListAdapter(List<Table> tables, Context context){
        this.context = context;
        this.tables = tables;
    }
    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.table_list_item, parent, false);
        return new TableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        Table table = tables.get(position);

        holder.tvTableName.setText(table.getName());
        holder.tvNumSeat.setText("Number of seat: "+table.getSeatNum());
//        if (table.getStatus() == 1){
//            holder.tvTableStatus.setText("Status: Empty table");
//            holder.btnReservation.setEnabled(true);
//        } else if (table.getStatus() == 2) {
//            holder.tvTableStatus.setText("Status: Live table");
//            holder.btnReservation.setEnabled(true);
//        } else if (table.getStatus() == 4) {
//            holder.tvTableStatus.setText("Status: Delete");
//            holder.btnReservation.setEnabled(false);
//        }else {
//            holder.tvTableStatus.setText("Status: Error table");
//            holder.btnReservation.setEnabled(true);
//        }
        holder.tvTableStatus.setText("Status: " + Common.tableStatus.get(table.getStatus()-1));
        holder.btnReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TableDetailActivity.class);
                intent.putExtra("table_id", table.getTableId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tables.size();
    }

    public class TableViewHolder extends RecyclerView.ViewHolder{
        private TextView tvNumSeat, tvTableName, tvTableStatus;
        private Button btnReservation;

        @SuppressLint("WrongViewCast")
        public TableViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTableName = itemView.findViewById(R.id.tv_table_item_name);
            tvNumSeat = itemView.findViewById(R.id.tv_table_item_num_seats);
            tvTableStatus = itemView.findViewById(R.id.tv_table_item_status);
            btnReservation = itemView.findViewById(R.id.table_detail_button);
        }
    }
}
