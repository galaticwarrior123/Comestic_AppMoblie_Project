package vn.appCosmetic.Controller.Admin.ManageProduct;

import static androidx.core.content.ContextCompat.checkSelfPermission;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import vn.appCosmetic.Model.Category;
import vn.appCosmetic.Model.Image;
import vn.appCosmetic.Model.Product;
import vn.appCosmetic.R;
import vn.appCosmetic.ServiceAPI.Category.APICategoryService;
import vn.appCosmetic.ServiceAPI.Category.RetrofitCategoryClient;
import vn.appCosmetic.ServiceAPI.Product.APIProductService;
import vn.appCosmetic.ServiceAPI.Product.RetrofitProductClient;

public class ManageProductActivity extends Fragment{
    private APIProductService apiProductService;
    private APICategoryService apiCategoryService;
private List<Uri> imageList = new ArrayList<>();


    private ImageAdapter imageAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.activity_manage_product, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rcViewManageProduct);
        List<Product> productModelList =new ArrayList<>();
        apiProductService= RetrofitProductClient.getRetrofit().create(APIProductService.class);
        apiProductService.getAllProduct().enqueue(new retrofit2.Callback<List<Product>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Product>> call, retrofit2.Response<List<Product>> response) {
                if(response.isSuccessful()){
                    productModelList.addAll(response.body());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Product>> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
        ProductAdapter productAdapter = new ProductAdapter(getContext(), productModelList);
        recyclerView.setAdapter(productAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);


        Spinner spinner = view.findViewById(R.id.spinnerManageProduct);
        apiCategoryService = RetrofitCategoryClient.getRetrofit().create(APICategoryService.class);
        apiCategoryService.getCategory().enqueue(new retrofit2.Callback<List<Category>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Category>> call, retrofit2.Response<List<Category>> response) {
                if(response.isSuccessful()){
                    System.out.println("spinner");
                    List<Category> categoryList = new ArrayList<>();
                    categoryList.addAll(response.body());
                    List<String> list = new ArrayList<>();
                    for (Category category : categoryList){
                        list.add(category.getNameCategory());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);


                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Category>> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });


        ImageButton btnAddProduct = view.findViewById(R.id.imageButtonAddProduct);
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.activity_add_product);

                Button btnChooseImage = dialog.findViewById(R.id.buttonChooseImage);
                RecyclerView rcImage = dialog.findViewById(R.id.recyclerViewImageProduct);
                btnChooseImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickRequestPermission();
                    }
                });
                imageAdapter = new ImageAdapter(imageList);
                rcImage.setAdapter(imageAdapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                rcImage.setLayoutManager(linearLayoutManager);


                Spinner spinner = dialog.findViewById(R.id.spinnerCategoryProduct);
                apiCategoryService.getCategory().enqueue(new retrofit2.Callback<List<Category>>() {
                    @Override
                    public void onResponse(retrofit2.Call<List<Category>> call, retrofit2.Response<List<Category>> response) {
                        if(response.isSuccessful()){
                            List<Category> categoryList = new ArrayList<>();
                            categoryList.addAll(response.body());
                            List<String> list = new ArrayList<>();
                            for (Category category : categoryList){
                                list.add(category.getNameCategory());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, list);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<List<Category>> call, Throwable t) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });


                Button btnAddProduct = dialog.findViewById(R.id.buttonAddProduct);

                EditText edtNameProduct = dialog.findViewById(R.id.editTextNameProduct);
                EditText edtDescriptionProduct = dialog.findViewById(R.id.editTextDescriptionProduct);
                EditText edtPriceProduct = dialog.findViewById(R.id.editTextPriceProduct);
                EditText edtStockProduct = dialog.findViewById(R.id.editTextQuantityProduct);


                btnAddProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = edtNameProduct.getText().toString().trim();
                        String description = edtDescriptionProduct.getText().toString().trim();
                        int price = Integer.parseInt(edtPriceProduct.getText().toString().trim());
                        int stock = Integer.parseInt(edtStockProduct.getText().toString().trim());
                        List<Image> images = new ArrayList<>();
                        for (Uri uri : imageList){
                            Image image = new Image();
                            image.setUrl(uri.toString());
                            images.add(image);
                        }
                        Product product = new Product();
                        product.setName(name);
                        product.setDescription(description);
                        product.setPrice(price);
                        product.setStock(stock);
                        product.setImages(images);

                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Category category = new Category();
                                category.setId(position);
                                product.setCategory(category);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        apiProductService.postProduct(product).enqueue(new retrofit2.Callback<Product>() {
                            @Override
                            public void onResponse(retrofit2.Call<Product> call, retrofit2.Response<Product> response) {
                                if(response.isSuccessful()){
                                    Product product = response.body();
                                    productModelList.add(product);
                                    productAdapter.notifyDataSetChanged();
                                    dialog.dismiss();
                                    Toast.makeText(getContext(), "Add product success", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getContext(), "Add product fail", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(retrofit2.Call<Product> call, Throwable t) {
                                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                dialog.show();
            }
        });

        return view;
    }

    private void onClickRequestPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, 1001);
            }else{
                openGallery();
            }
        }else{
            openGallery();
        }
    }

    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        mActivityResultLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Uri uri = result.getData().getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                            imageList.add(uri);
                            imageAdapter.notifyDataSetChanged();
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
    );

}