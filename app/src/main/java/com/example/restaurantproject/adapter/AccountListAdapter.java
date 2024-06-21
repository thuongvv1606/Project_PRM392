package com.example.restaurantproject.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantproject.AccountDetailsActivity;
import com.example.restaurantproject.AccountListActivity;
import com.example.restaurantproject.AccountUpdateActivity;
import com.example.restaurantproject.R;
import com.example.restaurantproject.entity.AccountDTO;
import com.example.restaurantproject.repository.AccountRepository;

import java.util.List;

// Adapter cho RecyclerView để hiển thị danh sách tài khoản
public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.AccountViewHolder> {

    private List<AccountDTO> accounts = null;
    private Context context;
    private AccountRepository accountRepository;

    public AccountListAdapter(List<AccountDTO> accounts, Context context) {
        this.context = context;
        this.accounts = accounts;
        this.accountRepository = new AccountRepository(context);
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout cho từng item trong danh sách
        View view = LayoutInflater.from(context).inflate(R.layout.account_item, parent, false);
        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        // Lấy thông tin tài khoản tại vị trí "position"
        AccountDTO account = accounts.get(position);

        // Hiển thị ID của tài khoản và gạch chân
        String id = "" + account.getAccountId();
        SpannableString spannableString = new SpannableString(id);
        spannableString.setSpan(new UnderlineSpan(), 0, id.length(), 0);

        holder.tvAccountId.setText(spannableString);
        holder.tvAccountUsername.setText(account.getUsername());
        holder.tvAccountRole.setText(String.valueOf(account.getRoleName()));

        // Ẩn icon xóa và khóa đối với tài khoản admin
        if ("admin".equalsIgnoreCase(account.getRoleName())) {
            holder.ivDeleteIcon.setImageResource(R.drawable.ic_none);
            holder.ivLockIcon.setImageResource(R.drawable.ic_none);
        } else {

            // Đặt biểu tượng lock/unlock dựa trên trạng thái của tài khoản
            if (account.isStatus()) {
                holder.ivLockIcon.setImageResource(R.drawable.ic_lock);
            } else {
                holder.ivLockIcon.setImageResource(R.drawable.ic_unlock);
            }

            // Xử lý sự kiện khi nhấn vào biểu tượng lock/unlock
            holder.ivLockIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean newStatus = !account.isStatus();
                    accountRepository.updateAccountStatus(Integer.parseInt(id), newStatus);
                    // Cập nhật trạng thái tài khoản
                    account.setStatus(newStatus);
                    notifyDataSetChanged();
                    String message = newStatus ? "Account Unlocked" : "Account Locked";
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            });

            // Xử lý sự kiện khi nhấn vào icon xóa
            holder.ivDeleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Delete Account");
                    builder.setMessage("Are you sure you want to delete this account?")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    AccountRepository accountRepository = new AccountRepository(context);
                                    accountRepository.deleteAccount(Integer.parseInt(id));
                                    Toast.makeText(context, "Delete Account successfully!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, AccountListActivity.class);
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

        // Xử lý sự kiện khi nhấn vào ID tài khoản
        holder.tvAccountId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AccountDetailsActivity.class);
                intent.putExtra("accountId", account.getAccountId());
                intent.putExtra("username", account.getUsername());
                intent.putExtra("password", account.getPassword());
                intent.putExtra("fullname", account.getFullname());
                intent.putExtra("email", account.getEmail());
                intent.putExtra("phoneNumber", account.getPhoneNumber());
                intent.putExtra("address", account.getAddress());
                intent.putExtra("roleId", account.getRoleId());
                intent.putExtra("restaurantId", account.getRestaurantId());
                intent.putExtra("status", account.isStatus());
                intent.putExtra("avatar", account.getAvatar());
                intent.putExtra("roleName", account.getRoleName());
                intent.putExtra("restaurantName", account.getRestaurantName());
                context.startActivity(intent);
            }
        });

        // Xử lý sự kiện khi nhấn vào icon edit
        holder.ivEditIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AccountUpdateActivity.class);
                intent.putExtra("accountId", account.getAccountId());
                intent.putExtra("username", account.getUsername());
                intent.putExtra("password", account.getPassword());
                intent.putExtra("fullname", account.getFullname());
                intent.putExtra("email", account.getEmail());
                intent.putExtra("phoneNumber", account.getPhoneNumber());
                intent.putExtra("address", account.getAddress());
                intent.putExtra("roleId", account.getRoleId());
                intent.putExtra("restaurantId", account.getRestaurantId());
                intent.putExtra("status", account.isStatus());
                intent.putExtra("avatar", account.getAvatar());
                intent.putExtra("roleName", account.getRoleName());
                intent.putExtra("restaurantName", account.getRestaurantName());
                context.startActivity(intent);
            }
        });

    }

    // ViewHolder để giữ các thành phần UI cho từng item
    @Override
    public int getItemCount() {
        return accounts.size();
    } // Trả về số lượng tài khoản

    public class AccountViewHolder extends RecyclerView.ViewHolder {
        TextView tvAccountId, tvAccountUsername, tvAccountRole;
        ImageView ivEditIcon, ivDeleteIcon, ivLockIcon;

        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAccountId = itemView.findViewById(R.id.tv_account_id);
            tvAccountUsername = itemView.findViewById(R.id.tv_account_username);
            tvAccountRole = itemView.findViewById(R.id.tv_account_role);
            ivEditIcon = itemView.findViewById(R.id.iv_edit_icon);
            ivDeleteIcon = itemView.findViewById(R.id.iv_delete_icon);
            ivLockIcon = itemView.findViewById(R.id.iv_lock_icon);
        }
    }
}
