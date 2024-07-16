package com.example.restaurantproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantproject.bean.Order;
import com.example.restaurantproject.bean.OrderDetails;
import com.example.restaurantproject.bean.Product;
import com.example.restaurantproject.bean.Restaurant;
import com.example.restaurantproject.bean.Table;
import com.example.restaurantproject.entity.AccountDTO;
import com.example.restaurantproject.entity.ProductOrderDTO;
import com.example.restaurantproject.repository.OrderDetailsRepository;
import com.example.restaurantproject.repository.OrderRepository;
import com.example.restaurantproject.repository.ProductRepository;
import com.example.restaurantproject.repository.RestaurantRepository;
import com.example.restaurantproject.repository.TableRepository;
import com.example.restaurantproject.ultils.session.SessionManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReservationActivity extends AppCompatActivity {
    private EditText edtCustomerName, edtNote;
    private Spinner spnRestaurant, spnTable;
    private TextView tvReservaionDate;
    private Button btnReservation, btnCancel, btnSelectDate;
    private RestaurantRepository restaurantRepository;
    private TableRepository tableRepository;
    private OrderRepository orderRepository;
    private SessionManager sessionManager;
    private ArrayAdapter<String> restaurantAdapter;
    private ArrayAdapter<String> tableAdapter;
    private Map<String, Integer> restaurantMap = new HashMap<>();
    private Map<String, Integer> tableMap = new HashMap<>();
    private AccountDTO loggedInAccount;
    private Calendar myCalendar = Calendar.getInstance();
    DateFormat fmtDateAndTime = DateFormat.getDateTimeInstance();
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener()
    {
        public void onDateSet(DatePicker view,
                              int year, int monthOfYear, int dayOfMonth) {
            Calendar currentDate = Calendar.getInstance();
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            if (myCalendar.before(currentDate)){
                tvReservaionDate.setError("");
                Toast.makeText(ReservationActivity.this, "Reservaion date must be greater current date", Toast.LENGTH_LONG).show();
                return;
            }else{
                new TimePickerDialog(ReservationActivity.this, t,
                        myCalendar.get(Calendar.HOUR_OF_DAY),
                        myCalendar.get(Calendar.MINUTE), true).show();
            }
        }
    };
    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if (hourOfDay >= 9 && hourOfDay <= 22) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                tvReservaionDate.setText(sdf.format(myCalendar.getTime()));
            }else{
                tvReservaionDate.setError("");
                Toast.makeText(ReservationActivity.this, "Restaurent open on time 9am to 22pm", Toast.LENGTH_LONG).show();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reservation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar_back_order_main);
        // Set the navigation icon click listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReservationActivity.this, OrderAddMainActivity.class);
                startActivity(intent);
            }
        });

        // Initialize repositories
        restaurantRepository = new RestaurantRepository(this);
        tableRepository = new TableRepository(this);
        orderRepository = new OrderRepository(this);
        sessionManager = new SessionManager(this);

        edtCustomerName = findViewById(R.id.edt_customer_name);
        edtNote = findViewById(R.id.edt_order_note);
        tvReservaionDate = findViewById(R.id.dtp_reservation_date);
        btnSelectDate = findViewById(R.id.btnDate);

        if (sessionManager.isLoggedIn()) {
            // User is logged in, show user info and logout/edit buttons
            loggedInAccount = sessionManager.getAccountFromSession();
            if(loggedInAccount != null) {
                edtCustomerName.setText(loggedInAccount.getFullname());
            }
        }else{
            Intent intent = new Intent(ReservationActivity.this, UserLoginActivity.class);
            intent.putExtra("msg", "Please login before reservation");
            startActivity(intent);
            finish();
        }
        
        spnRestaurant = findViewById(R.id.spn_restaurant);
        populateRestaurantSpinner();

        spnTable = findViewById(R.id.spn_number_table);
        populateTableSpinner();

        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new DatePickerDialog(ReservationActivity.this, d,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        btnReservation = findViewById(R.id.btn_add_reservation);
        btnReservation.setOnClickListener(v -> addOrder());

        btnCancel = findViewById(R.id.btn_cancel_add_order);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReservationActivity.this, OrderListActivity.class);
                startActivity(intent);
            }
        });
    }

    private int getRestaurantId(){
        String selectedRestaurantName = (String) spnRestaurant.getSelectedItem();
        if (selectedRestaurantName == null || selectedRestaurantName.isEmpty()) {
            Toast.makeText(this, "Please select a restaurant", Toast.LENGTH_SHORT).show();
            return 0;
        }

        return restaurantMap.get(selectedRestaurantName);
    }
    private Integer getTableId(){
        String selectedTableName = (String) spnTable.getSelectedItem();
        if (selectedTableName == null || selectedTableName.isEmpty()){
            return null;
        }
        return tableMap.get(selectedTableName);
    }

    private void addOrder() {

        String reservatonDate = tvReservaionDate.getText().toString();
        if (reservatonDate.isEmpty() || reservatonDate == ""){
            Toast.makeText(ReservationActivity.this, "Reservation date can't null", Toast.LENGTH_LONG).show();
            return;
        }
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        // Format date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(date);
        Order order = new Order();

        order.setCustomerId(loggedInAccount.getAccountId());
        order.setStatus(0);
        order.setPayment(false);
        order.setTableID(getTableId());
        order.setReservationDate(reservatonDate);
        order.setOrderDate(currentDate);
        order.setNote(edtNote.getText().toString());

        if (orderRepository != null && tableRepository != null){
            orderRepository.createOrder(order);
            int orderId = orderRepository.getOrderNewest().getOrderId();

            tableRepository.updateTableStatus(getTableId(), 2);
            Toast.makeText(ReservationActivity.this, "Add category successfully",
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ReservationActivity.this, OrderListActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(ReservationActivity.this, "Order/OrderDetail/Table repository is not initialized", Toast.LENGTH_LONG).show();
        }
    }

    private void populateTableSpinner() {
        int selectedRestaurantId = getRestaurantId();

        // Fetch table data
        List<Table> tableList = tableRepository.getTableByRestaurant(selectedRestaurantId);
        List<String> tableNames = new ArrayList<>();

        // Populate the map and the list of names
        for (Table item : tableList) {
            tableNames.add(item.getName());
            tableMap.put(item.getName(), item.getRestaurantId());
        }

        // Create an adapter for the spinner
        tableAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tableNames);
        tableAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTable.setAdapter(tableAdapter);
    }

    private void populateRestaurantSpinner() {
        // Fetch restaurant data
        List<Restaurant> restaurantList = restaurantRepository.getAllRestaurants();
        List<String> restaurantNames = new ArrayList<>();

        // Populate the map and the list of names
        for (Restaurant restaurant : restaurantList) {
            restaurantNames.add(restaurant.getRestaurantName());
            restaurantMap.put(restaurant.getRestaurantName(), restaurant.getRestaurantId());
        }

        // Create an adapter for the spinner
        restaurantAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, restaurantNames);
        restaurantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnRestaurant.setAdapter(restaurantAdapter);
    }
}