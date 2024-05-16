package vn.appCosmetic.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Cart implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("product")
    private List<Product> product;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("total")
    private float total;

    public Cart() {
    }

    public Cart(int id, List<Product> product, int quantity, float total) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
}
