package com.example.project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ViewBursaryQualificationsController implements Initializable {
    @FXML
    private TableView<Qualification> qualificationsTable;
    @FXML
    private TableColumn<Qualification, String> nameColumn;
    @FXML
    private TableColumn<Qualification, String> typeColumn;

    @FXML
    private Label bursaryNameLbl;

    Bursary bursary = new Bursary(2,"NSFAS", LocalDate.now(),"som","wft");
    //Bursary bursary = CurrentBursary.getCurrentBursary();
    int bursaryID;
    ArrayList<Integer> qualifIDs = new ArrayList<>();
    ObservableList<Qualification> BursaryQualificationsList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("QualificationName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));

        bursaryNameLbl.setText(bursary.getBurName());
        getQualifications();
    }

    private void getBurID() {
        System.out.println("------------------CHECK 1-----------------");

        try {
            DBConnection connectToDB = new DBConnection();
            Connection connection = connectToDB.connectToDB();
            String sqlQuery = "Select BursaryID From Bursary where BurName=?";
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, bursary.getBurName());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                bursaryID = result.getInt(1);
            } else {
                System.out.println("No uniID found");
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error: Cannot get university id\n" + e.getMessage());
        }
    }

    private void getBurQualificationsIDs(){
        //qualifIDs.clear();
        try {
            System.out.println("------------------CHECK 2-----------------");
            System.out.println(bursaryID);
            DBConnection connectToDB = new DBConnection();
            Connection connection = connectToDB.connectToDB();
            String sqlQuery = "Select QualID from BursaryQualification where BursaryID =?";
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.setInt(1,bursaryID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                int num = resultSet.getInt("QualID");
                System.out.println("Number: "+num);
                qualifIDs.add(num);
            }
            resultSet.close();
            statement.close();
            connection.close();
        }
        catch (Exception e){
            System.out.println("Error getting the list of qualification ids");
            e.printStackTrace();
        }

    }
    private void getQualifications(){
        getBurID();
        getBurQualificationsIDs();
        if(qualifIDs == null){
            System.out.println("No qualifications for this bursary");
        }
        else{
            System.out.println("------------------CHECK 3-----------------");

            for(Integer qualId : qualifIDs){
                System.out.println(qualId);
                getQualification(qualId);
            }
        }

    }

    public void getQualification(int id){



        try {
            DBConnection connectToDB = new DBConnection();
            Connection connection = connectToDB.connectToDB();
            String sqlQuery = "Select QualName, QualType From Qualification where QualID ="+ id;

            PreparedStatement stmt = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()){
                BursaryQualificationsList.add(new Qualification(
                        resultSet.getString(1),
                        resultSet.getString(2)
                ));
                qualificationsTable.setItems(BursaryQualificationsList);
            }

        } catch (Exception e) {
            e.getMessage();
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

}

