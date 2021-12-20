package com.example.gmailloginmvvm.model;

public class UserModel {
    private int id;

    private String displayName;

    private String token;

    public UserModel(String displayName, String token) {
        this.displayName = displayName;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getToken() {
        return token;
    }

}
