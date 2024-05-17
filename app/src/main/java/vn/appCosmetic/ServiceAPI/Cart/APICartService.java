package vn.appCosmetic.ServiceAPI.Cart;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import vn.appCosmetic.Model.Cart;

public interface APICartService {

    @GET("cart/all/{idUser}")
    Call<List<Cart>> getCarts(@Path("idUser") int idUser);

    @POST("cart/add/{idUser}")
    Call<Cart> createCart(@Path("idUser") int idUser);

}
