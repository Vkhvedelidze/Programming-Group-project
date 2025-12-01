package com.example.programminggroupproject.service;

import com.example.programminggroupproject.model.Payment;
import com.fasterxml.jackson.core.type.TypeReference;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for managing Payment entities with Supabase backend.
 * Provides payment-specific operations in addition to standard CRUD.
 */
public class PaymentService extends BaseSupabaseService<Payment> {
    
    private static PaymentService instance;
    
    private PaymentService() {
        super("payments", Payment.class, new TypeReference<List<Payment>>() {});
    }
    
    /**
     * Get singleton instance of PaymentService
     */
    public static synchronized PaymentService getInstance() {
        if (instance == null) {
            instance = new PaymentService();
        }
        return instance;
    }
    
    // ==================== PAYMENT-SPECIFIC OPERATIONS ====================
    
    /**
     * Get all payments for a specific service request
     * @param serviceRequestId The service request ID (UUID)
     * @return List of payments for the service request
     */
    public List<Payment> getByServiceRequestId(UUID serviceRequestId) {
        return findBy("service_request_id", serviceRequestId);
    }
    
    /**
     * Get payments by status
     * @param status The payment status (e.g., "Pending", "Completed", "Failed")
     * @return List of payments with the specified status
     */
    public List<Payment> getByStatus(String status) {
        return findBy("status", status);
    }
    
    /**
     * Get all pending payments
     * @return List of pending payments
     */
    public List<Payment> getPendingPayments() {
        return getByStatus("Pending");
    }
    
    /**
     * Get all completed payments
     * @return List of completed payments
     */
    public List<Payment> getCompletedPayments() {
        return getByStatus("Completed");
    }
    
    /**
     * Get all failed payments
     * @return List of failed payments
     */
    public List<Payment> getFailedPayments() {
        return getByStatus("Failed");
    }
    
    /**
     * Get the payment for a service request (usually just one)
     * @param serviceRequestId The service request ID (UUID)
     * @return Optional containing the payment if found
     */
    public Optional<Payment> getPaymentForServiceRequest(UUID serviceRequestId) {
        List<Payment> payments = getByServiceRequestId(serviceRequestId);
        return payments.isEmpty() ? Optional.empty() : Optional.of(payments.get(0));
    }
    
    /**
     * Get payments by amount range
     * @param minAmount Minimum amount
     * @param maxAmount Maximum amount
     * @return List of payments within the amount range
     */
    public List<Payment> getByAmountRange(BigDecimal minAmount, BigDecimal maxAmount) {
        return getByRange("amount", minAmount, maxAmount);
    }
    
    /**
     * Update payment status
     * @param paymentId The payment ID (UUID)
     * @param status The new status
     * @return Updated payment
     */
    public Payment updatePaymentStatus(UUID paymentId, String status) {
        Payment payment = get(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        
        payment.setStatus(status);
        return update(paymentId, payment);
    }
    
    /**
     * Mark payment as completed
     * @param paymentId The payment ID (UUID)
     * @return Updated payment
     */
    public Payment markAsCompleted(UUID paymentId) {
        return updatePaymentStatus(paymentId, "Completed");
    }
    
    /**
     * Mark payment as failed
     * @param paymentId The payment ID (UUID)
     * @return Updated payment
     */
    public Payment markAsFailed(UUID paymentId) {
        return updatePaymentStatus(paymentId, "Failed");
    }
    
    /**
     * Calculate total amount of payments
     * @param payments List of payments
     * @return Total amount
     */
    public BigDecimal calculateTotal(List<Payment> payments) {
        return payments.stream()
                .map(Payment::getAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * Get recent payments ordered by creation date
     * @param limit Maximum number of payments to return
     * @return List of recent payments
     */
    public List<Payment> getRecentPayments(int limit) {
        return getAllOrdered("created_at", false)
                .stream()
                .limit(limit)
                .toList();
    }
}

