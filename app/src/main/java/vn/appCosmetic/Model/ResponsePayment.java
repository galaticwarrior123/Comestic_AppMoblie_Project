package vn.appCosmetic.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponsePayment implements Serializable {
    @SerializedName("id")
    private int id;

    @SerializedName("price")
    private float price;

    @SerializedName("nameUrl")
    private String url;

    public ResponsePayment(int id, float price, String url) {
        this.id = id;
        this.price = price;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public float getPrice() {
        return price;
    }

    public String getUrl() {
        return url;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
