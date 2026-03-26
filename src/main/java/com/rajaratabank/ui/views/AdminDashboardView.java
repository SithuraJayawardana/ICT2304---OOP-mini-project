package com.rajaratabank.ui.views;

import com.rajaratabank.models.Administrator;
import com.rajaratabank.models.MonthlyAccountStatement;
import com.rajaratabank.services.AdminDashboard;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class AdminDashboardView {
    private VBox view;
    private AdminDashboard adminService;

    public AdminDashboardView() {
        Administrator adminUser = new Administrator("ADMIN-01", "System Supervisor", "admin@rajarata.lk", "0770000000", "adminPass");
        adminService = new AdminDashboard(adminUser);

        view = new VBox(25);
        view.getStyleClass().add("main-content");
        view.setPadding(new Insets(40));

        Label lblTitle = new Label("Administrator Dashboard & Compliance");
        lblTitle.getStyleClass().add("card-title");
        lblTitle.setStyle("-fx-font-size: 24px; -fx-padding: 0 0 10 0;");

        VBox statementCard = new VBox(20);
        statementCard.getStyleClass().add("glass-card");
        statementCard.setMaxWidth(600);
        
        Label lblStatement = new Label("Generate Monthly Account Statement");
        lblStatement.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");
        
        TextField txtAccountId = new TextField();
        txtAccountId.setPromptText("Enter Target Account ID (e.g., ACC-001)");
        txtAccountId.setStyle("-fx-background-color: rgba(255,255,255,0.05); -fx-text-fill: white; -fx-pref-height: 45px; -fx-border-color: rgba(255,255,255,0.1); -fx-border-radius: 8px; -fx-padding: 0 15 0 15;");

        Button btnGenerate = new Button("Run Firebase Query");
        btnGenerate.getStyleClass().add("action-btn");
        btnGenerate.setOnAction(e -> {
            if(txtAccountId.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please provide a target Account ID.");
                return;
            }
            MonthlyAccountStatement statement = adminService.generateMonthlyAccountStatement(txtAccountId.getText());
            showAlert(Alert.AlertType.INFORMATION, "Statement Generated", "Statement ID: " + statement.getStatementId() + "\nEnding Balance: Rs. " + statement.getEndingBalance() + "\nReview Java console for full nested breakdown.");
        });

        statementCard.getChildren().addAll(lblStatement, txtAccountId, btnGenerate);

        VBox exportCard = new VBox(20);
        exportCard.getStyleClass().add("glass-card");
        exportCard.setMaxWidth(600);

        Label lblExport = new Label("Compliance File Handling (I/O)");
        lblExport.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");
        
        Label lblDesc = new Label("Exports system-wide compliance reports and customer activity tracking directly to a local .csv format.");
        lblDesc.setStyle("-fx-text-fill: #94A3B8;");
        lblDesc.setWrapText(true);

        Button btnExport = new Button("Generate CSV Report");
        btnExport.getStyleClass().add("action-btn");
        btnExport.setStyle("-fx-background-color: #10B981; -fx-text-fill: white; -fx-font-weight: bold;");
        btnExport.setPrefHeight(45);
        btnExport.setOnAction(e -> {
            try {
                adminService.exportSystemComplianceReport();
                showAlert(Alert.AlertType.INFORMATION, "Export Successful", "system_compliance_report.csv has been successfully written to the project root directory using Java File I/O!");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Export Failed", ex.getMessage());
            }
        });

        exportCard.getChildren().addAll(lblExport, lblDesc, btnExport);
        view.getChildren().addAll(lblTitle, statementCard, exportCard);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public VBox getView() { return view; }
}
