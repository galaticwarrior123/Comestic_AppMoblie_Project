package vn.appCosmetic.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AuthLogin implements Serializable {
    @SerializedName("token")
    private String token;

    @SerializedName("user")
    private Users user;

    public AuthLogin(){
    }


    public AuthLogin(String token){
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }


}
