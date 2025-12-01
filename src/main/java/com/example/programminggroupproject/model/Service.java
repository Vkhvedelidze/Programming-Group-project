package com.example.programminggroupproject.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Service model matching the Supabase services table schema.
 * Schema: id (UUID), name, description, base_price, created_at
 * 
 * Note: This is different from the Service<T> interface in the service package.
 * This represents a service offered by the mechanic shop (e.g., "Oil Change", "Brake Repair").
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Service {
    
    @JsonProperty("id")
    private UUID id;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("base_price")
    private BigDecimal basePrice;
    
    @JsonProperty("created_at")
    private OffsetDateTime createdAt;

    // Default constructor for Jackson
    public Service() {
    }

    // Constructor for creating new services (without ID and created_at)
    public Service(String name, String description, BigDecimal basePrice) {
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
    }

    // Full constructor
    public Service(UUID id, String name, String description, BigDecimal basePrice, OffsetDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", basePrice=" + basePrice +
                ", createdAt=" + createdAt +
                '}';
    }
}
