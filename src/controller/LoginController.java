package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import model.UserSession;
import org.mindrot.jbcrypt.BCrypt;
import util.DatabaseConnector;
import util.SceneManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private CheckBox showPasswordCheckBox;
    @FXML
    private Button loginButton;
    @FXML
    private ImageView backArrow;


    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showErrorAlert("Login Failed", "Email and password fields cannot be empty.");
            return;
        }

        if (validateCredentials(email, password)) {
            SceneManager.switchTo("/view/dashboard.fxml");
        } else {
            showErrorAlert("Login Failed", "Invalid email or password. Please try again.");
        }
    }
    private boolean validateCredentials(String email, String password) {

        String adminSql = "SELECT UserName, Password FROM Admin WHERE UserName = ?";
        Connection conn = DatabaseConnector.getInstance().getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(adminSql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("AdminPassword");

                if (BCrypt.checkpw(password, storedHash)) {
                    String adminName = rs.getString("AdminName");

                    UserSession.getInstance().login(adminName, UserSession.UserRole.ADMIN);
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        String studentSql = "SELECT StuFName, Password FROM Student WHERE EmailAddress = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(studentSql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("StudentPassword");

                if (BCrypt.checkpw(password, storedHash)) {
                    String studentName = rs.getString("StudentName");

                    UserSession.getInstance().login(studentName, UserSession.UserRole.STUDENT);
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Database Error", "An error occurred while trying to log in.");
        }
        return false;
    }

    @FXML
    private void handleShowPassword() {
        if (showPasswordCheckBox.isSelected()) {
            passwordTextField.setText(passwordField.getText());
            passwordTextField.setVisible(true);
            passwordTextField.setManaged(true);
            passwordField.setVisible(false);
            passwordField.setManaged(false);
        } else {
            passwordField.setText(passwordTextField.getText());
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            passwordTextField.setVisible(false);
            passwordTextField.setManaged(false);
        }
    }

    @FXML
    private void handleRegister() {
        SceneManager.switchTo("/view/register.fxml");
    }

    @FXML
    private void handleResetPassword() {
        SceneManager.switchTo("/view/forgot_password.fxml");
    }

    @FXML
    private void handleGoBack() {
        SceneManager.switchTo("/view/dashboard.fxml");
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(content);
        DialogPane dialogPane = alert.getDialogPane();
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.setStyle("-fx-background-color: #d51e1e; -fx-text-fill: white; -fx-font-weight: bold;");
        alert.showAndWait();
    }

}
