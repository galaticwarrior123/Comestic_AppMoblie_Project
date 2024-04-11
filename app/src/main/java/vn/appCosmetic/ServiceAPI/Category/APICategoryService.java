package vn.appCosmetic.ServiceAPI.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import vn.appCosmetic.Model.Category;


public interface APICategoryService {
    @GET(" ")
    Call<List<Category>> getCategory();

}
