package vn.appCosmetic.Controller.User.User;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.appCosmetic.Model.Users;
import vn.appCosmetic.R;
import vn.appCosmetic.ServiceAPI.RetrofitClient;
import vn.appCosmetic.ServiceAPI.RetrofitPrivate;
import vn.appCosmetic.ServiceAPI.Users.APIUsersService;

public class ProfileActivity extends AppCompatActivity {
    private ImageView imgProfile;
    private TextView ProfileEmail;
    private EditText ProfileUsername, ProfilePhone, ProfileAddress;
    private Spinner ProfileGender;
    private Button btnProfileSave;

    private Context context = this;

    private List<String> listGender = new ArrayList<String>(){
        {
            add("Chọn giới tính");
            add("Nam");
            add("Nữ");
            add("Khác");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        imgProfile = findViewById(R.id.imgProfile);
        ProfileEmail = findViewById(R.id.ProfileEmail);
        ProfileUsername = findViewById(R.id.ProfileUsername);
        ProfilePhone = findViewById(R.id.ProfilePhone);
        ProfileAddress = findViewById(R.id.ProfileAddress);
        ProfileGender = findViewById(R.id.ProfileGender);
        btnProfileSave = findViewById(R.id.btnProfileSave);

        ImageButton btnBack = findViewById(R.id.btn_user_detail_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listGender);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ProfileGender.setAdapter(adapter);
        ProfileGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ProfileGender.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ProfileGender.setSelection(0);
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRequestPermission();
            }
        });


        loadProfileData();

        btnProfileSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileData();
            }
        });
    }

    private void loadProfileData() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("idUser", 0);
        String token = sharedPreferences.getString( "token", "");
        if (userId != 0) {
            // Create an instance of APIUsersService
            APIUsersService apiUsersService = RetrofitPrivate.getRetrofit(token).create(APIUsersService.class);
            // Call the API and handle the response
            apiUsersService.getUserById(userId).enqueue(new Callback<Users>() {
                @Override
                public void onResponse(Call<Users> call, Response<Users> response) {
                    if (response.isSuccessful()) {
                        Users user = response.body();
                        if (user != null) {
                            ProfileEmail.setText(user.getEmail());
                            ProfileUsername.setText(user.getUsername());
                            ProfilePhone.setText(user.getPhone());
                            ProfileAddress.setText(user.getAddress());
                            ProfileGender.setSelection(listGender.indexOf(user.getGender()));

                            if(user.getAvatar() != null){
                                // Load avatar with Glide
                                Glide.with(ProfileActivity.this)
                                        .load(user.getAvatar())
                                        .into(imgProfile);
                            }
                            else{
                                imgProfile.setImageResource(R.drawable.account);
                            }
                        }
                    }
                }
                @Override
                public void onFailure(Call<Users> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }


    }
    private void saveProfileData() {
        // Get data from UI
        String username = ProfileUsername.getText().toString();
        String phone = ProfilePhone.getText().toString();
        String address = ProfileAddress.getText().toString();
        String gender = ProfileGender.getSelectedItem().toString();






        // Get UserID from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("idUser", 0);
        String token = sharedPreferences.getString( "token", "");
        if (userId != 0) {
            // Create an instance of APIUsersService
            APIUsersService apiUsersService = RetrofitPrivate.getRetrofit(token).create(APIUsersService.class);
            Users updatedUser = new Users();
            updatedUser.setUsername(username);
            updatedUser.setPhone(phone);
            updatedUser.setAddress(address);
            updatedUser.setGender(gender);


            FirebaseStorage storage = FirebaseStorage.getInstance();
            if(sharedPreferences.getString("avatar", null) != null){
                Uri uri = Uri.parse(sharedPreferences.getString("avatar", null));
                String fileName = UUID.randomUUID().toString();
                storage.getReference().child("avatars/" + fileName).putFile(uri).addOnSuccessListener(taskSnapshot -> {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri1 -> {
                        updatedUser.setAvatar(uri1.toString());
                        sharedPreferences.edit().putString("urlImageAvatar", uri1.toString()).apply();
                        sharedPreferences.edit().remove("avatar").apply();


                        // Call the API and handle the response

                        apiUsersService.putUpdateUser(userId, updatedUser).enqueue(new Callback<Users>() {
                            @Override
                            public void onResponse(Call<Users> call, Response<Users> response) {
                                if (response.isSuccessful()) {
                                    // Handle successful update
                                    Toast.makeText(ProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                    setResult(Activity.RESULT_OK); // Notify UserFragment of update
                                    finish();

                                } else {
                                    // Handle unsuccessful update
                                    Toast.makeText(ProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Users> call, Throwable t) {
                                // Handle failure
                                t.printStackTrace();
                            }
                        });

                    });
                });
            }
            else{
                String avatar = sharedPreferences.getString("urlImageAvatar", null);
                updatedUser.setAvatar(avatar);
                // Call the API and handle the response
                apiUsersService.putUpdateUser(userId, updatedUser).enqueue(new Callback<Users>() {
                    @Override
                    public void onResponse(Call<Users> call, Response<Users> response) {
                        if (response.isSuccessful()) {
                            // Handle successful update
                            Toast.makeText(ProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle unsuccessful update
                            Toast.makeText(ProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Users> call, Throwable t) {
                        // Handle failure
                        t.printStackTrace();
                    }
                });
            }


        }
    }


    private void onClickRequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            openGallery();
            return;
        }
        if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            ((Activity) context).requestPermissions(permission, 1);
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == Activity.RESULT_OK){
                Uri uri = result.getData().getData();
                imgProfile.setImageURI(uri);

                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                sharedPreferences.edit().putString("avatar", uri.toString()).apply();

            }
        }
    });
}