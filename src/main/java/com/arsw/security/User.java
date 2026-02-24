package com.arsw.security;

import java.util.UUID;

public class User {
    private Long id;
    private String username;
    private String email;
    private String apiKey;
    
    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.apiKey = UUID.randomUUID().toString();
        this.id = System.currentTimeMillis(); // Simple ID generation
    }
    
    // Getters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getApiKey() { return apiKey; }
    
    // Setters
    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
}
