package com.example.programminggroupproject.controller;

import com.example.programminggroupproject.service.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private Label messageLabel;

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll("client", "mechanic");
        roleComboBox.setValue("client"); // Default
    }

    @FXML
    private void handleRegister() {
        String fullName = fullNameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();

        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || role == null) {
            messageLabel.setText("Please fill in all fields.");
            messageLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        boolean success = AuthService.register(username, password, fullName, role);

        if (success) {
            messageLabel.setText("Registration successful! You can now login.");
            messageLabel.setStyle("-fx-text-fill: green;");
            // Optional: Auto-redirect to login or clear fields
            fullNameField.clear();
            usernameField.clear();
            passwordField.clear();
        } else {
            messageLabel.setText("Username already exists.");
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/programminggroupproject/login-view.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);

            // Load CSS if available
            try {
                String css = getClass().getResource("/com/example/programminggroupproject/styles.css").toExternalForm();
                scene.getStylesheets().add(css);
            } catch (Exception e) {
                // Ignore if css not found
            }

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Login - Car Servicinator 3000");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
