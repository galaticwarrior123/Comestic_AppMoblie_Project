package vn.appCosmetic.Controller.LoginRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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



        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Username = userName.getText().toString().trim();
                String Email = eMail.getText().toString().trim();
                String Password = passWord.getText().toString().trim();
                Users users = new Users();
                users.setUsername(Username);
                users.setEmail(Email);
                users.setPassword(Password);
                System.out.println(Username+ Email+ Password);

                apiUsersService = RetrofitUsersClient.getRetrofit().create(APIUsersService.class);
                apiUsersService.postUsers(users).enqueue(new Callback<Users>() {
                    @Override
                    public void onResponse(@NonNull Call<Users> call, @NonNull Response<Users> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "Register successful", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Users> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this, "Register fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}