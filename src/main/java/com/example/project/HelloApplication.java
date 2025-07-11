package com.example.project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage){
        //WriteUnivertyReview(stage);
        //WriteBursaryReview(stage);
        ViewUniReviews(stage);
        //ViewBursaryReviews(stage);
    }

    public void WriteUnivertyReview(Stage stage100)
    {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ProjectFxml.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 320, 240);
            stage100.setTitle("Project");
            stage100.setWidth(605);
            stage100.setHeight(460);
            stage100.setScene(scene);
            //stage100.setMaximized(true);
            stage100.show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void WriteBursaryReview(Stage stage200)
    {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("WriteBursaryReview.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 320, 240);
            stage200.setTitle("Project");
            stage200.setWidth(605);
            stage200.setHeight(460);
            stage200.setScene(scene);
            //stage100.setMaximized(true);
            stage200.show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void ViewUniReviews(Stage stage700)
    {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ViewUniversityReview.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 320, 240);
            stage700.setTitle("Project");
            stage700.setWidth(605);
            stage700.setHeight(460);
            stage700.setScene(scene);
            //stage100.setMaximized(true);
            stage700.show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void ViewBursaryReviews(Stage stage800)
    {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ViewBursaryReview.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 320, 240);
            stage800.setTitle("Project");
            stage800.setWidth(605);
            stage800.setHeight(460);
            stage800.setScene(scene);
            //stage100.setMaximized(true);
            stage800.show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch();
    }
}