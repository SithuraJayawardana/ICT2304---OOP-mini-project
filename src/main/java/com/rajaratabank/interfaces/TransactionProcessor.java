package com.rajaratabank.interfaces;

/**
 * Interface defining processing capabilities for banking transactions.
 * Demonstrates strict Abstraction by specifying only what operations should be performed
 * for processing transactions, leaving the execution logic entirely to the implementing classes.
 */
public interface TransactionProcessor {
    /**
     * Processes a core financial transaction.
     * 
     * @param accountId The ID of the target account for the transaction.
     * @param amount The value of the transaction.
     * @param type The type of transaction being performed (e.g., DEPOSIT, WITHDRAWAL).
     * @return true if the transaction is successful, false otherwise.
     */
    boolean processTransaction(String accountId, double amount, String type);
}
