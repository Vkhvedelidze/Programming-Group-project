package com.example.programminggroupproject.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * ServiceStatusUpdate model matching the Supabase service_status_updates table schema.
 * Schema: id (UUID), service_request_id, status, note, created_by, created_at
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceStatusUpdate {

    @JsonProperty("id")
    private UUID id;
    
    @JsonProperty("service_request_id")
    private UUID serviceRequestId;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("note")
    private String note;
    
    @JsonProperty("created_by")
    private UUID createdBy;
    
    @JsonProperty("created_at")
    private OffsetDateTime createdAt;

    // Default constructor for Jackson
    public ServiceStatusUpdate() {
    }

    // Constructor for creating new status updates (without ID and created_at)
    public ServiceStatusUpdate(UUID serviceRequestId, String status, String note, UUID createdBy) {
        this.serviceRequestId = serviceRequestId;
        this.status = status;
        this.note = note;
        this.createdBy = createdBy;
    }

    // Full constructor
    public ServiceStatusUpdate(UUID id, UUID serviceRequestId, String status, String note, 
                               UUID createdBy, OffsetDateTime createdAt) {
        this.id = id;
        this.serviceRequestId = serviceRequestId;
        this.status = status;
        this.note = note;
        this.createdBy = createdBy;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ServiceStatusUpdate{" +
                "id=" + id +
                ", serviceRequestId=" + serviceRequestId +
                ", status='" + status + '\'' +
                ", note='" + note + '\'' +
                ", createdBy=" + createdBy +
                ", createdAt=" + createdAt +
                '}';
    }
}
