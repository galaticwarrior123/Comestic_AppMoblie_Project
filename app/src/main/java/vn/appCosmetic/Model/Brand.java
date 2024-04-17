package vn.appCosmetic.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Brand implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String nameBrand;

    public Brand(int id, String nameBrand) {
        this.id = id;
        this.nameBrand = nameBrand;
    }

    public Brand() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameBrand() {
        return nameBrand;
    }

    public void setNameBrand(String nameBrand) {
        this.nameBrand = nameBrand;
    }
}
