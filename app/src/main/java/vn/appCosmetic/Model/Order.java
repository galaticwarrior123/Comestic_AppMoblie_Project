package vn.appCosmetic.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Order implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("address")
    private String address;

    @SerializedName("orderDate")
    private String orderDate;

    @SerializedName("status")
    private boolean status;

    @SerializedName("total")
    private Long total;

    @SerializedName("phone")
    private String phone;

    @SerializedName("cart")
    private Cart cart;

    public Order() {
    }

    public Order(int id, String address, String orderDate, boolean status, Long total, String phone, Cart cart) {
        this.id = id;
        this.address = address;
        this.orderDate = orderDate;
        this.status = status;
        this.total = total;
        this.phone = phone;
        this.cart = cart;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}
