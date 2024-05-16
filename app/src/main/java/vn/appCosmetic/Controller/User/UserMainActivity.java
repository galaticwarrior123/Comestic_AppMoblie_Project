package vn.appCosmetic.Controller.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import vn.appCosmetic.Controller.User.Cart.CartFragment;
import vn.appCosmetic.Controller.User.Home.HomeFragment;
import vn.appCosmetic.Controller.User.User.UserFragment;
import vn.appCosmetic.R;

public class UserMainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        fragmentManager = getSupportFragmentManager();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.navigation_home) {
                    loadFragment(new HomeFragment());
                    return true;
                }
                else if(item.getItemId() == R.id.navigation_cart) {
                    loadFragment(new CartFragment());
                    return true;
                }
                else if(item.getItemId() == R.id.navigation_user) {
                    loadFragment(new UserFragment());
                    return true;
                }
                else {
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
}
