package com.example.programminggroupproject.controller;

import com.example.programminggroupproject.model.User;
import com.example.programminggroupproject.session.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientController {

    @FXML
    private ComboBox<String> vehicleComboBox;

    @FXML
    private ComboBox<String> shopComboBox;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Label messageLabel;

    @FXML
    private Label debugLabel;

    @FXML
    public void initialize() {
        // Placeholder data for now
        vehicleComboBox.getItems().addAll(
                "Toyota Corolla - ABC123",
                "Honda Civic - XYZ789",
                "BMW 3 Series - AAA111"
        );
        shopComboBox.getItems().addAll(
                "Downtown Mechanic Shop",
                "Highway Service Center",
                "Premium Auto Care"
        );
    }

    @FXML
    private void handleSubmit() {
        String vehicle = vehicleComboBox.getValue();
        String shop = shopComboBox.getValue();
        String description = descriptionArea.getText().trim();

        if (vehicle == null || shop == null || description.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Please fill in all fields before submitting.");
            return;
        }

        // For now just log to console – later this will go to Supabase / DB
        System.out.println("New service request:");
        System.out.println("Vehicle: " + vehicle);
        System.out.println("Shop: " + shop);
        System.out.println("Description: " + description);

        messageLabel.setStyle("-fx-text-fill: #28a745;");
        messageLabel.setText("Service request submitted (dummy)!");

        // Optionally clear fields
        descriptionArea.clear();
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/programminggroupproject/dashboard.fxml")
            );
            Scene scene = new Scene(loader.load(), 800, 600);

            // Restore current user & role-based dashboard
            DashboardController controller = loader.getController();
            User currentUser = Session.getCurrentUser();
            if (currentUser != null) {
                controller.setUser(currentUser);
            }

            Stage stage = (Stage) debugLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Dashboard - Car Servicinator 3000");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}