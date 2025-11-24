package com.example.programminggroupproject.controller;

import com.example.programminggroupproject.service.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;


    @FXML
    public void initialize() {
        errorLabel.setText("");
    }


    @FXML
    private void handleLogin() {
        errorLabel.setText("");

        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter username and password");
            return;
        }

        if (AuthService.authenticate(username, password)) {
            // Go to dashboard
            navigateToDashboard(username);
        } else {
            errorLabel.setText("Invalid username or password");
        }
    }


    private void navigateToDashboard(String username) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/programminggroupproject/dashboard.fxml"));
            Scene dashboardScene = new Scene(loader.load(), 800, 600);

            String css = getClass().getResource("/com/example/programminggroupproject/styles.css").toExternalForm();
            dashboardScene.getStylesheets().add(css);

            DashboardController controller = loader.getController();
            controller.setUsername(username);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(dashboardScene);
            stage.setTitle("Dashboard - Car Servicinator 3000");
        } catch (IOException e) {
            errorLabel.setText("Error loading dashboard");
            e.printStackTrace();
        }
    }
}

