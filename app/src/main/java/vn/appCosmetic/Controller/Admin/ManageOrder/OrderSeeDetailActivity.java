package vn.appCosmetic.Controller.Admin.ManageOrder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.appCosmetic.Controller.User.Order.OrderCartProductItemAdapter;
import vn.appCosmetic.Model.CartProduct;
import vn.appCosmetic.Model.Order;
import vn.appCosmetic.R;
import vn.appCosmetic.ServiceAPI.CartProduct.APICartProductService;
import vn.appCosmetic.ServiceAPI.Order.APIOrderService;
import vn.appCosmetic.ServiceAPI.RetrofitPrivate;

public class OrderSeeDetailActivity extends AppCompatActivity {
    private List<CartProduct> cartProductList=new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private OrderCartProductItemAdapter orderCartProductItemAdapter;

    private RecyclerView recyclerView;

    private APIOrderService apiOrderService;

    private APICartProductService apiCartProductService;

    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_see_detail);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        recyclerView = findViewById(R.id.rc_detail_order);
        int orderId = getIntent().getIntExtra("orderID", 0);
        apiCartProductService = RetrofitPrivate.getRetrofit(token).create(APICartProductService.class);
        apiOrderService = RetrofitPrivate.getRetrofit(token).create(APIOrderService.class);

        apiOrderService.getOrderById(orderId).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                Order order = response.body();

                apiCartProductService.getCartProductByCartId(order.getCart().getId()).enqueue(new Callback<List<CartProduct>>() {
                    @Override
                    public void onResponse(Call<List<CartProduct>> call, Response<List<CartProduct>> response) {
                        cartProductList = response.body();
                        orderCartProductItemAdapter = new OrderCartProductItemAdapter(OrderSeeDetailActivity.this, cartProductList);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(OrderSeeDetailActivity.this);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(orderCartProductItemAdapter);
                    }

                    @Override
                    public void onFailure(Call<List<CartProduct>> call, Throwable t) {
                        Toast.makeText(OrderSeeDetailActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(OrderSeeDetailActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack = findViewById(R.id.buttonBack_Detail_order);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}