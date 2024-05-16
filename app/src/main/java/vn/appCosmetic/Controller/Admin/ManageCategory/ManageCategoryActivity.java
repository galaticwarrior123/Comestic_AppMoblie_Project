package vn.appCosmetic.Controller.Admin.ManageCategory;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.jsonwebtoken.Claims;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.appCosmetic.Model.Category;
import vn.appCosmetic.R;
import vn.appCosmetic.ServiceAPI.Category.APICategoryService;
import vn.appCosmetic.ServiceAPI.RetrofitClient;
import vn.appCosmetic.ServiceAPI.RetrofitPrivate;

public class ManageCategoryActivity extends Fragment {
    RecyclerView recyclerView;
    CategoryAdapter categoryAdapter;
    List<Category> categoryList;
    APICategoryService apiCategoryService;

    ImageButton btnAddCategory;

    SharedPreferences sharedPreferences;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.activity_manage_category, container, false);
        recyclerView = view.findViewById(R.id.rcViewManageCategory);
        categoryList = new ArrayList<>();
        apiCategoryService = RetrofitClient.getRetrofit().create(APICategoryService.class);
        apiCategoryService.getCategory().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if(response.isSuccessful()){
                    categoryList = response.body();
                    categoryAdapter = new CategoryAdapter(getContext(), categoryList);
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(categoryAdapter);
                    categoryAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

        btnAddCategory = view.findViewById(R.id.imageButtonAddCategory);
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAdd();
            }
        });

        return view;

    }
    private void DialogAdd(){
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.activity_add_category);
        Button btnAdd, btnCancel;
        EditText edtNameCategory = dialog.findViewById(R.id.editTextNameCategory);
        btnAdd = dialog.findViewById(R.id.buttonAddCategory);
        btnCancel = dialog.findViewById(R.id.buttonCancelAddCategory);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameCategory = edtNameCategory.getText().toString().trim();
                Category category = new Category();
                category.setNameCategory(nameCategory);

                sharedPreferences = getContext().getSharedPreferences("MyPrefs", getContext().MODE_PRIVATE);
                String token = sharedPreferences.getString("token", "");

                apiCategoryService = RetrofitPrivate.getRetrofit(token).create(APICategoryService.class);
                apiCategoryService.postCategory(category).enqueue(new Callback<Category>() {
                    @Override
                    public void onResponse(Call<Category> call, Response<Category> response) {
                        if(response.isSuccessful()){
                            Category category = response.body();
                            categoryList.add(category);
                            categoryAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                            Toast.makeText(getContext(), "Add success", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Category> call, Throwable t) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
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
        dialog.show();
    }

}