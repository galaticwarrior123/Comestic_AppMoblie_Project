package vn.appCosmetic.Controller.User.Cart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.appCosmetic.Controller.User.Order.OrderFragment;
import vn.appCosmetic.Model.Cart;
import vn.appCosmetic.Model.CartProduct;
import vn.appCosmetic.R;
import vn.appCosmetic.ServiceAPI.Cart.APICartService;
import vn.appCosmetic.ServiceAPI.CartProduct.APICartProductService;
import vn.appCosmetic.ServiceAPI.RetrofitPrivate;

public class CartFragment extends Fragment implements CartItemAdapter.OnCartProductChangeListener {
    private RecyclerView recyclerCart;
    private CartItemAdapter cartItemAdapter;
    private List<CartProduct> cartProductList;
    private int totalPrice = 0;
    private TextView txtTotalPrice;
    private Button btnOrder;
    private SharedPreferences sharedPreferences;
    private String token;
    private int idUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerCart = view.findViewById(R.id.recycler_cart);
        cartProductList = new ArrayList<>();
        btnOrder = view.findViewById(R.id.btn_place_order);
        txtTotalPrice = view.findViewById(R.id.txt_total_price);

        sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        idUser = sharedPreferences.getInt("idUser", 0);
        token = sharedPreferences.getString("token", "");

        cartItemAdapter = new CartItemAdapter(getContext(), cartProductList, this);
        recyclerCart.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerCart.setAdapter(cartItemAdapter);

        loadCartData();

        // nếu nhận được broadcast thì load lại dữ liệu giỏ hàng
        if(getActivity() != null) {
            loadCartData();
            LocalBroadcastManager.getInstance(getContext()).registerReceiver(cartUpdateReceiver, new IntentFilter("cart-update"));
        }

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to OrderFragment
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new OrderFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    private void loadCartData() {
        APICartService apiCartService = RetrofitPrivate.getRetrofit(token).create(APICartService.class);
        apiCartService.getCarts(idUser).enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                if (response.isSuccessful()) {
                    List<Cart> carts = response.body();
                    if (carts != null) {
                        for (Cart cart : carts) {
                            if (!cart.getStatus() && !cart.isPaid()) {
                                int cartId = cart.getId();
                                APICartProductService apiCartProductService = RetrofitPrivate.getRetrofit(token).create(APICartProductService.class);
                                apiCartProductService.getCartProductByCartId(cartId).enqueue(new Callback<List<CartProduct>>() {
                                    @Override
                                    public void onResponse(Call<List<CartProduct>> call, Response<List<CartProduct>> response) {
                                        if (response.isSuccessful()) {
                                            List<CartProduct> cartProducts = response.body();
                                            if (cartProducts != null) {
                                                cartProductList.clear();
                                                totalPrice = 0;
                                                for (CartProduct cartProduct : cartProducts) {
                                                    cartProductList.add(cartProduct);
                                                    totalPrice += cartProduct.getProduct().getPrice() * cartProduct.getQuantity();
                                                }
                                                txtTotalPrice.setText(String.format("Total Price: %d VND", totalPrice));
                                                cartItemAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<List<CartProduct>> call, Throwable t) {
                                        t.printStackTrace();
                                    }
                                });
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

    private BroadcastReceiver cartUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadCartData();
        }
    };

    @Override
    public void onCartProductChange() {
        loadCartData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(cartUpdateReceiver);
    }


}


