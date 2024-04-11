package vn.appCosmetic.Controller.Admin.ManageBrand;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.appCosmetic.Model.Brand;
import vn.appCosmetic.R;

public class ManageBrandActivity extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.activity_manage_brand, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rcViewManageBrand);
        List<Brand> brandList = new ArrayList<>();
        brandList.add(new Brand(1, "Apple"));
        brandList.add(new Brand(2, "Samsung"));
        brandList.add(new Brand(3, "Xiaomi"));
        brandList.add(new Brand(4, "Oppo"));
        brandList.add(new Brand(5, "Vivo"));
        brandList.add(new Brand(6, "Nokia"));
        brandList.add(new Brand(7, "Sony"));
        brandList.add(new Brand(8, "LG"));
        brandList.add(new Brand(9, "HTC"));
        brandList.add(new Brand(10, "Huawei"));
        BrandAdapter brandAdapter = new BrandAdapter(getContext(), brandList);
        recyclerView.setAdapter(brandAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        ImageButton btnAddBrand = view.findViewById(R.id.imageButtonAddBrand);
        btnAddBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.activity_add_brand);
                dialog.show();
            }
        });
        return view;
    }
}
