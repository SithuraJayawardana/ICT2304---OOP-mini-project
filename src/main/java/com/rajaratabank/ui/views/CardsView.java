package com.rajaratabank.ui.views;

import com.rajaratabank.models.BankAccount;
import com.rajaratabank.ui.SessionManager;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

public class CardsView {
    private VBox view;
    private VBox cardsContainer;

    public CardsView() {
        view = new VBox(30);
        view.getStyleClass().add("main-content");
        view.setPadding(new Insets(40));

        Label lblTitle = new Label("Your Bank Accounts");
        lblTitle.getStyleClass().add("card-title");
        lblTitle.setStyle("-fx-font-size: 24px;");
        
        javafx.scene.control.Button btnAdd = new javafx.scene.control.Button("+ Open New Account");
        btnAdd.getStyleClass().add("action-btn");
        btnAdd.setStyle("-fx-background-color: #10B981; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16 8 16; -fx-cursor: hand;");
        btnAdd.setOnAction(e -> handleAddAccount());

        javafx.scene.layout.HBox header = new javafx.scene.layout.HBox(20);
        header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        header.getChildren().addAll(lblTitle, btnAdd);
        view.getChildren().add(header);

        // Sub-container specifically for re-rendering dynamically
        cardsContainer = new VBox(30);
        view.getChildren().add(cardsContainer);
        
        renderCards();
    }

    private void renderCards() {
        cardsContainer.getChildren().clear(); // Wipe the current list to refresh seamlessly
        List<BankAccount> accounts = SessionManager.getInstance().getProfile().getAccounts();
        
        for (BankAccount acc : accounts) {
            VBox card = new VBox(15);
            card.getStyleClass().addAll("glass-card", "balance-card");
            card.setMaxWidth(450);
            card.setPadding(new Insets(30));
            
            Label lblType = new Label(acc.getClass().getSimpleName().replace("Account", " Account"));
            lblType.setStyle("-fx-text-fill: #94A3B8; -fx-font-weight: bold; -fx-font-size: 14px; text-transform: uppercase;");
            
            Label lblBal = new Label(String.format("Rs. %,.2f", acc.getBalance()));
            lblBal.getStyleClass().add("balance-amount");
            
            Label lblAccId = new Label(acc.getAccountId() + " • " + acc.getCurrencyCode());
            lblAccId.getStyleClass().add("account-number");
            
            javafx.scene.control.Button btnClose = new javafx.scene.control.Button("Close Account");
            btnClose.setStyle("-fx-background-color: transparent; -fx-text-fill: #EF4444; -fx-border-color: #EF4444; -fx-border-radius: 4px; -fx-cursor: hand;");
            btnClose.setOnAction(e -> {
                try {
                    SessionManager.getInstance().getProfile().removeAccount(acc);
                    renderCards(); // Instantly update view bounds
                    
                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Account Successfully Dedicated");
                    alert.setContentText("Account has been completely destructed from your active array.");
                    alert.showAndWait();
                } catch (Exception ex) {
                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                    alert.setHeaderText("Account Closure Blocked");
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
                }
            });

            javafx.scene.layout.HBox bottomRow = new javafx.scene.layout.HBox(10);
            bottomRow.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
            javafx.scene.layout.HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
            bottomRow.getChildren().addAll(lblAccId, spacer, btnClose);
            
            card.getChildren().addAll(lblType, lblBal, bottomRow);
            cardsContainer.getChildren().add(card);
        }
    }

    private void handleAddAccount() {
        javafx.scene.control.ChoiceDialog<String> dialog = new javafx.scene.control.ChoiceDialog<>("Savings", java.util.Arrays.asList("Savings", "Checking"));
        dialog.setTitle("Open New Account");
        dialog.setHeaderText("Select Account Type");
        dialog.setContentText("Account Type:");
        
        dialog.showAndWait().ifPresent(type -> {
            String newId = "ACC-" + java.util.UUID.randomUUID().toString().substring(0, 4).toUpperCase();
            String customerId = SessionManager.getInstance().getActiveUser().getId();
            
            BankAccount newAcc;
            if (type.equals("Savings")) {
                newAcc = new com.rajaratabank.models.accounts.SavingsAccount(newId, customerId, 500.0, "LKR"); 
            } else {
                newAcc = new com.rajaratabank.models.accounts.CheckingAccount(newId, customerId, 0.0, "LKR", 500.0);
            }
            
            SessionManager.getInstance().getProfile().addAccount(newAcc);
            com.rajaratabank.services.AccountService.saveAccount(newAcc);
            renderCards(); // Immediately refresh the view bounds showing the latest OOP state metrics
            
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setHeaderText("Account Generated");
            alert.setContentText("Your new " + type + " Account (" + newId + ") has been securely modeled to your identity!");
            alert.showAndWait();
        });
    }

    public VBox getView() { return view; }
}
