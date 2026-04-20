package com.cefii.learning.learning_tracker.security;

// AuthRequest is a simple class used to encapsulate the connection's informations
public class AuthRequest {
    private String username;
    private String password;

    // Getters et Setters...
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
