package controller;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Bursary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddController {

    @FXML
    private TextField bursaryNameField;

    @FXML
    private TextField websiteLinkField;

    @FXML
    private DatePicker applicationDeadlineField;

    @FXML
    private TextArea descriptionField;

    @FXML
    public void handleSave() {
        String name = bursaryNameField.getText();
        String website = websiteLinkField.getText();
        LocalDate deadline = applicationDeadlineField.getValue();
        String description = descriptionField.getText();

        Bursary bursary = new Bursary(name, deadline, description, website);

        try (Connection conn = DatabaseConnector.connect()) {
            if (conn != null) {
                String sql = "INSERT INTO Bursary (BurName, ApplicationDeadline, Description, WebsiteLink) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, bursary.getBurName());
                pstmt.setDate(2, java.sql.Date.valueOf(bursary.getApplicationDeadline()));
                pstmt.setString(3, bursary.getDescription());
                pstmt.setString(4, bursary.getWebsiteLink());

                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    System.out.println("Bursary added successfully.");
                } else {
                    System.out.println("Failed to add bursary.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleCancel() {
        bursaryNameField.clear();
        websiteLinkField.clear();
        descriptionField.clear();
    }
}
