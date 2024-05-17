package vn.appCosmetic.Controller.User.Order;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.appCosmetic.Controller.Payment.BankActivity;
import vn.appCosmetic.Controller.User.Cart.CartItemAdapter;
import vn.appCosmetic.Model.Cart;
import vn.appCosmetic.Model.CartProduct;
import vn.appCosmetic.Model.Order;
import vn.appCosmetic.R;
import vn.appCosmetic.ServiceAPI.Cart.APICartService;
import vn.appCosmetic.ServiceAPI.CartProduct.APICartProductService;
import vn.appCosmetic.ServiceAPI.Order.APIOrderService;
import vn.appCosmetic.ServiceAPI.RetrofitPrivate;
import vn.appCosmetic.ServiceAPI.Users.APIUsersService;

public class OrderFragment extends Fragment {

    private RecyclerView recyclerOrder;
    private Button btnOrder;

    private TextView txtTotalPrice, txtQuantityProduct;

    private EditText edtAddress, edtPhone;

    private Spinner spnPayment;

    private List<String> paymentList = new ArrayList<String>(){
        {
            add("Chọn phương thức thanh toán");
            add("Thanh toán khi nhận hàng");
            add("Chuyển khoản ngân hàng");
        }
    };


    private Long totalPrice = 0L;

    private int totalQuantityProduct = 0;

    private SharedPreferences sharedPreferences;

    private CartItemAdapter cartItemAdapter;

    private List<CartProduct> cartProductList;

    private APICartProductService apiCartProductService;
    private APICartService apiCartService;

    private APIOrderService apiOrderService;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        txtTotalPrice = view.findViewById(R.id.txt_total_price);
        txtQuantityProduct = view.findViewById(R.id.txt_total_quantity);
        recyclerOrder = view.findViewById(R.id.recycler_order);
        btnOrder = view.findViewById(R.id.btn_order);
        edtAddress = view.findViewById(R.id.edt_address_order);
        edtPhone = view.findViewById(R.id.edt_phone_order);
        spnPayment = view.findViewById(R.id.spn_payment);

        ArrayAdapter<String> paymentAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, paymentList);
        paymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPayment.setAdapter(paymentAdapter);

        cartProductList = new ArrayList<>();


        sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int idUser = sharedPreferences.getInt("idUser", 0);
        String token = sharedPreferences.getString("token", "");


        apiCartProductService = RetrofitPrivate.getRetrofit(token).create(APICartProductService.class);
        apiCartService = RetrofitPrivate.getRetrofit(token).create(APICartService.class);
        apiOrderService = RetrofitPrivate.getRetrofit(token).create(APIOrderService.class);

        apiCartService.getCarts(idUser).enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                if (response.isSuccessful()) {
                    List<Cart> carts = response.body();
                    for (Cart cart : carts) {
                        if (!cart.getStatus()) {
                            int cartId = cart.getId();
                            apiCartProductService.getCartProductByCartId(cartId).enqueue(new Callback<List<CartProduct>>() {
                                @Override
                                public void onResponse(Call<List<CartProduct>> call, Response<List<CartProduct>> response) {
                                    if (response.isSuccessful()) {
                                        List<CartProduct> cartProducts = response.body();
                                        if (cartProducts != null) {
                                            for (CartProduct cartProduct : cartProducts) {
                                                totalPrice += cartProduct.getProduct().getPrice() * cartProduct.getQuantity();
                                                cartProductList.add(cartProduct);
                                                cartItemAdapter = new CartItemAdapter(getContext(), cartProductList);
                                                recyclerOrder.setLayoutManager(new LinearLayoutManager(getContext()));
                                                recyclerOrder.setAdapter(cartItemAdapter);

                                            }
                                        }

                                        txtTotalPrice.setText(String.format("%d VND", totalPrice));
                                        txtQuantityProduct.setText(String.format("%d", cartProductList.size())+" sản phẩm");
                                        cartItemAdapter.notifyDataSetChanged();

                                        btnOrder.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                String address = edtAddress.getText().toString();
                                                String phone = edtPhone.getText().toString();


                                                Order order = new Order();
                                                order.setCart(cart);
                                                order.setTotal(totalPrice);
                                                order.setAddress(address);
                                                order.setPhone(phone);

                                                apiOrderService.createOrder(order).enqueue(new Callback<Order>() {
                                                    @Override
                                                    public void onResponse(Call<Order> call, Response<Order> response) {
                                                        if (response.isSuccessful()) {
                                                            sharedPreferences.edit().putLong("totalPrice", totalPrice).apply();
                                                            sharedPreferences.edit().putInt("orderId", response.body().getId()).apply();
                                                            Intent intent = new Intent(getContext(), BankActivity.class);
                                                            startActivity(intent);
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Order> call, Throwable t) {

                                                    }
                                                });

                                            }
                                        });

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

            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {
                t.printStackTrace();
            }
        });


        return view;
    }



}
