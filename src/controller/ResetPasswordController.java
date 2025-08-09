package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.mindrot.jbcrypt.BCrypt; // Import jBcrypt
import util.DatabaseConnector;
import util.SceneManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;

public class ResetPasswordController {

    @FXML
    private TextField emailField;
    @FXML
    private TextField codeField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private void handleResetPassword() {
        String email = emailField.getText();
        String code = codeField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (email.isEmpty() || code.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields are required.");
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match.");
            return;
        }

        if (isCodeValid(email, code)) {
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
            if (updatePassword(email, hashedPassword)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Your password has been reset successfully. Please log in.");
                SceneManager.switchTo("/view/login.fxml");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update password.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid or expired reset code.");
        }
    }

    private boolean isCodeValid(String email, String code) {
        String sql = "SELECT ResetCode, ResetCodeTimestamp FROM Student WHERE StudentEmail = ?";
        try (PreparedStatement pstmt = DatabaseConnector.getInstance().getConnection().prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String dbCode = rs.getString("ResetCode");
                Timestamp dbTimestamp = rs.getTimestamp("ResetCodeTimestamp");

                if (dbCode == null || dbTimestamp == null) return false;

                Instant now = Instant.now();
                Instant codeTime = dbTimestamp.toInstant();
                long minutesElapsed = Duration.between(codeTime, now).toMinutes();

                return dbCode.equals(code) && minutesElapsed <= 15;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean updatePassword(String email, String hashedPassword) {
        String sql = "UPDATE Student SET Password = ?, ResetCode = NULL, ResetCodeTimestamp = NULL WHERE EmailAddress = ?";
        try (PreparedStatement pstmt = DatabaseConnector.getInstance().getConnection().prepareStatement(sql)) {
            pstmt.setString(1, hashedPassword);
            pstmt.setString(2, email);
            return pstmt.executeUpdate() > 0;
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
