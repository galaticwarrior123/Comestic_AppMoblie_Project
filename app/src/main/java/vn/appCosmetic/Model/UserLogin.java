package vn.appCosmetic.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serial;
import java.io.Serializable;

public class UserLogin implements Serializable {
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;

    public UserLogin(){
    }
    public UserLogin(String email, String password){
        this.email = email;
        this.password = password;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
