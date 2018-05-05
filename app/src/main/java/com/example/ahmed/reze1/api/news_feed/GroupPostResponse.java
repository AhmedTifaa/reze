package com.example.ahmed.reze1.api.news_feed;

import com.example.ahmed.reze1.api.post.PostResponse;
import com.example.ahmed.reze1.api.product.ProductResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mona Abdallh on 5/5/2018.
 */

public class GroupPostResponse {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("posts")
    @Expose
    private PostResponse[] posts;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PostResponse[] getPosts() {
        return posts;
    }

    public void setPosts(PostResponse[] posts) {
        this.posts = posts;
    }
}
