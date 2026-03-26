package com.rajaratabank.models;

/**
 * Represents a System Administrator with elevated privileges.
 * Demonstrates OOP Inheritance by inheriting from User.
 */
public class Administrator extends User {
    private String adminRole; // e.g., SuperAdmin, SecurityAdmin

    public Administrator(String id, String name, String email, String passwordHash, String adminRole) {
        super(id, name, email, passwordHash);
        this.adminRole = adminRole;
    }

    public String getAdminRole() { return adminRole; }
    public void setAdminRole(String adminRole) { this.adminRole = adminRole; }
}
