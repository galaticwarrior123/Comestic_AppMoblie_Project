package vn.appCosmetic.Controller.Admin.ManageProduct;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.appCosmetic.Model.Product;
import vn.appCosmetic.R;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{

    private Context context;
    private List<Product> productModelList;

    private LayoutInflater inflater;

    private AdapterView.OnItemClickListener onItemClickListener;



    public ProductAdapter(Context context, List<Product> productModelList) {
        this.context = context;
        this.productModelList = productModelList;
        inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_item_manage_product_page, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product productModel = productModelList.get(position);
        holder.txtNameProduct.setText(productModel.getName());
        holder.imgProduct.setImageResource(R.drawable.user);
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUpdateProduct();
            }
        });

    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtNameProduct;
        private ImageView imgProduct;
        private ImageButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameProduct = itemView.findViewById(R.id.textViewProductName);
            imgProduct = itemView.findViewById(R.id.imageViewProduct);
            btnEdit = itemView.findViewById(R.id.imageButtonEditProduct);
            btnDelete = itemView.findViewById(R.id.imageButtonDeleteProduct);

        }
    }

    private void DialogUpdateProduct() {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_edit_product);
        dialog.show();
    }



}
