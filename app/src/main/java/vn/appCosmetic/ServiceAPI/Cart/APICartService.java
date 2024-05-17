package vn.appCosmetic.ServiceAPI.Cart;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import vn.appCosmetic.Model.Cart;

public interface APICartService {

    @GET("cart/all/{idUser}")
    Call<List<Cart>> getCarts(@Path("idUser") int idUser);

    @GET("cart/all/{userId}")
    Call<Cart> getCartByUserId(@Path("userId") int userId);
}
