package vn.appCosmetic.Controller.Admin.ManageBrand;

import android.app.Dialog;
import android.content.Context;
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
import vn.appCosmetic.Model.Brand;
import vn.appCosmetic.R;
import vn.appCosmetic.ServiceAPI.Brand.APIBrandService;
import vn.appCosmetic.ServiceAPI.Brand.RetrofitBrandClient;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.ViewHolder> {
    private Context context;
    private List<Brand> brandList;
    private APIBrandService apiBrandService;

    public BrandAdapter(Context context, List<Brand> brandList) {
        this.context = context;
        this.brandList = brandList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_manage_brand_page, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Brand brand = brandList.get(position);
        holder.tvNameBrand.setText(brand.getNameBrand());
        holder.btnEditBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogEdit(brand.getId(),brand.getNameBrand());
            }
        });

        holder.btnDeleteBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogDelete(brand.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNameBrand;
        private ImageButton btnEditBrand, btnDeleteBrand;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameBrand = itemView.findViewById(R.id.textViewNameBrand);
            btnEditBrand = itemView.findViewById(R.id.imageButtonEditBrand);
            btnDeleteBrand = itemView.findViewById(R.id.imageButtonDeleteBrand);
        }

    }
    private void DialogEdit(int position, String nameBrand) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_edit_brand);
        EditText edtNameBrand = dialog.findViewById(R.id.editTextNameUpdateBrand);
        edtNameBrand.setText(nameBrand);
        Button btnUpdate, btnCancel;
        btnUpdate= dialog.findViewById(R.id.buttonUpdateBrand);
        btnCancel = dialog.findViewById(R.id.buttonCancelUpdateBrand);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameUpdateBrand = edtNameBrand.getText().toString().trim();
                System.out.println(nameUpdateBrand);
                Brand updateBrand = new Brand();
                updateBrand.setId(position);
                updateBrand.setNameBrand(nameUpdateBrand);
                apiBrandService= RetrofitBrandClient.getRetrofit().create(APIBrandService.class);
                apiBrandService.putBrand(position,updateBrand).enqueue(new Callback<Brand>() {
                    @Override
                    public void onResponse(Call<Brand> call, Response<Brand> response) {
                        if(response.isSuccessful()){
                            Brand brand = response.body();
                            for(int i=0; i<brandList.size(); i++){
                                if(brandList.get(i).getId() == position){
                                    brandList.get(i).setNameBrand(brand.getNameBrand());
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
                    public void onFailure(Call<Brand> call, Throwable t) {
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

    private void DialogDelete(int position){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_delete_brand);
        Button btnDelete, btnCancel;
        btnDelete = dialog.findViewById(R.id.buttonDeleteBrand);
        btnCancel = dialog.findViewById(R.id.buttonCancelDeleteBrand);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiBrandService = RetrofitBrandClient.getRetrofit().create(APIBrandService.class);
                apiBrandService.deleteBrand(position).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()){
                            for(int i=0; i<brandList.size(); i++){
                                if(brandList.get(i).getId() == position){
                                    brandList.remove(i);
                                    notifyDataSetChanged();
                                    dialog.dismiss();
                                    Toast.makeText(context, "Delete success", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        else{
                            int statusCode = response.code();
                            Log.e("Error", String.valueOf(statusCode));
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
