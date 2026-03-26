package com.rajaratabank.ui.views;

import com.rajaratabank.exceptions.InsufficientFundsException;
import com.rajaratabank.exceptions.OverdraftLimitExceededException;
import com.rajaratabank.models.BankAccount;
import com.rajaratabank.models.Customer;
import com.rajaratabank.services.TransactionEngine;
import com.rajaratabank.services.UtilityBillPayment;
import com.rajaratabank.ui.SessionManager;

import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Optional;

public class DashboardView {
    private VBox view;
    private Label lblAmount;
    private Label lblAccountNumber;
    
    private Customer activeUser;
    private BankAccount activeAccount;
    private TransactionEngine engine;

    public DashboardView() {
        SessionManager session = SessionManager.getInstance();
        activeUser = session.getActiveUser();
        activeAccount = session.getActiveAccount();
        engine = session.getEngine();

        view = new VBox(30);
        view.getStyleClass().add("main-content");
        view.setPadding(new Insets(40));

        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        
        VBox welcomeBox = new VBox(5);
        String firstName = activeUser.getName().split(" ")[0];
        Label lblWelcome = new Label("Welcome back, " + firstName + "! \uD83D\uDC4B");
        lblWelcome.getStyleClass().add("welcome-title");
        Label lblSubtitle = new Label("Here's your financial overview for today.");
        lblSubtitle.getStyleClass().add("welcome-subtitle");
        welcomeBox.getChildren().addAll(lblWelcome, lblSubtitle);

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        Circle profilePic = new Circle(20, Color.web("#3B82F6"));
        profilePic.getStyleClass().add("profile-pic");

        header.getChildren().addAll(welcomeBox, headerSpacer, profilePic);

        // --- Dynamic Account Selector ---
        HBox selectorBox = new HBox(15);
        selectorBox.setAlignment(Pos.CENTER_LEFT);
        Label lblSelect = new Label("Active Banking Profile:");
        lblSelect.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 14px; -fx-font-weight: bold;");
        
        javafx.scene.control.ComboBox<BankAccount> accSelector = new javafx.scene.control.ComboBox<>();
        accSelector.getItems().addAll(SessionManager.getInstance().getProfile().getAccounts());
        accSelector.setConverter(new javafx.util.StringConverter<BankAccount>() {
            @Override
            public String toString(BankAccount object) {
                return object.getAccountId() + " - " + object.getClass().getSimpleName().replace("Account", " Account");
            }
            @Override
            public BankAccount fromString(String string) { return null; }
        });
        accSelector.setValue(activeAccount);
        accSelector.setStyle("-fx-background-color: rgba(255,255,255,0.05); -fx-text-fill: white; -fx-border-color: rgba(255,255,255,0.1); -fx-border-radius: 4px;");
        
        accSelector.setOnAction(e -> {
            activeAccount = accSelector.getValue();
            SessionManager.getInstance().setActiveAccount(activeAccount);
            updateBalanceUI();
        });
        
        selectorBox.getChildren().addAll(lblSelect, accSelector);

        // Dashboard Grid Section
        HBox dashboardGrid = new HBox(30);

        // Balance Card
        VBox balanceCard = new VBox(10);
        balanceCard.getStyleClass().addAll("glass-card", "balance-card");
        Label lblTotalBalance = new Label("TOTAL BALANCE");
        lblTotalBalance.getStyleClass().add("card-subtitle");
        
        lblAmount = new Label(String.format("Rs. %,.2f", activeAccount.getBalance()));
        lblAmount.getStyleClass().add("balance-amount");
        
        lblAccountNumber = new Label(activeAccount.getAccountId() + " ••• " + activeAccount.getCurrencyCode());
        lblAccountNumber.getStyleClass().add("account-number");
        
        Label lblTrend = new Label("Active");
        lblTrend.getStyleClass().add("trend-positive");
        
        HBox balanceFooter = new HBox(15);
        balanceFooter.getChildren().addAll(lblTrend, lblAccountNumber);
        balanceCard.getChildren().addAll(lblTotalBalance, lblAmount, balanceFooter);
        HBox.setHgrow(balanceCard, Priority.ALWAYS);

        // Quick Actions
        VBox quickActionsCard = new VBox(15);
        quickActionsCard.getStyleClass().addAll("glass-card", "quick-actions-card");
        Label lblActions = new Label("Transaction Engine");
        lblActions.getStyleClass().add("card-title");
        
        GridPane actionGrid = new GridPane();
        actionGrid.setHgap(15);
        actionGrid.setVgap(15);
        
        Button btnDeposit = createActionButton("Deposit");
        btnDeposit.setOnAction(e -> handleDeposit());

        Button btnWithdraw = createActionButton("Withdraw");
        btnWithdraw.setOnAction(e -> handleWithdraw());

        Button btnTransfer = createActionButton("Transfer");
        btnTransfer.setOnAction(e -> handleTransfer());

        Button btnPayBill = createActionButton("Pay Utility Bill");
        btnPayBill.setOnAction(e -> handlePayBill());

        actionGrid.add(btnDeposit, 0, 0);
        actionGrid.add(btnWithdraw, 1, 0);
        actionGrid.add(btnTransfer, 0, 1);
        actionGrid.add(btnPayBill, 1, 1);
        
        quickActionsCard.getChildren().addAll(lblActions, actionGrid);

        dashboardGrid.getChildren().addAll(balanceCard, quickActionsCard);

        // Recent Transactions
        VBox transactionsCard = new VBox(15);
        transactionsCard.getStyleClass().add("glass-card");
        Label lblTransactions = new Label("Transaction History");
        lblTransactions.getStyleClass().add("card-title");

        VBox txnList = new VBox(10);
        txnList.getChildren().addAll(
            createTransactionItem("Electricity Bill", "Today, 10:45 AM", "- Rs. 1,250.00", "negative"),
            createTransactionItem("Initial Deposit", "Yesterday, 04:00 PM", "+ Rs. 5,000.00", "positive")
        );

        transactionsCard.getChildren().addAll(lblTransactions, txnList);

        view.getChildren().addAll(header, selectorBox, dashboardGrid, transactionsCard);
    }

    private void updateBalanceUI() {
        if (lblAmount != null) {
            lblAmount.setText(String.format("Rs. %,.2f", activeAccount.getBalance()));
        }
        if (lblAccountNumber != null) {
            lblAccountNumber.setText(activeAccount.getAccountId() + " ••• " + activeAccount.getCurrencyCode());
        }
    }

    private Button createActionButton(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("action-btn");
        btn.setPrefSize(120, 50);
        return btn;
    }

    private void handleDeposit() {
        processAmountDialog("Deposit", "Deposit Funds to Account", "Enter amount to deposit (Rs.):", (amount) -> {
            engine.processDeposit(activeAccount, amount);
            showSuccess("Successfully deposited Rs. " + amount + "!");
        });
    }

    private void handleWithdraw() {
        processAmountDialog("Withdraw", "Withdraw Funds", "Enter amount to withdraw (Rs.):", (amount) -> {
            try {
                engine.processWithdrawal(activeAccount, amount);
                showSuccess("Successfully withdrew Rs. " + amount + "!");
            } catch (InsufficientFundsException | OverdraftLimitExceededException ex) {
                throw new RuntimeException(ex.getMessage());
            }
        });
    }

    private void handleTransfer() {
        processAmountDialog("Fund Transfer", "Transfer to another account", "Enter amount to transfer (Rs.):", (amount) -> {
            try {
                engine.processWithdrawal(activeAccount, amount);
                showSuccess("Successfully transferred Rs. " + amount + " out of account.");
            } catch (InsufficientFundsException | OverdraftLimitExceededException ex) {
                throw new RuntimeException(ex.getMessage());
            }
        });
    }

    private void handlePayBill() {
        java.util.List<UtilityBillPayment.UtilityType> choices = java.util.Arrays.asList(UtilityBillPayment.UtilityType.values());
        javafx.scene.control.ChoiceDialog<UtilityBillPayment.UtilityType> typeDialog = new javafx.scene.control.ChoiceDialog<>(UtilityBillPayment.UtilityType.ELECTRICITY, choices);
        typeDialog.setTitle("Select Utility");
        typeDialog.setHeaderText("Choose Bill Type");
        typeDialog.setContentText("Utility:");
        
        typeDialog.showAndWait().ifPresent(biller -> {
            processAmountDialog("Pay " + biller.name() + " Bill", "Utility Payment", "Enter bill amount to pay (Rs.):", (amount) -> {
                try {
                    UtilityBillPayment billPayer = new UtilityBillPayment();
                    billPayer.payBill(activeAccount, amount, biller);
                    showSuccess("Successfully paid Rs. " + amount + " for " + biller.name() + "!");
                } catch (InsufficientFundsException ex) {
                    throw new RuntimeException(ex.getMessage());
                }
            });
        });
    }

    private void processAmountDialog(String title, String header, String prompt, java.util.function.Consumer<Double> action) {
        TextInputDialog dialog = new TextInputDialog("1000");
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(prompt);

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            try {
                double amount = Double.parseDouble(result.get());
                action.accept(amount);
                lblAmount.setText(String.format("Rs. %,.2f", activeAccount.getBalance()));
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid numeric amount.");
            } catch (RuntimeException ex) {
                showAlert(Alert.AlertType.ERROR, "Transaction Failed", ex.getMessage());
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.");
            }
        }
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private HBox createTransactionItem(String name, String time, String amount, String amountClass) {
        HBox row = new HBox(15);
        row.getStyleClass().add("transaction-item");
        row.setAlignment(Pos.CENTER_LEFT);

        VBox infoBox = new VBox(3);
        Label lblName = new Label(name);
        lblName.getStyleClass().add("txn-name");
        Label lblTime = new Label(time);
        lblTime.getStyleClass().add("txn-time");
        infoBox.getChildren().addAll(lblName, lblTime);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lblAmount = new Label(amount);
        lblAmount.getStyleClass().addAll("txn-amount", amountClass);

        row.getChildren().addAll(infoBox, spacer, lblAmount);
        return row;
    }

    public VBox getView() {
        return view;
    }
}
