package com.example.ahmed.reze1.api.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mona Abdallh on 4/16/2018.
 */

public class SearchUserResponse {

    @SerializedName("userId")
    @Expose
    private int userId;

    @SerializedName("username")
    @Expose
    private String username;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
