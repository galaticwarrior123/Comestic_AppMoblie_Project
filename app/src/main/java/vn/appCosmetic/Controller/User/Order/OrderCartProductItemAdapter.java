package vn.appCosmetic.Controller.User.Order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Locale;

import vn.appCosmetic.Model.CartProduct;
import vn.appCosmetic.R;

public class OrderCartProductItemAdapter extends RecyclerView.Adapter<OrderCartProductItemAdapter.OrderItemViewHolder> {
    private Context context;
    private List<CartProduct> cartProductList;
    private LayoutInflater layoutInflater;

    public OrderCartProductItemAdapter(Context context, List<CartProduct> cartProductList) {
        this.context = context;
        this.cartProductList = cartProductList;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_cart_product_order, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        CartProduct cartProduct = cartProductList.get(position);
        holder.txtOrderProductName.setText("Tên sản phẩm: "+cartProduct.getProduct().getName());
        holder.txtOrderProductQuantity.setText("Số lượng: "+ String.valueOf(cartProduct.getQuantity()));
        String price = String.format(Locale.getDefault(), "%,d", cartProduct.getProduct().getPrice());
        holder.txtProductPrice.setText("Giá: "+price+ " VND");
        String totalPrice = String.format(Locale.getDefault(), "%,d", cartProduct.getQuantity() * cartProduct.getProduct().getPrice());
        holder.txtOrderProductTotalPrice.setText("Tổng cộng: "+totalPrice+ " VND");
        if(cartProduct.getProduct().getImages().size() > 0) {
            Glide.with(context).load(cartProduct.getProduct().getImages().get(0)).into(holder.imgOrderProduct);
        }
    }

    @Override
    public int getItemCount() {
        return cartProductList.size();
    }

    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imgOrderProduct;
        TextView txtOrderProductName, txtOrderProductQuantity,txtProductPrice, txtOrderProductTotalPrice;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imgOrderProduct = itemView.findViewById(R.id.imgCartProductOrder);
            txtOrderProductName = itemView.findViewById(R.id.txtNameCartProductOrder);
            txtOrderProductQuantity = itemView.findViewById(R.id.txtQuantityCartProductOrder);
            txtProductPrice= itemView.findViewById(R.id.txtPriceProduct);
            txtOrderProductTotalPrice = itemView.findViewById(R.id.txtTotalPriceCartProductOrder);
        }
    }
}
