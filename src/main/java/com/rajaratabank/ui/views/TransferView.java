package com.rajaratabank.ui.views;

import com.rajaratabank.ui.SessionManager;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class TransferView {
    private VBox view;

    public TransferView() {
        view = new VBox(20);
        view.getStyleClass().add("main-content");
        view.setPadding(new Insets(40));

        Label lblTitle = new Label("Secure Fund Transfer");
        lblTitle.getStyleClass().add("card-title");
        lblTitle.setStyle("-fx-font-size: 24px; -fx-padding: 0 0 20 0;");

        VBox formCard = new VBox(25);
        formCard.getStyleClass().add("glass-card");
        formCard.setMaxWidth(500);

        // --- Dynamic Account Selector ---
        javafx.scene.layout.VBox selectorBox = new javafx.scene.layout.VBox(10);
        Label lblSelect = new Label("Transferring From (Source):");
        lblSelect.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 14px;");
        
        javafx.scene.control.ComboBox<com.rajaratabank.models.BankAccount> accSelector = new javafx.scene.control.ComboBox<>();
        accSelector.getItems().addAll(SessionManager.getInstance().getProfile().getAccounts());
        accSelector.setConverter(new javafx.util.StringConverter<com.rajaratabank.models.BankAccount>() {
            @Override
            public String toString(com.rajaratabank.models.BankAccount object) {
                return object.getAccountId() + " - " + object.getClass().getSimpleName().replace("Account", " Account");
            }
            @Override
            public com.rajaratabank.models.BankAccount fromString(String string) { return null; }
        });
        accSelector.setValue(SessionManager.getInstance().getActiveAccount());
        accSelector.setStyle("-fx-background-color: rgba(255,255,255,0.05); -fx-text-fill: white; -fx-border-color: rgba(255,255,255,0.1); -fx-border-radius: 4px; -fx-pref-width: 450px; -fx-pref-height: 45px;");
        
        accSelector.setOnAction(e -> {
            SessionManager.getInstance().setActiveAccount(accSelector.getValue());
        });
        
        selectorBox.getChildren().addAll(lblSelect, accSelector);

        TextField txtDest = new TextField();
        txtDest.setPromptText("Destination Account ID (e.g. ACC-002)");
        txtDest.setStyle("-fx-background-color: rgba(255,255,255,0.05); -fx-text-fill: white; -fx-pref-height: 45px; -fx-border-color: rgba(255,255,255,0.1); -fx-border-radius: 8px; -fx-padding: 0 15 0 15;");

        TextField txtAmount = new TextField();
        txtAmount.setPromptText("Amount (Rs.)");
        txtAmount.setStyle("-fx-background-color: rgba(255,255,255,0.05); -fx-text-fill: white; -fx-pref-height: 45px; -fx-border-color: rgba(255,255,255,0.1); -fx-border-radius: 8px; -fx-padding: 0 15 0 15;");

        Button btnTransfer = new Button("Process Transfer");
        btnTransfer.getStyleClass().add("action-btn");
        btnTransfer.setPrefWidth(Double.MAX_VALUE);
        btnTransfer.setPrefHeight(50);
        btnTransfer.setStyle("-fx-background-color: #3B82F6; -fx-text-fill: white; -fx-font-weight: bold;");
        btnTransfer.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(txtAmount.getText());
                if (txtDest.getText().isEmpty()) throw new Exception("Destination Account is required.");

                // In a multi-user environment, we would load the destination account from the DB.
                // For this demo, we validate structural mapping by withdrawing from the current user account.
                SessionManager.getInstance().getEngine().processWithdrawal(SessionManager.getInstance().getActiveAccount(), amount);
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Transfer Success");
                alert.setHeaderText(null);
                alert.setContentText("Successfully transferred Rs. " + amount + " to " + txtDest.getText());
                alert.showAndWait();
                
                txtDest.clear();
                txtAmount.clear();
            } catch (NumberFormatException ex) {
                showError("Please enter a valid numeric amount.");
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        formCard.getChildren().addAll(lblTitle, txtDest, txtAmount, btnTransfer);
        view.getChildren().add(formCard);
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Transfer Failed");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public VBox getView() { return view; }
}
