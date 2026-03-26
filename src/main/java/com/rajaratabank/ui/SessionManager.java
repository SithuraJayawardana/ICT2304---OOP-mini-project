package com.rajaratabank.ui;

import com.rajaratabank.models.BankAccount;
import com.rajaratabank.models.Customer;
import com.rajaratabank.models.CustomerProfile;
import com.rajaratabank.services.TransactionEngine;

/**
 * Singleton class to persist state across JavaFX Views.
 */
public class SessionManager {
    private static SessionManager instance;
    
    private Customer activeUser;
    private CustomerProfile profile;
    private BankAccount activeAccount;
    private TransactionEngine engine;

    private SessionManager() {
        engine = new TransactionEngine();
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void startSession(Customer user, CustomerProfile prof) {
        this.activeUser = user;
        this.profile = prof;
        if (!prof.getAccounts().isEmpty()) {
            this.activeAccount = prof.getAccounts().get(0);
        }
    }

    public void logout() {
        activeUser = null;
        profile = null;
        activeAccount = null;
    }

    public Customer getActiveUser() { return activeUser; }
    public CustomerProfile getProfile() { return profile; }
    public BankAccount getActiveAccount() { return activeAccount; }
    
    public void setActiveAccount(BankAccount account) {
        this.activeAccount = account;
    }
    
    public TransactionEngine getEngine() { return engine; }
}
