package vn.appCosmetic.Controller.User.Home.ProductDetail;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.appCosmetic.Controller.User.Cart.CartItemAdapter;
import vn.appCosmetic.Model.Cart;
import vn.appCosmetic.Model.CartProduct;
import vn.appCosmetic.Model.CartProductInput;
import vn.appCosmetic.Model.Product;
import vn.appCosmetic.R;
import vn.appCosmetic.ServiceAPI.Cart.APICartService;
import vn.appCosmetic.ServiceAPI.CartProduct.APICartProductService;
import vn.appCosmetic.ServiceAPI.Product.APIProductService;
import vn.appCosmetic.ServiceAPI.RetrofitClient;
import vn.appCosmetic.ServiceAPI.RetrofitPrivate;

public class ProductDetailActivity extends AppCompatActivity {
    private TextView productName, productDescription, productPrice, productStock;
    private ViewPager viewPager;
    private Product product;
    APICartService apiCartService;
    APIProductService apiProductService;
    APICartProductService apiCartProductService;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int idUser = sharedPreferences.getInt("idUser", 0);
        String token = sharedPreferences.getString("token", "");
        productName = findViewById(R.id.product_detail_name);
        productDescription = findViewById(R.id.product_detail_description);
        productPrice = findViewById(R.id.product_detail_price);
        productStock = findViewById(R.id.product_detail_stock);
        viewPager = findViewById(R.id.viewPager);
        ImageButton btnBack = findViewById(R.id.btn_product_detail_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        int productId = getIntent().getIntExtra("PRODUCT_ID", -1);
        if (productId != -1) {
            loadProductDetails(productId);
        }

        CartProductInput cartProduct = new CartProductInput();
        cartProduct.setIdProduct(productId);

        Button btnAddToCart = findViewById(R.id.buttonAddToCart);
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an alert dialog to let the user input quantity
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailActivity.this);
                builder.setTitle("Enter quantity");
                final EditText input = new EditText(ProductDetailActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

                apiCartService = RetrofitPrivate.getRetrofit(token).create(APICartService.class);
                apiCartService.getCarts(idUser).enqueue(new Callback<List<Cart>>() {
                    @Override
                    public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                        if (response.isSuccessful()) {
                            List<Cart> carts = response.body();
                            if (carts != null) {
                                for (Cart cart : carts) {
                                    if (!cart.getStatus() && !cart.isPaid()) {
                                        cartProduct.setIdCart(cart.getId());

                                        apiCartProductService = RetrofitPrivate.getRetrofit(token).create(APICartProductService.class);
                                        apiCartProductService.getCartProductByCartId(cart.getId()).enqueue(new Callback<List<CartProduct>>() {
                                            @Override
                                            public void onResponse(Call<List<CartProduct>> call, Response<List<CartProduct>> response) {
                                                if (response.isSuccessful()) {
                                                    List<CartProduct> cartProducts = response.body();
                                                    if (cartProducts != null) {
                                                        boolean productExistsInCart = false;
                                                        for (CartProduct cartProduct1 : cartProducts) {
                                                            if (cartProduct1.getProduct().getId() == productId) {
                                                                productExistsInCart = true;
                                                                input.setText(String.valueOf(cartProduct1.getQuantity()));
                                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        int quantity = Integer.parseInt(input.getText().toString());
                                                                        // kiểm tra số lượng sản phẩm còn trong kho
                                                                        if (product.getStock() < quantity) {
                                                                            Toast.makeText(ProductDetailActivity.this, "Not enough stock", Toast.LENGTH_SHORT).show();
                                                                            return;
                                                                        }
                                                                        cartProduct.setQuantity(quantity);
                                                                        apiCartProductService.putCartProduct(cartProduct1.getId(), cartProduct).enqueue(new Callback<CartProduct>() {
                                                                            @Override
                                                                            public void onResponse(Call<CartProduct> call, Response<CartProduct> response) {
                                                                                if (response.isSuccessful()) {
                                                                                    Toast.makeText(ProductDetailActivity.this, "Product quantity updated in cart", Toast.LENGTH_SHORT).show();
                                                                                    // Broadcast cart update
                                                                                    broadcastCartUpdate();
                                                                                } else {
                                                                                    Toast.makeText(ProductDetailActivity.this, "Failed to update product quantity in cart", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onFailure(Call<CartProduct> call, Throwable t) {
                                                                                t.printStackTrace();
                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        dialog.cancel();
                                                                    }
                                                                });
                                                                builder.show();
                                                                break;
                                                            }
                                                        }

                                                        if (!productExistsInCart) {
                                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    int quantity = Integer.parseInt(input.getText().toString());
                                                                    // kiểm tra số lượng sản phẩm còn trong kho
                                                                    if (product.getStock() < quantity) {
                                                                        Toast.makeText(ProductDetailActivity.this, "Not enough stock", Toast.LENGTH_SHORT).show();
                                                                        return;
                                                                    }
                                                                    cartProduct.setQuantity(quantity);
                                                                    apiCartProductService.postCartProduct(cartProduct).enqueue(new Callback<CartProduct>() {
                                                                        @Override
                                                                        public void onResponse(Call<CartProduct> call, Response<CartProduct> response) {
                                                                            if (response.isSuccessful()) {
                                                                                Toast.makeText(ProductDetailActivity.this, "Product added to cart", Toast.LENGTH_SHORT).show();
                                                                                // Broadcast cart update
                                                                                broadcastCartUpdate();
                                                                            } else {
                                                                                Toast.makeText(ProductDetailActivity.this, "Failed to add product to cart", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onFailure(Call<CartProduct> call, Throwable t) {
                                                                            t.printStackTrace();
                                                                        }
                                                                    });
                                                                }
                                                            });
                                                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    dialog.cancel();
                                                                }
                                                            });
                                                            builder.show();
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<List<CartProduct>> call, Throwable t) {
                                                t.printStackTrace();
                                            }
                                        });
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Cart>> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });
    }

    private void loadProductDetails(int productId) {
        APIProductService apiProductService = RetrofitClient.getRetrofit().create(APIProductService.class);
        apiProductService.getProductById(productId).enqueue(new retrofit2.Callback<Product>() {
            @Override
            public void onResponse(retrofit2.Call<Product> call, retrofit2.Response<Product> response) {
                if (response.isSuccessful()) {
                    product = response.body();
                    if (product != null) {
                        productName.setText(product.getName());
                        productDescription.setText(product.getDescription());
                        productPrice.setText("Giá: "+String.format("%d VND", product.getPrice()));
                        if (product.getStock() > 0) {
                            productStock.setText(String.format("Stock: %d", product.getStock()));
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

    // Method to broadcast cart updates
    private void broadcastCartUpdate() {
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("cart-update"));
        finish();
    }
}
