package vn.appCosmetic.ServiceAPI.Order;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import vn.appCosmetic.Model.Order;

public interface APIOrderService {

    @POST("order/create")
    Call<Order> createOrder(@Body Order order);

    @PUT("order/update-status/{orderId}")
    Call<Order> updateStatusOrder(@Path("orderId") int orderId);
}
