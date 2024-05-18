package vn.appCosmetic.Controller.User.Cart;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
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
import vn.appCosmetic.ServiceAPI.Product.APIProductService;
import vn.appCosmetic.ServiceAPI.RetrofitPrivate;

public class CartFragment extends Fragment {
    private RecyclerView recyclerCart;
    private CartItemAdapter cartItemAdapter;
    private List<CartProduct> cartProductList;

    private int totalPrice = 0;
    private TextView txtTotalPrice;

    private Button btnOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerCart = view.findViewById(R.id.recycler_cart);
        cartProductList = new ArrayList<>();

        btnOrder = view.findViewById(R.id.btn_place_order);

        cartItemAdapter = new CartItemAdapter(getContext(), cartProductList);
        recyclerCart.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerCart.setAdapter(cartItemAdapter);

        txtTotalPrice = view.findViewById(R.id.txt_total_price);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int idUser = sharedPreferences.getInt("idUser", 0);
        String token = sharedPreferences.getString("token", "");

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
                                                for (CartProduct cartProduct : cartProducts) {
                                                    cartProductList.add(cartProduct);
                                                    totalPrice += cartProduct.getProduct().getPrice() * cartProduct.getQuantity();
                                                }
                                                txtTotalPrice.setText(String.format("%d VND", totalPrice));
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

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển qua màn hình Order
                Fragment fragment = new OrderFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

            }
        });
        return view;
    }
}