package vn.appCosmetic.Controller.Admin.ManageProduct;



import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.PermissionChecker.checkSelfPermission;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.PermissionChecker;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.appCosmetic.Model.Brand;
import vn.appCosmetic.Model.Category;
import vn.appCosmetic.Model.Product;
import vn.appCosmetic.R;
import vn.appCosmetic.ServiceAPI.Brand.APIBrandService;
import vn.appCosmetic.ServiceAPI.Brand.RetrofitBrandClient;
import vn.appCosmetic.ServiceAPI.Category.APICategoryService;
import vn.appCosmetic.ServiceAPI.Category.RetrofitCategoryClient;
import vn.appCosmetic.ServiceAPI.Product.APIProductService;
import vn.appCosmetic.ServiceAPI.Product.RetrofitProductClient;
import vn.appCosmetic.Utils.ImageUtils;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{

    private Context context;
    private List<Product> productModelList;

    private LayoutInflater inflater;

    private AdapterView.OnItemClickListener onItemClickListener;

    private APIProductService apiProductService;
    private APICategoryService apiCategoryService;
    private APIBrandService apiBrandService;

    private List<Uri> imageList = new ArrayList<>();

//    private ImageProductUpdateAdapter imageAdapter = new ImageProductUpdateAdapter(imageList, context);





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
        System.out.println("id"+productModel.getId());
        holder.txtNameProduct.setText(productModel.getName());
        if(productModel.getImages() != null && productModel.getImages().size() > 0){
            String url = productModel.getImages().get(0);
            Uri uri = Uri.parse(url);
            Glide.with(holder.itemView.getContext()).load(uri).into(holder.imgProduct);
        }
        else{
            holder.imgProduct.setImageResource(R.drawable.ic_launcher_background);
        }


        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUpdateProduct(productModel.getId(),productModel);
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
        private TextView txtNameProduct;
        private ImageView imgProduct;
        private ImageButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameProduct = itemView.findViewById(R.id.textViewProductName);
            imgProduct = itemView.findViewById(R.id.imageViewProduct);
            btnEdit = itemView.findViewById(R.id.imageButtonEditProduct);
            btnDelete = itemView.findViewById(R.id.imageButtonDeleteProduct);

        }
    }

    private void DialogUpdateProduct(int position, Product productModel) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_edit_product);
        EditText edtNameUpdateProduct, edtPriceUpdateProduct, edtDescriptionUpdateProduct, edtQuantityUpdateProduct;
        Spinner spnCategoryUpdateProduct, spnBrandUpdateProduct;
        Button btnUpdateChooseImage, btnUpdate, btnCancel;
        RecyclerView rcViewImageUpdateProduct;


        rcViewImageUpdateProduct = dialog.findViewById(R.id.rcViewImageUpdateProduct);
        edtNameUpdateProduct = dialog.findViewById(R.id.editTextNameUpdateProduct);
        edtPriceUpdateProduct = dialog.findViewById(R.id.editTextUpdatePriceProduct);
        edtDescriptionUpdateProduct = dialog.findViewById(R.id.editTextUpdateDescriptionProduct);
        edtQuantityUpdateProduct = dialog.findViewById(R.id.editTextUpdateQuantityProduct);

        spnCategoryUpdateProduct = dialog.findViewById(R.id.spinnerUpdateCategoryProduct);
        spnBrandUpdateProduct = dialog.findViewById(R.id.spinnerUpdateBrandProduct);

        btnUpdateChooseImage = dialog.findViewById(R.id.buttonUpdateChooseImage);
        btnUpdate = dialog.findViewById(R.id.buttonUpdateProduct);
        btnCancel = dialog.findViewById(R.id.buttonCancelUpdate);

//        btnUpdateChooseImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onClickRequestPermission();
//            }
//        });


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameUpdate = edtNameUpdateProduct.getText().toString();
                String descriptionUpdate = edtDescriptionUpdateProduct.getText().toString();
                int priceUpdate = Integer.parseInt(edtPriceUpdateProduct.getText().toString());
                int quantityUpdate = Integer.parseInt(edtQuantityUpdateProduct.getText().toString());
                String categoryUpdate = spnCategoryUpdateProduct.getSelectedItem().toString();
                String brandUpdate = spnBrandUpdateProduct.getSelectedItem().toString();
                Product product = new Product();
                product.setName(nameUpdate);
                product.setDescription(descriptionUpdate);
                product.setPrice(priceUpdate);
                product.setStock(quantityUpdate);
                product.setIdCategory(Integer.parseInt(categoryUpdate));
                product.setIdBrand(Integer.parseInt(brandUpdate));


                // code here
            }
        });


        edtNameUpdateProduct.setText(productModel.getName());
        edtPriceUpdateProduct.setText(String.valueOf(productModel.getPrice()));
        edtDescriptionUpdateProduct.setText(productModel.getDescription());
        edtQuantityUpdateProduct.setText(String.valueOf(productModel.getStock()));

        List<Uri> imageList = new ArrayList<>();
        if(productModel.getImages() != null){
            for(String url: productModel.getImages()){
                Uri uri = Uri.parse(url);
                imageList.add(uri);
            }
        }
        ImageProductUpdateAdapter imageProductUpdateAdapter = new ImageProductUpdateAdapter(imageList, context);
        rcViewImageUpdateProduct.setAdapter(imageProductUpdateAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        rcViewImageUpdateProduct.setLayoutManager(layoutManager);

        apiCategoryService= RetrofitCategoryClient.getRetrofit().create(APICategoryService.class);
        apiCategoryService.getCategory().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if(response.isSuccessful()){
                    List<Category> categoryList = response.body();
                    List<String> listStringCategory= new ArrayList<>();
                    for(Category category: categoryList){
                        listStringCategory.add(category.getNameCategory());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, listStringCategory);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnCategoryUpdateProduct.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });
        apiBrandService= RetrofitBrandClient.getRetrofit().create(APIBrandService.class);
        apiBrandService.getAllBrand().enqueue(new Callback<List<Brand>>() {
            @Override
            public void onResponse(Call<List<Brand>> call, Response<List<Brand>> response) {
                if(response.isSuccessful()){
                    List<Brand> brandList = response.body();
                    List<String> listStringBrand= new ArrayList<>();
                    for(Brand brand: brandList){
                        listStringBrand.add(brand.getNameBrand());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, listStringBrand);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnBrandUpdateProduct.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<List<Brand>> call, Throwable t) {

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
                apiProductService = RetrofitProductClient.getRetrofit().create(APIProductService.class);
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
                            int statusCode = response.code();
                            System.out.println("Error"+statusCode);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        System.out.println("Error"+t.getMessage());
                    }
                });
            }
        });
        dialog.show();
    }


//    private void onClickRequestPermission() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            openGallery();
//            return;
//        }
//        if (checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED) {
//            openGallery();
//        } else {
//            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
//            requestPermissions((Activity) context, permission, 1);
//        }
//    }
//
//    private void openGallery() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
//    }
//
//    private final ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    if(result.getResultCode() == Activity.RESULT_OK){
//                        Uri uri = result.getData().getData();
//                        imageList.add(uri);
//                        imageAdapter.notifyDataSetChanged();
//                    }
//                }
//            }
//    );
//
//    private ActivityResultLauncher<Intent> registerForActivityResult(ActivityResultContracts.StartActivityForResult startActivityForResult, ActivityResultCallback<ActivityResult> activityResultCallback) {
//        return registerForActivityResult(startActivityForResult, activityResultCallback);
//    }


}
