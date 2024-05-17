package vn.appCosmetic.Controller.LoginRegister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.appCosmetic.Controller.Admin.AdminMainActivity;
import vn.appCosmetic.Controller.User.UserMainActivity;
import vn.appCosmetic.MainActivity;
import vn.appCosmetic.Model.AuthLogin;
import vn.appCosmetic.Model.UserLogin;
import vn.appCosmetic.Model.Users;
import vn.appCosmetic.R;
import vn.appCosmetic.ServiceAPI.Auth.APIAuthService;
import vn.appCosmetic.ServiceAPI.RetrofitClient;
import vn.appCosmetic.ServiceAPI.Users.APIUsersService;

public class LoginActivity extends AppCompatActivity {
    TextView signUp;
    Button btnSignIp;
    EditText passWord, eMail;
    APIAuthService apiAuthService;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signUp = findViewById(R.id.sign_up);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        eMail = findViewById(R.id.email);
        passWord = findViewById(R.id.password);
        btnSignIp= findViewById(R.id.btn_sign_in);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        btnSignIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = eMail.getText().toString().trim();
                String Password = passWord.getText().toString().trim();
                UserLogin userLogin = new UserLogin();
                userLogin.setEmail(Email);
                userLogin.setPassword(Password);

                if (!TextUtils.isEmpty(Email) && !TextUtils.isEmpty(Password)){
                    apiAuthService = RetrofitClient.getRetrofit().create(APIAuthService.class);
                   apiAuthService.loginUsers(userLogin).enqueue(new Callback<AuthLogin>() {
                       @Override
                       public void onResponse(Call<AuthLogin> call, Response<AuthLogin> response) {
                           if(response.isSuccessful()){
                               AuthLogin users = response.body();
                               SharedPreferences.Editor editor = sharedPreferences.edit();
                               editor.putString("token", users.getToken());
                               editor.putInt("idUser", users.getUser().getId());
                               editor.putString("userName", users.getUser().getUsername());
                               editor.apply();


                               if(users.getUser().isAdmin()){
                                      Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                                      startActivity(intent);
                               }
                               else {
                                   Intent intent = new Intent(LoginActivity.this, UserMainActivity.class);
                                   startActivity(intent);
                               }
                           }
                           else {
                               Toast.makeText(LoginActivity.this, "Login fail, Email or Password is incorrect", Toast.LENGTH_SHORT).show();
                           }
                       }

                       @Override
                       public void onFailure(Call<AuthLogin> call, Throwable t) {
                           Toast.makeText(LoginActivity.this, "Login fail, Element is null ", Toast.LENGTH_SHORT).show();
                       }
                   });

                }
                else {
                    Toast.makeText(LoginActivity.this, "Login fail, Element is null", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}