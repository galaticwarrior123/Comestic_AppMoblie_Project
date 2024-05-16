package vn.appCosmetic.ServiceAPI.Users;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import vn.appCosmetic.Model.Category;
import vn.appCosmetic.Model.UserLogin;
import vn.appCosmetic.Model.Users;

public interface APIUsersService {
    @GET("users/all")
    Call<List<Users>> getUsers();

    @GET("users/{id}")
    Call<Users> getUserById(@Path("id") int id);

    @PUT("users/status/{id}")
    Call<Users> putStatusUser(@Path("id") int id);

    @PUT("users/update/{id}")
    Call<Users> putUpdateUser(@Path("id") int id, @Body Users user);
}
