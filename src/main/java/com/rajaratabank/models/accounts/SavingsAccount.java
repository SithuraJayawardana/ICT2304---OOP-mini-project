package com.rajaratabank.models.accounts;

import com.rajaratabank.models.BankAccount;

/**
 * Savings Account with minimum balance limits and standard interest processing.
 */
public class SavingsAccount extends BankAccount {
    private static final double MINIMUM_BALANCE = 500.0;
    private static final double INTEREST_RATE = 0.04; // 4% annual interest

    public SavingsAccount(String accountId, String ownerId, double initialBalance, String currencyCode) {
        super(accountId, ownerId, initialBalance, currencyCode);
    }

    @Override
    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }

    /**
     * Polymorphic implementation of withdraw focusing on standard restrictions.
     */
    @Override
    public void withdraw(double amount) {
        if (amount > 0 && (this.balance - amount) >= MINIMUM_BALANCE) {
            this.balance -= amount;
        } else {
            System.err.println("Withdrawal failed: Minimum balance requirement of " + MINIMUM_BALANCE + " not met.");
        }
    }

    /**
     * Polymorphic implementation of interest calculation for Savings accounts.
     */
    @Override
    public double calculateInterest() {
        return this.balance * INTEREST_RATE;
    }
}
