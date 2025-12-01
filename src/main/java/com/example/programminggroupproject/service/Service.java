package com.example.programminggroupproject.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Generic service interface for Supabase CRUD operations.
 * Provides standard database operations with Supabase-specific features.
 *
 * @param <T> The model type this service manages
 */
public interface Service<T> {

    // ==================== BASIC CRUD OPERATIONS ====================
    
    /**
     * Retrieve a single record by ID.
     * @param id The unique identifier
     * @return Optional containing the record if found, empty otherwise
     */
    Optional<T> get(UUID id);
    
    /**
     * Retrieve all records from the table.
     * @return List of all records
     */
    List<T> getAll();
    
    /**
     * Retrieve records with pagination.
     * @param limit Maximum number of records to return
     * @param offset Number of records to skip
     * @return List of records within the specified range
     */
    List<T> getAll(int limit, int offset);
    
    /**
     * Create a new record.
     * @param object The object to create
     * @return The created object with generated fields (id, timestamps, etc.)
     */
    T create(T object);
    
    /**
     * Update an existing record by ID.
     * @param id The unique identifier
     * @param object The updated object data
     * @return The updated object
     */
    T update(UUID id, T object);
    
    /**
     * Insert or update a record (based on unique constraints).
     * @param object The object to upsert
     * @return The upserted object
     */
    T upsert(T object);
    
    /**
     * Delete a record by ID.
     * @param id The unique identifier
     */
    void delete(UUID id);
    
    /**
     * Delete all records from the table.
     * WARNING: Use with extreme caution!
     */
    void deleteAll();
    
    // ==================== FILTERING & QUERYING ====================
    
    /**
     * Filter records by a single condition.
     * Supabase operators: eq, neq, gt, gte, lt, lte, like, ilike, is, in, contains
     * 
     * @param column The column name to filter on
     * @param operator The comparison operator
     * @param value The value to compare against
     * @return List of matching records
     */
    List<T> filter(String column, String operator, Object value);
    
    /**
     * Filter records by multiple equality conditions (AND logic).
     * @param criteria Map of column names to values (all must match)
     * @return List of matching records
     */
    List<T> filterMultiple(Map<String, Object> criteria);
    
    /**
     * Find a single record matching the criteria.
     * @param criteria Map of column names to values
     * @return Optional containing the first matching record
     */
    Optional<T> findOne(Map<String, Object> criteria);
    
    /**
     * Find a single record by a specific column value.
     * @param column The column name
     * @param value The value to match
     * @return Optional containing the matching record
     */
    Optional<T> findOneBy(String column, Object value);
    
    /**
     * Find all records by a specific column value.
     * @param column The column name
     * @param value The value to match
     * @return List of matching records
     */
    List<T> findBy(String column, Object value);
    
    // ==================== RANGE & SEARCH ====================
    
    /**
     * Get records where a column value falls within a range.
     * Useful for dates, numbers, etc.
     * 
     * @param column The column name
     * @param min Minimum value (inclusive)
     * @param max Maximum value (inclusive)
     * @return List of records within the range
     */
    List<T> getByRange(String column, Object min, Object max);
    
    /**
     * Search for records using text matching (case-insensitive).
     * Uses Supabase's ilike operator.
     * 
     * @param column The column to search in
     * @param searchTerm The search term (supports % wildcards)
     * @return List of matching records
     */
    List<T> search(String column, String searchTerm);
    
    /**
     * Full-text search across multiple columns.
     * @param searchTerm The search term
     * @param columns Columns to search in
     * @return List of matching records
     */
    List<T> searchMultiple(String searchTerm, String... columns);
    
    // ==================== ORDERING & SORTING ====================
    
    /**
     * Get all records ordered by a specific column.
     * @param orderBy The column to order by
     * @param ascending True for ascending, false for descending
     * @return Ordered list of records
     */
    List<T> getAllOrdered(String orderBy, boolean ascending);
    
    /**
     * Filter and order records in one query.
     * @param column Filter column
     * @param operator Filter operator
     * @param value Filter value
     * @param orderBy Column to order by
     * @param ascending Sort direction
     * @return Filtered and ordered list of records
     */
    List<T> filterAndOrder(String column, String operator, Object value, 
                           String orderBy, boolean ascending);
    
    // ==================== BULK OPERATIONS ====================
    
    /**
     * Create multiple records in a single request.
     * @param objects List of objects to create
     * @return List of created objects
     */
    List<T> createMultiple(List<T> objects);
    
    /**
     * Delete multiple records by their IDs.
     * @param ids List of IDs to delete
     */
    void deleteMultiple(List<UUID> ids);
    
    // ==================== UTILITY OPERATIONS ====================
    
    /**
     * Check if a record exists by ID.
     * @param id The unique identifier
     * @return True if exists, false otherwise
     */
    boolean exists(UUID id);
    
    /**
     * Check if a record exists matching a specific column value.
     * @param column The column name
     * @param value The value to check
     * @return True if exists, false otherwise
     */
    boolean existsBy(String column, Object value);
    
    /**
     * Count total number of records.
     * @return Total count
     */
    int count();
    
    /**
     * Count records matching a filter.
     * @param column Filter column
     * @param operator Filter operator
     * @param value Filter value
     * @return Count of matching records
     */
    int countFiltered(String column, String operator, Object value);
    
    // ==================== ASYNC OPERATIONS (Optional - for JavaFX) ====================
    
    /**
     * Asynchronously retrieve a record by ID.
     * Useful for keeping JavaFX UI responsive.
     * 
     * @param id The unique identifier
     * @return CompletableFuture with the result
     */
    CompletableFuture<Optional<T>> getAsync(UUID id);
    
    /**
     * Asynchronously retrieve all records.
     * @return CompletableFuture with the result
     */
    CompletableFuture<List<T>> getAllAsync();
    
    /**
     * Asynchronously create a record.
     * @param object The object to create
     * @return CompletableFuture with the created object
     */
    CompletableFuture<T> createAsync(T object);
}
