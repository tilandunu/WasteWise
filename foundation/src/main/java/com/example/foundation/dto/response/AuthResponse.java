package com.example.foundation.dto.response;

import com.example.foundation.model.User;

public class AuthResponse {
    private String message;
    private User user;

    public AuthResponse(String message, User user) {
        this.message = message;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}
