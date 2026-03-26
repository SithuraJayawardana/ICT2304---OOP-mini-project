package com.rajaratabank.models.accounts;

import com.rajaratabank.models.BankAccount;

/**
 * Student Account which includes strict singular withdrawal caps.
 */
public class StudentAccount extends BankAccount {
    private static final double MAX_WITHDRAWAL_LIMIT = 200.0;

    public StudentAccount(String accountId, String ownerId, double initialBalance, String currencyCode) {
        super(accountId, ownerId, initialBalance, currencyCode);
    }

    @Override
    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }

    /**
     * Polymorphic withdraw implementation featuring daily caps.
     */
    @Override
    public void withdraw(double amount) {
        if (amount > 0 && amount <= MAX_WITHDRAWAL_LIMIT && this.balance >= amount) {
            this.balance -= amount;
        } else {
            System.err.println("Withdrawal failed: Exceeds student account limits or insufficient funds.");
        }
    }

    @Override
    public double calculateInterest() {
        return this.balance * 0.02; // 2% interest
    }
}
