package com.rajaratabank;

import com.rajaratabank.config.FirebaseConfig;
import com.rajaratabank.models.Customer;
import com.rajaratabank.models.accounts.SavingsAccount;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to Rajarata Digital Bank!");

        // Initialize Firebase
        FirebaseConfig.initialize();

        // Example logic
        Customer customer = new Customer("C001", "Amal Perera", "amal@example.com", "0771234567", "password123");
        SavingsAccount account = new SavingsAccount("ACC-001", customer.getId(), 5000.0, "LKR");

        System.out.println("\nCreated Customer: " + customer.getName());
        System.out.println("Created Account: " + account.getAccountId());
        System.out.println("Account Balance: Rs." + account.getBalance());

        System.out.println("\nEnd of execution. The server models are ready to be used.");
    }
}
