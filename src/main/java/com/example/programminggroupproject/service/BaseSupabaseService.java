package com.example.programminggroupproject.service;

import com.example.programminggroupproject.client.SupabaseClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Response;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Abstract base class implementing Service interface with Supabase backend.
 * Provides all CRUD operations using Supabase PostgREST API.
 * 
 * @param <T> The model type this service manages
 */
public abstract class BaseSupabaseService<T> implements Service<T> {
    
    protected final SupabaseClient client;
    protected final ObjectMapper objectMapper;
    protected final String tableName;
    protected final Class<T> modelClass;
    protected final TypeReference<List<T>> listTypeRef;
    
    /**
     * Constructor for base service
     * @param tableName The Supabase table name
     * @param modelClass The model class
     * @param listTypeRef TypeReference for deserializing lists
     */
    protected BaseSupabaseService(String tableName, Class<T> modelClass, TypeReference<List<T>> listTypeRef) {
        this.client = SupabaseClient.getInstance();
        this.objectMapper = client.getObjectMapper();
        this.tableName = tableName;
        this.modelClass = modelClass;
        this.listTypeRef = listTypeRef;
    }
    
    // ==================== BASIC CRUD OPERATIONS ====================
    
    @Override
    public Optional<T> get(UUID id) {
        try {
            Map<String, String> params = SupabaseClient.buildParams();
            params.put("id", "eq." + id.toString());
            params.put("limit", "1");
            
            String response = client.get(tableName, params);
            List<T> results = objectMapper.readValue(response, listTypeRef);
            
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } catch (IOException e) {
            throw new RuntimeException("Error fetching record by ID: " + id, e);
        }
    }
    
    @Override
    public List<T> getAll() {
        try {
            String response = client.get(tableName, null);
            return objectMapper.readValue(response, listTypeRef);
        } catch (IOException e) {
            throw new RuntimeException("Error fetching all records", e);
        }
    }
    
    @Override
    public List<T> getAll(int limit, int offset) {
        try {
            Map<String, String> params = SupabaseClient.buildParams();
            params.put("limit", String.valueOf(limit));
            params.put("offset", String.valueOf(offset));
            
            String response = client.get(tableName, params);
            return objectMapper.readValue(response, listTypeRef);
        } catch (IOException e) {
            throw new RuntimeException("Error fetching paginated records", e);
        }
    }
    
    @Override
    public T create(T object) {
        try {
            String jsonBody = objectMapper.writeValueAsString(object);
            String response = client.post(tableName, jsonBody);
            
            // Response is an array with single element
            List<T> results = objectMapper.readValue(response, listTypeRef);
            return results.isEmpty() ? null : results.get(0);
        } catch (IOException e) {
            throw new RuntimeException("Error creating record", e);
        }
    }
    
    @Override
    public T update(UUID id, T object) {
        try {
            Map<String, String> params = SupabaseClient.buildParams();
            params.put("id", "eq." + id.toString());
            
            String jsonBody = objectMapper.writeValueAsString(object);
            String response = client.patch(tableName, jsonBody, params);
            
            List<T> results = objectMapper.readValue(response, listTypeRef);
            return results.isEmpty() ? null : results.get(0);
        } catch (IOException e) {
            throw new RuntimeException("Error updating record: " + id, e);
        }
    }
    
    @Override
    public T upsert(T object) {
        try {
            String jsonBody = objectMapper.writeValueAsString(object);
            String response = client.upsert(tableName, jsonBody);
            
            List<T> results = objectMapper.readValue(response, listTypeRef);
            return results.isEmpty() ? null : results.get(0);
        } catch (IOException e) {
            throw new RuntimeException("Error upserting record", e);
        }
    }
    
    @Override
    public void delete(UUID id) {
        try {
            Map<String, String> params = SupabaseClient.buildParams();
            params.put("id", "eq." + id.toString());
            
            client.delete(tableName, params);
        } catch (IOException e) {
            throw new RuntimeException("Error deleting record: " + id, e);
        }
    }
    
    @Override
    public void deleteAll() {
        try {
            // WARNING: This deletes ALL records in the table
            client.delete(tableName, null);
        } catch (IOException e) {
            throw new RuntimeException("Error deleting all records", e);
        }
    }
    
    // ==================== FILTERING & QUERYING ====================
    
    @Override
    public List<T> filter(String column, String operator, Object value) {
        try {
            Map<String, String> params = SupabaseClient.buildParams();
            params.put(column, operator + "." + value.toString());
            
            String response = client.get(tableName, params);
            return objectMapper.readValue(response, listTypeRef);
        } catch (IOException e) {
            throw new RuntimeException("Error filtering records", e);
        }
    }
    
    @Override
    public List<T> filterMultiple(Map<String, Object> criteria) {
        try {
            Map<String, String> params = SupabaseClient.buildParams();
            criteria.forEach((key, value) -> 
                params.put(key, "eq." + value.toString())
            );
            
            String response = client.get(tableName, params);
            return objectMapper.readValue(response, listTypeRef);
        } catch (IOException e) {
            throw new RuntimeException("Error filtering records with multiple criteria", e);
        }
    }
    
    @Override
    public Optional<T> findOne(Map<String, Object> criteria) {
        try {
            Map<String, String> params = SupabaseClient.buildParams();
            criteria.forEach((key, value) -> 
                params.put(key, "eq." + value.toString())
            );
            params.put("limit", "1");
            
            String response = client.get(tableName, params);
            List<T> results = objectMapper.readValue(response, listTypeRef);
            
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } catch (IOException e) {
            throw new RuntimeException("Error finding single record", e);
        }
    }
    
    @Override
    public Optional<T> findOneBy(String column, Object value) {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put(column, value);
        return findOne(criteria);
    }
    
    @Override
    public List<T> findBy(String column, Object value) {
        return filter(column, "eq", value);
    }
    
    // ==================== RANGE & SEARCH ====================
    
    @Override
    public List<T> getByRange(String column, Object min, Object max) {
        try {
            Map<String, String> params = SupabaseClient.buildParams();
            params.put(column, "gte." + min.toString());
            params.put(column, "lte." + max.toString());
            
            String response = client.get(tableName, params);
            return objectMapper.readValue(response, listTypeRef);
        } catch (IOException e) {
            throw new RuntimeException("Error getting records by range", e);
        }
    }
    
    @Override
    public List<T> search(String column, String searchTerm) {
        return filter(column, "ilike", "%" + searchTerm + "%");
    }
    
    @Override
    public List<T> searchMultiple(String searchTerm, String... columns) {
        try {
            Map<String, String> params = SupabaseClient.buildParams();
            
            // Build OR query for multiple columns
            StringBuilder orQuery = new StringBuilder();
            for (int i = 0; i < columns.length; i++) {
                if (i > 0) orQuery.append(",");
                orQuery.append(columns[i]).append(".ilike.%").append(searchTerm).append("%");
            }
            params.put("or", "(" + orQuery + ")");
            
            String response = client.get(tableName, params);
            return objectMapper.readValue(response, listTypeRef);
        } catch (IOException e) {
            throw new RuntimeException("Error searching multiple columns", e);
        }
    }
    
    // ==================== ORDERING & SORTING ====================
    
    @Override
    public List<T> getAllOrdered(String orderBy, boolean ascending) {
        try {
            Map<String, String> params = SupabaseClient.buildParams();
            params.put("order", orderBy + "." + (ascending ? "asc" : "desc"));
            
            String response = client.get(tableName, params);
            return objectMapper.readValue(response, listTypeRef);
        } catch (IOException e) {
            throw new RuntimeException("Error getting ordered records", e);
        }
    }
    
    @Override
    public List<T> filterAndOrder(String column, String operator, Object value, 
                                   String orderBy, boolean ascending) {
        try {
            Map<String, String> params = SupabaseClient.buildParams();
            params.put(column, operator + "." + value.toString());
            params.put("order", orderBy + "." + (ascending ? "asc" : "desc"));
            
            String response = client.get(tableName, params);
            return objectMapper.readValue(response, listTypeRef);
        } catch (IOException e) {
            throw new RuntimeException("Error filtering and ordering records", e);
        }
    }
    
    // ==================== BULK OPERATIONS ====================
    
    @Override
    public List<T> createMultiple(List<T> objects) {
        try {
            String jsonBody = objectMapper.writeValueAsString(objects);
            String response = client.post(tableName, jsonBody);
            
            return objectMapper.readValue(response, listTypeRef);
        } catch (IOException e) {
            throw new RuntimeException("Error creating multiple records", e);
        }
    }
    
    @Override
    public void deleteMultiple(List<UUID> ids) {
        try {
            Map<String, String> params = SupabaseClient.buildParams();
            
            // Build IN query: id=in.(uuid1,uuid2,uuid3)
            StringBuilder inQuery = new StringBuilder("in.(");
            for (int i = 0; i < ids.size(); i++) {
                if (i > 0) inQuery.append(",");
                inQuery.append(ids.get(i).toString());
            }
            inQuery.append(")");
            
            params.put("id", inQuery.toString());
            client.delete(tableName, params);
        } catch (IOException e) {
            throw new RuntimeException("Error deleting multiple records", e);
        }
    }
    
    // ==================== UTILITY OPERATIONS ====================
    
    @Override
    public boolean exists(UUID id) {
        return get(id).isPresent();
    }
    
    @Override
    public boolean existsBy(String column, Object value) {
        return findOneBy(column, value).isPresent();
    }
    
    @Override
    public int count() {
        try {
            Map<String, String> params = SupabaseClient.buildParams();
            params.put("select", "count");
            
            Response response = client.getWithCount(tableName, params);
            
            // Get count from Content-Range header
            String contentRange = response.header("Content-Range");
            if (contentRange != null) {
                // Format: "0-9/100" where 100 is total count
                String[] parts = contentRange.split("/");
                if (parts.length == 2) {
                    response.close();
                    return Integer.parseInt(parts[1]);
                }
            }
            
            response.close();
            return 0;
        } catch (IOException e) {
            throw new RuntimeException("Error counting records", e);
        }
    }
    
    @Override
    public int countFiltered(String column, String operator, Object value) {
        try {
            Map<String, String> params = SupabaseClient.buildParams();
            params.put(column, operator + "." + value.toString());
            params.put("select", "count");
            
            Response response = client.getWithCount(tableName, params);
            
            String contentRange = response.header("Content-Range");
            if (contentRange != null) {
                String[] parts = contentRange.split("/");
                if (parts.length == 2) {
                    response.close();
                    return Integer.parseInt(parts[1]);
                }
            }
            
            response.close();
            return 0;
        } catch (IOException e) {
            throw new RuntimeException("Error counting filtered records", e);
        }
    }
    
    // ==================== ASYNC OPERATIONS ====================
    
    @Override
    public CompletableFuture<Optional<T>> getAsync(UUID id) {
        return CompletableFuture.supplyAsync(() -> get(id));
    }
    
    @Override
    public CompletableFuture<List<T>> getAllAsync() {
        return CompletableFuture.supplyAsync(this::getAll);
    }
    
    @Override
    public CompletableFuture<T> createAsync(T object) {
        return CompletableFuture.supplyAsync(() -> create(object));
    }
}

