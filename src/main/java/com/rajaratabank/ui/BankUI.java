package com.rajaratabank.ui;

import com.rajaratabank.ui.views.CardsView;
import com.rajaratabank.ui.views.DashboardView;
import com.rajaratabank.ui.views.LoginView;
import com.rajaratabank.ui.views.TransferView;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BankUI extends Application {

    private static Scene mainScene;
    private static BorderPane appLayout;
    private static VBox sidebar;

    @Override
    public void start(Stage primaryStage) {
        try {
            com.rajaratabank.config.FirebaseConfig.initialize();
            System.out.println("Firebase Runtime Configuration Booted Successfully.");
        } catch (Exception e) {
            System.err.println("Firebase Initialization Deferred: Running Local Sandbox Simulator (" + e.getMessage() + ")");
        }

        primaryStage.setTitle("Rajarata Digital Bank");

        LoginView login = new LoginView();
        mainScene = new Scene(login.getView(), 1000, 700);
        
        loadCss(mainScene);

        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public static void loadDashboard() {
        appLayout = new BorderPane();
        appLayout.getStyleClass().add("app-container");
        
        sidebar = createSidebar();
        appLayout.setLeft(sidebar);
        
        DashboardView dash = new DashboardView();
        appLayout.setCenter(dash.getView());

        mainScene.setRoot(appLayout);
    }

    public static void switchView(javafx.scene.Node newView) {
        if (appLayout != null) {
            appLayout.setCenter(newView);
        }
    }

    public static void logout() {
        SessionManager.getInstance().logout();
        LoginView login = new LoginView();
        mainScene.setRoot(login.getView());
    }

    private static VBox createSidebar() {
        VBox side = new VBox(20);
        side.getStyleClass().add("sidebar");
        side.setPadding(new Insets(30, 20, 30, 20));

        Label logoLabel = new Label("🎓 Rajarata Bank");
        logoLabel.getStyleClass().add("logo");

        Button btnDashboard = createSidebarButton("Dashboard");
        btnDashboard.setOnAction(e -> switchView(new DashboardView().getView()));

        Button btnAccounts = createSidebarButton("Accounts");
        btnAccounts.setOnAction(e -> switchView(new CardsView().getView()));

        Button btnTransfers = createSidebarButton("Transfers");
        btnTransfers.setOnAction(e -> switchView(new TransferView().getView()));

        Button btnLoans = createSidebarButton("Loans");
        btnLoans.setOnAction(e -> switchView(new com.rajaratabank.ui.views.LoansView().getView()));

        Button btnNotifications = createSidebarButton("Notifications");
        btnNotifications.setOnAction(e -> switchView(new com.rajaratabank.ui.views.NotificationsView().getView()));

        Button btnAdmin = createSidebarButton("Admin Dashboard");
        btnAdmin.setOnAction(e -> {
            try {
                com.rajaratabank.security.AuthManager auth = new com.rajaratabank.security.AuthManager();
                auth.authorize(SessionManager.getInstance().getActiveUser(), com.rajaratabank.models.Administrator.class);
                switchView(new com.rajaratabank.ui.views.AdminDashboardView().getView());
            } catch (com.rajaratabank.exceptions.UnauthorizedAccessException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Strict Access Denied");
                alert.setHeaderText("Unauthorized View Detected");
                alert.setContentText(ex.getMessage() + "\n\nThis security breach has been permanently recorded in the audit_security_log.txt file.");
                alert.showAndWait();
            }
        });

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button btnLogout = createSidebarButton("Logout");
        btnLogout.getStyleClass().add("btn-logout");
        btnLogout.setOnAction(e -> logout());

        side.getChildren().addAll(logoLabel, btnDashboard, btnAccounts, btnTransfers, btnLoans, btnNotifications, btnAdmin, spacer, btnLogout);
        return side;
    }

    private static Button createSidebarButton(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("sidebar-btn");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        return btn;
    }

    private static void loadCss(Scene scene) {
        try {
            String cssPath = BankUI.class.getResource("/com/rajaratabank/ui/styles.css").toExternalForm();
            scene.getStylesheets().add(cssPath);
        } catch (Exception e) {
            System.err.println("Warning: Unable to load styles.css.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
