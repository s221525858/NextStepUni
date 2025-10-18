package com.example.project;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ViewQualificationsController implements Initializable {
    @FXML
    private TableView<Qualification> qualificationTable;
    @FXML
    private TableColumn<Qualification, String> nameCol;
    @FXML
    private TableColumn<Qualification, String> typeCol;
    @FXML
    private TableColumn<Qualification, String> facultyCol;
    @FXML
    private TableColumn<Qualification, String> editCol;
    @FXML
    private TextField searchTxt;
    @FXML
    private Button searchBtn;
    @FXML
    private ChoiceBox<String> sortingChoiceBox;

    ObservableList<Qualification> QualificationList = FXCollections.observableArrayList();

    private String[] columnName = {"QualificationName","Type","Faculty"};
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            sortingChoiceBox.getItems().addAll(columnName) ;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        loadQualifications();

        sortByColumn();
        searchingOnAction();
    }


    private void sortByColumn() {

        try {

            sortingChoiceBox.setOnAction((ActionEvent event)-> {
                QualificationList.clear();

                String selectedColumnName = sortingChoiceBox.getSelectionModel().getSelectedItem();
                String sqlQuery;
                switch (selectedColumnName){
                    case "QualificationName":
                        sqlQuery = "Select * From Qualification Order by QualName";
                        break;
                    case "Type":
                        sqlQuery = "Select * From Qualification Order by QualType";
                        break;
                    case "Faculty":
                        sqlQuery = "Select * From Qualification Order by QualFaculty";
                        break;
                    default:
                        sqlQuery = "Select * From Qualification";

                }
                try {

                    DBConnection dbConnection = new DBConnection();
                    Connection connection = dbConnection.connectToDB();
                    PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()){
                        QualificationList.add(new Qualification(
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getString(4)
                        ));
                        qualificationTable.setItems(QualificationList);
                    }
                    qualificationTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
                    });
                    System.out.println("-------Sorted TABLE------");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void updateTable(){
        QualificationList.clear();
        try{
            DBConnection connectToDB = new DBConnection();
            Connection connection = connectToDB.connectToDB();
            String sqlQuery = "Select * From Qualification";
            PreparedStatement stmt = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = stmt.executeQuery();
            while(resultSet.next()){
                QualificationList.add(new Qualification(
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4)
                ));
                qualificationTable.setItems(QualificationList);
            }
            qualificationTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
                System.out.println("Selected: " + newSel);
            });
            System.out.println("-------UPDATED TABLE------");
        }
        catch (Exception e){
            System.out.println("-------Update table Problem-----");
        }
    }

    private void loadQualifications(){
        updateTable();

        nameCol.setCellValueFactory(new PropertyValueFactory<>("QualificationName"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
        facultyCol.setCellValueFactory(new PropertyValueFactory<>("Faculty"));

        Callback<TableColumn<Qualification, String>, TableCell<Qualification, String>> cellFactory = (TableColumn<Qualification, String> param) -> {
            final TableCell<Qualification, String> cell = new TableCell<Qualification, String>(){
                @Override
                public void updateItem(String item, boolean empty){
                    super.updateItem(item,empty);
                    if(empty){
                        setGraphic(null);
                        setText(null);
                    }
                    else{
                        ImageView editImg = new ImageView("editQual.png");
                        editImg.setFitWidth(30);
                        editImg.setFitHeight(30);
                        ImageView deleteImg = new ImageView("trash.png");
                        deleteImg.setFitWidth(30);
                        deleteImg.setFitHeight(30);
                        Button edit = new Button("",editImg);
                        edit.setPrefWidth(31);
                        edit.setPrefHeight(31);
                        edit.setBackground(Background.fill(Color.WHITE));
                        Button delete = new Button("", deleteImg);
                        delete.setPrefWidth(31);
                        delete.setPrefHeight(31);
                        delete.setBackground(Background.fill(Color.WHITE));

                        edit.setOnAction((ActionEvent event) -> {
                            try{
                                Qualification qualification = getTableView().getItems().get(getIndex());//qualificationTable.getSelectionModel().getSelectedItem();
                                if(qualification == null){
                                    Alert alert = new Alert(Alert.AlertType.ERROR);

                                    System.out.println("QUALIFICATION IS NULL");
                                }
                                CurrentQualification.setCurrentQualification(qualification);
                                SwitchPages switchPage = new SwitchPages();
                                switchPage.switching("update_Qualification.fxml", event);
                            }
                            catch(Exception e){
                                System.out.println("--------------edit Button------");
                                e.getMessage();
                            }
                        });
                        delete.setOnMouseClicked((MouseEvent event) -> {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setContentText("Are you sure you want to delete the qualification?");
                            alert.setTitle("DELETE Qualification");
                            alert.setHeaderText("");

                            ImageView newIcon = new ImageView(new Image("exclamation-mark.png"));
                            newIcon.setFitHeight(48);
                            newIcon.setFitWidth(48);
                            alert.getButtonTypes().clear();
                            alert.setGraphic(newIcon);

                            ButtonType deleteBtnType = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
                            ButtonType cancelBtnType = new ButtonType("Cancel",ButtonBar.ButtonData.CANCEL_CLOSE);

                            //deleteBtn.getButtonData();
                            alert.getButtonTypes().setAll(deleteBtnType,cancelBtnType);

                            Button deleteBtn = (Button) alert.getDialogPane().lookupButton(deleteBtnType) ;
                            deleteBtn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                            Optional<ButtonType> btnClicked = alert.showAndWait();


                            if(btnClicked.isPresent() && btnClicked.get() == deleteBtnType){
                                try{
                                    DBConnection connectToDB = new DBConnection();
                                    Connection connection = connectToDB.connectToDB();
                                    System.out.println(qualificationTable.getSelectionModel().getSelectedItem());

                                    Qualification qualification = getTableView().getItems().get(getIndex()); //qualificationTable.getSelectionModel().getSelectedItem();
                                    String sqlQuery = "Delete from Qualification where QualName = ? and QualType = ? and Qualfaculty = ?";
                                    PreparedStatement stmt = connection.prepareStatement(sqlQuery);
                                    stmt.setString(1,qualification.getQualificationName());
                                    stmt.setString(2,qualification.getType());
                                    stmt.setString(3,qualification.getFaculty());

                                    stmt.executeUpdate();
                                    stmt.close();

                                    connection.close();
                                    updateTable();
                                    Alert newAlert = new Alert(Alert.AlertType.INFORMATION);
                                    newAlert.setContentText("You have deleted a qualification");
                                    newAlert.showAndWait();
                                }
                                catch (Exception e){
                                    System.out.println("___________DELETE BUTTON PROBLEM----------");
                                    //System.out.println(e.getMessage());
                                    e.printStackTrace();
                                }
                            }
                            if(btnClicked.isPresent() && btnClicked.get() == cancelBtnType){
                                SwitchPages switchPage = new SwitchPages();
                                switchPage.switching("view_Qualifications.fxml", new ActionEvent());

                            }

                        });
                        HBox buttons = new HBox(edit,delete);
                        setGraphic(buttons);
                        setText(null);
                    }
                }


            };
            return cell;
        };

        editCol.setCellFactory(cellFactory);
        qualificationTable.setItems(QualificationList);



    }

    private void searchingOnAction() {
        try {

                FilteredList<Qualification> filteredList = new FilteredList<>(QualificationList, b -> true);
                searchTxt.textProperty().addListener((observable, oldValue, newValue) ->{
                    //System.out.println("CHECK 1");
                    searchBtn.setOnAction((ActionEvent event) -> {
                        filteredList.setPredicate(data -> {
                            if (newValue.isEmpty() || newValue.isBlank()) {
                                return true;
                            }
                            String searchWord = newValue.toLowerCase();

                            if (data.getQualificationName().toLowerCase().contains(searchWord)) {
                                return true;
                            } else if (data.getType().toLowerCase().contains(searchWord)) {
                                return true;
                            } else return data.getFaculty().toLowerCase().contains(searchWord);

                        });
                    });
                });

                SortedList<Qualification> sortedList = new SortedList<>(filteredList);
                sortedList.comparatorProperty().bind(qualificationTable.comparatorProperty());

                qualificationTable.setItems(sortedList);



        } catch (Exception e) {
            System.out.println("-----CANNOT DO SEARCH----");
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void backBtnClicked(ActionEvent event){
        SwitchPages switchPage = new SwitchPages();
        switchPage.switching("view_Qualifications.fxml", event);

    }
    @FXML
    private void homeBtnClicked(ActionEvent event) {
        SwitchPages switchPages = new SwitchPages();
        switchPages.switching("home_Page.fxml",event);
    }
    @FXML
    private void addQualifClicked(ActionEvent event) {
        SwitchPages switchPages = new SwitchPages();
        switchPages.switching("add_Qualification.fxml",event);
    }

}
