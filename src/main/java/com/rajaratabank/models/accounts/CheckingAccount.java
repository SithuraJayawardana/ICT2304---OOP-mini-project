package com.rajaratabank.models.accounts;

import com.rajaratabank.models.BankAccount;

/**
 * Checking Account applying overdraft protection. Does not accumulate interest.
 */
public class CheckingAccount extends BankAccount {
    private double overdraftLimit;

    public CheckingAccount(String accountId, String ownerId, double initialBalance, String currencyCode, double overdraftLimit) {
        super(accountId, ownerId, initialBalance, currencyCode);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }

    /**
     * Polymorphic implementation of withdraw utilizing overdraft limits.
     */
    @Override
    public void withdraw(double amount) {
        if (amount > 0 && (this.balance + overdraftLimit) >= amount) {
            this.balance -= amount;
        } else {
            System.err.println("Withdrawal failed: Overdraft limit exceeded.");
        }
    }

    /**
     * Checking accounts do not yield interest. 
     */
    @Override
    public double calculateInterest() {
        return 0.0; 
    }
}
