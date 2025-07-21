package com.example.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {

    @FXML
    private TextField FnameTxt;
    @FXML
    private TextField LnameTxt;
    @FXML
    private TextField emailTxt;
    @FXML
    private TextField PasswordTxt;
    @FXML
    private TextField ConfirmPasswordTxt;
    @FXML
    private ChoiceBox<String> uniList;
    @FXML
    private DatePicker DOBDatePicker;
    @FXML
    private Label regProofNameLbl;
    @FXML
    private Label warningLbl;

    private String fName, lName, email, password, confirmPassword, universityName,universityID;
    private Date dateOfBirth;
    private String pathToProofOfReg;
    private File proofOfRegFile;
    private FileInputStream fis;
    Student newStudent;
    @FXML
    public void date(ActionEvent event) {
        try {
            dateOfBirth = Date.valueOf(DOBDatePicker.getValue());

        } catch (Exception e) {
            System.out.println("DOB Error: \n" + e.getMessage());
        }
    }
    @FXML
    public void importBtnAction(ActionEvent event){

        try {
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.setInitialDirectory(new File("C:\\OtherProjectFiles"));
            fileChooser.getExtensionFilters().add(filter);
            proofOfRegFile = fileChooser.showOpenDialog(new Stage());
            fis = new FileInputStream(proofOfRegFile);

            if(proofOfRegFile != null){
                pathToProofOfReg = proofOfRegFile.getName();
                regProofNameLbl.setText(pathToProofOfReg);
            }
            else{
                regProofNameLbl.setText("No Proof of Reg found");
            }

        } catch (Exception e) {
            System.out.println("Error: Cannot import picture\n" + e.getMessage());
        }
    }
    public void getUniFK(String uniName){
        try{
            DBConnection connectToDB = new DBConnection();
            Connection connection = connectToDB.connectToDB();
            String sqlQuery = "Select UniversityId From University where UniName=?";
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, uniName);
            ResultSet result = statement.executeQuery();
            if(result.next()){
                int found = result.getInt(1);
                universityID = String.valueOf(found);
            }
            else{
                System.out.println("No uniID found");
            }
            statement.close();
            connection.close();
        }
        catch (SQLException e){
            System.out.println("Error: Cannot get university id\n" + e.getMessage());
        }
        //return uniID;
    }
    public boolean emailValidation(String newEmail){
        char[] emailArray = newEmail.toCharArray();
        int emailLength = newEmail.length();
        int charPos = 0;
        char atSign = '@';
        char dotSign = '.';
        while(charPos < (emailLength)){
            //if the first or last character is an @ or . then email is invalid
            if(emailArray[0] == atSign || emailArray[emailLength-1] == atSign || emailArray[0] == dotSign || emailArray[emailLength-1] == dotSign){
                return false;
            }
            if(emailArray[charPos] == atSign){
                return true;
            }
            charPos++;
        }
        return false;
    }
    public ArrayList<String> listOfUnis(){
        ArrayList<String> listOfExistingUnis = new ArrayList<>();

        PreparedStatement stmt = null;
        try {
            DBConnection connectToDB = new DBConnection();
            Connection connection = connectToDB.connectToDB();
            String query = "Select UniName From University";
            stmt = connection.prepareStatement(query);
            ResultSet result = stmt.executeQuery();
            while(result.next()){
                listOfExistingUnis.add(result.getString(1));
            }
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error: Cannot select unis\n" + e.getMessage());
        }

        Collections.sort(listOfExistingUnis);
        return listOfExistingUnis;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        uniList.getItems().addAll(listOfUnis());
    }

    @FXML
    public void registerBtnAction(ActionEvent event) throws IOException {

        fName = FnameTxt.getText();
        lName = LnameTxt.getText();
        email = emailTxt.getText();
        password = PasswordTxt.getText();
        confirmPassword = ConfirmPasswordTxt.getText();


        if((fName==null) || (lName.isBlank()) || (email.isBlank()) || (password.isBlank()) || (confirmPassword.isBlank())) {
            warningLbl.setText("Please fill all the fields");
        }
        else{
            byte[] image = fis.readAllBytes();
            fis.close();
            boolean isVerified = false;
            universityName = uniList.getValue();
            getUniFK(universityName);
            try {
                DBConnection connectToDB = new DBConnection();
                Connection connection = connectToDB.connectToDB();
                System.out.println("-----------Connected to DB-----------");
                if(password.equals(confirmPassword) && (password.length() >= 8) && (emailValidation(email))) {
                    newStudent = new Student(fName,lName,email,password,dateOfBirth.toString(),universityName);

                    String sqlQuery = "insert into Student(StuLName,StuFName,ProofOfReg,EmailAddress,Password,StudDob,IsVerified,UniversityID) values(?,?,?,?,?,?,?,?)";
                    PreparedStatement stmt = connection.prepareStatement(sqlQuery);
                    stmt.setString(1, lName);
                    stmt.setString(2, fName);
                    stmt.setBytes(3,image);
                    stmt.setString(4, email);
                    stmt.setString(5, password);
                    stmt.setDate(6, dateOfBirth);
                    stmt.setBoolean(7, isVerified);
                    stmt.setString(8, universityID);
                    stmt.executeUpdate();

                    stmt.close();
                    connection.close();

                    SwitchPages switchPage = new SwitchPages();
                    switchPage.switching("loginPage.fxml", event);

                }else{
                    if(!emailValidation(email)){
                        emailTxt.setBorder(Border.stroke(Color.RED));
                    }
                    if(!password.equals(confirmPassword)){
                        ConfirmPasswordTxt.setBorder(Border.stroke(Color.RED));
                    }
                    if(password.length() < 8){
                        PasswordTxt.setBorder(Border.stroke(Color.RED));
                    }
                }

            }catch (SQLException e) {
                System.err.println("SQL State: " + e.getSQLState());
                System.err.println("Error Code: " + e.getErrorCode());
                System.err.println("Cause: " + e.getCause());
            }
        }


    }
    @FXML
    public void cancelBtnAction(ActionEvent event) {
        SwitchPages page = new SwitchPages();
        page.switching("loginPage.fxml", event);
    }
    @FXML
    public void loginBtnAction(ActionEvent event) {
        SwitchPages page = new SwitchPages();
        page.switching("loginPage.fxml", event);
    }

}
