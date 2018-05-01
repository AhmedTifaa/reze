package com.example.ahmed.reze1.api.post;

import com.example.ahmed.reze1.api.product.ProductResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mona Abdallh on 4/12/2018.
 */

public class ApiResponse {

    @SerializedName("next_cursor")
    @Expose
    private int nextCursor;

    @SerializedName("posts")
    @Expose
    private PostResponse[] posts;

    @SerializedName("products")
    @Expose
    private ProductResponse[] products;

    @SerializedName("now")
    @Expose
    private long now;

    public ProductResponse[] getProducts() {
        return products;
    }

    public void setProducts(ProductResponse[] products) {
        this.products = products;
    }

    public int getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(int nextCursor) {
        this.nextCursor = nextCursor;
    }

    public PostResponse[] getPosts() {
        return posts;
    }

    public void setPosts(PostResponse[] posts) {
        this.posts = posts;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }
}
