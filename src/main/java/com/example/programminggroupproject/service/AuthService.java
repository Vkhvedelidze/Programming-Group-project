package com.example.programminggroupproject.service;

import com.example.programminggroupproject.model.User;
import java.util.Optional;

/**
 * Authentication service using Supabase backend.
 * Now uses email-based authentication instead of username.
 */
public class AuthService {

    /**
     * Authenticate a user with email and password
     * @param email The user's email
     * @param password The password (plaintext - should be hashed in production)
     * @return User object if authentication succeeds, null otherwise
     */
    public static User authenticate(String email, String password) {
        Optional<User> user = UserService.getInstance().authenticate(email, password);
        return user.orElse(null);
    }

    /**
     * Register a new user
     * @param email The user's email
     * @param password The password (should be hashed before storing)
     * @param fullName The user's full name
     * @param role The user's role (client, mechanic, admin)
     * @param shopId Optional shop ID (null if not associated with a shop)
     * @return true if registration succeeds, false otherwise
     */
    public static boolean register(String email, String password, String fullName, String role, Long shopId) {
        // Basic validation
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }

        try {
            // TODO: Hash password before storing (use BCrypt in production)
            User newUser = new User(email, password, fullName, role, shopId);
            UserService.getInstance().registerUser(newUser);
            return true;
        } catch (IllegalArgumentException e) {
            // Email already exists
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Register a new user without shop association
     * @param email The user's email
     * @param password The password
     * @param fullName The user's full name
     * @param role The user's role
     * @return true if registration succeeds, false otherwise
     */
    public static boolean register(String email, String password, String fullName, String role) {
        return register(email, password, fullName, role, null);
    }
}