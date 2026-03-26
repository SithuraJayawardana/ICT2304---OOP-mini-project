package com.rajaratabank.ui.views;

import com.rajaratabank.models.Customer;
import com.rajaratabank.models.CustomerProfile;
import com.rajaratabank.models.accounts.SavingsAccount;
import com.rajaratabank.ui.BankUI;
import com.rajaratabank.ui.SessionManager;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class LoginView {
    
    private VBox view;

    public LoginView() {
        view = new VBox(20);
        view.setAlignment(Pos.CENTER);
        view.getStyleClass().add("app-container");
        view.setStyle("-fx-background-color: #0F172A;");
        
        VBox loginBox = new VBox(25);
        loginBox.getStyleClass().add("glass-card");
        loginBox.setMaxWidth(400);
        loginBox.setAlignment(Pos.CENTER);

        Label lblTitle = new Label("🎓 Rajarata Bank");
        lblTitle.getStyleClass().add("logo");
        lblTitle.setStyle("-fx-font-size: 28px; -fx-padding: 0 0 10 0;");

        Label lblSubtitle = new Label("Secure Digital Banking Core");
        lblSubtitle.getStyleClass().add("welcome-subtitle");
        lblSubtitle.setStyle("-fx-padding: 0 0 20 0;");

        TextField txtEmail = new TextField("amal@example.com");
        txtEmail.setPromptText("Email Address");
        txtEmail.setPrefHeight(45);
        txtEmail.setStyle("-fx-background-color: rgba(255,255,255,0.05); -fx-text-fill: white; -fx-border-color: rgba(255,255,255,0.1); -fx-border-radius: 8px; -fx-padding: 0 15 0 15;");

        PasswordField txtPass = new PasswordField();
        txtPass.setText("password123");
        txtPass.setPromptText("Password");
        txtPass.setPrefHeight(45);
        txtPass.setStyle("-fx-background-color: rgba(255,255,255,0.05); -fx-text-fill: white; -fx-border-color: rgba(255,255,255,0.1); -fx-border-radius: 8px; -fx-padding: 0 15 0 15;");

        Button btnLogin = new Button("Secure Login");
        btnLogin.getStyleClass().add("action-btn");
        btnLogin.setStyle("-fx-background-color: #3B82F6; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        btnLogin.setPrefWidth(Double.MAX_VALUE);
        btnLogin.setPrefHeight(50);
        btnLogin.setOnAction(e -> processLogin(txtEmail.getText(), txtPass.getText()));

        loginBox.getChildren().addAll(lblTitle, lblSubtitle, txtEmail, txtPass, btnLogin);
        view.getChildren().add(loginBox);
    }

    private void processLogin(String email, String password) {
        try {
            // Connect Login Action directly to your OOP Authentication module
            com.rajaratabank.security.AuthManager auth = new com.rajaratabank.security.AuthManager();
            auth.login(email, password); 

            // Simulating successful response mapping structurally
            Customer customer = new Customer("C001", "Amal Perera", email, "0771234567", password);
            CustomerProfile profile = new CustomerProfile(customer);
            
            // Demonstrating Phase 3 OOP Composition: Adding multiple accounts to one profile
            profile.addAccount(new SavingsAccount("ACC-001", customer.getId(), 5000.0, "LKR"));
            profile.addAccount(new com.rajaratabank.models.accounts.CheckingAccount("ACC-002", customer.getId(), 15500.0, "USD", 1000.0));
            
            SessionManager.getInstance().startSession(customer, profile);
            BankUI.loadDashboard();
            
        } catch (com.rajaratabank.exceptions.InvalidLoginException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Authentication Error");
            alert.setHeaderText("Invalid Credentials");
            alert.setContentText(ex.getMessage() + "\n\nReview local audit_security_log.txt for compliance trails.");
            alert.showAndWait();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("System Error");
            alert.setHeaderText("Login Failed");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }

    public VBox getView() {
        return view;
    }
}
