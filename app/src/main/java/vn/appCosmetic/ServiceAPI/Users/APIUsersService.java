package vn.appCosmetic.ServiceAPI.Users;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import vn.appCosmetic.Model.Category;
import vn.appCosmetic.Model.Users;

public interface APIUsersService {
    @POST("")
    Call<Users> postUsers(@Body Users users);
}
