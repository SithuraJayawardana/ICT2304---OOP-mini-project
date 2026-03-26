package com.rajaratabank.services;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rajaratabank.models.BankAccount;
import com.rajaratabank.models.accounts.CheckingAccount;
import com.rajaratabank.models.accounts.SavingsAccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class AccountService {

    public static void saveAccount(BankAccount account) {
        try {
            Map<String, Object> accountData = new HashMap<>();
            accountData.put("accountId", account.getAccountId());
            accountData.put("ownerId", account.getOwnerId());
            accountData.put("balance", account.getBalance());
            accountData.put("currencyCode", account.getCurrencyCode());

            if (account instanceof SavingsAccount) {
                accountData.put("type", "Savings");
            } else if (account instanceof CheckingAccount) {
                accountData.put("type", "Checking");
                accountData.put("overdraftLimit", 500.0); // Defaulting overdraft constraint for Checking Accounts
            }

            com.google.api.core.ApiFuture<Void> apiFuture = FirebaseDatabase.getInstance()
                    .getReference("accounts")
                    .child(account.getAccountId())
                    .setValueAsync(accountData);
            
            // Do not block the UI Thread. Provide non-obstructive console warnings on strict timeouts.
            apiFuture.addListener(() -> {
                try {
                    apiFuture.get();
                    System.out.println("Account " + account.getAccountId() + " successfully permanently saved to Firebase.");
                } catch (Exception e) {
                    System.err.println("Firebase Background Save Failed for " + account.getAccountId() + ": " + e.getMessage());
                }
            }, com.google.common.util.concurrent.MoreExecutors.directExecutor());
        } catch (Exception e) {
            System.err.println("Failed to save account to database: " + e.getMessage());
        }
    }

    public static CompletableFuture<List<BankAccount>> getAccountsForUser(String userId) {
        CompletableFuture<List<BankAccount>> future = new CompletableFuture<>();

        try {
            FirebaseDatabase.getInstance()
                    .getReference("accounts")
                    .orderByChild("ownerId")
                    .equalTo(userId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<BankAccount> accounts = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                try {
                                    String id = snapshot.child("accountId").getValue(String.class);
                                    String type = snapshot.child("type").getValue(String.class);
                                    Double balance = snapshot.child("balance").getValue(Double.class);
                                    String curr = snapshot.child("currencyCode").getValue(String.class);

                                    if (id == null || type == null) continue;
                                    if (balance == null) balance = 0.0;
                                    if (curr == null) curr = "LKR";

                                    if (type.equals("Savings")) {
                                        accounts.add(new SavingsAccount(id, userId, balance, curr));
                                    } else if (type.equals("Checking")) {
                                        Double odLimit = snapshot.child("overdraftLimit").getValue(Double.class);
                                        if (odLimit == null) odLimit = 500.0;
                                        accounts.add(new CheckingAccount(id, userId, balance, curr, odLimit));
                                    }
                                } catch (Exception e) {
                                    System.err.println("Error parsing account snapshot: " + e.getMessage());
                                }
                            }
                            future.complete(accounts);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.err.println("Firebase Query Cancelled: " + databaseError.getMessage());
                            future.complete(new ArrayList<>());
                        }
                    });
        } catch (Exception e) {
            System.err.println("Firebase getAccounts operation failed: " + e.getMessage());
            // Return empty list if Firebase is offline
            future.complete(new ArrayList<>());
        }

        // Fallback to an empty list if Firebase is unresponsive or offline after 3 seconds
        return future.completeOnTimeout(new ArrayList<>(), 3, java.util.concurrent.TimeUnit.SECONDS);
    }
}
