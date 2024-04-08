package vn.appComestic.Admin.ManageBrand;

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

import vn.appComestic.R;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.ViewHolder> {
    private Context context;
    private List<Brand> brandList;

    public BrandAdapter(Context context, List<Brand> brandList) {
        this.context = context;
        this.brandList = brandList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_manage_brand_page, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Brand brand = brandList.get(position);
        holder.tvNameBrand.setText(brand.getNameBrand());
        holder.btnEditBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogEdit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNameBrand;
        private ImageButton btnEditBrand, btnDeleteBrand;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameBrand = itemView.findViewById(R.id.textViewNameBrand);
            btnEditBrand = itemView.findViewById(R.id.imageButtonEditBrand);
            btnDeleteBrand = itemView.findViewById(R.id.imageButtonDeleteBrand);
        }

    }
    private void DialogEdit() {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_edit_brand);
        dialog.show();
    }
}
