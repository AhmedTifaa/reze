package com.example.ahmed.reze1.api.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mona Abdallh on 4/16/2018.
 */

public class SearchResponse {

    @SerializedName("users")
    @Expose
    private SearchUserResponse[] users;

    @SerializedName("groups")
    @Expose
    private SearchGroupsResponse[] groups;

    public SearchUserResponse[] getUsers() {
        return users;
    }

    public void setUsers(SearchUserResponse[] users) {
        this.users = users;
    }

    public SearchGroupsResponse[] getGroups() {
        return groups;
    }

    public void setGroups(SearchGroupsResponse[] groups) {
        this.groups = groups;
    }
}
