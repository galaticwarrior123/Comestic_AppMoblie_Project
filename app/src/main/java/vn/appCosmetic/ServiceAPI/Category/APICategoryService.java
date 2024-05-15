package vn.appCosmetic.ServiceAPI.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import vn.appCosmetic.Model.Category;


public interface APICategoryService {
    @GET("category/")
    Call<List<Category>> getCategory();

    @POST("category/")
    Call<Category> postCategory(@Body Category category);

    @PUT("category/{id}")
    Call<Category> putCategory(@Path("id") int id, @Body Category category);


    @DELETE("category/{id}")
    Call<Void> deleteCategory(@Path("id") int id);

}
