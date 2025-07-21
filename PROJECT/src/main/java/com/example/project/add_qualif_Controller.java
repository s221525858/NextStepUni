package com.example.project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.Scanner;

public class add_qualif_Controller implements Initializable {

    @FXML
    private ChoiceBox<String> qualificationTypes;
    @FXML
    private ChoiceBox<String> qualificationFaculties;

    @FXML
    private TextField nameTxt;
    @FXML
    private Label errorLbl;
    @FXML
    private Label nameErrLbl;
    @FXML
    private Label facultyErrLbl;
    @FXML
    private Label typeErrLbl;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private String qualifName, qualifFaculty, qualifType;

    private final String[] qualifTypes = {"Undergraduate", "Postgraduate"};
    private final ArrayList<String> faculties = new ArrayList<>();
    public ObservableList<Qualification> allQualifications = FXCollections.observableArrayList();

    public ArrayList<String> getFaculties() throws FileNotFoundException {
        File facultiesFile = new File("facultiesList.txt");
        Scanner scanner = new Scanner(facultiesFile);

        while (scanner.hasNextLine()) {
            faculties.add(scanner.nextLine());
        }
        scanner.close();
        Collections.sort(faculties);
        return faculties;
    }

    @FXML
    public void cancelBtnOnAction(ActionEvent event) throws IOException {
        SwitchPages page = new SwitchPages();
        page.switching("admin_Dashboard.fxml", event);

    }

    @FXML
    public void homeBtnOnAction(ActionEvent event) throws IOException {
        SwitchPages page = new SwitchPages();
        page.switching("home_Page.fxml", event);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            qualificationTypes.getItems().addAll(qualifTypes);
            qualificationFaculties.getItems().addAll(getFaculties());

        } catch (Exception e) {
            System.out.println("---Error---" + e.getMessage());
        }
    }

    @FXML
    public void addBtnOnAction(ActionEvent event) {

        qualifName = nameTxt.getText();
        qualifType = qualificationTypes.getValue();
        qualifFaculty = qualificationFaculties.getValue();

        if((qualifName != null) && (qualifFaculty != null) && (qualifType != null)){
            try{

                Qualification newQualification = new Qualification(qualifName,qualifType, qualifFaculty);
                allQualifications.add(newQualification);

                DBConnection connectToDB = new DBConnection();
                Connection connection = connectToDB.connectToDB();
                System.out.println("-----------Connected to DB-----------");
                String sqlQuery = "insert into Qualification(QualName,QualType,Qualfaculty) values(?,?,?)";
                PreparedStatement stmt = connection.prepareStatement(sqlQuery);
                stmt.setString(1, qualifName);
                stmt.setString(2, qualifType);
                stmt.setString(3, qualifFaculty);
                stmt.executeUpdate();
                System.out.println("-----------Inserted into Qualification values----------");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Confirmation");
                alert.setContentText("You have successfully added qualification");
                alert.setResizable(false);
                alert.showAndWait();

                stmt.close();
                connection.close();
            }catch(Exception e){
                System.out.println("---Error in adding a qualfication---\n" + e.getMessage());
            }
        }
        else{
            errorLbl.setText("Please fill all the fields");

            if(qualifName.isEmpty()){
                nameErrLbl.setText("Please enter a valid qualification name");

            }
            if(qualifType == null){
                typeErrLbl.setText("Please select a qualification type");
            }
            if(qualifFaculty == null){
                facultyErrLbl.setText("Please select a qualification faculty");
            }
        }

        nameTxt.clear();
        qualificationTypes.getSelectionModel().clearSelection();
        qualificationFaculties.getSelectionModel().clearSelection();
    }

}