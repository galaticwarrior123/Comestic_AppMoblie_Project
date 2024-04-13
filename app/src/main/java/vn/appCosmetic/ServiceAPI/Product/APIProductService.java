package vn.appCosmetic.ServiceAPI.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import vn.appCosmetic.Model.Product;

public interface APIProductService {
    @GET(" ")
    Call<List<Product>> getAllProduct();

    @GET(" ")
    Call<List<Product>> getProductByCategory(@Field("idcategory") int idCategory);
}
