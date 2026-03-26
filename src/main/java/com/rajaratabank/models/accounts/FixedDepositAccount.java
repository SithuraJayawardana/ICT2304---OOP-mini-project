package com.rajaratabank.models.accounts;

import com.rajaratabank.models.BankAccount;
import java.time.LocalDate;

/**
 * Fixed Deposit Account featuring dynamic higher interest, restricting additions, 
 * and applying stringent early-withdrawal penalties.
 */
public class FixedDepositAccount extends BankAccount {
    private double interestRate;
    private LocalDate maturityDate;
    private static final double PENALTY_RATE = 0.05; // 5% penalty for early withdrawal

    public FixedDepositAccount(String accountId, String ownerId, double initialBalance, String currencyCode, double interestRate, int termMonths) {
        super(accountId, ownerId, initialBalance, currencyCode);
        this.interestRate = interestRate;
        this.maturityDate = LocalDate.now().plusMonths(termMonths);
    }

    @Override
    public void deposit(double amount) {
        System.err.println("Deposits are not allowed in Fixed Deposit accounts after initialization.");
    }

    @Override
    public void withdraw(double amount) {
        if (amount > 0 && this.balance >= amount) {
            if (LocalDate.now().isBefore(maturityDate)) {
                System.out.println("Warning: Early withdrawal penalty applied.");
                double penalty = amount * PENALTY_RATE;
                this.balance -= (amount + penalty);
            } else {
                this.balance -= amount;
            }
        } else {
            System.err.println("Withdrawal failed: Insufficient funds.");
        }
    }

    @Override
    public double calculateInterest() {
        return this.balance * interestRate;
    }
}
