package com.example.programminggroupproject.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;


public class DashboardController {

    @FXML
    private Label welcomeLabel;

    public void setUsername(String username) {
        welcomeLabel.setText("Welcome, " + username + "!");
    }


    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/programminggroupproject/login-view.fxml"));
            Scene loginScene = new Scene(loader.load(), 800, 600);

            String css = getClass().getResource("/com/example/programminggroupproject/styles.css").toExternalForm();
            loginScene.getStylesheets().add(css);

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("Login - Car Servicinator 3000");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

