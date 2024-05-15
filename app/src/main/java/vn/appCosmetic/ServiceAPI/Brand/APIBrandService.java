package vn.appCosmetic.ServiceAPI.Brand;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import vn.appCosmetic.Model.Brand;

public interface APIBrandService {
    @GET("brand/")
    Call<List<Brand>> getAllBrand();

    @POST("brand/")
    Call<Brand> postBrand(@Body Brand brand);

    @PUT("brand/{id}")
    Call<Brand> putBrand(@Path("id") int id, @Body Brand brand);

    @DELETE("brand/{id}")
    Call<Void> deleteBrand(@Path("id") int id);

}
