package vn.appCosmetic.Controller.User.User.ManagerMyOrder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.appCosmetic.Controller.Payment.BankActivity;
import vn.appCosmetic.Controller.User.Order.OrderCartProductItemAdapter;
import vn.appCosmetic.Model.CartProduct;
import vn.appCosmetic.Model.Order;
import vn.appCosmetic.R;
import vn.appCosmetic.ServiceAPI.CartProduct.APICartProductService;
import vn.appCosmetic.ServiceAPI.Order.APIOrderService;
import vn.appCosmetic.ServiceAPI.RetrofitPrivate;

public class SeeDetailOrderMainActivity extends AppCompatActivity {

    private RecyclerView recyclerOrderDetail;

    private OrderCartProductItemAdapter orderCartProductItemAdapter;

    private TextView txtOrderTotalPrice, txtOrderTotalQuantity;

    private Long total = 0L;

    private LinearLayout layoutInfoOrder;
    private Button btnUpdateOrder, btnConfirmOrder;

    private EditText edtAddress, edtPhone, edtName;

    private List<CartProduct> cartProductList=new ArrayList<>();

    private APIOrderService apiOrderService;
    private APICartProductService apiCartProductService;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_detail_order_main);

        int orderId = getIntent().getIntExtra("orderId", 0);

        recyclerOrderDetail = findViewById(R.id.recycler_order_seeDetail);
        txtOrderTotalPrice = findViewById(R.id.txt_total_price_seeDetail);
        txtOrderTotalQuantity = findViewById(R.id.txt_total_quantity_seeDetail);

        layoutInfoOrder = findViewById(R.id.ll_order_seeDetail);

        edtAddress = findViewById(R.id.edt_address_order_seeDetail);
        edtPhone = findViewById(R.id.edt_phone_order_seeDetail);
        edtName = findViewById(R.id.edt_name_order_seeDetail);

        btnUpdateOrder = findViewById(R.id.btn_order_seeDetail);
        btnConfirmOrder= findViewById(R.id.btn_confirm_seeDetail);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        // Call API to get order by id
        // Call API to get all cart product by order id
        apiCartProductService= RetrofitPrivate.getRetrofit(token).create(APICartProductService.class);
        apiOrderService = RetrofitPrivate.getRetrofit(token).create(APIOrderService.class);

        apiOrderService.getOrderById(orderId).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if(response.isSuccessful()){
                    Order order = response.body();

                    apiCartProductService.getCartProductByCartId(order.getCart().getId()).enqueue(new Callback<List<CartProduct>>() {
                        @Override
                        public void onResponse(Call<List<CartProduct>> call, Response<List<CartProduct>> response1) {
                            if(response1.isSuccessful()){
                                cartProductList = response1.body();
                                orderCartProductItemAdapter = new OrderCartProductItemAdapter(SeeDetailOrderMainActivity.this, cartProductList);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SeeDetailOrderMainActivity.this);
                                recyclerOrderDetail.setLayoutManager(layoutManager);
                                recyclerOrderDetail.setAdapter(orderCartProductItemAdapter);
                                for (CartProduct cartProduct : cartProductList) {
                                    total += cartProduct.getQuantity() * cartProduct.getProduct().getPrice();
                                }
                                txtOrderTotalPrice.setText(String.valueOf(total));
                                txtOrderTotalQuantity.setText(String.valueOf(cartProductList.size()));
                                if(!order.isStatus()){
                                    btnUpdateOrder.setVisibility(View.VISIBLE);


                                    // Tiến hành thanh toán
                                    btnUpdateOrder.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            btnConfirmOrder.setVisibility(View.VISIBLE);
                                            btnUpdateOrder.setVisibility(View.GONE);
                                            layoutInfoOrder.setVisibility(View.VISIBLE);

                                            btnConfirmOrder.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    String name = edtName.getText().toString().trim();
                                                    String phone = edtPhone.getText().toString().trim();
                                                    String address = edtAddress.getText().toString().trim();

                                                    if(name.isEmpty() || phone.isEmpty() || address.isEmpty()){
                                                        Toast.makeText(SeeDetailOrderMainActivity.this, "Please fill all information", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else{
                                                        order.setAddress(address);
                                                        order.setPhone(phone);
                                                        apiOrderService.updateOrder(order.getId(),order).enqueue(new Callback<Order>() {
                                                            @Override
                                                            public void onResponse(Call<Order> call, Response<Order> response) {
                                                                if(response.isSuccessful()){
                                                                    Toast.makeText(SeeDetailOrderMainActivity.this, "Update success", Toast.LENGTH_SHORT).show();
                                                                    sharedPreferences.edit().putLong("total", order.getTotal()).apply();
                                                                    sharedPreferences.edit().putInt("orderId", order.getId()).apply();
                                                                    Intent intent = new Intent(SeeDetailOrderMainActivity.this, BankActivity.class);
                                                                    startActivity(intent);
                                                                }
                                                                else{
                                                                    Toast.makeText(SeeDetailOrderMainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<Order> call, Throwable t) {
                                                                Toast.makeText(SeeDetailOrderMainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                }
                                            });

                                        }
                                    });
                                }
                            }
                            else{
                                Toast.makeText(SeeDetailOrderMainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<CartProduct>> call, Throwable t) {
                            Toast.makeText(SeeDetailOrderMainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(SeeDetailOrderMainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });


    }
}