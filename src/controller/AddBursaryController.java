package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Bursary;
import model.Qualification;
import util.AlertUtil;
import util.DatabaseConnector;
import util.SceneManager;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static controller.AddUniversityController.validateDateAndWebLink;

public class AddBursaryController extends BaseController implements Initializable {

    @FXML
    private TextField bursaryNameField;

    @FXML
    private TextField websiteLinkField;

    @FXML
    private DatePicker applicationDeadlineField;
    @FXML
    private ListView<Qualification> qualificationListView;

    @FXML
    private TextArea descriptionField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadQualifications();
        setupHeader();
    }
    private void loadQualifications() {
        try (Connection conn = DatabaseConnector.getInstance().getConnection()) {
            String sql = "SELECT QualID, QualType, QualFaculty FROM Qualification";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            ObservableList<Qualification> qualificationList = FXCollections.observableArrayList();
            while (rs.next()) {
                Qualification q = new Qualification(
                        rs.getInt("QualID"),
                        rs.getString("QualType"),
                        rs.getString("QualFaculty")
                );
                qualificationList.add(q);
            }
            qualificationListView.setItems(qualificationList);
            qualificationListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        } catch (SQLException e) {
            e.printStackTrace();
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
                if (bursary.getApplicationDeadline() != null) {
                    pstmt.setDate(2, java.sql.Date.valueOf(bursary.getApplicationDeadline()));
                } else {
                    pstmt.setNull(2, java.sql.Types.DATE);
                }

                pstmt.setDate(2, java.sql.Date.valueOf(bursary.getApplicationDeadline()));
                pstmt.setString(3, bursary.getDescription());
                pstmt.setString(4, bursary.getWebsiteLink());

                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    ResultSet generatedKeys = pstmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int bursaryID = generatedKeys.getInt(1);
                        saveBursaryQualifications(conn, bursaryID);
                        showSuccessAlertAndNavigate(event);
                    }
                } else {
                   showErrorAlert();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void saveBursaryQualifications(Connection conn, int bursaryID) throws SQLException {
        ObservableList<Qualification> selectedQuals = qualificationListView.getSelectionModel().getSelectedItems();
        if (selectedQuals == null || selectedQuals.isEmpty()) {
            return;
        }
        String sql = "INSERT INTO BursaryQualification (QualID, BursaryID) VALUES (?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        for (Qualification q : selectedQuals) {
            pstmt.setInt(1, q.getQualID());
            pstmt.setInt(2, bursaryID);
            pstmt.addBatch();
        }
        pstmt.executeBatch();
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

        if (name == null || name.trim().isEmpty()) {
            AlertUtil.showError("Validation Error", "Bursary name is required.");
            return false;
        }
        if (qualificationListView.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertUtil.showError("Validation Error", "Select at least one qualification.");
            return false;
        }

        return validateDateAndWebLink(deadline, website);
    }
}
