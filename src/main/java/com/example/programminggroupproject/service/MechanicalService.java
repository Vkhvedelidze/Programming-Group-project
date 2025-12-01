package com.example.programminggroupproject.service;

import com.example.programminggroupproject.model.Service;
import com.fasterxml.jackson.core.type.TypeReference;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing Service entities (services offered by shops) with Supabase backend.
 * Named "MechanicalService" to avoid confusion with the Service interface.
 * Provides service-specific operations in addition to standard CRUD.
 */
public class MechanicalService extends BaseSupabaseService<Service> {
    
    private static MechanicalService instance;
    
    private MechanicalService() {
        super("services", Service.class, new TypeReference<List<Service>>() {});
    }
    
    /**
     * Get singleton instance of MechanicalService
     */
    public static synchronized MechanicalService getInstance() {
        if (instance == null) {
            instance = new MechanicalService();
        }
        return instance;
    }
    
    // ==================== SERVICE-SPECIFIC OPERATIONS ====================
    
    /**
     * Find service by name
     * @param name The service name
     * @return Optional containing the service if found
     */
    public Optional<Service> findByName(String name) {
        return findOneBy("name", name);
    }
    
    /**
     * Search services by name or description
     * @param searchTerm The search term
     * @return List of matching services
     */
    public List<Service> searchServices(String searchTerm) {
        return searchMultiple(searchTerm, "name", "description");
    }
    
    /**
     * Get services within a price range
     * @param minPrice Minimum base price
     * @param maxPrice Maximum base price
     * @return List of services within the price range
     */
    public List<Service> getByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return getByRange("base_price", minPrice, maxPrice);
    }
    
    /**
     * Get services ordered by price (ascending)
     * @return List of services ordered by base price (low to high)
     */
    public List<Service> getAllOrderedByPriceAsc() {
        return getAllOrdered("base_price", true);
    }
    
    /**
     * Get services ordered by price (descending)
     * @return List of services ordered by base price (high to low)
     */
    public List<Service> getAllOrderedByPriceDesc() {
        return getAllOrdered("base_price", false);
    }
    
    /**
     * Get services ordered by name
     * @return List of services ordered alphabetically by name
     */
    public List<Service> getAllOrderedByName() {
        return getAllOrdered("name", true);
    }
    
    /**
     * Get affordable services (under a certain price)
     * @param maxPrice Maximum base price
     * @return List of services under the specified price
     */
    public List<Service> getAffordableServices(BigDecimal maxPrice) {
        return filter("base_price", "lt", maxPrice);
    }
    
    /**
     * Check if a service name already exists
     * @param name The service name to check
     * @return true if service exists, false otherwise
     */
    public boolean serviceNameExists(String name) {
        return existsBy("name", name);
    }
}

