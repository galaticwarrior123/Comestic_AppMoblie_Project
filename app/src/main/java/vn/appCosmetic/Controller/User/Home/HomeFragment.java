package vn.appCosmetic.Controller.User.Home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.appCosmetic.Model.Category;
import vn.appCosmetic.Model.Product;
import vn.appCosmetic.R;
import vn.appCosmetic.ServiceAPI.Category.APICategoryService;
import vn.appCosmetic.ServiceAPI.Product.APIProductService;
import vn.appCosmetic.ServiceAPI.RetrofitClient;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserProductAdapter productAdapter;
    private SearchView searchView;
    private Spinner categorySpinner;
    private List<Category> categories = new ArrayList<>(); // List to store categories
    private APICategoryService apiCategoryService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        searchView = view.findViewById(R.id.search_view);
        categorySpinner = view.findViewById(R.id.category_spinner);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        setupSearchView();

        apiCategoryService = RetrofitClient.getRetrofit().create(APICategoryService.class);
        loadCategories();

        LoadProducts("", 0);
        return view;
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                LoadProducts(query, categories.get(categorySpinner.getSelectedItemPosition()).getId());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                LoadProducts(newText, categories.get(categorySpinner.getSelectedItemPosition()).getId());
                return false;
            }
        });
    }

    private void loadCategories() {
        apiCategoryService.getCategory().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categories.clear();
                    categories.add(new Category(0, "All")); // Add a category "All" with id 0
                    categories.addAll(response.body());
                    CategoryAdapter adapter = new CategoryAdapter(getContext(), android.R.layout.simple_spinner_item, categories);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categorySpinner.setAdapter(adapter);
                    setupCategorySpinner();
                } else {
                    Toast.makeText(getContext(), "Failed to retrieve categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(getContext(), "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupCategorySpinner() {
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoadProducts(searchView.getQuery().toString(), categories.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void LoadProducts(String query, int categoryId) {
        APIProductService apiService = RetrofitClient.getRetrofit().create(APIProductService.class);
        Call<List<Product>> call;
        if (categoryId == 0) {
            call = apiService.getAllProduct();
        } else {
            call = apiService.getProductByCategory(categoryId);
        }
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> filteredList = new ArrayList<>();
                    for (Product product : response.body()) {
                        boolean matchesName = product.getName().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT));
                        if (matchesName) {
                            filteredList.add(product);
                        }
                    }
                    productAdapter = new UserProductAdapter(getContext(), filteredList);
                    recyclerView.setAdapter(productAdapter);
                    productAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Failed to retrieve products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getContext(), "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Custom Adapter to display category names
    private class CategoryAdapter extends ArrayAdapter<Category> {
        private LayoutInflater inflater;
        private List<Category> categories;

        public CategoryAdapter(Context context, int resource, List<Category> categories) {
            super(context, resource, categories);
            inflater = LayoutInflater.from(context);
            this.categories = categories;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
            }

            // Set category name
            TextView textView = convertView.findViewById(android.R.id.text1);
            textView.setText(categories.get(position).getNameCategory());

            return convertView;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
            }

            // Set category name
            TextView textView = convertView.findViewById(android.R.id.text1);
            textView.setText(categories.get(position).getNameCategory());

            return convertView;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        disableBackButton();
    }

    private void disableBackButton() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
            }
        });
    }
}
