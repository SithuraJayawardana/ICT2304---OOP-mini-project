package com.rajaratabank.models;

import java.util.List;

/**
 * Encapsulated read footprint defining the account action limits resolving across a defined temporal span.
 */
public class MonthlyAccountStatement {
    private String statementId;
    private String accountId;
    private double startingBalance;
    private double endingBalance;
    private double interestEarned;
    private List<Transaction> transactions;

    public MonthlyAccountStatement(String statementId, String accountId, double startingBalance, double endingBalance, double interestEarned, List<Transaction> transactions) {
        this.statementId = statementId;
        this.accountId = accountId;
        this.startingBalance = startingBalance;
        this.endingBalance = endingBalance;
        this.interestEarned = interestEarned;
        this.transactions = transactions;
    }

    public String getStatementId() { return statementId; }
    public String getAccountId() { return accountId; }
    public double getStartingBalance() { return startingBalance; }
    public double getEndingBalance() { return endingBalance; }
    public double getInterestEarned() { return interestEarned; }
    public List<Transaction> getTransactions() { return transactions; }

    @Override
    public String toString() {
        return "Statement Resource Map: " + statementId + "\n" +
               "Account Metric Context: " + accountId + "\n" +
               "Baseline Starting State: $" + startingBalance + "\n" +
               "Derived Interest Profit: $" + interestEarned + "\n" +
               "Adjusted Ending State: $" + endingBalance + "\n" +
               "Total Active Mapped Steps: " + transactions.size();
    }
}
