package com.rajaratabank.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer's profile capturing all their data contexts.
 * Demonstrates OOP Composition, as the CustomerProfile structurally owns a List of BankAccounts.
 */
public class CustomerProfile {
    private Customer customer;
    private List<BankAccount> accounts;

    public CustomerProfile(Customer customer) {
        this.customer = customer;
        this.accounts = new ArrayList<>();
    }

    /**
     * Demonstrating Composition behavior to encapsulate account lists natively.
     */
    public void addAccount(BankAccount account) {
        if (account != null) {
            this.accounts.add(account);
        }
    }

    /**
     * Demonstrating Composition behavior to legally destruct composed account maps.
     */
    public void removeAccount(BankAccount account) {
        if (account != null && this.accounts.contains(account)) {
            // Strict OOP Business Rule constraints
            if (account.getBalance() == 0) {
                this.accounts.remove(account);
            } else {
                throw new IllegalStateException("Strict Error: Cannot destruct accounts possessing active balance metrics. Please withdraw or transfer funds strictly to $0.00 first.");
            }
        }
    }

    public List<BankAccount> getAccounts() {
        return accounts;
    }

    public Customer getCustomer() {
        return customer;
    }

    public double getTotalBalance() {
        double total = 0;
        for (BankAccount account : accounts) {
            total += account.getBalance();
        }
        return total;
    }
}
