package vn.appComestic.Admin.ManageProduct;

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

import vn.appComestic.R;

public class ManageProductActivity extends Fragment{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.activity_manage_product, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rcViewManageProduct);
        List<ProductModel> productModelList =new ArrayList<>();
        productModelList.add(new ProductModel("1","Iphone 12","abcd"));
        productModelList.add(new ProductModel("2","Iphone 11","abcd"));
        productModelList.add(new ProductModel("3","Iphone 10","abcd"));
        productModelList.add(new ProductModel("4","Iphone 9","abcd"));
        productModelList.add(new ProductModel("5","Iphone 8","abcd"));
        productModelList.add(new ProductModel("6","Iphone 7","abcd"));
        productModelList.add(new ProductModel("7","Iphone 6","abcd"));
        productModelList.add(new ProductModel("8","Iphone 5","abcd"));
        productModelList.add(new ProductModel("9","Iphone 4","abcd"));
        productModelList.add(new ProductModel("10","Iphone 3","abcd"));
        ProductAdapter productAdapter = new ProductAdapter(getContext(), productModelList);
        recyclerView.setAdapter(productAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        String[] data = {"Iphone 12","Iphone 11","Iphone 10","Iphone 9","Iphone 8","Iphone 7","Iphone 6","Iphone 5","Iphone 4","Iphone 3"};
        Spinner spinner = view.findViewById(R.id.spinnerManageProduct);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), data[position], Toast.LENGTH_SHORT).show();
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
                dialog.show();
            }
        });





        return view;
    }



}