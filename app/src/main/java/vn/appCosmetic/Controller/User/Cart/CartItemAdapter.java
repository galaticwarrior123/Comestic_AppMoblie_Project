package vn.appCosmetic.Controller.User.Cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import vn.appCosmetic.Model.Product;
import vn.appCosmetic.R;
import vn.appCosmetic.Model.CartProduct;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {
    private Context context;
    private List<CartProduct> cartProductList;
    public CartItemAdapter(Context context, List<CartProduct> cartProductList) {
        this.context = context;
        this.cartProductList = cartProductList;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_cart_product, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {

        CartProduct cartProduct = cartProductList.get(position);
        Product product = cartProduct.getProduct();
        holder.txtCartProductName.setText(product.getName());
        holder.txtCartProductQuantity.setText(String.valueOf(cartProduct.getQuantity()));
        holder.txtCartProductTotalPrice.setText(String.valueOf(cartProduct.getQuantity() * product.getPrice()));
        if(product.getImages().size() > 0) {
            Glide.with(context).load(product.getImages().get(0)).into(holder.imgCartProduct);
        }
    }

    @Override
    public int getItemCount() {
        return cartProductList.size();
    }

    public static class CartItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCartProduct;
        TextView txtCartProductName, txtCartProductQuantity, txtCartProductTotalPrice;
        ImageButton imgCartProductEdit, imgCartProductDelete;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCartProduct = itemView.findViewById(R.id.imgCartProduct);
            txtCartProductName = itemView.findViewById(R.id.txtCartProductName);
            txtCartProductQuantity = itemView.findViewById(R.id.txtCartProductQuantity);
            txtCartProductTotalPrice = itemView.findViewById(R.id.txtCartProductTotalPrice);
            imgCartProductEdit = itemView.findViewById(R.id.imgCartProductEdit);
            imgCartProductDelete = itemView.findViewById(R.id.imgCartProductDelete);
        }
    }
}