package com.rajaratabank.services;

import com.rajaratabank.exceptions.InsufficientFundsException;
import com.rajaratabank.models.BankAccount;
import com.rajaratabank.models.Transaction;

import java.util.UUID;

/**
 * Processes integrated utility bill deduction tasks structurally.
 */
public class UtilityBillPayment {

    public enum UtilityType {
        ELECTRICITY, WATER, INTERNET
    }

    public Transaction payBill(BankAccount account, double amount, UtilityType biller) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid bill mapping amount requested.");
        }

        double initialBalance = account.getBalance();
        
        try {
            account.withdraw(amount);
            if (account.getBalance() == initialBalance) {
                // If it fails structurally, raise the specific rejection mapping
                throw new InsufficientFundsException("Insufficient mapped funds or generic structural limit block resolving bill payment.");
            }
        } catch (Exception e) {
            throw new InsufficientFundsException("Bill payment failure map triggered: " + e.getMessage());
        }

        String txId = UUID.randomUUID().toString();
        // Specifically format custom transaction type identifying the isolated BILL context
        Transaction transaction = new Transaction(txId, account.getAccountId(), "BILL_PAYMENT", amount, "COMPLETED");
        
        System.out.println("Resolved Utility Bill payment covering logic [" + biller.name() + "] mapping ($" + amount + ") executed specifically against generic context: " + account.getAccountId());
        saveTransactionToFirebase(transaction);
        
        return transaction;
    }

    private void saveTransactionToFirebase(Transaction transaction) {
        System.out.println("Syncing isolated custom Utility Payment " + transaction.getTransactionId() + " targeting contextual DB.");
    }
}
