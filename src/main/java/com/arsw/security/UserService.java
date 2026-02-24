package com.arsw.security;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {
    private final Map<String, User> usersByUsername = new ConcurrentHashMap<>();
    private final Map<String, User> usersByApiKey = new ConcurrentHashMap<>();
    
    public User createUser(String username, String email) {
        if (usersByUsername.containsKey(username)) {
            throw new RuntimeException("Username already exists: " + username);
        }
        
        User user = new User(username, email);
        usersByUsername.put(username, user);
        usersByApiKey.put(user.getApiKey(), user);
        
        return user;
    }
    
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(usersByUsername.get(username));
    }
    
    public Optional<User> findByApiKey(String apiKey) {
        return Optional.ofNullable(usersByApiKey.get(apiKey));
    }
    
    public boolean validateApiKey(String apiKey) {
        return usersByApiKey.containsKey(apiKey);
    }
    
    public List<User> getAllUsers() {
        return new ArrayList<>(usersByUsername.values());
    }
    
    public boolean deleteUser(String username) {
        User user = usersByUsername.remove(username);
        if (user != null) {
            usersByApiKey.remove(user.getApiKey());
            return true;
        }
        return false;
    }
}
