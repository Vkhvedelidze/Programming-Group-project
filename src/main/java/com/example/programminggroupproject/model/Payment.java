package com.example.programminggroupproject.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Payment model matching the Supabase payments table schema.
 * Schema: id (UUID), service_request_id, amount, status, created_at
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Payment {
    
    @JsonProperty("id")
    private UUID id;
    
    @JsonProperty("service_request_id")
    private UUID serviceRequestId;
    
    @JsonProperty("amount")
    private BigDecimal amount;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("created_at")
    private OffsetDateTime createdAt;

    // Default constructor for Jackson
    public Payment() {
    }

    // Constructor for creating new payments (without ID and created_at)
    public Payment(UUID serviceRequestId, BigDecimal amount, String status) {
        this.serviceRequestId = serviceRequestId;
        this.amount = amount;
        this.status = status;
    }

    // Full constructor
    public Payment(UUID id, UUID serviceRequestId, BigDecimal amount, String status, OffsetDateTime createdAt) {
        this.id = id;
        this.serviceRequestId = serviceRequestId;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getServiceRequestId() {
        return serviceRequestId;
    }

    public void setServiceRequestId(UUID serviceRequestId) {
        this.serviceRequestId = serviceRequestId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", serviceRequestId=" + serviceRequestId +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
