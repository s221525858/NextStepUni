package controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Bursary;
import model.University;
import util.AlertUtil;
import util.DatabaseConnector;
import util.SceneManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateBursaryController {
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

    private Bursary currentBursary;
    @FXML
    public void handleBackClick(ActionEvent event) {
        boolean confirm = AlertUtil.showConfirmation("Clicked back", "Any unsaved changes will be lost. Are you sure?");
        if (confirm) {
            SceneManager.switchTo("/view/manage_bursaries.fxml");
        }
    }
    public void initData(Bursary bursary) {
        this.currentBursary = bursary;
        bursaryNameField.setText(bursary.getBurName());
        websiteLinkField.setText(bursary.getWebsiteLink());
        descriptionField.setText(bursary.getDescription());
        applicationDeadlineField.setValue(bursary.getApplicationDeadline());
    }
    @FXML
    public void handleUpdate(ActionEvent event) {
        if (bursaryNameField.getText().isEmpty()) {
            AlertUtil.showError("Validation Error", "Bursary name is required");
        }
        String sql = "UPDATE Bursary SET BurName = ?, ApplicationDeadline = ?, Description = ?, WebsiteLink = ?  WHERE BursaryID = ?";
        Connection conn = DatabaseConnector.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bursaryNameField.getText());
            pstmt.setDate(2, java.sql.Date.valueOf(applicationDeadlineField.getValue()));
            pstmt.setString(3,descriptionField.getText());
            pstmt.setString(4, websiteLinkField.getText());
            pstmt.setInt(5,currentBursary.getBursaryID());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                AlertUtil.showInfo("Success", "Bursary updated successfully.");
                SceneManager.switchTo("/view/manage_bursaries.fxml");
            }
            else {
                AlertUtil.showError( "Update Failed", "Could not update the bursary in the database.");
            }

        }catch (SQLException e){
            AlertUtil.showError("Error","An error occurred: " +e.getMessage());
        }
    }
    @FXML
    public void handleCancel(Event event) {
        boolean confirm = AlertUtil.showConfirmation("Confirm Cancellation", "Any unsaved changes will be lost. Are you sure?");
        if (confirm) {
            SceneManager.switchTo("/view/manage_bursaries.fxml");
        }
    }
}
