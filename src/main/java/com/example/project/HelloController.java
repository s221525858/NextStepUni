package com.example.project;
import java.sql.*;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.time.LocalDate;

public class HelloController {

    Connection connect = DatabaseConnect.connect(); //Databaseconnect to connect to DB
    @FXML
    private javafx.scene.control.TextArea UnivesirtyReviewTextArea;

    @FXML
    private javafx.scene.control.TextArea BursaryReviewTextAreaC200;

    @FXML
   // private javafx.scene.control.Spinner<Integer> ratingSpinner;

    private final int studentID = 1; // Replace with actual logged-in student
    private final int universityID = 1; // Replace with selected university
    private final int BursaryID = 1;

    @FXML
    private Button SubmitButton;  //First Submit button on WriteUniversityReview page

    @FXML
    private Button SubmitButtonC200;  //First Submit button on WriteBursaryReview page

    @FXML
    private Button cancelButton;  //Cancel Button on WriteUniversity Review

    @FXML
    private Button cancelButtonC200;  //Cancel Button on WriteBursary Review

    @FXML
    private Button ConfirmSubmitButton; //confirming submit button on WriteUniversityReview

    @FXML
    private Button ConfirmSubmitButtonC200; //confirming submit button on WriteBursaryReview

    @FXML
    private AnchorPane ConfirmationPopUp;  //Anchorpane confirmation information on writeUniReview submit button

    @FXML
    private AnchorPane ConfirmationPopUpC200;  //Anchorpane confirmation information on writeBursary submit button

    /*
    Handling Buttons and text areas for Write University review Use case
    All the methods for Write University Review are here
    C100 is Use Case ID for Write University Review Use case, in this case method names have no ID since it was the first
     */
    @FXML
    private void onSubmitButtonClicked() {
        ConfirmationPopUp.setVisible(true); //show confirmation anchorpane - writeuniversityreview
    }

    @FXML
    private void onCancelButtonClicked() {
        ConfirmationPopUp.setVisible(false); //hide confirmation anchorpane-writeUniReview
    }

    @FXML
    private void onConfirmSubmitButtonClicked() {
        ConfirmationPopUp.setVisible(false);

        // Example hardcoded values
        int universityID = 1; // Replace with actual selected university ID
        int studentID = 1;  // Replace with actual logged-in student ID
        String content = UnivesirtyReviewTextArea.getText();  // gte written from text area in UI with fxId UniversityReviewTextArea
        int rating = 5;  // Default rating
        java.sql.Date datePosted = java.sql.Date.valueOf(java.time.LocalDate.now());

        String insertSQL = "INSERT INTO UniversityReviews (UniversityID, StudentID, Content, Rating, dateposted) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnect.connect();
             PreparedStatement stmt = conn.prepareStatement(insertSQL)) {

            stmt.setInt(1, universityID);
            stmt.setInt(2, studentID);
            stmt.setString(3, content);
            stmt.setInt(4, rating);
            stmt.setDate(5, datePosted);

            stmt.executeUpdate();
            System.out.println("Review inserted into database!");

        } catch (SQLException e) {
            System.out.println("Error inserting review.");
            e.printStackTrace();
        }
        System.out.println("Review Submitted!");

    }

    @FXML
    private void onBackButtonClicked() {
        System.out.println("Back Button clicked!");//place holder for some logic-WriteUniReview
    }

    /*
    Handling Buttons and text areas for Write Bursary review Use case
    All the methods for Write bursary Review are here
    C200 is Use Case ID for Bursary Review
     */

    @FXML
    private void onSubmitButtonClickedC200() {
        ConfirmationPopUpC200.setVisible(true); //show confirmation anchorpane - writeBursaryreview
    }
    @FXML
    private void onCancelButtonClickedC200() {
        ConfirmationPopUpC200.setVisible(false); //hide confirmation anchorpane-writeBursaryReview
    }
    @FXML
    private void onBackButtonClickedC200() {
        System.out.println("Back Button clicked!");//place holder for some logic-WriteBursaryReview
    }
    @FXML
    private void onConfirmSubmitButtonClickedC200() { //inserting int Bursary Review table after User enters submit when confirming
        ConfirmationPopUpC200.setVisible(false);

        // Example hardcoded values
        int bursaryID = 1; // Replace with actual selected university ID
        int studentID = 1;  // Replace with actual logged-in student ID
        String content = BursaryReviewTextAreaC200.getText();  // gte written from text area in UI with fxId UniversityReviewTextArea
        int rating = 5;  // Default rating -- will change soon
        java.sql.Date datePosted = java.sql.Date.valueOf(java.time.LocalDate.now());

        String insertSQL = "INSERT INTO BursaryReviews (BursaryID, StudentID, Content, Rating, dateposted) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnect.connect();
             PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
            //statements inserting into table
            stmt.setInt(1, bursaryID);
            stmt.setInt(2, studentID);
            stmt.setString(3, content);
            stmt.setInt(4, rating);
            stmt.setDate(5, datePosted);

            stmt.executeUpdate();
            System.out.println("Review inserted into database!");

        } catch (SQLException e) {
            System.out.println("Error inserting review.");
            e.printStackTrace();
        }
        System.out.println("Review Submitted!");

    }

}