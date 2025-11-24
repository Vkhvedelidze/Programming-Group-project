package com.example.programminggroupproject.service;


public class AuthService {

    public static boolean authenticate(String username, String password) {
        if (username.equals("client") && password.equals("client123")) {
            return true;
        }
        if (username.equals("mechanic") && password.equals("mechanic123")) {
            return true;
        }
        if (username.equals("admin") && password.equals("admin123")) {
            return true;
        }
        return false;
    }
}

