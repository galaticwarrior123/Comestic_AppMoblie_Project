package vn.appCosmetic.Controller.User.Home.ProductDetail;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
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

    private ImageButton imgBtnSub, imgBtnAdd;
    private EditText edtQuantity;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productName = findViewById(R.id.product_detail_name);
        productDescription = findViewById(R.id.product_detail_description);
        productPrice = findViewById(R.id.product_detail_price);
        productStock = findViewById(R.id.product_detail_stock);
        imgBtnSub = findViewById(R.id.imageButtonSubtract);
        imgBtnAdd = findViewById(R.id.imageButtonAdd);
        edtQuantity = findViewById(R.id.editTextNumberQuantity);
        viewPager = findViewById(R.id.viewPager);

        handleButtonQuantity();

        int productId = getIntent().getIntExtra("PRODUCT_ID", -1);
        if (productId != -1) {
            loadProductDetails(productId);
        }
    }


    private void handleButtonQuantity() {
        imgBtnSub.setOnClickListener(v -> {
            int quantity = Integer.parseInt(edtQuantity.getText().toString());
            if (quantity > 1) {
                quantity--;
                edtQuantity.setText(String.valueOf(quantity));
            }
        });

        imgBtnAdd.setOnClickListener(v -> {
            int quantity = Integer.parseInt(edtQuantity.getText().toString());
            quantity++;
            edtQuantity.setText(String.valueOf(quantity));
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