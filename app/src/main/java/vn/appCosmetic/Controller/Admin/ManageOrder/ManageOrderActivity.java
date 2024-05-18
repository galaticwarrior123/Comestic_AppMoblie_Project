package vn.appCosmetic.Controller.Admin.ManageOrder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.appCosmetic.Controller.User.User.ManagerMyOrder.OrderItemAdapter;
import vn.appCosmetic.Model.Order;
import vn.appCosmetic.R;
import vn.appCosmetic.ServiceAPI.Order.APIOrderService;
import vn.appCosmetic.ServiceAPI.RetrofitPrivate;

public class ManageOrderActivity extends Fragment {
    private List<Order> orderList=new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private OrderAdapter orderAdapter;

    private RecyclerView recyclerView;

    private APIOrderService apiOrderService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_manage_order, container, false);
        sharedPreferences = getActivity().getSharedPreferences("MyPrefs", getActivity().MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        recyclerView= view.findViewById(R.id.rc_manage_order);
        apiOrderService = RetrofitPrivate.getRetrofit(token).create(APIOrderService.class);
        apiOrderService.getAllOrder().enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                orderList = response.body();
                orderAdapter = new OrderAdapter(getActivity(), orderList);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(orderAdapter);
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}