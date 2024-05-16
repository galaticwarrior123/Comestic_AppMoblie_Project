package vn.appCosmetic.Controller.Admin;

import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import vn.appCosmetic.Controller.Admin.ManageBrand.ManageBrandActivity;
import vn.appCosmetic.Controller.Admin.ManageCategory.ManageCategoryActivity;
import vn.appCosmetic.Controller.Admin.ManageProduct.ManageProductActivity;
import vn.appCosmetic.Controller.Admin.ManageUser.ManageUserActivity;
import vn.appCosmetic.R;

public class AdminMainActivity extends AppCompatActivity {
    ImageButton btnSlide;
    Toolbar toolbar;
    LinearLayout linearLayout;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        btnSlide = findViewById(R.id.buttonSlide);
        linearLayout = findViewById(R.id.linearLayout);
        toolbar = findViewById(R.id.toolbar2);




        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutManage, new ManageProductActivity()).commit();
        linearLayout.bringToFront();
        linearLayout.startAnimation(AnimationUtils.loadAnimation(AdminMainActivity.this, R.anim.slide_out));
        linearLayout.setVisibility(View.GONE);

        // khi click vào linearLayout thì sẽ ẩn linearLayout
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation slideOutAnimation = AnimationUtils.loadAnimation(AdminMainActivity.this, R.anim.slide_out);
                linearLayout.startAnimation(slideOutAnimation);
                linearLayout.setVisibility(View.GONE);
            }
        });
        




        findViewById(R.id.buttonManageProduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutManage, new ManageProductActivity()).commit();
                linearLayout.bringToFront();
                linearLayout.startAnimation(AnimationUtils.loadAnimation(AdminMainActivity.this, R.anim.slide_out));
                linearLayout.setVisibility(View.GONE);

            }
        });

        findViewById(R.id.buttonManageCategory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutManage, new ManageCategoryActivity()).commit();
                linearLayout.bringToFront();
                linearLayout.startAnimation(AnimationUtils.loadAnimation(AdminMainActivity.this, R.anim.slide_out));
                linearLayout.setVisibility(View.GONE);
            }
        });

        findViewById(R.id.buttonManageUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutManage, new ManageUserActivity()).commit();
                linearLayout.bringToFront();
                linearLayout.startAnimation(AnimationUtils.loadAnimation(AdminMainActivity.this, R.anim.slide_out));
                linearLayout.setVisibility(View.GONE);
            }
        });

        findViewById(R.id.buttonManageBrand).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutManage, new ManageBrandActivity()).commit();
                linearLayout.bringToFront();
                linearLayout.startAnimation(AnimationUtils.loadAnimation(AdminMainActivity.this, R.anim.slide_out));
                linearLayout.setVisibility(View.GONE);

            }
        });

        btnSlide.setOnClickListener(v -> {
            if(linearLayout.getVisibility()== View.VISIBLE){
                Animation slideOutAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_out);
                linearLayout.startAnimation(slideOutAnimation);
                linearLayout.setVisibility(View.GONE);
            }
            else{
                Animation slideInAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in);
                linearLayout.startAnimation(slideInAnimation);
                linearLayout.setVisibility(View.VISIBLE);
            }

        });

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "");
        TextView txtUserName = findViewById(R.id.textViewUserNameAdmin);
        txtUserName.setText(userName);

        TextView txtTitle = findViewById(R.id.textViewTitleAdmin);
        txtTitle.setText("Admin Page");

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int x = (int) event.getX();
        int y = (int) event.getY();

        Rect layoutBounds = new Rect();
        linearLayout.getGlobalVisibleRect(layoutBounds);

        if(!layoutBounds.contains(x,y) && linearLayout.getVisibility()==View.VISIBLE){
            Animation slideOutAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_out);
            linearLayout.startAnimation(slideOutAnimation);
            linearLayout.setVisibility(View.GONE);
            return true;
        }
        return super.onTouchEvent(event);
    }

}