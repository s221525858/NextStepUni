package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import model.*;
import util.SceneManager;

import java.net.URL;
import java.util.*;

public class DashboardController implements Initializable {

    @FXML
    private Button loginButton;
    @FXML
    private MenuButton profileButton;
    @FXML
    private Label welcomeLabel;
    @FXML
    private TextField searchField;
    @FXML
    private FlowPane dashboardFlowPane;


    private final List<University> allUniversities = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boolean isUserLoggedIn = checkUserLoginStatus();
        setupDashboard(isUserLoggedIn);
    }

    private void setupDashboard(boolean isLoggedIn) {
        loginButton.setVisible(!isLoggedIn);
        loginButton.setManaged(!isLoggedIn);
        profileButton.setVisible(isLoggedIn);
        profileButton.setManaged(isLoggedIn);

        welcomeLabel.setVisible(isLoggedIn);
        welcomeLabel.setManaged(isLoggedIn);
        if (isLoggedIn) {
            String studentName = UserSession.getInstance().getUserName();
            welcomeLabel.setText("Welcome, " + studentName);
        }

        populateDashboardCards(isLoggedIn);
    }

    private void populateDashboardCards(boolean isLoggedIn) {
        dashboardFlowPane.getChildren().clear();

        Node universitiesCard = createDashboardCard("View Universities", "/images/icons/universities_icon.png", this::handleViewUniversities);
        Node bursariesCard = createDashboardCard("View Bursaries", "/images/icons/bursaries_icon.png", this::handleViewBursaries);
        Node reviewsCard = createDashboardCard("View Reviews", "/images/icons/reviews_icon.png", this::handleViewReviews);

        dashboardFlowPane.getChildren().addAll(universitiesCard, bursariesCard, reviewsCard);

        if (isLoggedIn) {
            Node writeReviewCard = createDashboardCard("Write Reviews", "/images/icons/write_review_icon.png", this::handleWriteReview);
            dashboardFlowPane.getChildren().add(writeReviewCard);
        }
    }

    private Node createDashboardCard(String title, String imagePath, Runnable action) {
        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);
        card.setCursor(Cursor.HAND);

        VBox imageContainer = new VBox();
        imageContainer.setAlignment(Pos.CENTER);
        imageContainer.setPrefSize(200, 150);
        imageContainer.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 15; -fx-background-radius: 15;");

        ImageView icon = new ImageView();
        try {
            icon.setImage(new Image(getClass().getResourceAsStream(imagePath)));
        } catch (Exception e) {
            System.err.println("Could not load icon: " + imagePath);
        }
        icon.setFitHeight(100);
        icon.setFitWidth(100);
        icon.setPreserveRatio(true);
        imageContainer.getChildren().add(icon);

        Button cardButton = new Button(title);
        cardButton.setPrefWidth(200);
        cardButton.setStyle("-fx-background-color: #d51e1e; -fx-text-fill: white; -fx-background-radius: 5;");
        cardButton.setFont(Font.font("System", FontWeight.BOLD, 14));
        cardButton.setOnAction(event -> action.run());

        card.getChildren().addAll(imageContainer, cardButton);
        card.setOnMouseClicked(event -> action.run());

        return card;
    }

    @FXML
    private void handleLoginRegister() {
        SceneManager.switchTo("/view/login_view.fxml");
    }

    private void handleViewUniversities() {
        SceneManager.switchTo("/view/view_universities.fxml");
    }

    private void handleViewBursaries() {
        SceneManager.switchTo("/view/view_bursaries.fxml");
    }

    private void handleViewReviews() {
        SceneManager.switchTo("/view/view_reviews.fxml");
    }

    private void handleWriteReview() {
        SceneManager.switchTo("/view/write_review.fxml");
    }

    @FXML
    private void handleViewProfile() {
        SceneManager.switchTo("/view/profile.fxml");
    }

    @FXML
    private void handleLogout() {
        System.out.println("User logging out...");
        UserSession.getInstance().logout();
        setupDashboard(false);
    }
    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean checkUserLoginStatus() {
        return UserSession.getInstance().isLoggedIn();
    }
}
