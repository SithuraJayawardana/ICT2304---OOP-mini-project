package com.rajaratabank.ui.views;

import com.rajaratabank.models.Customer;
import com.rajaratabank.models.Loan;
import com.rajaratabank.services.LoanManager;
import com.rajaratabank.ui.SessionManager;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class LoansView {
    private VBox view;
    private LoanManager loanManager;

    public LoansView() {
        loanManager = new LoanManager();
        
        view = new VBox(20);
        view.getStyleClass().add("main-content");
        view.setPadding(new Insets(40));

        Label lblTitle = new Label("Request a New System Loan");
        lblTitle.getStyleClass().add("card-title");
        lblTitle.setStyle("-fx-font-size: 24px; -fx-padding: 0 0 20 0;");

        VBox formCard = new VBox(25);
        formCard.getStyleClass().add("glass-card");
        formCard.setMaxWidth(500);

        TextField txtPrincipal = new TextField();
        txtPrincipal.setPromptText("Loan Principal Amount (Rs.)");
        txtPrincipal.setStyle("-fx-background-color: rgba(255,255,255,0.05); -fx-text-fill: white; -fx-pref-height: 45px; -fx-border-color: rgba(255,255,255,0.1); -fx-border-radius: 8px; -fx-padding: 0 15 0 15;");

        TextField txtTerm = new TextField();
        txtTerm.setPromptText("Term Duration (Months) e.g. 12");
        txtTerm.setStyle("-fx-background-color: rgba(255,255,255,0.05); -fx-text-fill: white; -fx-pref-height: 45px; -fx-border-color: rgba(255,255,255,0.1); -fx-border-radius: 8px; -fx-padding: 0 15 0 15;");

        Button btnRequest = new Button("Submit Formal Application");
        btnRequest.getStyleClass().add("action-btn");
        btnRequest.setPrefWidth(Double.MAX_VALUE);
        btnRequest.setPrefHeight(50);
        btnRequest.setStyle("-fx-background-color: #3B82F6; -fx-text-fill: white; -fx-font-weight: bold;");
        
        btnRequest.setOnAction(e -> {
            try {
                double principal = Double.parseDouble(txtPrincipal.getText());
                int term = Integer.parseInt(txtTerm.getText());
                double defaultInterestRate = 0.05; // 5% base rate modeled
                
                Customer activeUser = SessionManager.getInstance().getActiveUser();
                Loan newLoan = loanManager.requestLoan(activeUser, principal, defaultInterestRate, term);
                
                double monthlyRepayment = newLoan.calculateMonthlyRepayment();
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Loan Application Submitted");
                alert.setHeaderText("Status: " + newLoan.getStatus());
                alert.setContentText("Your system loan for Rs. " + principal + " has been logged successfully.\n\n" + 
                                     "Calculated Minimum Monthly Repayment: Rs. " + String.format("%.2f", monthlyRepayment) + "\n\n" +
                                     "Pending formal BankStaff approval mapping.");
                alert.showAndWait();
                
                txtPrincipal.clear();
                txtTerm.clear();
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input Matrix");
                alert.setContentText("Please provide numeric structural boundaries for Principal and Months.");
                alert.showAndWait();
            }
        });

        formCard.getChildren().addAll(lblTitle, txtPrincipal, txtTerm, btnRequest);
        view.getChildren().add(formCard);
    }

    public VBox getView() { return view; }
}
