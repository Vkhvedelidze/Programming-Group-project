package com.example.programminggroupproject.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * ServiceRequestItem model matching the Supabase service_request_items table schema.
 * Schema: id (UUID), service_request_id, service_id, quantity, price_estimated,
 *         price_final, source, is_approved, created_at
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceRequestItem {

    @JsonProperty("id")
    private UUID id;
    
    @JsonProperty("service_request_id")
    private UUID serviceRequestId;
    
    @JsonProperty("service_id")
    private UUID serviceId;
    
    @JsonProperty("quantity")
    private Integer quantity;
    
    @JsonProperty("price_estimated")
    private BigDecimal priceEstimated;
    
    @JsonProperty("price_final")
    private BigDecimal priceFinal;
    
    @JsonProperty("source")
    private String source;
    
    @JsonProperty("is_approved")
    private Boolean isApproved;
    
    @JsonProperty("created_at")
    private OffsetDateTime createdAt;

    // Default constructor for Jackson
    public ServiceRequestItem() {
    }

    // Constructor for creating new items (without ID and created_at)
    public ServiceRequestItem(UUID serviceRequestId, UUID serviceId, Integer quantity,
                              BigDecimal priceEstimated, BigDecimal priceFinal,
                              String source, Boolean isApproved) {
        this.serviceRequestId = serviceRequestId;
        this.serviceId = serviceId;
        this.quantity = quantity;
        this.priceEstimated = priceEstimated;
        this.priceFinal = priceFinal;
        this.source = source;
        this.isApproved = isApproved;
    }

    // Full constructor
    public ServiceRequestItem(UUID id, UUID serviceRequestId, UUID serviceId, Integer quantity,
                              BigDecimal priceEstimated, BigDecimal priceFinal,
                              String source, Boolean isApproved, OffsetDateTime createdAt) {
        this.id = id;
        this.serviceRequestId = serviceRequestId;
        this.serviceId = serviceId;
        this.quantity = quantity;
        this.priceEstimated = priceEstimated;
        this.priceFinal = priceFinal;
        this.source = source;
        this.isApproved = isApproved;
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

    public UUID getServiceId() {
        return serviceId;
    }

    public void setServiceId(UUID serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPriceEstimated() {
        return priceEstimated;
    }

    public void setPriceEstimated(BigDecimal priceEstimated) {
        this.priceEstimated = priceEstimated;
    }

    public BigDecimal getPriceFinal() {
        return priceFinal;
    }

    public void setPriceFinal(BigDecimal priceFinal) {
        this.priceFinal = priceFinal;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean approved) {
        isApproved = approved;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Helper methods
    /**
     * Calculate total estimated price (price * quantity)
     * @return Total estimated price or null if price is not set
     */
    public BigDecimal getTotalEstimated() {
        if (priceEstimated == null) return null;
        return priceEstimated.multiply(BigDecimal.valueOf(quantity != null ? quantity : 0));
    }

    /**
     * Calculate total final price (price * quantity)
     * @return Total final price or null if price is not set
     */
    public BigDecimal getTotalFinal() {
        if (priceFinal == null) return null;
        return priceFinal.multiply(BigDecimal.valueOf(quantity != null ? quantity : 0));
    }

    @Override
    public String toString() {
        return "ServiceRequestItem{" +
                "id=" + id +
                ", serviceRequestId=" + serviceRequestId +
                ", serviceId=" + serviceId +
                ", quantity=" + quantity +
                ", priceEstimated=" + priceEstimated +
                ", priceFinal=" + priceFinal +
                ", source='" + source + '\'' +
                ", isApproved=" + isApproved +
                ", createdAt=" + createdAt +
                '}';
    }
}
