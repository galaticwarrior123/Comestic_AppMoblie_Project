package vn.appCosmetic.Model;

import java.io.Serializable;

public class Image implements Serializable {

    private int id;
    private String url;

    public Image() {
    }

    public Image(int id, String url) {
        this.id = id;
        this.url = url;
    }

    public Image(String string) {
        this.url = string;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
