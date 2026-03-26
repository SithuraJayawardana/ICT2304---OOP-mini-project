package com.rajaratabank.models;

import java.time.LocalDateTime;

/**
 * Represents a discrete financial transfer/transaction.
 */
public class Transaction {
    private String transactionId;
    private String accountId;
    private String type; // e.g., DEPOSIT, WITHDRAWAL, TRANSFER
    private double amount;
    private String status; // PENDING, COMPLETED, FAILED, FLAGGED
    private String timestamp;

    public Transaction(String transactionId, String accountId, String type, double amount, String status) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.status = status;
        this.timestamp = LocalDateTime.now().toString();
    }

    public String getTransactionId() { return transactionId; }
    public String getAccountId() { return accountId; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getTimestamp() { return timestamp; }
}
