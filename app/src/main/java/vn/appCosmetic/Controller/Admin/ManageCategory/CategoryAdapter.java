package vn.appCosmetic.Controller.Admin.ManageCategory;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.appCosmetic.Model.Category;
import vn.appCosmetic.R;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{
    private Context context;
    private List<Category> categoryList;
    private LayoutInflater inflater;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
        inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_item_manage_category_page, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.txtNameCategory.setText(category.getNameCategory());
        holder.btnEditCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogEdit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        private TextView txtNameCategory;
        private ImageButton btnEditCategory, btnDeleteCategory;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameCategory = itemView.findViewById(R.id.textViewNameCategory);
            btnEditCategory = itemView.findViewById(R.id.imageButtonEditCategory);
            btnDeleteCategory = itemView.findViewById(R.id.imageButtonDeleteCategory);
        }
    }

    private void DialogEdit(){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_edit_category);
        dialog.show();
    }
}
