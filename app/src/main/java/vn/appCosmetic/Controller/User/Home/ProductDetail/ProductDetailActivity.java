package vn.appCosmetic.Controller.User.Home.ProductDetail;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import vn.appCosmetic.Model.Product;
import vn.appCosmetic.R;
import vn.appCosmetic.ServiceAPI.Product.APIProductService;
import vn.appCosmetic.ServiceAPI.RetrofitClient;

public class ProductDetailActivity extends AppCompatActivity {
    private TextView productName, productDescription, productPrice, productStock;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productName = findViewById(R.id.product_detail_name);
        productDescription = findViewById(R.id.product_detail_description);
        productPrice = findViewById(R.id.product_detail_price);
        productStock = findViewById(R.id.product_detail_stock);
        viewPager = findViewById(R.id.viewPager);
        Button btnBack = findViewById(R.id.btn_product_detail_back);
        int productId = getIntent().getIntExtra("PRODUCT_ID", -1);
        if (productId != -1) {
            loadProductDetails(productId);
        }
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void loadProductDetails(int productId) {
        APIProductService apiProductService = RetrofitClient.getRetrofit().create(APIProductService.class);
        apiProductService.getProductById(productId).enqueue(new retrofit2.Callback<Product>() {
            @Override
            public void onResponse(retrofit2.Call<Product> call, retrofit2.Response<Product> response) {
                if (response.isSuccessful()) {
                    Product product = response.body();
                    if (product != null) {
                        productName.setText(product.getName());
                        productDescription.setText(product.getDescription());
                        productPrice.setText(String.format("%d VND", product.getPrice()));
                        if (product.getStock() > 0) {
                            productStock.setText(String.valueOf(product.getStock()));
                        } else {
                            productStock.setText("Out of stock");
                        }

                        ImagePagerAdapter adapter = new ImagePagerAdapter(getSupportFragmentManager(), product.getImages());
                        viewPager.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Product> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}