package com.example.ahmed.reze1.api.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mona Abdallh on 4/29/2018.
 */

public class ApiResponse {

    @SerializedName("products")
    @Expose
    private ProductResponse[] products;

    public ProductResponse[] getProducts() {
        return products;
    }

    public void setProducts(ProductResponse[] products) {
        this.products = products;
    }
}
