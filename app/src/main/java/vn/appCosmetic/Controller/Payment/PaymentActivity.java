package vn.appCosmetic.Controller.Payment;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.appCosmetic.Controller.User.UserMainActivity;
import vn.appCosmetic.Model.Cart;
import vn.appCosmetic.Model.Order;
import vn.appCosmetic.Model.Payment;
import vn.appCosmetic.Model.ResponsePayment;
import vn.appCosmetic.ServiceAPI.Bank.APIPaymentService;
import vn.appCosmetic.ServiceAPI.Cart.APICartService;
import vn.appCosmetic.ServiceAPI.Order.APIOrderService;
import vn.appCosmetic.ServiceAPI.RetrofitClient;
import vn.appCosmetic.ServiceAPI.RetrofitPrivate;
import vn.appCosmetic.databinding.ActivityPaymentBinding;

public class PaymentActivity extends AppCompatActivity {
    private ActivityPaymentBinding binding;
    private APIPaymentService apiPaymentService;
    private APIOrderService apiOrderService;
    private SharedPreferences sharedPreferences;

    @SuppressLint({"SetJavaScriptEnabled", "SetDomStorageEnabled", "SetDatabaseEnabled", "SetCacheMode","WebViewApiAvailability"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.webViewPayment.getSettings().setLoadWithOverviewMode(true);
        binding.webViewPayment.getSettings().setUseWideViewPort(true);
        binding.webViewPayment.getSettings().setJavaScriptEnabled(true);
        binding.webViewPayment.setWebViewClient(new WebViewClient());
        binding.webViewPayment.getSettings().setBuiltInZoomControls(true);
        binding.webViewPayment.getSettings().setDomStorageEnabled(true);
        binding.webViewPayment.getSettings().setDatabaseEnabled(true);
        binding.webViewPayment.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        binding.webViewPayment.setWebViewClient(new CustomWebViewClient(this));

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Long totalPrice = sharedPreferences.getLong("totalPrice", 0);
        System.out.println("totalPrice: " + totalPrice);
        String token = sharedPreferences.getString("token", "");
        Payment payment = new Payment("Payment", totalPrice);

        apiPaymentService = RetrofitPrivate.getRetrofit(token).create(APIPaymentService.class);
        apiPaymentService.createPayment(payment).enqueue(new Callback<ResponsePayment>() {
            @Override
            public void onResponse(Call<ResponsePayment> call, Response<ResponsePayment> response) {
                if (response.isSuccessful()) {
                    System.out.println(response.body().getUrl());
                    binding.webViewPayment.loadUrl(response.body().getUrl());
                }
                else{
                    Toast.makeText(PaymentActivity.this, "Error get link", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponsePayment> call, Throwable t) {
                Toast.makeText(PaymentActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class CustomWebViewClient extends WebViewClient {
        private Activity activity ;
        public CustomWebViewClient(Activity activity) {
            this.activity = activity;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        @Override
        public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
            if(url.contains("http://localhost:8080/api/payment/pay_success")) {
                int orderId= sharedPreferences.getInt("orderId", 0);
                String token = sharedPreferences.getString("token", "");
                int idUser = sharedPreferences.getInt("idUser", 0);
                apiOrderService= RetrofitPrivate.getRetrofit(token).create(APIOrderService.class);
                apiOrderService.updateStatusOrder(orderId).enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(PaymentActivity.this, "Payment success", Toast.LENGTH_SHORT).show();
                            sharedPreferences.edit().remove("totalPrice").apply();
                            sharedPreferences.edit().remove("orderId").apply();
                            Intent intent = new Intent(PaymentActivity.this, UserMainActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(PaymentActivity.this, "Error update status", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Order> call, Throwable t) {
                        Toast.makeText(PaymentActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
                return true;
            }
            return false;
        }
    }
}