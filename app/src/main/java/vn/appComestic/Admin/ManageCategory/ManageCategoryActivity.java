package vn.appComestic.Admin.ManageCategory;

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

import vn.appComestic.R;

public class ManageCategoryActivity extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.activity_manage_category, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rcViewManageCategory);
        List <Category> categoryList = new ArrayList<>();
        categoryList.add(new Category(1, "Kem dưỡng da"));
        categoryList.add(new Category(2, "Kem chống nắng"));
        categoryList.add(new Category(3, "Kem trị mụn"));
        categoryList.add(new Category(4, "Kem dưỡng ẩm"));
        categoryList.add(new Category(5, "Kem chống lão hóa"));
        categoryList.add(new Category(6, "Kem trắng da"));
        CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), categoryList);
        recyclerView.setAdapter(categoryAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        ImageButton btnAddCategory = view.findViewById(R.id.imageButtonAddCategory);
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.activity_add_category);
                dialog.show();
            }
        });
        return view;
    }

}