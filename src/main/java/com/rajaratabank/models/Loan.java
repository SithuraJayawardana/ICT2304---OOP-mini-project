package com.rajaratabank.models;

import java.util.UUID;

/**
 * Represents a discrete customer loan.
 * Capable of automatically calculating schedules and tracking metrics.
 */
public class Loan {
    private String loanId;
    private String customerId;
    private double principal;
    private double interestRate;
    private int termMonths;
    private double remainingBalance;
    private String status; // PENDING, APPROVED, REJECTED, ACTIVE, PAID_OFF

    private static final double LATE_PENALTY_FEE = 50.0;

    public Loan(String customerId, double principal, double interestRate, int termMonths) {
        this.loanId = UUID.randomUUID().toString();
        this.customerId = customerId;
        this.principal = principal;
        this.interestRate = interestRate;
        this.termMonths = termMonths;
        
        // Simplified total lifecycle repayment metric
        this.remainingBalance = principal + (principal * interestRate * (termMonths / 12.0));
        this.status = "PENDING";
    }

    /**
     * Determines required scheduled monthly repayment mapping.
     */
    public double calculateMonthlyRepayment() {
        if (termMonths <= 0) return 0;
        double totalRepaymentAmount = principal + (principal * interestRate * (termMonths / 12.0));
        return totalRepaymentAmount / termMonths;
    }

    /**
     * Incurs a late penalty fee against the remaining sum.
     */
    public void applyLatePenalty() {
        if ("ACTIVE".equals(this.status)) {
            this.remainingBalance += LATE_PENALTY_FEE;
            System.out.println("Late penalty of $" + LATE_PENALTY_FEE + " applied to active Loan " + loanId);
        }
    }

    public void makePayment(double amount) {
        if (amount > 0 && this.remainingBalance > 0) {
            this.remainingBalance -= amount;
            if (this.remainingBalance <= 0) {
                this.remainingBalance = 0;
                this.status = "PAID_OFF";
            }
        }
    }

    public String getLoanId() { return loanId; }
    public String getCustomerId() { return customerId; }
    public double getPrincipal() { return principal; }
    public double getInterestRate() { return interestRate; }
    public int getTermMonths() { return termMonths; }
    public double getRemainingBalance() { return remainingBalance; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
