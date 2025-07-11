package com.example.project;
import java.sql.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.SVGPath;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HelloController {

    //Connecting to DataBase
    Connection connect = DatabaseConnect.connect(); //Databaseconnect to connect to DB

    //@FXML Variables and handles for Write University and Bursary review starts here
    //C100 and C200 declared vars
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
    private AnchorPane ConfirmationPopUp;  //Anchor pane confirmation information on writeUniReview submit button
    @FXML
    private AnchorPane ConfirmationPopUpC200;  //Anchor pane confirmation information on writeBursary submit button
    //C100 and C200 @FXML end here


    //C700 USE CASE
    //@FXML for View University Reviews
    @FXML
    private StackPane UserLogoStackPaneC700;

    @FXML
    private SVGPath HomeLogoC700;

    @FXML
    private TextField SearchTextFieldC700;

    @FXML
    private SVGPath SearchIconC700;

    @FXML
    private Button BackButtonC700;

    @FXML
    private Pane UniLogoPaneC700;

    @FXML
    private ImageView ImageViewUniLogoC700;

    @FXML
    private Label UniNameReviewsLabelC700;

    @FXML
    private ScrollPane ScrollPaneC700;

    @FXML
    private VBox ReviewsVBoxC700;

    @FXML
    private HBox HBoxReviewsC700;

    @FXML
    private Label ReviewLabelC700;

    @FXML
    private SVGPath DeleteReviewIconC700;

    @FXML
    private Button ReviewBackButtonC700;

    @FXML
    private StackPane ReviewStackPaneC700;

    @FXML
    private TextArea ReviewTextAreaC700;

    public List<String> AllReviews = new ArrayList<>();
    //ALL @FXML for use case C700 View University Review ID


    //C800 USE CASE
    //@FXML for View Bursary Reviews
    @FXML
    private StackPane UserLogoStackPaneC800;

    @FXML
    private SVGPath HomeLogoC800;

    @FXML
    private TextField SearchTextFieldC800;

    @FXML
    private SVGPath SearchIconC800;

    @FXML
    private Button BackButtonC800;

    @FXML
    private Pane UniLogoPaneC800;

    @FXML
    private ImageView ImageViewUniLogoC800;

    @FXML
    private Label UniNameReviewsLabelC800;

    @FXML
    private ScrollPane ScrollPaneC800;

    @FXML
    private VBox ReviewsVBoxC800;

    @FXML
    private HBox HBoxReviewsC800;

    @FXML
    private Label ReviewLabelC800;

    @FXML
    private SVGPath DeleteReviewIconC800;

    @FXML
    private Button ReviewBackButtonC800;

    @FXML
    private StackPane ReviewStackPaneC800;

    @FXML
    private TextArea ReviewTextAreaC800;

    public List<String> AllBursaryReviews = new ArrayList<>();
    //ALL @FXML for use case C700 View University Review ID


    //C100 USE CASE
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


    //C200 USE CASE
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


    //C700 USE CASE
    /*
    Handling Buttons and text areas for View University review Use case
    All the methods for View University Review are here
    C700 is Use Case ID for View University Review
     */
    @FXML
    public void onDragUserLogoC700()
    {
        System.out.printf("only if the the user is registered");
        System.out.println("Should be implemented in such a way that it shows the user her profile");
    }

    @FXML
    private void onHandleBackButtonC700()
    {
        System.out.println("Back Button clicked!"); //to be implemented
    }

    public void onHandleSearchTextFieldC700()
    {
        String Keyword = SearchTextFieldC700.getText().trim();     //string entered by user in UI

        List<String> filteredReviews = AllReviews.stream().filter(review -> review.toLowerCase().contains(Keyword.toLowerCase())).collect(Collectors.toList());
        ReviewsVBoxC700.getChildren().clear();
        loadreviews(filteredReviews);
    }
    public void loadreviews(List<String>reviews)
    {
        for(String reviewtext : reviews)
        {
            Label reviewlabel = new Label(reviewtext);
            reviewlabel.setWrapText(true);

            HBox labelrow = new HBox(reviewlabel);
            labelrow.setSpacing(10);
            labelrow.setStyle("-fx-padding: 5; -fx-border-color: #e6e7e8  ; -fx-border-width: 0 0 1 0;");
            labelrow.setOnMouseClicked(event -> {
                ReviewTextAreaC700.setText(reviewtext);
                ReviewTextAreaC700.setVisible(true);
                ReviewBackButtonC700.setVisible(true);
                ReviewTextAreaC700.setStyle(
                        "-fx-background-color: #737373 ; " +
                                "-fx-text-fill: #000000; " +
                                "-fx-border-color: #000000; " +
                                "-fx-border-width: 0.1; " +
                                "-fx-font-size: 11px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-padding: 2;"
                );
            });
            ReviewsVBoxC700.getChildren().add(labelrow);
        }
    }
    public List<String> retrieveUniReviews() {
        AllReviews = new ArrayList<>();
        String SQL = "SELECT content FROM UniversityReviews ";
        try (Connection conn = DatabaseConnect.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            while (rs.next()) {
                AllReviews.add(rs.getString("content"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return AllReviews;
    }
    public void DisplayUniReviews()
    {
        AllReviews = retrieveUniReviews();      //get reviews from DB
        ReviewsVBoxC700.getChildren().clear();                //clear old reviews

        //creating HBox for each review and add it to VBox
        for(String reviewtext :  AllReviews)
        {
            Label reviewlabel = new Label(reviewtext);
            reviewlabel.setWrapText(true);

            HBox labelrowC700 = new HBox(reviewlabel);
            labelrowC700.setSpacing(10);
            labelrowC700.setStyle("-fx-padding: 5; -fx-border-color: #e6e7e8  ; -fx-border-width: 0 0 1 0;");

            //add event for each row with review clicked
            labelrowC700.setOnMouseClicked(event -> {
                ReviewTextAreaC700.setText(reviewtext);
                ReviewTextAreaC700.setVisible(true);
                ReviewBackButtonC700.setVisible(true);
                ReviewTextAreaC700.setStyle(
                        "-fx-background-color: #737373 ; " +
                                "-fx-text-fill: #000000; " +
                                "-fx-border-color: #000000; " +
                                "-fx-border-width: 0.1; " +
                                "-fx-font-size: 11px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-padding: 2;"
                );
            });

            // Add row to the VBox
            ReviewsVBoxC700.getChildren().add(labelrowC700);
        }
    }
    @FXML
    private void initialize() { // to be implemented when a specified uni is selected

        retrieveUniReviews();
        DisplayUniReviews();  // Load the reviews once the scene is ready
        ReviewTextAreaC700.setVisible(false);
        ReviewBackButtonC700.setVisible(false);
//        retrieveBursaryReviews();
//        DisplayBursaryReviews(); // Load the reviews once the scene is ready
//        ReviewTextAreaC800.setVisible(false);
//        ReviewBackButtonC800.setVisible(false);
    }

    @FXML
    public void onHandleUniLogoPaneC700()
    {
        System.out.println("should handle university logo for certain uni");
    }

    @FXML
    public void onHandleReviewBackButtonC700()
    {
        ReviewTextAreaC700.setVisible(false);
        ReviewBackButtonC700.setVisible(false);
    }

    @FXML
    public void onDetectedimageViewUniLogoC700()
    {

    }
    @FXML
    public void onDetectedUniNamereviewsLabelC700()
    {

    }
    @FXML
    public void onDetectedscrollPaneC700()
    {

    }
    @FXML
    public void onDetectedReviewsVBoxC700()
    {

    }

    @FXML
    public void onDetectedHboxReviewsC700()
    {

    }
    @FXML
    public void onDetecetdReviewLabelC700()
    {

    }
    @FXML
    public void onDetectedDeleteReviewIconC700()
    {

    }
    @FXML
    public void onDetectedReviewTextAreaC700()
    {

    }
    @FXML
    public void onHandleSearchIconC700()
    {

    }


    //C800 USE CASE
    /*
    Handling Buttons and text areas for View Bursary review Use case
    All the methods for View Bursary Review are here
    C800 is Use Case ID for View Bursary Review
     */
    @FXML
    public void onDragUserLogoC800()
    {
        //homo user profile for admin and registered students to be implemented
        System.out.printf("only if the the user is registered");
        System.out.println("Should be implemented in such a way that it shows the user her profile");
    }

    @FXML
    private void onHandleBackButtonC800()
    {
        System.out.println("Back Button clicked!"); //to be implemented
    }

    public void onHandleSearchTextFieldC800()
    {
        String BursaryKeyword = SearchTextFieldC800.getText().trim();     //string entered by user in UI

        List<String> filteredBursaryReviews = AllBursaryReviews.stream().filter(review -> review.toLowerCase().contains(BursaryKeyword.toLowerCase())).collect(Collectors.toList());
        ReviewsVBoxC800.getChildren().clear();
        loadBursaryreviews(filteredBursaryReviews);

    }

    public void loadBursaryreviews(List<String>reviews)
    {
        for(String reviewtext : reviews)
        {
            Label Bursaryreviewlabel = new Label(reviewtext);
            Bursaryreviewlabel.setWrapText(true);
            HBox labelrow = new HBox(Bursaryreviewlabel);
            labelrow.setSpacing(10);
            labelrow.setStyle("-fx-padding: 5; -fx-border-color: #e6e7e8  ; -fx-border-width: 0 0 1 0;");
            labelrow.setOnMouseClicked(event -> {
                ReviewTextAreaC800.setText(reviewtext);
                ReviewTextAreaC800.setVisible(true);
                ReviewBackButtonC800.setVisible(true);
                ReviewTextAreaC800.setStyle(
                        "-fx-background-color: #737373 ; " +
                                "-fx-text-fill: #000000; " +
                                "-fx-border-color: #000000; " +
                                "-fx-border-width: 0.1; " +
                                "-fx-font-size: 11px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-padding: 2;"
                );
            });
            ReviewsVBoxC800.getChildren().add(labelrow);
        }
    }

    public List<String> retrieveBursaryReviews() {
        AllBursaryReviews = new ArrayList<>();
        String SQL = "SELECT content FROM BursaryReviews ";
        try (Connection conn = DatabaseConnect.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            while (rs.next()) {
                AllBursaryReviews.add(rs.getString("content"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return AllBursaryReviews;
    }

    public void DisplayBursaryReviews()
    {
         AllBursaryReviews = retrieveBursaryReviews();      //get reviews from DB bursary Reviews
        ReviewsVBoxC800.getChildren().clear();                //clear old reviews

        //creating HBox for each review and add it to VBox
        for(String reviewtext : AllBursaryReviews)
        {
            Label Bursaryreviewlabel = new Label(reviewtext);
            Bursaryreviewlabel.setWrapText(true);

            HBox labelrow = new HBox(Bursaryreviewlabel);
            labelrow.setSpacing(10);
            labelrow.setStyle("-fx-padding: 5; -fx-border-color: #e6e7e8  ; -fx-border-width: 0 0 1 0;");

            //add event for each row with review clicked
            labelrow.setOnMouseClicked(event -> {
                ReviewTextAreaC800.setText(reviewtext);

                ReviewTextAreaC800.setVisible(true);
                ReviewBackButtonC800.setVisible(true);
                ReviewTextAreaC800.setStyle(
                        "-fx-background-color: #737373 ; " +
                                "-fx-text-fill: #000000; " +
                                "-fx-border-color: #000000; " +
                                "-fx-border-width: 0.1; " +
                                "-fx-font-size: 11px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-padding: 2;"
                );
            });

            // Add row to the VBox
            ReviewsVBoxC800.getChildren().add(labelrow);
        }
    }

    @FXML
    public void onHandleUniLogoPaneC800()
    {
        System.out.println("should handle university logo for certain uni");
    }

    public void onHandleReviewBackButtonC800()
    {
        ReviewTextAreaC800.setVisible(false);
        ReviewBackButtonC800.setVisible(false);
    }

    @FXML
    public void onDetectedimageViewUniLogoC800()
    {

    }
    @FXML
    public void onDetectedUniNamereviewsLabelC800()
    {

    }
    @FXML
    public void onDetectedscrollPaneC800()
    {

    }
    @FXML
    public void onDetectedReviewsVBoxC800()
    {

    }

    @FXML
    public void onDetectedHboxReviewsC800()
    {

    }
    @FXML
    public void onDetecetdReviewLabelC800()
    {

    }
    @FXML
    public void onDetectedDeleteReviewIconC800()
    {

    }
    @FXML
    public void onDetectedReviewTextAreaC800()
    {

    }
    @FXML
    public void onHandleSearchIconC800()
    {

    }


}