package com.rajaratabank.services;

import com.rajaratabank.exceptions.InsufficientFundsException;
import com.rajaratabank.exceptions.OverdraftLimitExceededException;
import com.rajaratabank.interfaces.TransactionProcessor;
import com.rajaratabank.models.BankAccount;
import com.rajaratabank.models.Transaction;
import com.rajaratabank.security.FraudDetector;

import java.util.UUID;

/**
 * Processes core operations (deposit, withdrawal, transfer).
 */
public class TransactionEngine implements TransactionProcessor {

    private FraudDetector fraudDetector;

    public TransactionEngine() {
        this.fraudDetector = new FraudDetector();
    }

    @Override
    public boolean processTransaction(String accountId, double amount, String type) {
        return false; // Inherited placeholder, operational actions utilize rich models below.
    }

    public Transaction processDeposit(BankAccount account, double amount) {
        String txId = UUID.randomUUID().toString();
        Transaction transaction = new Transaction(txId, account.getAccountId(), "DEPOSIT", amount, "PENDING");
        
        account.deposit(amount);
        transaction.setStatus("COMPLETED");
        
        saveTransactionToFirebase(transaction);
        return transaction;
    }

    public Transaction processWithdrawal(BankAccount account, double amount) throws InsufficientFundsException, OverdraftLimitExceededException {
        String txId = UUID.randomUUID().toString();
        Transaction transaction = new Transaction(txId, account.getAccountId(), "WITHDRAWAL", amount, "PENDING");

        if (fraudDetector.isFraudulent(transaction)) {
            fraudDetector.flagTransaction(transaction);
            saveTransactionToFirebase(transaction);
            return transaction;
        }

        double initialBalance = account.getBalance();
        
        try {
            account.withdraw(amount);
            if (account.getBalance() == initialBalance) {
                // If balance is unchanged, the specific subclass blocked the withdrawal
                transaction.setStatus("FAILED");
                fraudDetector.isFraudulent(transaction); 
                throw new InsufficientFundsException("Withdrawal rejected due to overdraft or generic insufficient funds.");
            }
            transaction.setStatus("COMPLETED");
        } catch (Exception e) {
            transaction.setStatus("FAILED");
            fraudDetector.isFraudulent(transaction); 
            throw new InsufficientFundsException(e.getMessage());
        }

        saveTransactionToFirebase(transaction);
        return transaction;
    }

    public Transaction processTransfer(BankAccount fromAccount, BankAccount toAccount, double amount) throws InsufficientFundsException {
        String txId = UUID.randomUUID().toString();
        Transaction transaction = new Transaction(txId, fromAccount.getAccountId(), "TRANSFER", amount, "PENDING");

        if (fraudDetector.isFraudulent(transaction)) {
            fraudDetector.flagTransaction(transaction);
            saveTransactionToFirebase(transaction);
            return transaction;
        }
        
        double initialBalance = fromAccount.getBalance();
        fromAccount.withdraw(amount);
        
        if (fromAccount.getBalance() == initialBalance) {
            transaction.setStatus("FAILED");
            fraudDetector.isFraudulent(transaction);
            throw new InsufficientFundsException("Transfer failed: Insufficient funds in origin account.");
        }

        // Apply Multi-Currency Conversion logically if types differ before adding to Target.
        double amountToDeposit = amount;
        if (!fromAccount.getCurrencyCode().equals(toAccount.getCurrencyCode())) {
            amountToDeposit = convertCurrency(amount, fromAccount.getCurrencyCode(), toAccount.getCurrencyCode());
        }

        toAccount.deposit(amountToDeposit);
        transaction.setStatus("COMPLETED");
        
        saveTransactionToFirebase(transaction);
        return transaction;
    }

    private double convertCurrency(double amount, String fromCurrency, String toCurrency) {
        // Base simplistic standard utilizing USD benchmark
        double fromRate = getExchangeRate(fromCurrency);
        double toRate = getExchangeRate(toCurrency);
        double amountInUSD = amount / fromRate;
        return amountInUSD * toRate;
    }

    private double getExchangeRate(String currency) {
        switch (currency) {
            case "USD": return 1.0;
            case "EUR": return 0.92;
            case "LKR": return 300.0;
            case "GBP": return 0.79;
            default: return 1.0;
        }
    }

    private void saveTransactionToFirebase(Transaction transaction) {
        System.out.println("Syncing Transaction " + transaction.getTransactionId() + " [" + transaction.getType() + "] to Firebase.");
    }
}
