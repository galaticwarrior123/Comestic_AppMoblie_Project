package vn.appComestic.Admin;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import vn.appComestic.Admin.ManageBrand.ManageBrandActivity;
import vn.appComestic.Admin.ManageCategory.ManageCategoryActivity;
import vn.appComestic.Admin.ManageProduct.ManageProductActivity;
import vn.appComestic.R;

public class AdminMainActivity extends AppCompatActivity {
    ImageButton btnSlide;
    Toolbar toolbar;
    LinearLayout linearLayout;
    FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        btnSlide = findViewById(R.id.buttonSlide);
        linearLayout = findViewById(R.id.linearLayout);
        toolbar = findViewById(R.id.toolbar2);


        findViewById(R.id.buttonManageProduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutManage, new ManageProductActivity()).commit();
                linearLayout.bringToFront();

            }
        });

        findViewById(R.id.buttonManageCategory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutManage, new ManageCategoryActivity()).commit();
                linearLayout.bringToFront();
            }
        });

        findViewById(R.id.buttonManageBrand).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutManage, new ManageBrandActivity()).commit();
                linearLayout.bringToFront();
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