package com.rajaratabank.security;

import com.rajaratabank.models.Transaction;

import java.util.HashMap;
import java.util.Map;

/**
 * Heuristic-driven system observing transaction flows for potential fraud.
 */
public class FraudDetector {

    private static final double LARGE_TRANSACTION_THRESHOLD = 10000.0;
    private static final int MAX_FAILED_ATTEMPTS = 3;

    // Tracking for consecutive logic flow
    private Map<String, Integer> failedAttemptsTracker = new HashMap<>();

    public boolean isFraudulent(Transaction transaction) {
        if ("WITHDRAWAL".equals(transaction.getType()) || "TRANSFER".equals(transaction.getType())) {
            // Rule 1: Abnormally large withdrawals
            if (transaction.getAmount() > LARGE_TRANSACTION_THRESHOLD) {
                System.err.println("FRAUD ALERT: Transaction " + transaction.getTransactionId() + " flagged - exceeds generic large-amount threshold.");
                return true;
            }
        }
        
        // Rule 2: Multiple rapid failures contextually observed
        String accountId = transaction.getAccountId();
        if ("FAILED".equals(transaction.getStatus())) {
            int failures = failedAttemptsTracker.getOrDefault(accountId, 0) + 1;
            failedAttemptsTracker.put(accountId, failures);
            
            if (failures >= MAX_FAILED_ATTEMPTS) {
                System.err.println("FRAUD ALERT: Account " + accountId + " flagged for numerous rapid transaction failures.");
                return true;
            }
        } else if ("COMPLETED".equals(transaction.getStatus())) {
            failedAttemptsTracker.remove(accountId); 
        }

        return false;
    }

    public void flagTransaction(Transaction transaction) {
        transaction.setStatus("FLAGGED");
        generateAdminAlert(transaction);
    }

    private void generateAdminAlert(Transaction transaction) {
        System.out.println("ALERT TO ADMIN: Action required validating potential fraudulent transaction " + transaction.getTransactionId());
        // Fictitious Firebase Alert Path write would be triggered here in real deployment
    }
}
