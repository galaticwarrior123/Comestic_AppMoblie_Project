package vn.appCosmetic.Controller.User.User.ManagerMyOrder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.appCosmetic.Model.Order;
import vn.appCosmetic.R;
import vn.appCosmetic.ServiceAPI.Order.APIOrderService;
import vn.appCosmetic.ServiceAPI.RetrofitPrivate;

public class MyOrdersActivity extends AppCompatActivity {

    private OrderItemAdapter orderItemAdapter;
    private List<Order> orderList;

    private RecyclerView recyclerOrder;

    private ImageButton btnBack;

    private SharedPreferences sharedPreferences;

    private APIOrderService apiOrderService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        recyclerOrder = findViewById(R.id.rc_my_orders);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("idUser", 0);
        String token = sharedPreferences.getString("token", "");


        orderList = new ArrayList<>();
        apiOrderService = RetrofitPrivate.getRetrofit(token).create(APIOrderService.class);
        apiOrderService.getAllOrderByIdUser(userId).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful()) {
                    orderList = response.body();
                    orderItemAdapter = new OrderItemAdapter(MyOrdersActivity.this, orderList);
                    recyclerOrder.setLayoutManager(new LinearLayoutManager(MyOrdersActivity.this));
                    recyclerOrder.setAdapter(orderItemAdapter);

                }
                else{
                    Toast.makeText(MyOrdersActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(MyOrdersActivity.this, "Error 1", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack = findViewById(R.id.imgOrderBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}