package com.rajaratabank.services;

import com.rajaratabank.models.BankStaff;
import com.rajaratabank.models.Customer;
import com.rajaratabank.models.Loan;
import com.rajaratabank.models.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages loan workflow generation, permissions, and status mapping.
 */
public class LoanManager {
    // Isolated system reference simulated to reflect Firebase storage
    private Map<String, Loan> systemLoans = new HashMap<>();

    public Loan requestLoan(Customer customer, double principal, double interestRate, int termMonths) {
        Loan newLoan = new Loan(customer.getId(), principal, interestRate, termMonths);
        systemLoans.put(newLoan.getLoanId(), newLoan);
        System.out.println("Loan request " + newLoan.getLoanId() + " generated for Customer " + customer.getId());
        saveLoanToFirebase(newLoan);
        return newLoan;
    }

    public void approveLoan(User staffUser, String loanId) {
        if (!(staffUser instanceof BankStaff)) {
            System.err.println("Unauthorized: Administrative Bank Staff privileges required to approve loans.");
            return;
        }

        Loan loan = systemLoans.get(loanId);
        if (loan != null && "PENDING".equals(loan.getStatus())) {
            loan.setStatus("ACTIVE");
            System.out.println("Loan " + loanId + " approved and instantiated by Staff " + staffUser.getId());
            saveLoanToFirebase(loan);
        } else {
            System.err.println("Loan context unavailable or invalid pending state observed.");
        }
    }

    public void rejectLoan(User staffUser, String loanId) {
        if (!(staffUser instanceof BankStaff)) {
            System.err.println("Unauthorized: Bank Staff permission limits missing.");
            return;
        }

        Loan loan = systemLoans.get(loanId);
        if (loan != null && "PENDING".equals(loan.getStatus())) {
            loan.setStatus("REJECTED");
            System.out.println("Loan " + loanId + " decisively rejected by Staff " + staffUser.getId());
            saveLoanToFirebase(loan);
        }
    }

    private void saveLoanToFirebase(Loan loan) {
        System.out.println("Syncing complex Loan configuration for " + loan.getLoanId() + " to simulated Firebase Firestore mapping.");
    }
}
