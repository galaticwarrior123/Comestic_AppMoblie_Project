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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.appCosmetic.Controller.LoginRegister.LoginActivity;
import vn.appCosmetic.Controller.User.User.ManagerMyOrder.MyOrdersActivity;
import vn.appCosmetic.Model.Users;
import vn.appCosmetic.R;
import vn.appCosmetic.ServiceAPI.RetrofitPrivate;
import vn.appCosmetic.ServiceAPI.Users.APIUsersService;

public class UserFragment extends Fragment {
    private Button btnProfile, btnChangePassword, btnLogout, btnMyOrders;
    private TextView txtUserFragmentName;
    private ImageView imgUserFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        txtUserFragmentName = view.findViewById(R.id.txtUserFragmentName);
        imgUserFragment = view.findViewById(R.id.imgUserFragment);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        Integer idUser = sharedPreferences.getInt("idUser", 0);
        String token = sharedPreferences.getString("token", "");
        if (idUser != 0) {
            APIUsersService apiService = RetrofitPrivate.getRetrofit(token).create(APIUsersService.class);
            Call<Users> call = apiService.getUserById(idUser);
            call.enqueue(new Callback<Users>() {
                @Override
                public void onResponse(@NonNull Call<Users> call, @NonNull Response<Users> response) {
                    if (response.isSuccessful() && response.body() != null && isAdded()) {
                        Users user = response.body();
                        txtUserFragmentName.setText(user.getUsername());
                        Glide.with(requireContext()).load(user.getAvatar()).into(imgUserFragment);
                    } else if (isAdded()) {
                        Toast.makeText(requireContext(), "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Users> call, @NonNull Throwable t) {
                    if (isAdded()) {
                        Toast.makeText(requireContext(), "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        btnProfile = view.findViewById(R.id.btnUserFragmentUpdateProfile);
        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        });

        btnMyOrders = view.findViewById(R.id.btnUserFragmentOrder);
        btnMyOrders.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MyOrdersActivity.class);
            startActivity(intent);
        });

        btnChangePassword = view.findViewById(R.id.btnUserFragmentChangePassword);
        btnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
            startActivity(intent);
        });

        btnLogout = view.findViewById(R.id.btnUserFragmentLogOut);
        btnLogout.setOnClickListener(v -> {
            SharedPreferences sharedPreferences1 = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences1.edit();
            editor.clear();
            editor.apply();

            Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserData();
    }

    private void loadUserData() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        Integer idUser = sharedPreferences.getInt("idUser", 0);
        String token = sharedPreferences.getString("token", "");
        if (idUser != 0) {
            APIUsersService apiService = RetrofitPrivate.getRetrofit(token).create(APIUsersService.class);
            Call<Users> call = apiService.getUserById(idUser);
            call.enqueue(new Callback<Users>() {
                @Override
                public void onResponse(@NonNull Call<Users> call, @NonNull Response<Users> response) {
                    if (response.isSuccessful() && response.body() != null && isAdded()) {
                        Users user = response.body();
                        txtUserFragmentName.setText(user.getUsername());
                        Glide.with(requireContext()).load(user.getAvatar()).into(imgUserFragment);
                    } else if (isAdded()) {
                        Toast.makeText(requireContext(), "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Users> call, @NonNull Throwable t) {
                    if (isAdded()) {
                        Toast.makeText(requireContext(), "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
