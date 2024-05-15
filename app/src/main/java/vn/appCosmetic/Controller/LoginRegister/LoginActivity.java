package vn.appCosmetic.Controller.LoginRegister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import vn.appCosmetic.Model.UserLogin;
import vn.appCosmetic.R;
import vn.appCosmetic.ServiceAPI.RetrofitClient;
import vn.appCosmetic.ServiceAPI.Users.APIUsersService;

public class LoginActivity extends AppCompatActivity {
    TextView signUp;
    Button btnSignIp;
    EditText passWord, eMail;
    APIUsersService apiUsersService;
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

        apiUsersService = RetrofitClient.getRetrofit().create(APIUsersService.class);

        btnSignIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = eMail.getText().toString().trim();
                String Password = passWord.getText().toString().trim();

                System.out.println(Email+Password);

                UserLogin userLogin = new UserLogin();
                userLogin.setEmail(Email);
                userLogin.setPassword(Password);

                if (!TextUtils.isEmpty(Email) && !TextUtils.isEmpty(Password)){
                    apiUsersService.loginUsers(userLogin).enqueue(new Callback<UserLogin>() {
                        @Override
                        public void onResponse(Call<UserLogin> call, Response<UserLogin> response) {
                            if(response.isSuccessful()){
                                System.out.println("gsugyu");
                                Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                                startActivity(intent);
                            }
                            else{Toast.makeText(LoginActivity.this, "Login fail", Toast.LENGTH_SHORT).show();}
                        }
                        @Override
                        public void onFailure(Call<UserLogin> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Login fail, Email or password wrong", Toast.LENGTH_SHORT).show();
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