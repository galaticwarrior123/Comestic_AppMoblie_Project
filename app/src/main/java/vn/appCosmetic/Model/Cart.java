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

    @SerializedName("status")
    private boolean status;

    @SerializedName("user")
    private Users user;


    public Cart() {
    }

    public Cart(int id, List<Product> product, int quantity, float total, boolean status, Users user) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.total = total;
        this.status = status;
        this.user = user;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
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
