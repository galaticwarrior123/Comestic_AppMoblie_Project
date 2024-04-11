package vn.appCosmetic.ServiceAPI.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import vn.appCosmetic.Model.Category;


public interface APICategoryService {
    @GET(" ")
    Call<List<Category>> getCategory();

    @POST(" ")
    Call<Category> postCategory(@Body Category category);

    @PUT("/{id}")
    Call<Category> putCategory(@Path("id") int id, @Body Category category);

    @DELETE("/{id}")
    Call<Category> deleteCategory(@Path("id") int id);

}
