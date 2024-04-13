package vn.appCosmetic.Controller.Admin.ManageProduct;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.appCosmetic.Model.Category;
import vn.appCosmetic.Model.Product;
import vn.appCosmetic.R;
import vn.appCosmetic.ServiceAPI.Category.APICategoryService;
import vn.appCosmetic.ServiceAPI.Product.APIProductService;
import vn.appCosmetic.ServiceAPI.Product.RetrofitProductClient;

public class ManageProductActivity extends Fragment{
    private APIProductService apiProductService;
    private APICategoryService apiCategoryService;

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
        apiCategoryService = RetrofitProductClient.getRetrofit().create(APICategoryService.class);
        apiCategoryService.getCategory().enqueue(new retrofit2.Callback<List<Category>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Category>> call, retrofit2.Response<List<Category>> response) {
                if(response.isSuccessful()){
                    List<Category> categoryList = response.body();
                    List<String> listNameCategory = new ArrayList<>();
                    for (Category category : categoryList){
                        listNameCategory.add(category.getNameCategory());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, listNameCategory);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            apiProductService.getProductByCategory(position).enqueue(new retrofit2.Callback<List<Product>>() {
                                @Override
                                public void onResponse(retrofit2.Call<List<Product>> call, retrofit2.Response<List<Product>> response) {
                                    if(response.isSuccessful()){
                                        productModelList.clear();
                                        productModelList.addAll(response.body());
                                        productAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onFailure(retrofit2.Call<List<Product>> call, Throwable t) {
                                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
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
                dialog.show();
            }
        });





        return view;
    }



}