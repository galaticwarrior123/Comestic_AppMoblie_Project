package vn.appCosmetic.Controller.Admin.ManageProduct;

import static androidx.core.content.ContextCompat.checkSelfPermission;




import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.appCosmetic.Model.Brand;
import vn.appCosmetic.Model.Category;
import vn.appCosmetic.Model.Image;
import vn.appCosmetic.Model.Product;
import vn.appCosmetic.R;
import vn.appCosmetic.ServiceAPI.Brand.APIBrandService;
import vn.appCosmetic.ServiceAPI.Brand.RetrofitBrandClient;
import vn.appCosmetic.ServiceAPI.Category.APICategoryService;
import vn.appCosmetic.ServiceAPI.Category.RetrofitCategoryClient;
import vn.appCosmetic.ServiceAPI.Product.APIProductService;
import vn.appCosmetic.ServiceAPI.Product.RetrofitProductClient;

public class ManageProductActivity extends Fragment{
    private APIProductService apiProductService;
    private APICategoryService apiCategoryService;
    private APIBrandService apiBrandService;
    private List<Uri> imageList = new ArrayList<>();
    private Context context;

    private ImageAdapter imageAdapter;

    private ProductAdapter productAdapter;

    private List<Product> productModelList;
    private RecyclerView recyclerViewProduct;

    private Spinner spinnerCategory;
    private Spinner spnCate;
    private Spinner spinnerBrand;
    private List<Category> categoryList = new ArrayList<>();


    private int count = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.activity_manage_product, container, false);
        apiProductService= RetrofitProductClient.getRetrofit().create(APIProductService.class);
        productModelList = new ArrayList<>();
        recyclerViewProduct = view.findViewById(R.id.rcViewManageProduct);
        context = inflater.getContext();
        apiProductService.getAllProduct().enqueue(new retrofit2.Callback<List<Product>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Product>> call, retrofit2.Response<List<Product>> response) {
                if(response.isSuccessful()){
                    productModelList= response.body();
                    productAdapter = new ProductAdapter(context, productModelList);
                    recyclerViewProduct.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    recyclerViewProduct.setLayoutManager(layoutManager);
                    recyclerViewProduct.setAdapter(productAdapter);
                    productAdapter.notifyDataSetChanged();

                }
                else{
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Product>> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });



        spnCate = view.findViewById(R.id.spinnerManageProduct);
        final List<Category>[] categoryList = new List[]{new ArrayList<>()};
        apiCategoryService = RetrofitCategoryClient.getRetrofit().create(APICategoryService.class);
        apiCategoryService.getCategory().enqueue(new retrofit2.Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if(response.isSuccessful()){
                    categoryList[0].addAll(response.body());
                    List<String> list = new ArrayList<>();
                    list.add("All");
                    for (Category category : categoryList[0]){
                        list.add(category.getNameCategory());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnCate.setAdapter(adapter);


                }
            }
            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

        spnCate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String nameCategory = parent.getItemAtPosition(position).toString();
                if(nameCategory.equals("All")){
                    apiProductService.getAllProduct().enqueue(new retrofit2.Callback<List<Product>>() {
                        @Override
                        public void onResponse(retrofit2.Call<List<Product>> call, retrofit2.Response<List<Product>> response) {
                            if(response.isSuccessful()){
                                productModelList.clear();
                                productModelList.addAll(response.body());
                                productAdapter.notifyDataSetChanged();
                            }
                            else{
                                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<List<Product>> call, Throwable t) {
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    int idCategory = categoryList[0].get(position-1).getId();
                    apiProductService.getProductByCategory(idCategory).enqueue(new retrofit2.Callback<List<Product>>() {
                        @Override
                        public void onResponse(retrofit2.Call<List<Product>> call, retrofit2.Response<List<Product>> response) {
                            if(response.isSuccessful()){
                                productModelList.clear();
                                productModelList.addAll(response.body());
                                productAdapter.notifyDataSetChanged();
                            }
                            else{
                                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<List<Product>> call, Throwable t) {
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ImageButton btnAddProduct = view.findViewById(R.id.imageButtonAddProduct);
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.activity_add_product);
                Product product = new Product();
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


                spinnerCategory = dialog.findViewById(R.id.spinnerCategoryProduct);
                apiCategoryService.getCategory().enqueue(new retrofit2.Callback<List<Category>>() {
                    @Override
                    public void onResponse(retrofit2.Call<List<Category>> call, retrofit2.Response<List<Category>> response) {
                        if(response.isSuccessful()){
                            categoryList[0].addAll(response.body());
                            List<String> list = new ArrayList<>();
                            for (Category category : categoryList[0]){
                                list.add(category.getNameCategory());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, list);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerCategory.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<List<Category>> call, Throwable t) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });

                spinnerBrand = dialog.findViewById(R.id.spinnerBrandProduct);
                List<Brand> brandList = new ArrayList<>();
                apiBrandService = RetrofitBrandClient.getRetrofit().create(APIBrandService.class);
                apiBrandService.getAllBrand().enqueue(new Callback<List<Brand>>() {
                    @Override
                    public void onResponse(Call<List<Brand>> call, Response<List<Brand>> response) {
                        if(response.isSuccessful()){
                            brandList.addAll(response.body());
                            List<String> list = new ArrayList<>();
                            for (Brand brand : brandList){
                                list.add(brand.getNameBrand());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, list);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerBrand.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Brand>> call, Throwable t) {

                    }
                });

                Button btnAddProduct = dialog.findViewById(R.id.buttonAddProduct);
                EditText edtNameProduct = dialog.findViewById(R.id.editTextNameProduct);
                EditText edtDescriptionProduct = dialog.findViewById(R.id.editTextDescriptionProduct);
                EditText edtPriceProduct = dialog.findViewById(R.id.editTextPriceProduct);
                EditText edtStockProduct = dialog.findViewById(R.id.editTextQuantityProduct);

                spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int categoryID = categoryList[0].get(position).getId();
                        product.setIdCategory(categoryID);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                spinnerBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int brandID = brandList.get(position).getId();
                        product.setIdBrand(brandID);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }


                });

                btnAddProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = edtNameProduct.getText().toString().trim();
                        String description = edtDescriptionProduct.getText().toString().trim();
                        int price = Integer.parseInt(edtPriceProduct.getText().toString().trim());
                        int stock = Integer.parseInt(edtStockProduct.getText().toString().trim());

                        product.setName(name);
                        product.setDescription(description);
                        product.setPrice(price);
                        product.setStock(stock);

                        List<String> listURLImage = new ArrayList<>();
                        count=0;

                        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                        StorageReference imageRef = storageReference.child("images/");

                        for (Uri uri : imageList){
                            String imageName = UUID.randomUUID().toString();
                            imageRef.child(imageName).putFile(uri).addOnSuccessListener(taskSnapshot -> {
                                imageRef.child(imageName).getDownloadUrl().addOnSuccessListener(uri1 -> {
                                    listURLImage.add(uri1.toString());
                                    count++;
                                    if(count == imageList.size()){
                                        product.setImages(listURLImage);

                                        apiProductService = RetrofitProductClient.getRetrofit().create(APIProductService.class);
                                        apiProductService.postProduct(product).enqueue(new retrofit2.Callback<Product>() {
                                            @Override
                                            public void onResponse(retrofit2.Call<Product> call, retrofit2.Response<Product> response) {
                                                if(response.isSuccessful()){
                                                    Product product = response.body();
                                                    productModelList.add(product);
                                                    productAdapter.notifyDataSetChanged();
                                                    listURLImage.clear();
                                                    imageList.clear();
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
                            });
                        }

                    }
                });

                Button btnCancelAdd = dialog.findViewById(R.id.buttonCancelAddProduct);
                btnCancelAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        return view;
    }

    private void onClickRequestPermission(){
        if (getContext() == null) {
            // Handle the case when the context is null
            return;
        }
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2){
            openGallery();
            return;
        }
        if(checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            openGallery();
        }
        else{
            String [] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission, 1);
        }
    }

    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        mActivityResultLauncher.launch(intent);
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


    public void updateListProduct() {
        apiProductService = RetrofitProductClient.getRetrofit().create(APIProductService.class);
        apiProductService.getAllProduct().enqueue(new retrofit2.Callback<List<Product>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Product>> call, retrofit2.Response<List<Product>> response) {
                if(response.isSuccessful()){
                    productModelList.clear();
                    productModelList.addAll(response.body());
                    productAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Product>> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

        apiCategoryService = RetrofitCategoryClient.getRetrofit().create(APICategoryService.class);
        apiCategoryService.getCategory().enqueue(new retrofit2.Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if(response.isSuccessful()){
                    categoryList.addAll(response.body());
                    List<String> list = new ArrayList<>();
                    for (Category category : categoryList){
                        list.add(category.getNameCategory());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnCate.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

        apiCategoryService = RetrofitCategoryClient.getRetrofit().create(APICategoryService.class);
        apiCategoryService.getCategory().enqueue(new retrofit2.Callback<List<Category>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Category>> call, retrofit2.Response<List<Category>> response) {
                if(response.isSuccessful()){
                    categoryList.clear();
                    categoryList.addAll(response.body());
                    List<String> list = new ArrayList<>();
                    for (Category category : categoryList){
                        list.add(category.getNameCategory());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategory.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Category>> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

        apiBrandService = RetrofitBrandClient.getRetrofit().create(APIBrandService.class);
        apiBrandService.getAllBrand().enqueue(new Callback<List<Brand>>() {
            @Override
            public void onResponse(Call<List<Brand>> call, Response<List<Brand>> response) {
                if(response.isSuccessful()){
                    List<Brand> brandList = response.body();
                    List<String> list = new ArrayList<>();
                    for (Brand brand : brandList){
                        list.add(brand.getNameBrand());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerBrand.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Brand>> call, Throwable t) {

            }
        });

    }
}