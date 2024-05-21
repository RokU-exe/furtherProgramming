package gui;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.*;
import utils.DBUtil;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static models.UserRole.*;


public class PolicyOwnerController implements Initializable {


    private PolicyOwner owner;

    @FXML
    private TextField email;
    @FXML
    private TextField fullname;
    @FXML
    private TextField password;
    @FXML
    private TextField userID;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }


    public PolicyOwnerController(PolicyOwner owner) {
        this.owner = owner;
    }

    ;

    public PolicyOwnerController() {
        //default constructor
    }

    ;

    @FXML
    private MenuButton beneficiary_role;

    private UserRole getRole(String role) {
        if (role.equals("Policy Owner")) {
            return POLICY_OWNER;
        } else if (role.equals("Policy Holder")) {
            return POLICY_HOLDER;
        } else if (role.equals("Dependent")) {
            return DEPENDENT;
        } else if (role.equals("Insurance Surveyor")) {
            return INSURANCE_SURVEYOR;
        } else if (role.equals("Insurance Manager")) {
            return INSURANCE_MANAGER;
        } else {
            // Handle the case when the role is not recognized
            // For example, you can throw an IllegalArgumentException
            throw new IllegalArgumentException("Unknown role: " + role);
        }
    }

    //1.start with opening the Owner Dashboard
    public void openPolicyOwnerDashboard(Button loginButton) {
        // Open the Policy Owner Dashboard
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/PolicyOwnerDashBoard.fxml"));
            Parent adminDashboardRoot = loader.load();

            Node sourceNode = loginButton; // Use any node from the current scene

            // Get the primary stage from the source node's scene
            Stage primaryStage = (Stage) sourceNode.getScene().getWindow();

            primaryStage.setScene(new Scene(adminDashboardRoot));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any errors loading the admin dashboard FXML
        }
    }

    //2. enter the beneficiary menu
    @FXML
    private Button beneficiary_menu_button;

    //Adding beneficiaries and their roles
    @FXML
    public void beneficiary_Menu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/PolicyOwnerGUI/Beneficiary_Menu.fxml"));
            Parent adminDashboardRoot = loader.load();

            Node sourceNode = beneficiary_menu_button; // Use any node from the current scene

            // Get the primary stage from the source node's scene
            Stage primaryStage = (Stage) sourceNode.getScene().getWindow();

            primaryStage.setScene(new Scene(adminDashboardRoot));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any errors loading the admin dashboard FXML
        }
    }

    // enter the Interface of adding user
    @FXML
    private Button add_Bene_Button;

    public void add_beneficiary_window() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/PolicyOwnerGUI/Add_Beneficiary.fxml"));
            Parent adminDashboardRoot = loader.load();

            Node sourceNode = add_Bene_Button; // Use any node from the current scene

            // Get the primary stage from the source node's scene
            Stage primaryStage = (Stage) sourceNode.getScene().getWindow();

            primaryStage.setScene(new Scene(adminDashboardRoot));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any errors loading the admin dashboard FXML
        }
    }

    @FXML
    private void selectRole(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String roleName = menuItem.getText();

        // Set the selected role as the text of the MenuButton
        beneficiary_role.setText(roleName);
    }

    @FXML
    private Button beneficiary_Adding_Button;

    //add the user, go back to dashboard
    public void add_Bene() {
        String roleName = beneficiary_role.getText();
        System.out.println(roleName);
        // Check if a role is selected
        if (roleName.equals("Select Beneficiary's Role")) {
            System.out.println("Please select a valid role.");
            return;
        }

        UserRole userRole = getRole(roleName);
        String id = DBUtil.getLargestIdByUserRole(userRole);
        if (id == null) {
            // Handle case when no matching records are found
            System.out.println("No matching records found for role: " + roleName);
            return;
        }
        User user = new User(id, fullname.getText(), email.getText(), password.getText(), userRole);

        // Add a new user to the database
        DBUtil.addUser(user);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Add " + roleName);
        alert.setHeaderText(roleName + " Added Successfully!");
        alert.setContentText(user.toString());
        alert.showAndWait();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/PolicyOwnerDashBoard.fxml"));
            Parent adminDashboardRoot = loader.load();

            Node sourceNode = beneficiary_Adding_Button; // Use any node from the current scene

            // Get the primary stage from the source node's scene
            Stage primaryStage = (Stage) sourceNode.getScene().getWindow();

            primaryStage.setScene(new Scene(adminDashboardRoot));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any errors loading the admin dashboard FXML
        }
    }


    @FXML
    private Button remove_Bene_Button;

    public void remove_beneficiary_window() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/PolicyOwnerGUI/Remove_Beneficiary.fxml"));
            Parent adminDashboardRoot = loader.load();

            Node sourceNode = remove_Bene_Button; // Use any node from the current scene

            // Get the primary stage from the source node's scene
            Stage primaryStage = (Stage) sourceNode.getScene().getWindow();

            primaryStage.setScene(new Scene(adminDashboardRoot));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any errors loading the admin dashboard FXML
        }
    }

    @FXML
    public void remove_Bene() {
        String beneficiaryID = userID.getText(); // Assuming userID is the TextField for beneficiary ID
        // Check if beneficiaryID is empty
        if (beneficiaryID.isEmpty()) {
            System.out.println("Please enter a valid Beneficiary ID.");
            return;
        }
        String roleName = beneficiary_role.getText();

        // Check if a role is selected
        if (roleName.equals("Select Beneficiary's Role")) {
            System.out.println("Please select a valid role.");
            return;
        }

        UserRole userRole;
        try {
            userRole = getRole(String.valueOf(roleName)); // Convert roleName to UserRole
        } catch (IllegalArgumentException e) {
            System.out.println("Unknown role: " + roleName);
            return;
        }

        // Call the DBUtil method to delete the user
        try {
            DBUtil.PO_deleteUser(beneficiaryID, userRole);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Remove " + roleName);
            alert.setHeaderText(roleName + " Removed Successfully!");
            alert.setContentText("Beneficiary ID: " + beneficiaryID + "\nRole: " + roleName);
            alert.showAndWait();

            // Navigate back to the Policy Owner Dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/PolicyOwnerDashBoard.fxml"));
            Parent adminDashboardRoot = loader.load();
            if (remove_Bene_Button != null && remove_Bene_Button.getScene() != null) {
                Node sourceNode = remove_Bene_Button; // Use any node from the current scene
                Stage primaryStage = (Stage) sourceNode.getScene().getWindow();
                primaryStage.setScene(new Scene(adminDashboardRoot));
                primaryStage.show();
            } else {
                System.out.println("Error: sourceNode is null or not attached to any scene.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any errors during the deletion process
        }
    }

    @FXML
    private Button open_update_window_Button;

    public void update_beneficiary_window_Page1() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/PolicyOwnerGUI/Update_Beneficiary_Page1.fxml"));
            Parent adminDashboardRoot = loader.load();

            Node sourceNode = open_update_window_Button; // Use any node from the current scene

            // Get the primary stage from the source node's scene
            Stage primaryStage = (Stage) sourceNode.getScene().getWindow();

            primaryStage.setScene(new Scene(adminDashboardRoot));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any errors loading the admin dashboard FXML
        }
    }

    @FXML
    private Button find_Updated_Beneficiary_button;
    public void find_updated_beneficiary_Page2() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/PolicyOwnerGUI/Update_Beneficiary_Page2.fxml"));
            Parent updateBeneficiaryPage2Root = loader.load();

            Node sourceNode = find_Updated_Beneficiary_button; // Use any node from the current scene

            // Get the primary stage from the source node's scene
            Stage primaryStage = (Stage) sourceNode.getScene().getWindow();

            primaryStage.setScene(new Scene(updateBeneficiaryPage2Root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any errors loading the update beneficiary page 2 FXML
        }
    }

//        if (user != null) {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/PolicyOwnerGUi/Update_Beneficiary_Page2.fxml"));
//            Parent root = loader.load();
//
//            UpdateBeneficiaryPage2Controller controller = loader.getController();
//            controller.initData(String.valueOf(fullname)); // Pass the data to the controller
//
//            // Proceed with setting the scene and showing the stage
//            Scene scene = new Scene(root);
//            Stage stage = (Stage) find_Updated_Beneficiary_button.getScene().getWindow(); // Accessing the button's scene to get the window
//            stage.setScene(scene);
//            stage.show();
//        } else {
//            // Handle case where user is not found
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("User Not Found");
//            alert.setHeaderText(null);
//            alert.setContentText("User with ID " + user_ID + " not found!");
//            alert.showAndWait();
//        }
//        String user_ID = userID.getText();
//        User user = DBUtil.getUser(user_ID);
//        if (user != null) {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/PolicyOwnerGUi/Update_Beneficiary_Page2.fxml"));
//            Parent root = loader.load();
//
//            UpdateBeneficiaryPage2Controller controller = loader.getController();
//            controller.initData(String.valueOf(user)); // Pass the User object to the next page
//
//            // Proceed with setting the scene and showing the stage
//            Scene scene = new Scene(root);
//            Stage stage = (Stage) find_Updated_Beneficiary_button.getScene().getWindow(); // Accessing the button's scene to get the window
//            stage.setScene(scene);
//            stage.show();
//        } else {
//            // Handle case where user is not found
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("User Not Found");
//            alert.setHeaderText(null);
//            alert.setContentText("User with ID " + user_ID + " not found!");
//            alert.showAndWait();
//        }





    @FXML
    private Button back;

    public void backtoDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/SystemAdminGUI/systemAdminDashboard.fxml"));
            Parent adminDashboardRoot = loader.load();

            Node sourceNode = back; // Use any node from the current scene

            // Get the primary stage from the source node's scene
            Stage primaryStage = (Stage) sourceNode.getScene().getWindow();

            primaryStage.setScene(new Scene(adminDashboardRoot));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any errors loading the admin dashboard FXML
        }
    }

    @FXML
    private Button deleteClaimButton;

    public void deleteClaim() {

    }


    //Initialization of buttons in PolicyOwner Dashboard
    @FXML
    private Button policyHolderChooseButton;

    @FXML
    private Button dependentChooseButton;

    @FXML
    private Button backtoDashoardButton;

    @FXML
    private Button updateClaimButtom;

    public void updateClaim() {

    }
}






