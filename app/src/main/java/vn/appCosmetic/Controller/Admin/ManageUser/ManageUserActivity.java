package vn.appCosmetic.Controller.Admin.ManageUser;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.appCosmetic.Model.Users;
import vn.appCosmetic.R;
import vn.appCosmetic.ServiceAPI.Users.APIUsersService;
import vn.appCosmetic.ServiceAPI.Users.RetrofitUsersClient;

public class ManageUserActivity extends Fragment {

    private APIUsersService apiUsersService;
    private UserAdapter userAdapter;
    private RecyclerView rcViewUser;

    private List<Users> usersList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_manage_user, container, false);
        rcViewUser = view.findViewById(R.id.rcViewManageUser);
        apiUsersService = RetrofitUsersClient.getRetrofit().create(APIUsersService.class);
        apiUsersService.getUsers().enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                if(response.isSuccessful()){
                    usersList = response.body();
                    System.out.println(usersList.size());
                    userAdapter = new UserAdapter(getContext(), usersList);
                    rcViewUser.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rcViewUser.setLayoutManager(layoutManager);
                    rcViewUser.setAdapter(userAdapter);
                    userAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }

        });
        return view;
    }
}
