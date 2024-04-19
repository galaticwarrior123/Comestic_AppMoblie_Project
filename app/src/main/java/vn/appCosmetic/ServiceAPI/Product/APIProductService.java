package vn.appCosmetic.ServiceAPI.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.appCosmetic.Model.Product;

public interface APIProductService {
    @GET(" ")
    Call<List<Product>> getAllProduct();


    @GET("category/{id}")
    Call<List<Product>> getProductByCategory(@Path("id") int id);

    @POST(" ")
    Call<Product> postProduct(@Body Product product);

    @PUT("{id}")
    Call<Product> putProduct(@Path("id") int id, @Body Product product);

    @DELETE("{id}")
    Call<Void> deleteProduct(@Path("id") int id);
}
