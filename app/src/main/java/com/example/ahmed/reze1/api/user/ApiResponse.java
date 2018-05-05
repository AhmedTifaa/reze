package com.example.ahmed.reze1.api.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by amr on 4/19/2018.
 */

public class ApiResponse{

    @SerializedName("users")
    @Expose
    private ArrayList<UserResponse>users;

    public ArrayList<UserResponse> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<UserResponse> users) {
        this.users = users;
    }
}
