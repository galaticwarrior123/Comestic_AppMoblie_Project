package vn.appComestic.Admin.ManageProduct;

import java.io.Serializable;

public class ProductModel implements Serializable {
    private String id;
    private String name;
    private String image;

    public ProductModel(String id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public ProductModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
