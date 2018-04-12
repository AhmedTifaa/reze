package com.example.ahmed.reze1.api.post;

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
}