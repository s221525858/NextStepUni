package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import util.DatabaseConnector;
import util.SceneManager;


import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ViewUniversitiesController implements Initializable {

    @FXML
    private FlowPane universityFlowPane; // The type and name must be updated
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> sortComboBox;
    @FXML
    private ImageView backArrow;


    private final List<UniversityData> allUniversities = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadUniversitiesFromDatabase();
        displayUniversities(allUniversities);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterUniversities(newValue);
        });
    }

    private void loadUniversitiesFromDatabase() {
        allUniversities.clear();
        String sql = "SELECT UniName, UniPicturePath FROM University";

        try (Connection conn = DatabaseConnector.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                allUniversities.add(new UniversityData(
                        rs.getString("UniName"),
                        rs.getString("UniPicturePath")
                ));
            }
        } catch (Exception e) {
            System.err.println("Error loading universities from the database.");
            e.printStackTrace();
        }
    }

    private void displayUniversities(List<UniversityData> universitiesToShow) {
        universityFlowPane.getChildren().clear();
        for (UniversityData uni : universitiesToShow) {
            Node universityCard = createUniversityCard(uni);
            universityFlowPane.getChildren().add(universityCard);
        }
    }


    private void filterUniversities(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            displayUniversities(allUniversities);
            return;
        }

        List<UniversityData> filteredList = new ArrayList<>();
        String lowerCaseFilter = searchText.toLowerCase();

        for (UniversityData uni : allUniversities) {
            if (uni.getName().toLowerCase().contains(lowerCaseFilter)) {
                filteredList.add(uni);
            }
        }
        displayUniversities(filteredList);
    }

    private Node createUniversityCard(UniversityData uni) {

        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(220, 250);
        card.setStyle("-fx-background-color: white; -fx-border-color: #d51e1e; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;");
        card.setPadding(new javafx.geometry.Insets(10));

        ImageView logoView = new ImageView();
        logoView.setFitHeight(120);
        logoView.setFitWidth(180);
        logoView.setPreserveRatio(true);

        try {

            File imageFile = new File("university_images", uni.getImagePath());

            if (imageFile.exists()) {
                Image logo = new Image(imageFile.toURI().toString());
                logoView.setImage(logo);
            } else {
                System.err.println("Image not found: " + imageFile.getAbsolutePath());
                logoView.setImage(new Image(getClass().getResourceAsStream("/images/icons/placeholder.png")));
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + uni.getImagePath());
            e.printStackTrace();

            logoView.setImage(new Image(getClass().getResourceAsStream("/images/icons/placeholder.png")));
        }

        Label nameLabel = new Label(uni.getName());
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        nameLabel.setWrapText(true);
        nameLabel.setAlignment(Pos.CENTER);


        Button profileButton = new Button("View Profile");
        profileButton.setStyle("-fx-background-color: #d51e1e; -fx-text-fill: white; -fx-font-weight: bold;");
        profileButton.setOnAction(event -> {
            System.out.println("Viewing profile for: " + uni.getName());
            // TODO: Add navigation logic to the detailed profile view
        });

        card.getChildren().addAll(logoView, nameLabel, profileButton);
        return card;
    }

    @FXML
    private void handleLoginRegister() {
        System.out.println("Login/Register button clicked.");
        // TODO: Navigation to login/register screen
    }

    @FXML
    private void handleBackArrowClick(MouseEvent event) {
       SceneManager.switchTo("/view/dashboard.fxml");
    }


    private record UniversityData(String name, String imagePath) {
        public String getName() {
            return name;
        }
        public String getImagePath() {
            return imagePath;
        }
    }
}
