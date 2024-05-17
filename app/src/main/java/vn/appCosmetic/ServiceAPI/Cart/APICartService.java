package vn.appCosmetic.ServiceAPI.Cart;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import vn.appCosmetic.Model.Cart;

public interface APICartService {
    @GET("cartProduct/add")
    Call<Cart> getCartByUserId();
}
