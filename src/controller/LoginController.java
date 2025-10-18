package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import model.UserSession;
import org.mindrot.jbcrypt.BCrypt;
import util.AlertUtil;
import util.DatabaseConnector;
import util.SceneManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private ToggleGroup roleGroup;
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
    private RadioButton adminRadio;


    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();
        String selectedRole = adminRadio.isSelected() ? "Admin" : "Student";

        if (email.isEmpty() || password.isEmpty()) {
            AlertUtil.showError("Login Failed", "All fields including role selection are required.");
            return;
        }

        if (validateCredentials(email, password, selectedRole)) {
            SceneManager.switchTo("/view/dashboard.fxml");
        } else {
            AlertUtil.showError("Login Failed", "Invalid credentials for selected role.");
        }
    }
    @FXML
    private void handleContinueAsGuest() {
        UserSession.getInstance().login(0, "Guest", UserSession.UserRole.GUEST);
        SceneManager.switchTo("/view/dashboard.fxml");
    }
    private boolean validateCredentials(String email, String password, String role) {
        String sql;
        boolean isAdmin = role.equals("Admin");

        if (isAdmin) {
            sql = "SELECT AdminID, UserName, Password FROM Admin WHERE UserName = ?";
        } else {
            sql = "SELECT StudentID, StuFName, Password FROM Student WHERE EmailAddress = ?";
        }

        Connection conn = DatabaseConnector.getInstance().getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("Password");

                if (password.equals(storedPassword)) {
                    if (isAdmin) {
                        int id = rs.getInt("AdminID");
                        String adminName = rs.getString("UserName");
                        UserSession.getInstance().login(id, adminName, UserSession.UserRole.ADMIN);
                    } else {
                        int id = rs.getInt("StudentID");
                        String studentName = rs.getString("StuFName");
                        UserSession.getInstance().login(id, studentName, UserSession.UserRole.STUDENT);
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtil.showError("Database Error", "An error occurred while trying to log in.");
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

}
