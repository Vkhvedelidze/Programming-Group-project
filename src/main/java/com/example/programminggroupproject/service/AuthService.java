package com.example.programminggroupproject.service;

import com.example.programminggroupproject.model.User;
import java.util.Optional;

public class AuthService {

    public static User authenticate(String username, String password) {
        Optional<User> user = DataService.getInstance().authenticate(username, password);
        return user.orElse(null);
    }

    public static boolean register(String username, String password, String fullName, String role) {
        // Basic validation
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }

        // Default email for now
        String email = username + "@example.com";

        User newUser = new User(username, password, role, fullName, email);
        return DataService.getInstance().registerUser(newUser);
    }
}