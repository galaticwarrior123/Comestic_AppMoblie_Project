package vn.appCosmetic.Controller.Payment;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.appCosmetic.Controller.User.UserMainActivity;
import vn.appCosmetic.Model.Payment;
import vn.appCosmetic.Model.ResponsePayment;
import vn.appCosmetic.ServiceAPI.Bank.APIPaymentService;
import vn.appCosmetic.ServiceAPI.RetrofitClient;
import vn.appCosmetic.databinding.ActivityPaymentBinding;

public class PaymentActivity extends AppCompatActivity {
    private ActivityPaymentBinding binding;
    private APIPaymentService apiPaymentService;
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
        Payment payment = new Payment("Payment", 10000);
        apiPaymentService = RetrofitClient.getRetrofit().create(APIPaymentService.class);
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

        @Override
        public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
            if(url.contains("http://localhost:8080/api/payment/pay_success")) {
                Intent intent = new Intent(activity, UserMainActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        }
    }
}