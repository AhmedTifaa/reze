package com.example.ahmed.reze1;

/**
 * Created by tifaa on 15/04/18.
 */

public class buildFriend {
    private String name;
    private String id;

    public buildFriend(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
