package com.example.programminggroupproject.controller;

import com.example.programminggroupproject.model.User;
import com.example.programminggroupproject.session.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientController {

    @FXML
    private ComboBox<String> vehicleComboBox;

    @FXML
    private ComboBox<String> shopComboBox;

    @FXML
    private CheckBox engineIssueCheck;

    @FXML
    private CheckBox brakeIssueCheck;

    @FXML
    private CheckBox tireIssueCheck;

    @FXML
    private CheckBox batteryIssueCheck;

    @FXML
    private CheckBox noiseIssueCheck;

    @FXML
    private CheckBox fluidLeakIssueCheck;

    @FXML
    private CheckBox otherIssueCheck;

    @FXML
    private TextArea notesArea;

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

        List<String> issues = new ArrayList<>();
        if (engineIssueCheck.isSelected()) {
            issues.add("Engine / performance");
        }
        if (brakeIssueCheck.isSelected()) {
            issues.add("Brakes");
        }
        if (tireIssueCheck.isSelected()) {
            issues.add("Tires / alignment");
        }
        if (batteryIssueCheck.isSelected()) {
            issues.add("Battery / electrical");
        }
        if (noiseIssueCheck.isSelected()) {
            issues.add("Unusual noises");
        }
        if (fluidLeakIssueCheck.isSelected()) {
            issues.add("Fluid leak");
        }
        if (otherIssueCheck.isSelected()) {
            issues.add("Other / not sure");
        }

        String notes = notesArea.getText().trim();

        // Validation
        if (vehicle == null || shop == null) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Please select a vehicle and a mechanic shop.");
            return;
        }

        if (issues.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Please select at least one issue.");
            return;
        }

        // For now just log to console – later this becomes a DB record
        System.out.println("=== New service request ===");
        System.out.println("Vehicle: " + vehicle);
        System.out.println("Shop: " + shop);
        System.out.println("Issues: " + String.join(", ", issues));
        System.out.println("Notes: " + (notes.isEmpty() ? "(none)" : notes));

        messageLabel.setStyle("-fx-text-fill: #28a745;");
        messageLabel.setText("Service request submitted (dummy) with structured issues!");

        // Optional: reset checkboxes and notes
        engineIssueCheck.setSelected(false);
        brakeIssueCheck.setSelected(false);
        tireIssueCheck.setSelected(false);
        batteryIssueCheck.setSelected(false);
        noiseIssueCheck.setSelected(false);
        fluidLeakIssueCheck.setSelected(false);
        otherIssueCheck.setSelected(false);
        notesArea.clear();
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