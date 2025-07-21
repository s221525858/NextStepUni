package com.example.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class SwitchPages {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switching(String fxmlName, ActionEvent event) {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlName)));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.out.println("---Error - Can't switch pages--- CHECK FILE NAME\n" + e.getMessage());
        }
    }
}
