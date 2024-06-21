package com.example.restaurantproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantproject.adapter.AccountListAdapter;
import com.example.restaurantproject.entity.AccountDTO;
import com.example.restaurantproject.repository.AccountRepository;

import java.util.List;

public class AccountListActivity extends AppCompatActivity {

    // Khai báo các biến cho các thành phần UI
    private RecyclerView recyclerView;
    private AccountListAdapter adapter;
    private List<AccountDTO> accountList;
    private AccountRepository accountRepository;
    private EditText edtSearchAccount;
    private Button btnSearchAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo đối tượng AccountRepository
        accountRepository = new AccountRepository(this);
        // Khởi tạo RecyclerView và thiết lập layout manager
        recyclerView = findViewById(R.id.account_list_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Lấy danh sách tài khoản từ DB
        accountList = accountRepository.getAllAccountDTOs();

        // Khởi tạo adapter và thiết lập cho RecyclerView
        setAccountList(accountList);

        // Hiển thị số lượng tài khoản tìm thấy
        TextView txtCount = findViewById(R.id.tv_category_count);
        txtCount.setText("Found " + accountList.size() + " account(s)");

        // Khởi tạo các thành phần search
        edtSearchAccount = findViewById(R.id.edt_search_account);
        btnSearchAccount = findViewById(R.id.btn_search_account);

        // Thiết lập sự kiện click cho nút search
        btnSearchAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tìm kiếm tài khoản và cập nhật danh sách theo username
                accountList = accountRepository.searchAccounts(edtSearchAccount.getText().toString().trim().toLowerCase());
                setAccountList(accountList);
            }
        });

        // Khởi tạo nút Add account và thiết lập sự kiện click
        Button btnAddAccount = findViewById(R.id.btn_add_account);
        btnAddAccount.setOnClickListener(v -> {
            Intent intent = new Intent(AccountListActivity.this, AccountAddActivity.class);
            startActivity(intent);
        });
    }
    // Phương thức thiết lập danh sách tài khoản cho RecyclerView
    private void setAccountList(List<AccountDTO> accountList) {
        adapter = new AccountListAdapter(accountList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}