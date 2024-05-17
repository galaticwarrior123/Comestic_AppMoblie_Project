package vn.appCosmetic.ServiceAPI.CartProduct;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import vn.appCosmetic.Model.CartProduct;
import vn.appCosmetic.Model.CartProductInput;

public interface APICartProductService {

    @POST("cartProduct/add")
    Call<CartProduct> addCartProduct(@Body CartProductInput cartProductInput);
}
