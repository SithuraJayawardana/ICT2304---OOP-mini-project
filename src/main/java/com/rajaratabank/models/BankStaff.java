package com.rajaratabank.models;

/**
 * Represents a Banking Staff member.
 * Demonstrates OOP Inheritance by inheriting from the User abstract class.
 */
public class BankStaff extends User {
    private String branchCode;
    private String position;

    public BankStaff(String id, String name, String email, String passwordHash, String branchCode, String position) {
        super(id, name, email, passwordHash);
        this.branchCode = branchCode;
        this.position = position;
    }

    public String getBranchCode() { return branchCode; }
    public void setBranchCode(String branchCode) { this.branchCode = branchCode; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
}
