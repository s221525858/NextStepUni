package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.University;
import util.DatabaseConnector;
import util.SceneManager;


import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManageUniversitiesController implements Initializable {

    @FXML
    private TableView<University> universityTableView;
    @FXML
    private TableColumn<University, String> nameColumn;
    @FXML
    private TableColumn<University, String> locationColumn;
    @FXML
    private TableColumn<University, LocalDate> deadlineColumn;
    @FXML
    private TableColumn<University, String> websiteColumn;
    @FXML
    private TableColumn<University, Void> actionColumn;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> sortComboBox;


    private final ObservableList<University> universityList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadUniversitiesFromDatabase();
        configureTableColumns();
        setupFilterAndSort();
    }
    private void loadUniversitiesFromDatabase() {
        universityList.clear();

        String sql = "SELECT UniversityID, UniName, Location, ApplicationDeadline, Description, WebsiteLink, UniPicturePath FROM University";

        try (Connection conn = DatabaseConnector.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                universityList.add(new University(
                        rs.getInt("UniversityID"),
                        rs.getString("UniName"),
                        rs.getString("Location"),
                        rs.getDate("ApplicationDeadline").toLocalDate(),
                        rs.getString("Description"),
                        rs.getString("WebsiteLink"),
                        rs.getString("UniPicturePath")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configureTableColumns() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("uniName"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("applicationDeadline"));
        websiteColumn.setCellFactory(createWebsiteCellFactory());
        actionColumn.setCellFactory(createActionCellFactory());
        universityTableView.setRowFactory(tv -> {
            TableRow<University> row = new TableRow<>();
            row.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) {
                    row.setStyle("-fx-background-color: #d51e1e; -fx-text-fill: white;");
                } else {
                    row.setStyle("");
                }
            });
            return row;
        });
    }

    private void setupFilterAndSort() {
        FilteredList<University> filteredData = new FilteredList<>(universityList, b -> true);
        searchField.textProperty().addListener((obs, old, aNew) -> {
            filteredData.setPredicate(university -> {
                if (aNew == null || aNew.isEmpty()) return true;
                return university.getUniName().toLowerCase().contains(aNew.toLowerCase());
            });
        });

        SortedList<University> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(universityTableView.comparatorProperty());
        universityTableView.setItems(sortedData);
    }

    private Callback<TableColumn<University, String>, TableCell<University, String>> createWebsiteCellFactory() {
        return param -> new TableCell<>() {
            private final Hyperlink link = new Hyperlink("[Link]");
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    University uni = getTableView().getItems().get(getIndex());
                    link.setOnAction(event -> handleWebsiteLink(uni.getWebsiteLink()));
                    setGraphic(link);
                    setAlignment(Pos.CENTER);
                }
            }
        };
    }

    private Callback<TableColumn<University, Void>, TableCell<University, Void>> createActionCellFactory() {
        return param -> new TableCell<>() {
            private final Button editButton = createIconButton("/images/icons/edit_icon.png");
            private final Button deleteButton = createIconButton("/images/icons/delete_icon.png");
            private final HBox pane = new HBox(5, editButton, deleteButton);

            {
                pane.setAlignment(Pos.CENTER);
                editButton.setOnAction(event -> {
                    University university = getTableView().getItems().get(getIndex());
                    handleEditUniversity(university);
                });
                deleteButton.setOnAction(event -> {
                    University university = getTableView().getItems().get(getIndex());
                    handleDeleteUniversity(university);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        };
    }

    @FXML
    private void handleAddUniversity() {
        SceneManager.switchTo("/view/add_university.fxml");
    }

    private void handleEditUniversity(University university) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/update_university.fxml"));
            Parent root = loader.load();

            UpdateUniversityController controller = loader.getController();

            controller.initData(university);


            Stage stage = (Stage) universityTableView.getScene().getWindow();
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteUniversity(University university) {
        ButtonType deleteButtonType = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Delete: " + university.getUniName());
        alert.setContentText("Are you sure you want to delete this university?");
        alert.getButtonTypes().setAll(cancelButtonType,deleteButtonType);
        final Button deleteButton = (Button) alert.getDialogPane().lookupButton(deleteButtonType);
        deleteButton.setStyle("-fx-background-color: #d51e1e; -fx-text-fill: white; -fx-font-weight: bold;");

        final Button cancelButton = (Button) alert.getDialogPane().lookupButton(cancelButtonType);
        cancelButton.setDefaultButton(true);

        Optional<ButtonType> result = alert.showAndWait();


        if (result.isPresent() && result.get() == ButtonType.OK) {
            String sql = "DELETE FROM University WHERE UniversityID = ?";
            try (Connection conn = DatabaseConnector.getInstance().getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, university.getUniversityID());
                if (pstmt.executeUpdate() > 0) {
                    universityList.remove(university);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleWebsiteLink(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackArrowClick(MouseEvent event) {
        SceneManager.switchTo("/view/dashboard.fxml");
    }


    private Button createIconButton(String imagePath) {
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
        icon.setFitHeight(16);
        icon.setFitWidth(16);
        Button button = new Button();
        button.setGraphic(icon);
        button.setStyle("-fx-background-color: transparent; -fx-padding: 3;");
        return button;
    }

}
