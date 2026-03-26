package com.rajaratabank.ui.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class NotificationsView {
    private VBox view;

    public NotificationsView() {
        view = new VBox(20);
        view.getStyleClass().add("main-content");
        view.setPadding(new Insets(40));

        Label lblTitle = new Label("System Notifications & Alerts");
        lblTitle.getStyleClass().add("card-title");
        lblTitle.setStyle("-fx-font-size: 24px; -fx-padding: 0 0 20 0;");

        VBox listCard = new VBox(15);
        listCard.getStyleClass().add("glass-card");
        listCard.setMaxWidth(800);

        // Rendering simulated alerts reflecting Phase 6 AlertSystem mapping logic
        listCard.getChildren().addAll(
            createAlertItem("CRITICAL ALERT", "Your specific mapped account ACC-001 has critically compromised balance metrics reading $45.50", "negative"),
            createAlertItem("TRANSACTION UPDATE", "Record TX-9912 (BILL_PAYMENT) resolved state is COMPLETED applied for total sum $125.0", "positive"),
            createAlertItem("REMINDER MAP", "Schedule designates your loan installment configuring $150.00 is actively due connected to Loan LN-88A", "negative"),
            createAlertItem("DEADLINE WARNING", "System maps standard configuration bill tracking INTERNET due strict on 2026-04-01", "negative")
        );

        view.getChildren().addAll(lblTitle, listCard);
    }

    private HBox createAlertItem(String type, String message, String typeClass) {
        HBox row = new HBox(15);
        row.getStyleClass().add("transaction-item"); 
        row.setAlignment(Pos.CENTER_LEFT);

        VBox infoBox = new VBox(5);
        Label lblType = new Label(type);
        lblType.getStyleClass().add("txn-name");
        lblType.setStyle("-fx-text-fill: " + (typeClass.equals("negative") ? "#EF4444" : "#10B981") + "; -fx-font-weight: bold; text-transform: uppercase;");
        
        Label lblMsg = new Label(message);
        lblMsg.getStyleClass().add("txn-time");
        lblMsg.setWrapText(true);
        lblMsg.setMaxWidth(600);

        infoBox.getChildren().addAll(lblType, lblMsg);
        row.getChildren().add(infoBox);
        return row;
    }

    public VBox getView() { return view; }
}
