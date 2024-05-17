package vn.appCosmetic.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Payment implements Serializable {
    @SerializedName("name")
    private String name;
    @SerializedName("amount")
    private Long amount;

    public Payment() {
    }

    public Payment(String name, Long amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

}
