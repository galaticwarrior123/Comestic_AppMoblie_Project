package vn.appCosmetic.Controller.User.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.appCosmetic.Model.Users;
import vn.appCosmetic.R;
import vn.appCosmetic.ServiceAPI.RetrofitClient;
import vn.appCosmetic.ServiceAPI.RetrofitPrivate;
import vn.appCosmetic.ServiceAPI.Users.APIUsersService;

public class UserFragment extends Fragment {
    private Button btnProfile,btnChangePassword;
    private TextView txtUserFragmentName;
    private ImageView imgUserFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        txtUserFragmentName = view.findViewById(R.id.txtUserFragmentName);
        imgUserFragment = view.findViewById(R.id.imgUserFragment);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        Integer idUser = sharedPreferences.getInt("idUser", 0);
        String token = sharedPreferences.getString("token", "");
        if (idUser!=0) {

            APIUsersService apiService = RetrofitPrivate.getRetrofit(token).create(APIUsersService.class);
            Call<Users> call = apiService.getUserById(idUser);
            call.enqueue(new Callback<Users>() {
                @Override
                public void onResponse(@NonNull Call<Users> call, @NonNull Response<Users> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Users user = response.body();
                        txtUserFragmentName.setText(user.getUsername());
                        Glide.with(requireContext()).load(user.getAvatar()).into(imgUserFragment);
                    } else {
                        Toast.makeText(getContext(), "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(@NonNull Call<Users> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        btnProfile = view.findViewById(R.id.btnUserFragmentUpdateProfile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        btnChangePassword = view.findViewById(R.id.btnUserFragmentChangePassword);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}