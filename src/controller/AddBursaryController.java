package controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Bursary;
import util.AlertUtil;
import util.DatabaseConnector;
import util.SceneManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddBursaryController {

    @FXML
    private TextField bursaryNameField;

    @FXML
    private TextField websiteLinkField;

    @FXML
    private DatePicker applicationDeadlineField;

    @FXML
    private TextArea descriptionField;
    @FXML
    private Button backButton;

    @FXML
    public void handleBackClick(ActionEvent event) {
        boolean confirm = AlertUtil.showConfirmation("Clicked back", "Any unsaved changes will be lost. Are you sure?");
        if (confirm) {
            SceneManager.switchTo("/view/manage_bursaries.fxml");
        }
    }
    @FXML
    public void handleSave(ActionEvent event) {
        if (!validateInput()) {
            return;
        }
        String name = bursaryNameField.getText();
        String website = websiteLinkField.getText();
        LocalDate deadline = applicationDeadlineField.getValue();
        String description = descriptionField.getText();

        Bursary bursary = new Bursary(name, deadline, description, website);

        try (Connection conn = DatabaseConnector.getInstance().getConnection()) {
            if (conn != null) {
                String sql = "INSERT INTO Bursary (BurName, ApplicationDeadline, Description, WebsiteLink) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, bursary.getBurName());
                pstmt.setDate(2, java.sql.Date.valueOf(bursary.getApplicationDeadline()));
                pstmt.setString(3, bursary.getDescription());
                pstmt.setString(4, bursary.getWebsiteLink());

                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    showSuccessAlertAndNavigate(event);
                } else {
                   showErrorAlert();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void showErrorAlert() {
        AlertUtil.showError("Save Failed", "The Bursary could not be saved to the database.");
    }

    private void showSuccessAlertAndNavigate(ActionEvent event) {
        AlertUtil.showInfo("Bursary Added Successfully", "Your Bursary has been saved.");
        SceneManager.switchTo("/view/manage_bursaries.fxml");
    }

    @FXML
    public void handleCancel(Event event) {
        boolean confirm = AlertUtil.showConfirmation("Confirm Cancellation", "Any unsaved changes will be lost. Are you sure?");
        if (confirm) {
            SceneManager.switchTo("/view/manage_bursaries.fxml");
        }
    }
    private boolean validateInput() {
        String name = bursaryNameField.getText();
        String website = websiteLinkField.getText();
        LocalDate deadline = applicationDeadlineField.getValue();
        String description = descriptionField.getText();

        if (name == null || name.trim().isEmpty()) {
            AlertUtil.showError("Validation Error", "Bursary name is required.");
            return false;
        }

        if (website != null && !website.trim().isEmpty()) {
            if (!website.matches("^(http|https)://.*$")) {
                AlertUtil.showError("Validation Error", "Website link must start with http:// or https://");
                return false;
            }
        }

        if (deadline == null) {
            AlertUtil.showError("Validation Error", "Application deadline is required.");
            return false;
        }

        if (deadline.isBefore(LocalDate.now())) {
            AlertUtil.showError("Validation Error", "Application deadline cannot be in the past.");
            return false;
        }
//        if (description != null && description.length() > 1000) {
//            AlertUtil.showError("Validation Error", "Description is too long. Max 1000 characters.");
//            return false;
//        }

        return true;
    }
}
