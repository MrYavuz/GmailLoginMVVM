package com.example.gmailloginmvvm.model;

import androidx.annotation.Nullable;


/**
 * Authentication result : success (user details) or error message.
 */
public class LoginResult {
    @Nullable
    private UserModel success;
    @Nullable
    private Integer error;

    public LoginResult(@Nullable Integer error) {
        this.error = error;
    }

    public LoginResult(@Nullable UserModel success) {
        this.success = success;
    }

    @Nullable
    public UserModel getSuccess() {
        return success;
    }

    @Nullable
    public Integer getError() {
        return error;
    }
}