package vn.appCosmetic.Controller.Admin.ManageProduct;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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

public class EditProductActivity extends AppCompatActivity {

    private Product product;
    private List<Uri> imageList = new ArrayList<>();
    private ImageAdapter imageAdapter = new ImageAdapter(imageList);

    private Context context = this;

    private APICategoryService apiCategoryService;
    private APIBrandService apiBrandService;

    private APIProductService apiProductService;

    EditText edtNameUpdateProduct, edtPriceUpdateProduct, edtDescriptionUpdateProduct, edtQuantityUpdateProduct;
    Spinner spnCategoryUpdateProduct, spnBrandUpdateProduct;
    Button btnUpdateChooseImage, btnUpdate, btnCancel;
    RecyclerView rcViewImageUpdateProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        product = (Product) getIntent().getSerializableExtra("product");
        initUI();
        setInfoProduct();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameUpdate = edtNameUpdateProduct.getText().toString();
                String descriptionUpdate = edtDescriptionUpdateProduct.getText().toString();
                int priceUpdate = Integer.parseInt(edtPriceUpdateProduct.getText().toString());
                int quantityUpdate = Integer.parseInt(edtQuantityUpdateProduct.getText().toString());
                String categoryUpdate = spnCategoryUpdateProduct.getSelectedItem().toString();
                String brandUpdate = spnBrandUpdateProduct.getSelectedItem().toString();
                Product productUpdate = new Product();
                productUpdate.setName(nameUpdate);
                productUpdate.setDescription(descriptionUpdate);
                productUpdate.setPrice(priceUpdate);
                productUpdate.setStock(quantityUpdate);
                productUpdate.setIdCategory(Integer.parseInt(categoryUpdate));
                productUpdate.setIdBrand(Integer.parseInt(brandUpdate));

                List<String> listImage = new ArrayList<>();
                for(Uri uri: imageList){
                    listImage.add(uri.toString());
                }
                productUpdate.setImages(listImage);

                APIProductService apiProductService = RetrofitProductClient.getRetrofit().create(APIProductService.class);
                apiProductService.putProduct(product.getId(),productUpdate).enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        if(response.isSuccessful()){
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {

                    }
                });
            }
        });

    }
    private void setInfoProduct() {
        edtNameUpdateProduct.setText(product.getName());
        edtPriceUpdateProduct.setText(String.valueOf(product.getPrice()));
        edtDescriptionUpdateProduct.setText(product.getDescription());
        edtQuantityUpdateProduct.setText(String.valueOf(product.getStock()));
        for(String url: product.getImages()){
            imageList.add(Uri.parse(url));
        }
        imageAdapter.notifyDataSetChanged();
        rcViewImageUpdateProduct.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rcViewImageUpdateProduct.setAdapter(imageAdapter);

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

        btnUpdateChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRequestPermission();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initUI() {
        edtNameUpdateProduct = findViewById(R.id.editTextNameUpdateProduct);
        edtPriceUpdateProduct = findViewById(R.id.editTextUpdatePriceProduct);
        edtDescriptionUpdateProduct = findViewById(R.id.editTextUpdateDescriptionProduct);
        edtQuantityUpdateProduct = findViewById(R.id.editTextUpdateQuantityProduct);

        spnCategoryUpdateProduct = findViewById(R.id.spinnerUpdateCategoryProduct);
        spnBrandUpdateProduct = findViewById(R.id.spinnerUpdateBrandProduct);

        btnUpdateChooseImage = findViewById(R.id.buttonUpdateChooseImage);
        btnUpdate = findViewById(R.id.buttonUpdateProduct);
        btnCancel = findViewById(R.id.buttonCancelUpdate);
        rcViewImageUpdateProduct = findViewById(R.id.recyclerViewUpdateImageProduct);
    }

    private void onClickRequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openGallery();
            return;
        }
        if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            ((Activity) context).requestPermissions(permission, 1);
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == Activity.RESULT_OK){
                Uri uri = result.getData().getData();
                imageList.add(uri);
                imageAdapter.notifyDataSetChanged();
            }
        }
    });
}