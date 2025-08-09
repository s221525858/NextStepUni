package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import util.DatabaseConnector;
import util.SceneManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Random;

public class ForgotPasswordController {

    @FXML
    private TextField emailField;

    @FXML
    private void handleSendCode() {
        String email = emailField.getText();
        if (email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Email field cannot be empty.");
            return;
        }

        // 1. Check if the user exists
        if (!userExists(email)) {
            showAlert(Alert.AlertType.ERROR, "Error", "No account found with that email address.");
            return;
        }
        String resetCode = generateResetCode();
        if (saveResetCode(email, resetCode)) {

            System.out.println("Password Reset Code for " + email + " is: " + resetCode);
            showAlert(Alert.AlertType.INFORMATION, "Code Sent", "A reset code has been generated. (Check the console).");

            SceneManager.switchTo("/view/reset_password.fxml");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not generate a reset code. Please try again.");
        }
    }

    @FXML
    private void handleGoBack() {
        SceneManager.switchTo("/view/login_view.fxml");
    }

    private boolean userExists(String email) {
        String sql = "SELECT StudentID FROM Student WHERE EmailAddress = ?";
        try (PreparedStatement pstmt = DatabaseConnector.getInstance().getConnection().prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String generateResetCode() {
        // Generate a 6-digit random number as a string
        return String.format("%06d", new Random().nextInt(999999));
    }

    private boolean saveResetCode(String email, String code) {
        String sql = "UPDATE Student SET ResetCode = ?, ResetCodeTimestamp = ? WHERE StudentEmail = ?";
        try (PreparedStatement pstmt = DatabaseConnector.getInstance().getConnection().prepareStatement(sql)) {
            pstmt.setString(1, code);
            pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            pstmt.setString(3, email);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}