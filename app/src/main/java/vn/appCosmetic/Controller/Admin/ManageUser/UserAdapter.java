package vn.appCosmetic.Controller.Admin.ManageUser;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.appCosmetic.Model.Users;
import vn.appCosmetic.R;
import vn.appCosmetic.ServiceAPI.Users.APIUsersService;
import vn.appCosmetic.ServiceAPI.Users.RetrofitUsersClient;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{
    List<Users> userList;
    Context context;
    LayoutInflater inflater;

    private APIUsersService apiUsersService;

    public UserAdapter(Context context, List<Users> userList) {
        this.context = context;
        this.userList = userList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_item_manage_user_page, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users user = userList.get(position);
        holder.txtName.setText(user.getUsername());
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEditUser(user);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton btnEdit, btnDelete;
        TextView txtName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnDelete = itemView.findViewById(R.id.imageButtonDeleteUser);
            btnEdit = itemView.findViewById(R.id.imageButtonEditUser);
            txtName = itemView.findViewById(R.id.textViewUserName);
        }
    }

    private void dialogEditUser(Users user){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_edit_user);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        TextView txtUsername = dialog.findViewById(R.id.textViewUserNameEdit);
        TextView txtEmail = dialog.findViewById(R.id.textViewEmailEdit);
        TextView txtPhone = dialog.findViewById(R.id.textViewPhoneNumberEdit);
        TextView txtAddress = dialog.findViewById(R.id.textViewAddressEdit);
        Button btnUpdate = dialog.findViewById(R.id.buttonEditUser);
        Button btnCancel = dialog.findViewById(R.id.buttonCancelEditUser);

        txtUsername.setText(user.getUsername());
        txtEmail.setText(user.getEmail());
        txtPhone.setText(user.getPhone());
        txtAddress.setText(user.getAddress());

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update user
                apiUsersService = RetrofitUsersClient.getRetrofit().create(APIUsersService.class);
                apiUsersService.putStatusUser(user.getId()).enqueue(new Callback<Users>() {
                    @Override
                    public void onResponse(Call<Users> call, Response<Users> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(context, "update status", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        else{
                            Toast.makeText(context, "Error update status", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<Users> call, Throwable t) {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
