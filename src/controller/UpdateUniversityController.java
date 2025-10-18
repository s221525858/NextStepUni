package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import model.University;
import util.AlertUtil;
import util.DatabaseConnector;
import util.SceneManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static controller.AddUniversityController.validateDateAndWebLink;


public class UpdateUniversityController extends BaseController implements Initializable {
    @FXML
    private ImageView universityImageView;
    @FXML
    private TextField universityNameField;
    @FXML
    private TextField locationField;
    @FXML
    private TextField websiteLinkField;
    @FXML
    private DatePicker applicationDeadlinePicker;
    @FXML
    private TextArea descriptionArea;

    private University currentUniversity;

    private File newImageFile;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupHeader();
    }

    public void initData(University university) {
        this.currentUniversity = university;
        this.newImageFile = null;
        universityNameField.setText(university.getUniName());
        locationField.setText(university.getLocation());
        websiteLinkField.setText(university.getWebsiteLink());
        applicationDeadlinePicker.setValue(university.getApplicationDeadline());
         descriptionArea.setText(university.getDescription());

        loadUniversityImage(university.getUniPicturePath());
    }
    @FXML
    private void handleUpdate() {
        if (!validateInput()) {
            return;
        }

        String sql = "UPDATE University SET UniName = ?, Location = ?, WebsiteLink = ?, ApplicationDeadline = ?, Description = ?, UniPicturePath = ? WHERE UniversityID = ?";
        String imagePath = currentUniversity.getUniPicturePath();

        try {
            if (newImageFile != null) {
                imagePath = saveImageToDataDirectory(newImageFile, universityNameField.getText());
            }

            Connection conn = DatabaseConnector.getInstance().getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, universityNameField.getText());
                pstmt.setString(2, locationField.getText());
                pstmt.setString(3, websiteLinkField.getText());
                pstmt.setDate(4, java.sql.Date.valueOf(applicationDeadlinePicker.getValue()));
                pstmt.setString(5, descriptionArea.getText());
                pstmt.setString(6, imagePath);
                pstmt.setInt(7, currentUniversity.getUniversityID()); // Use the ID in the WHERE clause

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    AlertUtil.showInfo("Success", "University updated successfully.");
                    SceneManager.switchTo("/view/manage_universities.fxml");
                } else {
                    AlertUtil.showError( "Update Failed", "Could not update the university in the database.");
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            AlertUtil.showError( "Error", "An error occurred: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select New University Picture");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp"));
        Stage stage = (Stage) universityImageView.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            this.newImageFile = file;
            try {
                universityImageView.setImage(new Image(file.toURI().toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void handleRemoveImage() {
        universityImageView.setImage(null);
        this.newImageFile = null;
        currentUniversity.setUniPicturePath(null);
    }

    @FXML
    private void handleCancel() {
        boolean confirm = AlertUtil.showConfirmation("Confirm Cancellation", "Any unsaved changes will be lost. Are you sure?");
        if(confirm) {
            SceneManager.switchTo("/view/manage_universities.fxml");
        }
    }

    private void loadUniversityImage(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                File imageFile = new File("university_images", imagePath);
                if (imageFile.exists()) {
                    universityImageView.setImage(new Image(imageFile.toURI().toString()));
                } else {
                    System.err.println("Image file not found: " + imageFile.getAbsolutePath());
                    universityImageView.setImage(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                universityImageView.setImage(null);
            }
        } else {
            universityImageView.setImage(null);
        }
    }

    private String saveImageToDataDirectory(File sourceFile, String universityName) throws IOException {
        Path imageDir = Paths.get("university_images");
        if (!Files.exists(imageDir)) {
            Files.createDirectories(imageDir);
        }
        String sanitizedName = universityName.replaceAll("[^a-zA-Z0-9.-]", "_").toLowerCase();
        String fileExtension = getFileExtension(sourceFile.getName());
        String newFileName = sanitizedName + "_" + System.currentTimeMillis() + "." + fileExtension;
        Path destinationFile = imageDir.resolve(newFileName);
        Files.copy(sourceFile.toPath(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
        return newFileName;
    }

    private String getFileExtension(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        return (lastIndexOf == -1) ? "" : fileName.substring(lastIndexOf + 1);
    }
    private boolean validateInput() {
        LocalDate deadline = applicationDeadlinePicker.getValue();
        String website = websiteLinkField.getText();
        if (universityNameField.getText() == null || universityNameField.getText().trim().isEmpty()) {
            AlertUtil.showError("Invalid Input", "University Name is a required field.");
            return false;
        }

        return validateDateAndWebLink(deadline, website);
    }


}
