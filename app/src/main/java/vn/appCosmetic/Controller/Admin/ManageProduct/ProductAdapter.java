package vn.appCosmetic.Controller.Admin.ManageProduct;



import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.appCosmetic.Model.Product;
import vn.appCosmetic.R;
import vn.appCosmetic.ServiceAPI.Product.APIProductService;
import vn.appCosmetic.ServiceAPI.RetrofitClient;
import vn.appCosmetic.ServiceAPI.RetrofitPrivate;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context context;
    private List<Product> productModelList;

    private LayoutInflater inflater;


    private APIProductService apiProductService;

    private SharedPreferences sharedPreferences;



    public ProductAdapter(Context context, List<Product> productModelList) {
        this.context = context;
        this.productModelList = productModelList;
        inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_item_manage_product_page, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product productModel = productModelList.get(position);
        holder.txtNameProduct.setText(productModel.getName());
        if(productModel.getStock()==0){
            holder.txtStockProduct.setText("Hết hàng");
        }
        else{
            holder.txtStockProduct.setText("Số lượng: " + productModel.getStock());
        }
        if(productModel.getImages() != null && productModel.getImages().size() > 0){
            String url = productModel.getImages().get(0);
            Uri uri = Uri.parse(url);
            Glide.with(holder.itemView.getContext()).load(uri).into(holder.imgProduct);
        }
        else{
            holder.imgProduct.setImageResource(R.drawable.noimageproduct);
        }

        if(productModel.isStatus()){
            holder.switchStatus.setChecked(true);
        }
        else{
            holder.switchStatus.setChecked(false);
        }

       holder.switchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                apiProductService = RetrofitClient.getRetrofit().create(APIProductService.class);
                apiProductService.putStatusProduct(productModel.getId()).enqueue(new Callback<Product>() {
                     @Override
                     public void onResponse(Call<Product> call, Response<Product> response) {
                          if(response.isSuccessful()){
                            if(isChecked){
                                 Toast.makeText(context, "Product is active", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                 Toast.makeText(context, "Product is inactive", Toast.LENGTH_SHORT).show();
                            }
                          }
                          else{
                            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                          }
                     }

                     @Override
                     public void onFailure(Call<Product> call, Throwable t) {
                          Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                     }
                });
           }
       });


        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditProductActivity.class);
                intent.putExtra("product", productModel);
                context.startActivity(intent);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogDeleteProduct(productModel.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtNameProduct, txtStockProduct;
        private ImageView imgProduct;
        private ImageButton btnEdit, btnDelete;
        private Switch switchStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameProduct = itemView.findViewById(R.id.textViewProductName);
            txtStockProduct = itemView.findViewById(R.id.textViewProductStock);
            switchStatus = itemView.findViewById(R.id.switchProductStatus);
            imgProduct = itemView.findViewById(R.id.imageViewProduct);
            btnEdit = itemView.findViewById(R.id.imageButtonEditProduct);
            btnDelete = itemView.findViewById(R.id.imageButtonDeleteProduct);

        }
    }



    private void DialogDeleteProduct(int position) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_delete_product);
        Button btnDelete = dialog.findViewById(R.id.buttonDeleteProduct);
        Button btnCancel = dialog.findViewById(R.id.buttonCancelDeleteProduct);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                String token = sharedPreferences.getString("token", "");

                apiProductService = RetrofitPrivate.getRetrofit(token).create(APIProductService.class);
                apiProductService.deleteProduct(position).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()){
                            for(int i=0; i<productModelList.size(); i++){
                                if(productModelList.get(i).getId() == position){
                                    productModelList.remove(i);
                                }
                            }
                            notifyDataSetChanged();
                            dialog.dismiss();
                            Toast.makeText(context, "Delete success", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dialog.show();
    }






}
