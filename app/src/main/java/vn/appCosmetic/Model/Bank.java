package vn.appCosmetic.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Bank implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("code")
    private String code;
    @SerializedName("bin")
    private String bin;

    @SerializedName("shortName")
    private String shortName;

    @SerializedName("transferSupported")
    private int transferSupported;

    @SerializedName("lookupSupported")
    private int lookupSupported;

    @SerializedName("short_name")
    private String short_name;

    @SerializedName("support")
    private int support;

    @SerializedName("isTransfer")
    private int isTransfer;

    @SerializedName("swift_code")
    private String swift_code;

    @SerializedName("logo")
    private String logo;


    public Bank() {
    }

    public Bank(int id, String name, String code, String bin, String shortName, int transferSupported, int lookupSupported, String short_name, int support, int isTransfer, String swift_code, String logo) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.bin = bin;
        this.shortName = shortName;
        this.transferSupported = transferSupported;
        this.lookupSupported = lookupSupported;
        this.short_name = short_name;
        this.support = support;
        this.isTransfer = isTransfer;
        this.swift_code = swift_code;
        this.logo = logo;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getTransferSupported() {
        return transferSupported;
    }

    public void setTransferSupported(int transferSupported) {
        this.transferSupported = transferSupported;
    }

    public int getLookupSupported() {
        return lookupSupported;
    }

    public void setLookupSupported(int lookupSupported) {
        this.lookupSupported = lookupSupported;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public int getSupport() {
        return support;
    }

    public void setSupport(int support) {
        this.support = support;
    }

    public int getIsTransfer() {
        return isTransfer;
    }

    public void setIsTransfer(int isTransfer) {
        this.isTransfer = isTransfer;
    }

    public String getSwift_code() {
        return swift_code;
    }

    public void setSwift_code(String swift_code) {
        this.swift_code = swift_code;
    }
}
