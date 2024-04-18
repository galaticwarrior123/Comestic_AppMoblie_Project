package vn.appCosmetic.Controller.Admin.ManageProduct;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;

import vn.appCosmetic.Model.Product;
import vn.appCosmetic.R;
import vn.appCosmetic.Utils.ImageUtils;

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
        // lay ảnh từ dũ liệu firebase về và hiển thị
        if(productModel.getImages() != null && productModel.getImages().size() > 0){
            String url = productModel.getImages().get(0);
            System.out.println("URL: " + url);
            Uri uri = Uri.parse(url);
            System.out.println("URI: " + uri);
            holder.imgProduct.setImageURI(uri);
        }
        else{
            holder.imgProduct.setImageResource(R.drawable.ic_launcher_background);
        }


        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUpdateProduct();
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // xóa sản phẩm
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
