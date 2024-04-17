package vn.appCosmetic.ServiceAPI.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import vn.appCosmetic.Model.Product;

public interface APIProductService {
    @GET(" ")
    Call<List<Product>> getAllProduct();

    @GET(" ")
    Call<List<Product>> getProductByCategory(@Field("idcategory") int idCategory);

    @POST(" ")
    Call<Product> postProduct(@Body Product product);
}
