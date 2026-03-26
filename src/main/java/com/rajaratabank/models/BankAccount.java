package com.rajaratabank.models;

/**
 * Abstract class representing a generic Bank Account.
 * Applies Abstraction by defining abstract methods that concrete account types must implement.
 * Applies Encapsulation by hiding sensitive data like the balance behind logic-controlled access points
 * so that it cannot be modified arbitrarily without proper mechanisms (deposit/withdraw).
 */
public abstract class BankAccount {
    private String accountId;
    private String ownerId;
    protected double balance; // Protected to allow specific subclasses to apply unique operational logic during transactions
    private String currencyCode;

    public BankAccount(String accountId, String ownerId, double initialBalance, String currencyCode) {
        this.accountId = accountId;
        this.ownerId = ownerId;
        this.balance = initialBalance;
        this.currencyCode = currencyCode;
    }

    // Abstract methods to enforce implementation in specific account types
    public abstract void deposit(double amount);
    public abstract void withdraw(double amount);
    public abstract double calculateInterest();

    // Getters
    public String getAccountId() { return accountId; }
    public String getOwnerId() { return ownerId; }
    
    public double getBalance() { return balance; }
    // No setBalance method is exposed to ensure balance integrity
    
    public String getCurrencyCode() { return currencyCode; }
}
