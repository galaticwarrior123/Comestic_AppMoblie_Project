package vn.appCosmetic.ServiceAPI.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import vn.appCosmetic.Model.Order;

public interface APIOrderService {

    @POST("order/create")
    Call<Order> createOrder(@Body Order order);

    @PUT("order/update-status/{orderId}")
    Call<Order> updateStatusOrder(@Path("orderId") int orderId);

    @PUT("order/update/{orderId}")
    Call<Order> updateOrder(@Path("orderId") int orderId, @Body Order order);

    @GET("order/all/{userId}")
    Call<List<Order>> getAllOrderByIdUser(@Path("userId") int userId);

    @GET("order/{orderId}")
    Call<Order> getOrderById(@Path("orderId") int orderId);

    @GET("order/all")
    Call<List<Order>> getAllOrder();
}
