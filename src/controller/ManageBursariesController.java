package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Bursary;
import model.University;
import util.AlertUtil;
import util.DatabaseConnector;
import util.SceneManager;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

import java.util.ResourceBundle;

public class ManageBursariesController implements Initializable {


    @FXML
    private TableView<Bursary> bursaryTableView;
    @FXML
    private TableColumn<Bursary, String> bursaryNameColumn;
    @FXML
    private TableColumn<Bursary, LocalDate> deadlineColumn;
    @FXML
    private TableColumn<Bursary, String> websiteColumn;
    @FXML
    private TableColumn<Bursary, Void> actionColumn;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> sortComboBox;
    @FXML
    private Button addButton;

    private final ObservableList<Bursary> bursaryList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadBursariesFromDatabase();
        configureTableColumns();
        setupFilterAndSort();
        sortComboBox.getItems().addAll("By Name", "By Deadline");
    }

    private void loadBursariesFromDatabase() {
        bursaryList.clear();
        String sql = "SELECT BursaryID, BurName, ApplicationDeadline, Description, WebsiteLink FROM Bursary";
        try (Connection conn = DatabaseConnector.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                bursaryList.add(new Bursary(
                        rs.getInt("BursaryID"),
                        rs.getString("BurName"),
                        rs.getDate("ApplicationDeadline").toLocalDate(),
                        rs.getString("Description"),
                        rs.getString("WebsiteLink")
                ));
            }
        } catch (Exception e) {
            System.err.println("Error loading bursaries from the database.");
            e.printStackTrace();
        }
    }

    private void configureTableColumns() {
        bursaryNameColumn.setCellValueFactory(new PropertyValueFactory<>("burName"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("applicationDeadline"));
        websiteColumn.setCellFactory(createWebsiteCellFactory());
        actionColumn.setCellFactory(createActionCellFactory());

    }

    private void setupFilterAndSort() {
        FilteredList<Bursary> filteredData = new FilteredList<>(bursaryList, b -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(bursary -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerCaseFilter = newValue.toLowerCase();
                return bursary.getBurName().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<Bursary> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(bursaryTableView.comparatorProperty());
        bursaryTableView.setItems(sortedData);
    }

    private Callback<TableColumn<Bursary, String>, TableCell<Bursary, String>> createWebsiteCellFactory() {
        return param -> new TableCell<>() {
            private final Hyperlink link = new Hyperlink("[Link]");
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Bursary bursary = getTableView().getItems().get(getIndex());
                    link.setOnAction(event -> handleWebsiteLink(bursary.getWebsiteLink()));
                    setGraphic(link);
                    setAlignment(Pos.CENTER);
                }
            }
        };
    }

    private Callback<TableColumn<Bursary, Void>, TableCell<Bursary, Void>> createActionCellFactory() {
        return param -> new TableCell<>() {
            private final Button editButton = createIconButton("/images/icons/edit_icon.png");
            private final Button deleteButton = createIconButton("/images/icons/delete_icon.png");
            private final HBox pane = new HBox(5, editButton, deleteButton);

            {
                pane.setAlignment(Pos.CENTER);
                editButton.setOnAction(event -> {
                    Bursary bursary = getTableView().getItems().get(getIndex());
                    handleEditBursary(bursary);

                });
                deleteButton.setOnAction(event -> {
                    Bursary bursary = getTableView().getItems().get(getIndex());
                    handleDeleteBursary(bursary);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        };
    }
    @FXML
    private void handleAddBursary(ActionEvent event) {
        SceneManager.switchTo("/view/add_bursary.fxml");
    }

    private void handleEditBursary(Bursary bursary) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/update_bursary.fxml"));
            Parent root = loader.load();

            UpdateBursaryController controller = loader.getController();

            controller.initData(bursary);


            Stage stage = (Stage) bursaryTableView.getScene().getWindow();
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteBursary(Bursary bursary) {
        boolean confirmed = AlertUtil.showCustomConfirmation(
                "Confirm Deletion",
                "Delete Bursary: " + bursary.getBurName(),
                "Are you sure you want to permanently delete this bursary?",
                "Delete",
                "Cancel"
        );

        if (confirmed) {
            String sql = "DELETE FROM Bursary WHERE BurName = ?";
            try (Connection conn = DatabaseConnector.getInstance().getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, bursary.getBurName());
                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Successfully deleted " + bursary.getBurName());
                    bursaryList.remove(bursary);
                    AlertUtil.showInfo("Deleted", bursary.getBurName() + " was removed.");
                } else {
                    AlertUtil.showError("Delete Failed", "Could not find the bursary to delete.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                AlertUtil.showError("Error", "Something went wrong while deleting.");
            }
        } else {
            AlertUtil.showInfo("Cancelled","Deletion cancelled.");
        }
    }


    private void handleWebsiteLink(String url) {
        try {
            String fullUrl = url.toLowerCase().startsWith("http") ? url : "https://" + url;
            Desktop.getDesktop().browse(new URI(fullUrl));
        } catch (IOException | URISyntaxException e) {
            System.err.println("Failed to open link: " + e.getMessage());
        }
    }

    private Button createIconButton(String imagePath) {
        try {
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
            icon.setFitHeight(16);
            icon.setFitWidth(16);
            Button button = new Button();
            button.setGraphic(icon);
            button.setStyle("-fx-background-color: transparent; -fx-padding: 3;");
            return button;
        } catch (Exception e) {
            System.err.println("Could not load icon: " + imagePath);
            String text = imagePath.contains("edit") ? "E" : "D";
            return new Button(text);
        }
    }
}
