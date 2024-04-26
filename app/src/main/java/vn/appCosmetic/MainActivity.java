package vn.appCosmetic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import vn.appCosmetic.Controller.Admin.AdminMainActivity;
import vn.appCosmetic.Controller.LoginRegister.LoginActivity;
import vn.appCosmetic.Controller.User.UserMainActivity;


public class MainActivity extends AppCompatActivity {
    Button btnLogin, btnUser, btnAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin = findViewById(R.id.buttonLoginRegister);
        btnUser = findViewById(R.id.buttonUser);
        btnAdmin = findViewById(R.id.buttonAdmin);

        btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });
        btnUser.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, UserMainActivity.class));
        });
        btnAdmin.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AdminMainActivity.class));
        });
    }
}