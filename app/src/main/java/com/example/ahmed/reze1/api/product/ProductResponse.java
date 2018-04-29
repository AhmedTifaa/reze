package com.example.ahmed.reze1.api.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Mona Abdallh on 4/29/2018.
 */

public class ProductResponse implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("vendor_id")
    @Expose
    private int vendorId;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("price")
    @Expose
    private float price;

    @SerializedName("amount")
    @Expose
    private int amount;

    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("sale")
    @Expose
    private int sale;

    @SerializedName("sold_amount")
    @Expose
    private int soldAmount;

    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSoldAmount() {
        return soldAmount;
    }

    public void setSoldAmount(int soldAmount) {
        this.soldAmount = soldAmount;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
