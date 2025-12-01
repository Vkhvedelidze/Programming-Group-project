package com.example.programminggroupproject.service;

import com.example.programminggroupproject.model.ServiceRequestItem;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.UUID;

/**
 * Service for managing ServiceRequestItem entities with Supabase backend.
 * Provides service request item-specific operations in addition to standard CRUD.
 */
public class ServiceRequestItemService extends BaseSupabaseService<ServiceRequestItem> {
    
    private static ServiceRequestItemService instance;
    
    private ServiceRequestItemService() {
        super("service_request_items", ServiceRequestItem.class, new TypeReference<List<ServiceRequestItem>>() {});
    }
    
    /**
     * Get singleton instance of ServiceRequestItemService
     */
    public static synchronized ServiceRequestItemService getInstance() {
        if (instance == null) {
            instance = new ServiceRequestItemService();
        }
        return instance;
    }
    
    // ==================== SERVICE REQUEST ITEM-SPECIFIC OPERATIONS ====================
    
    /**
     * Get all items for a specific service request
     * @param serviceRequestId The service request ID (UUID)
     * @return List of items for the service request
     */
    public List<ServiceRequestItem> getByServiceRequestId(UUID serviceRequestId) {
        return findBy("service_request_id", serviceRequestId);
    }
    
    /**
     * Get all items for a specific service type
     * @param serviceId The service ID (UUID)
     * @return List of items using the specified service
     */
    public List<ServiceRequestItem> getByServiceId(UUID serviceId) {
        return findBy("service_id", serviceId);
    }
    
    /**
     * Get approved items for a service request
     * @param serviceRequestId The service request ID (UUID)
     * @return List of approved items
     */
    public List<ServiceRequestItem> getApprovedItems(UUID serviceRequestId) {
        List<ServiceRequestItem> items = getByServiceRequestId(serviceRequestId);
        return items.stream()
                .filter(item -> Boolean.TRUE.equals(item.getIsApproved()))
                .toList();
    }
    
    /**
     * Get pending (not approved) items for a service request
     * @param serviceRequestId The service request ID (UUID)
     * @return List of pending items
     */
    public List<ServiceRequestItem> getPendingItems(UUID serviceRequestId) {
        List<ServiceRequestItem> items = getByServiceRequestId(serviceRequestId);
        return items.stream()
                .filter(item -> !Boolean.TRUE.equals(item.getIsApproved()))
                .toList();
    }
    
    /**
     * Get items by source (e.g., "client", "mechanic")
     * @param source The source of the items
     * @return List of items from the specified source
     */
    public List<ServiceRequestItem> getBySource(String source) {
        return findBy("source", source);
    }
    
    /**
     * Approve an item
     * @param itemId The item ID (UUID)
     * @return Updated item
     */
    public ServiceRequestItem approveItem(UUID itemId) {
        ServiceRequestItem item = get(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Service request item not found"));
        
        item.setIsApproved(true);
        return update(itemId, item);
    }
    
    /**
     * Update item quantities for a service request
     * @param itemId The item ID (UUID)
     * @param newQuantity The new quantity
     * @return Updated item
     */
    public ServiceRequestItem updateQuantity(UUID itemId, Integer newQuantity) {
        ServiceRequestItem item = get(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Service request item not found"));
        
        item.setQuantity(newQuantity);
        return update(itemId, item);
    }
}

