package com.example.programminggroupproject.model;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Vehicle model matching the Supabase vehicles table schema.
 * Schema: id (UUID), client_id, make, model, year, license_plate, created_at
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Vehicle {

    @JsonProperty("id")
    private UUID id;
    
    @JsonProperty("client_id")
    private UUID clientId;
    
    @JsonProperty("make")
    private String make;
    
    @JsonProperty("model")
    private String model;
    
    @JsonProperty("year")
    private Integer year;
    
    @JsonProperty("license_plate")
    private String licensePlate;
    
    @JsonProperty("created_at")
    private OffsetDateTime createdAt;

    // Default constructor for Jackson
    public Vehicle() {
    }

    // Constructor for creating new vehicles (without ID and created_at)
    public Vehicle(UUID clientId, String make, String model, Integer year, String licensePlate) {
        this.clientId = clientId;
        this.make = make;
        this.model = model;
        this.year = year;
        this.licensePlate = licensePlate;
    }
    
    // Full constructor
    public Vehicle(UUID id, UUID clientId, String make, String model, Integer year, 
                   String licensePlate, OffsetDateTime createdAt) {
        this.id = id;
        this.clientId = clientId;
        this.make = make;
        this.model = model;
        this.year = year;
        this.licensePlate = licensePlate;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    /**
     * Helper method to get vehicle display name
     * @return Formatted string like "2020 Toyota Camry"
     */
    public String getDisplayName() {
        return year + " " + make + " " + model;
    }
    
    /**
     * Helper method to get full vehicle info with license plate
     * @return Formatted string like "2020 Toyota Camry - ABC123"
     */
    public String getFullInfo() {
        String info = getDisplayName();
        if (licensePlate != null && !licensePlate.isEmpty()) {
            info += " - " + licensePlate;
        }
        return info;
    }
    
    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", licensePlate='" + licensePlate + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
