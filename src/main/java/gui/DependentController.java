package gui;

import controllers.SystemAdmin;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.*;
import utils.DBUtil;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DependentController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
    @FXML
    private TextField insuredPerson;
    Dependent dependent;
    public DependentController(Dependent dependent) {
        this.dependent = dependent;
    }
    public DependentController() {
        // Initialization code here, if needed
    }

    @FXML
    //Open Dependent dashboard
    void openDependentDashboard(Button loginButton) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/DependentGUI/DependentCheckAgain.fxml"));
            Parent adminDashboardRoot = loader.load();
            Scene adminDashboardScene = new Scene(adminDashboardRoot);

            // Get any node from the current scene
            Node sourceNode = loginButton; // Use any node from the current scene

            // Get the primary stage from the source node's scene
            Stage primaryStage = (Stage) sourceNode.getScene().getWindow();

            primaryStage.setScene(adminDashboardScene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any errors loading the admin dashboard FXML
        }
    }


    //METHOD TO VIEW PROFILE AND MOVE TO DEPENDENT VIEW PROFILE FXML
    @FXML
    private Button profileButton;
    public  Button getProfileButton() {
        return profileButton;
    }

    @FXML
    public void getProfile() {
        // Get the current user from the session
        LoginSession loginSession = LoginSession.getInstance();
        User currentUser = loginSession.getCurrentUser();

        if (currentUser != null) {
            // Fetch the profile information based on the user's email and password
            User userProfile = DBUtil.getUserByEmailAndPassword(currentUser.getEmail(), currentUser.getPassword());

            if (userProfile != null) {
                // Display the profile information
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Profile Information");
                alert.setHeaderText("Profile Retrieved Successfully");
                alert.setContentText(userProfile.toString());
                alert.showAndWait();

                // Depending on your GUI setup, you may want to pass the userProfile object to the profile view controller
                // and display the profile information there
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/DependentGUI/DependentViewProfile.fxml"));
                    Parent adminDashboardRoot = loader.load();
                    Node sourceNode = profileButton;

                    // Get the primary stage from the current scene
                    Stage primaryStage = (Stage) sourceNode.getScene().getWindow();

                    primaryStage.setScene(new Scene(adminDashboardRoot));
                    primaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Handle case where profile information could not be retrieved
                System.out.println("Error: Profile information not available.");
            }
        } else {
            // Handle case where no user is logged in
            System.out.println("Error: No user logged in.");
        }
    }


    //METHOD TO RETRIEVE CLAIM AND MOVE TO VIEW CLAIM FXML
    @FXML
    private Button claimButton;
    public  Button getClaimButton() {
        return claimButton;
    }
    @FXML
    public void getClaim() {
        LoginSession loginSession = LoginSession.getInstance();
        User currentUser = loginSession.getCurrentUser();

        if (currentUser != null) {
            List<Claim> userClaim =  DBUtil.getFilteredClaims();

            if (userClaim != null) {
                // Display the claim information
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Claim Information");
                alert.setHeaderText("Claim Retrieved Successfully");
                alert.setContentText(userClaim.toString());
                alert.showAndWait();
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/DependentGUI/DependentViewClaim.fxml"));
                    Parent adminDashboardRoot = loader.load();

                    Node sourceNode = claimButton;

                    // Get the primary stage from the current scene
                    Stage primaryStage = (Stage) sourceNode.getScene().getWindow();

                    primaryStage.setScene(new Scene(adminDashboardRoot));
                    primaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Handle case where claim information could not be retrieved
                System.out.println("Error: Claim information not available.");
            }
        } else {
            // Handle case where no claim is found
            System.out.println("Error: No claim found.");
        }
    }

}
