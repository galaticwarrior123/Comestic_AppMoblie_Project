package vn.appCosmetic.Controller.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import vn.appCosmetic.Controller.LoginRegister.LoginActivity;
import vn.appCosmetic.Controller.User.Cart.CartFragment;
import vn.appCosmetic.Controller.User.Home.HomeFragment;
import vn.appCosmetic.Controller.User.User.UserFragment;
import vn.appCosmetic.R;

public class UserMainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        fragmentManager = getSupportFragmentManager();
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Update the menu based on the presence of the token
        Menu menu = bottomNavigationView.getMenu();
        MenuItem userItem = menu.findItem(R.id.navigation_user);
        MenuItem loginItem = menu.findItem(R.id.navigation_login);

        if (token.isEmpty()) {
            userItem.setVisible(false);
            loginItem.setVisible(true);
        } else {
            userItem.setVisible(true);
            loginItem.setVisible(false);
        }


        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_home) {
                    loadFragment(new HomeFragment());
                    return true;
                } else if (item.getItemId() == R.id.navigation_cart) {
                    loadFragment(new CartFragment());
                    return true;
                } else if (item.getItemId() == R.id.navigation_user) {
                    loadFragment(new UserFragment());
                    return true;
                } else if (item.getItemId() == R.id.navigation_login) {
                    loadActivity(LoginActivity.class);
                    return true;
                } else {
                    return false;
                }
            }
        });

        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    private void loadActivity(Class<?> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}