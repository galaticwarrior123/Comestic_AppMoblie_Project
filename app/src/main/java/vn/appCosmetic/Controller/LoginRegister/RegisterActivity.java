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
import vn.appCosmetic.Model.Users;
import vn.appCosmetic.R;
import vn.appCosmetic.ServiceAPI.Users.APIUsersService;
import vn.appCosmetic.ServiceAPI.Users.RetrofitUsersClient;

public class RegisterActivity extends AppCompatActivity {

    TextView signIn;
    Button btnSignUp;
    EditText userName, passWord, eMail;
    APIUsersService apiUsersService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        signIn = findViewById(R.id.sign_in);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        eMail = findViewById(R.id.email);
        userName = findViewById(R.id.username);
        passWord = findViewById(R.id.password);
        btnSignUp= findViewById(R.id.btn_sign_up);

        apiUsersService = RetrofitUsersClient.getRetrofit().create(APIUsersService.class);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Username = userName.getText().toString().trim();
                String Email = eMail.getText().toString().trim();
                String Password = passWord.getText().toString().trim();
                if(!TextUtils.isEmpty(Username) && !TextUtils.isEmpty(Email) && !TextUtils.isEmpty(Password)){
                    Users users = new Users();
                    users.setUsername(Username);
                    users.setEmail(Email);
                    users.setPassword(Password);
                    apiUsersService.postUsers(users).enqueue(new Callback<Users>() {
                        @Override
                        public void onResponse(Call<Users> call, Response<Users> response) {
                            if(response.isSuccessful()){
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<Users> call, Throwable t) {
                            Toast.makeText(RegisterActivity.this, "Register fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Register fail, Element is null", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}