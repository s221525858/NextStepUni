package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import model.Bursary;
import util.DatabaseConnector;

import java.awt.Desktop; // Used to open web links
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ViewBursariesController implements Initializable {

    @FXML
    private TableView<Bursary> bursaryTableView;
    @FXML
    private TableColumn<Bursary, String> bursaryNameColumn;
    @FXML
    private TableColumn<Bursary, LocalDate> deadlineColumn;
    @FXML
    private TableColumn<Bursary, String> websiteColumn;
    @FXML
    private TableColumn<Bursary, Void> detailsColumn;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> sortComboBox;


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

        try (Connection conn =  DatabaseConnector.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT BurName, ApplicationDeadline, Description, WebsiteLink FROM Bursary")) {

            while (rs.next()) {
                String name = rs.getString("BurName");
                LocalDate deadline = rs.getDate("ApplicationDeadline").toLocalDate();
                String description = rs.getString("Description");
                String website = rs.getString("WebsiteLink");

                bursaryList.add(new Bursary(name, deadline, description, website));
            }
        } catch (Exception e) {
            System.err.println("Error loading bursaries from the database.");
            e.printStackTrace();
        }
    }

    private void configureTableColumns() {

        bursaryNameColumn.setCellValueFactory(new PropertyValueFactory<>("burName"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("applicationDeadline"));


        Callback<TableColumn<Bursary, String>, TableCell<Bursary, String>> websiteCellFactory = (param) -> {
            final TableCell<Bursary, String> cell = new TableCell<Bursary, String>() {
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
                    setText(null);
                }
            };
            return cell;
        };
        websiteColumn.setCellFactory(websiteCellFactory);


        Callback<TableColumn<Bursary, Void>, TableCell<Bursary, Void>> detailsCellFactory = (param) -> {
            final TableCell<Bursary, Void> cell = new TableCell<Bursary, Void>() {
                private final Button btn = new Button("View details");
                {
                    btn.setStyle("-fx-background-color: #d51e1e; -fx-text-fill: white; -fx-font-weight: bold;");
                    btn.setOnAction(event -> {
                        Bursary data = getTableView().getItems().get(getIndex());
                        handleViewDetails(data);
                    });
                }

                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(btn);
                        setAlignment(Pos.CENTER);
                    }
                }
            };
            return cell;
        };
        detailsColumn.setCellFactory(detailsCellFactory);
    }

    private void setupFilterAndSort() {

        FilteredList<Bursary> filteredData = new FilteredList<>(bursaryList, b -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(bursary -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (bursary.getBurName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<Bursary> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(bursaryTableView.comparatorProperty());

        bursaryTableView.setItems(sortedData);
    }


    private void handleWebsiteLink(String url) {
        try {
            String fullUrl = url;
            if (!url.toLowerCase().startsWith("http://") && !url.toLowerCase().startsWith("https://")) {
                fullUrl = "https://" + url;
            }
            Desktop.getDesktop().browse(new URI(fullUrl));
        } catch (IOException | URISyntaxException e) {
            System.err.println("Failed to open link: " + e.getMessage());

        }
    }

    private void handleViewDetails(Bursary bursary) {
        System.out.println("Viewing details for: " + bursary.getBurName());
        System.out.println("Description: " + bursary.getDescription());

        // TODO: Implement navigation to a detailed view scene.
    }
}
