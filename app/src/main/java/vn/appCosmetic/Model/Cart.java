package vn.appCosmetic.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Cart implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("product")
    private List<Product> product;

    @SerializedName("name")
    private String name;


    @SerializedName("quantity")
    private int quantity;

    @SerializedName("total")
    private float total;

    @SerializedName("status")
    private boolean status;

    @SerializedName("paid")
    private boolean paid;

    @SerializedName("user")
    private Users user;


    public Cart() {
    }

    public Cart(int id, List<Product> product, String name, int quantity, float total, boolean status, boolean paid, Users user) {
        this.id = id;
        this.product = product;
        this.name = name;
        this.quantity = quantity;
        this.total = total;
        this.status = status;
        this.paid = paid;
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

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
