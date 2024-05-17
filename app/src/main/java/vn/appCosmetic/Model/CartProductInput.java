package vn.appCosmetic.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CartProductInput implements Serializable {

    @SerializedName("quantity")
    private int quantity;
    @SerializedName("productId")
    private int idProduct;

    @SerializedName("cartId")
    private int idCart;

    public CartProductInput() {
    }

    public CartProductInput( int quantity, int idProduct, int idCart) {
        this.quantity = quantity;
        this.idProduct = idProduct;
        this.idCart = idCart;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getIdCart() {
        return idCart;
    }

    public void setIdCart(int idCart) {
        this.idCart = idCart;
    }
}
