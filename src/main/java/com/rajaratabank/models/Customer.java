package com.rajaratabank.models;

/**
 * Represents a Customer of the bank.
 * Demonstrates OOP Inheritance by inheriting attributes and methods from the User abstract class.
 */
public class Customer extends User {
    private String customerLevel; // e.g., Standard, Premium

    public Customer(String id, String name, String email, String passwordHash, String customerLevel) {
        super(id, name, email, passwordHash);
        this.customerLevel = customerLevel;
    }

    public String getCustomerLevel() { return customerLevel; }
    public void setCustomerLevel(String customerLevel) { this.customerLevel = customerLevel; }
}
