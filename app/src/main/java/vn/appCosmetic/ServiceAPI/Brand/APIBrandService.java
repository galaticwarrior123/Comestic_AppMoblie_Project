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
    @GET(" ")
    Call<List<Brand>> getAllBrand();

    @POST(" ")
    Call<Brand> postBrand(@Body Brand brand);

    @PUT("{id}")
    Call<Brand> putBrand(@Path("id") int id, @Body Brand brand);

    @DELETE("{id}")
    Call<Void> deleteBrand(@Path("id") int id);

}
