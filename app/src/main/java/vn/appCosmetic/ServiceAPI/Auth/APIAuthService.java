package vn.appCosmetic.ServiceAPI.Auth;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import vn.appCosmetic.Model.AuthLogin;
import vn.appCosmetic.Model.UserLogin;
import vn.appCosmetic.Model.Users;

public interface APIAuthService {
    @POST("auth/register")
    Call<Users> postUsers(@Body Users users);
    @POST("auth/login")
    Call<AuthLogin> loginUsers(@Body UserLogin userLogin);
}
