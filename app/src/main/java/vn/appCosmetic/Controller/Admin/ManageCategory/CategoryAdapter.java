package vn.appCosmetic.Controller.Admin.ManageCategory;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.appCosmetic.Model.Category;
import vn.appCosmetic.R;
import vn.appCosmetic.ServiceAPI.Category.APICategoryService;
import vn.appCosmetic.ServiceAPI.RetrofitClient;
import vn.appCosmetic.ServiceAPI.RetrofitPrivate;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{
    private Context context;
    private List<Category> categoryList;
    private LayoutInflater inflater;
    private SharedPreferences sharedPreferences;
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
                System.out.println("Update Click");
                if(nameUpdateCategory.equals("")){
                    edtNameCategory.setError("Vui lòng nhập tên danh mục");
                }else{
                    //Call API update category

                    apiCategoryService= RetrofitClient.getRetrofit().create(APICategoryService.class);
                    Category category = new Category();
                    category.setId(category.getId());
                    category.setNameCategory(nameUpdateCategory);

                    sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    String token = sharedPreferences.getString("token", "");
                    apiCategoryService = RetrofitPrivate.getRetrofit(token).create(APICategoryService.class);
                    apiCategoryService.putCategory(position,category).enqueue(new Callback<Category>() {
                        @Override
                        public void onResponse(Call<Category> call, Response<Category> response) {
                            if(response.isSuccessful()){
                                Category category = response.body();
                                for(int i=0; i<categoryList.size(); i++){
                                    if(categoryList.get(i).getId() == category.getId()){
                                        categoryList.get(i).setNameCategory(category.getNameCategory());
                                    }
                                }
                                dialog.dismiss();
                                notifyDataSetChanged();
                                Toast.makeText(context, "Update success", Toast.LENGTH_SHORT).show();

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

                sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                String token = sharedPreferences.getString("token", "");

                apiCategoryService= RetrofitPrivate.getRetrofit(token).create(APICategoryService.class);
                apiCategoryService.deleteCategory(position).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()){
                            for(int i=0; i<categoryList.size(); i++){
                                if(categoryList.get(i).getId() == position){
                                    categoryList.remove(i);
                                }
                            }
                            dialog.dismiss();
                            notifyDataSetChanged();
                            Toast.makeText(context, "Delete success", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
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
