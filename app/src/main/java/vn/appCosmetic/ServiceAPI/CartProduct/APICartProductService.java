package vn.appCosmetic.ServiceAPI.CartProduct;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import vn.appCosmetic.Model.CartProduct;
import retrofit2.http.DELETE;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import vn.appCosmetic.Model.CartProductInput;


public interface APICartProductService {
    @POST("cartProduct/add")
    Call<CartProduct> postCartProduct(@Body CartProductInput cartProductInput);

    @PUT("cartProduct/update/{cartProductId}")
    Call<CartProduct> putCartProduct(@Path("cartProductId") int cartProductId, @Body CartProduct cartProduct);

    @DELETE("cartProduct/delete/{cartProductId}")
    Call<Void> deleteCartProduct(@Path("cartProductId") int cartProductId);
}
