package com.example.programminggroupproject.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * User model matching the Supabase users table schema.
 * Schema: id (UUID), email, password_hash, full_name, role, shop_id, created_at
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    @JsonProperty("id")
    private UUID id;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("password_hash")
    private String passwordHash;
    
    @JsonProperty("full_name")
    private String fullName;
    
    @JsonProperty("role")
    private String role;
    
    @JsonProperty("shop_id")
    private Long shopId;
    
    @JsonProperty("created_at")
    private OffsetDateTime createdAt;

    // Default constructor for Jackson
    public User() {
    }

    // Constructor for creating new users (without ID and created_at)
    public User(String email, String passwordHash, String fullName, String role, Long shopId) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.role = role;
        this.shopId = shopId;
    }
    
    // Full constructor
    public User(UUID id, String email, String passwordHash, String fullName, String role, Long shopId, OffsetDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.role = role;
        this.shopId = shopId;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role='" + role + '\'' +
                ", shopId=" + shopId +
                ", createdAt=" + createdAt +
                '}';
    }
}