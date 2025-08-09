package util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {

    private static Stage stage;


    public static void setStage(Stage primaryStage) {
        stage = primaryStage;
    }


    public static void switchTo(String fxmlPath) {
        if (stage == null) {
            System.err.println("Stage has not been set in SceneManager. Please call setStage() first.");
            return;
        }

        try {
            Parent root = FXMLLoader.load(SceneManager.class.getResource(fxmlPath));


            if (stage.getScene() == null) {

                stage.setScene(new Scene(root));
            } else {

                stage.getScene().setRoot(root);
            }

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText("Could not load the screen.");
            alert.setContentText("An error occurred while trying to load: " + fxmlPath);
            alert.showAndWait();
        }
    }
}
