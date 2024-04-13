package vn.appCosmetic.Controller.Admin.ManageCategory;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.appCosmetic.Model.Category;
import vn.appCosmetic.R;
import vn.appCosmetic.ServiceAPI.Category.APICategoryService;
import vn.appCosmetic.ServiceAPI.Category.RetrofitCategoryClient;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{
    private Context context;
    private List<Category> categoryList;
    private LayoutInflater inflater;

    private APICategoryService apiCategoryService;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
        inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_item_manage_category_page, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.txtNameCategory.setText(category.getNameCategory());
        holder.btnEditCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogEdit(category.getId(),category.getNameCategory());
            }
        });

        holder.btnDeleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogDelete(category.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        private TextView txtNameCategory;
        private ImageButton btnEditCategory, btnDeleteCategory;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameCategory = itemView.findViewById(R.id.textViewNameCategory);
            btnEditCategory = itemView.findViewById(R.id.imageButtonEditCategory);
            btnDeleteCategory = itemView.findViewById(R.id.imageButtonDeleteCategory);
        }
    }

    private void DialogEdit(int position, String nameCategory){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_edit_category);
        Button btnUpdate, btnCancel;
        EditText edtNameCategory = dialog.findViewById(R.id.editTextNameUpdateCategory);
        edtNameCategory.setText(nameCategory);
        btnUpdate = dialog.findViewById(R.id.buttonUpdateCategory);
        btnCancel = dialog.findViewById(R.id.buttonCancelUpdateCategory);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameUpdateCategory= edtNameCategory.getText().toString().trim();
                if(nameUpdateCategory.equals("")){
                    edtNameCategory.setError("Vui lòng nhập tên danh mục");
                }else{
                    //Call API update category
                    apiCategoryService= RetrofitCategoryClient.getRetrofit().create(APICategoryService.class);
                    Category category = new Category();
                    category.setId(position);
                    category.setNameCategory(nameUpdateCategory);

                    apiCategoryService = RetrofitCategoryClient.getRetrofit().create(APICategoryService.class);
                    apiCategoryService.putCategory(position,category).enqueue(new Callback<Category>() {
                        @Override
                        public void onResponse(Call<Category> call, Response<Category> response) {
                            if(response.isSuccessful()){
                                Category category = response.body();
                                categoryList.set(position, category);
                                dialog.dismiss();
//                                Intent intent = new Intent(context, ManageCategoryActivity.class);
//                                context.startActivity(intent);
                            }
                            else{
                                int statusCode = response.code();
                                Log.e("Error", String.valueOf(statusCode));
                            }
                        }

                        @Override
                        public void onFailure(Call<Category> call, Throwable t) {
                            Log.e("Error", t.getMessage());
                        }
                    });


                }

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


    private void DialogDelete(int position){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_delete_category);
        Button btnDelete, btnCancel;
        btnDelete = dialog.findViewById(R.id.buttonDeleteCategory);
        btnCancel = dialog.findViewById(R.id.buttonCancelDeleteCategory);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Call API delete category
                apiCategoryService= RetrofitCategoryClient.getRetrofit().create(APICategoryService.class);
                apiCategoryService.deleteCategory(position).enqueue(new Callback<Category>() {
                    @Override
                    public void onResponse(Call<Category> call, Response<Category> response) {
                        if(response.isSuccessful()){
                            categoryList.remove(position);
                            dialog.dismiss();
//                            Intent intent = new Intent(context, ManageCategoryActivity.class);
//                            context.startActivity(intent);
                        }
                        else{
                            int statusCode = response.code();
                            Log.e("Error", String.valueOf(statusCode));
                        }
                    }

                    @Override
                    public void onFailure(Call<Category> call, Throwable t) {
                        Log.e("Error", t.getMessage());
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