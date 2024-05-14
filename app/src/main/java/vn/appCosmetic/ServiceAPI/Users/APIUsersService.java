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
    @POST(" ")
    Call<Users> postUsers(@Body Users users);
    @POST(" ")
    Call<UserLogin> loginUsers(@Body UserLogin userLogin);

    @GET("all")
    Call<List<Users>> getUsers();

    @GET("{id}")
    Call<Users> getUserById(@Path("id") int id);

    @PUT("status/{id}")
    Call<Users> putStatusUser(@Path("id") int id);

}
