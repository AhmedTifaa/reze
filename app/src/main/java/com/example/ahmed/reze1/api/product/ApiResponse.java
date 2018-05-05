package com.example.ahmed.reze1.api.product;

import com.example.ahmed.reze1.api.news_feed.EventResponse;
import com.example.ahmed.reze1.api.news_feed.GroupPostResponse;
import com.example.ahmed.reze1.api.post.PostResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
