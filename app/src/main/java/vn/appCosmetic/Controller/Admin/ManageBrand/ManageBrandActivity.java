package vn.appCosmetic.Controller.Admin.ManageBrand;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.appCosmetic.Model.Brand;
import vn.appCosmetic.R;
import vn.appCosmetic.ServiceAPI.Brand.APIBrandService;
import vn.appCosmetic.ServiceAPI.Brand.RetrofitBrandClient;

public class ManageBrandActivity extends Fragment {
    private APIBrandService apiBrandService;
    private RecyclerView recyclerView;
    private BrandAdapter brandAdapter;
    private List<Brand> brandList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.activity_manage_brand, container, false);
        recyclerView = view.findViewById(R.id.rcViewManageBrand);
        apiBrandService = RetrofitBrandClient.getRetrofit().create(APIBrandService.class);
        apiBrandService.getAllBrand().enqueue(new Callback<List<Brand>>() {
            @Override
            public void onResponse(Call<List<Brand>> call, Response<List<Brand>> response) {
                if(response.isSuccessful()){
                    brandList = response.body();
                    brandAdapter = new BrandAdapter(getContext(), brandList);
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(brandAdapter);
                    brandAdapter.notifyDataSetChanged();
                }
                else{
                    int statusCode = response.code();
                    Log.e("Error", String.valueOf(statusCode));
                }
            }

            @Override
            public void onFailure(Call<List<Brand>> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });

        ImageButton btnAddBrand = view.findViewById(R.id.imageButtonAddBrand);
        btnAddBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.activity_add_brand);
                EditText edtNameBrand = dialog.findViewById(R.id.editTextNameBrand);
                Button btnAdd, btnCancel;
                btnAdd = dialog.findViewById(R.id.buttonAddBrand);
                btnCancel = dialog.findViewById(R.id.buttonCancelAddBrand);
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nameBrand = edtNameBrand.getText().toString().trim();
                        Brand newBrand = new Brand();
                        newBrand.setNameBrand(nameBrand);
                        apiBrandService.postBrand(newBrand).enqueue(new Callback<Brand>() {
                            @Override
                            public void onResponse(Call<Brand> call, Response<Brand> response) {
                                if(response.isSuccessful()){
                                    Brand brand = response.body();
                                    brandList.add(brand);
                                    brandAdapter.notifyDataSetChanged();
                                    dialog.dismiss();
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
        });
        return view;
    }
}
