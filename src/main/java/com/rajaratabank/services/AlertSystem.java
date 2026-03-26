package com.rajaratabank.services;

import com.rajaratabank.models.User;
import com.rajaratabank.models.BankAccount;
import com.rajaratabank.models.Transaction;
import com.rajaratabank.models.Loan;

/**
 * Handles generalized alert dispatching and mappings targeting specific isolated events.
 */
public class AlertSystem {

    public void notifyLowBalance(User user, BankAccount account) {
        if (account.getBalance() < 100.0) { 
            String message = "CRITICAL ALERT: Your specific mapped account " + account.getAccountId() + " has critically compromised balance metrics reading $" + account.getBalance();
            storeAlertInFirebase(user.getId(), message);
            System.out.println("Notification metric generated targeting " + user.getName() + ": " + message);
        }
    }

    public void notifyTransaction(User user, Transaction transaction) {
        String message = "TRANSACTION UPDATE: Record " + transaction.getTransactionId() + " (" + transaction.getType() + ") resolved state is " + transaction.getStatus() + " applied for total sum $" + transaction.getAmount();
        storeAlertInFirebase(user.getId(), message);
        System.out.println("Notification metric generated targeting " + user.getName() + ": " + message);
    }

    public void notifyUpcomingLoanInstallment(User user, Loan loan) {
        String message = "REMINDER MAP: Schedule designates your loan installment configuring $" + String.format("%.2f", loan.calculateMonthlyRepayment()) + " is actively due connected to Loan " + loan.getLoanId();
        storeAlertInFirebase(user.getId(), message);
        System.out.println("Notification metric generated targeting " + user.getName() + ": " + message);
    }

    public void notifyBillDeadline(User user, String billerName, String dueDate) {
        String message = "DEADLINE WARNING: System maps standard configuration bill tracking " + billerName + " due strict on " + dueDate;
        storeAlertInFirebase(user.getId(), message);
        System.out.println("Notification metric generated targeting " + user.getName() + ": " + message);
    }

    /**
     * Integrates arbitrary simulated maps to generic user records.
     */
    private void storeAlertInFirebase(String userId, String message) {
        // Pseudo execution to insert alert state for targeted UID DB space.
    }
}
