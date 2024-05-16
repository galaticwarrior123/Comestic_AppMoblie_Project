package vn.appCosmetic.Controller.User.Home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import vn.appCosmetic.Controller.User.Home.ProductDetail.ProductDetailActivity;
import vn.appCosmetic.Model.Product;
import vn.appCosmetic.R;

public class UserProductAdapter extends RecyclerView.Adapter<UserProductAdapter.ProductViewHolder>{
    private Context context;
    private List<Product> productList;

    public UserProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_product_user_page, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.format("%d VND", product.getPrice()));

        if (product.getImages() != null && !product.getImages().isEmpty()) {
            Glide.with(context).load(product.getImages().get(0)).into(holder.productImage);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("PRODUCT_ID", product.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_user_page_image);
            productName = itemView.findViewById(R.id.product_user_page_name);
            productPrice = itemView.findViewById(R.id.product_user_page_price);
        }
    }
}