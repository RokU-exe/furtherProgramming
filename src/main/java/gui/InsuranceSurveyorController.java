package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import models.Claim;
import models.ClaimStatus;
import models.User;
import utils.DBUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class InsuranceSurveyorController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    void openSurveyorDashboard(Button loginButton) {
        // Open the Insurance Surveyor Dashboard
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/InsuranceSurveyorGUI/InsuranceSurveyor.fxml"));
            Parent insuranceSurveyorDashboardRoot = loader.load();
            Scene insuranceSurveyorDashboardScene = new Scene(insuranceSurveyorDashboardRoot);

            // Get any node from the current scene
            Node sourceNode = loginButton; // Use any node from the current scene

            // Get the primary stage from the source node's scene
            Stage primaryStage = (Stage) sourceNode.getScene().getWindow();

            primaryStage.setScene(insuranceSurveyorDashboardScene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any errors loading the admin dashboard FXML
        }
    }


    //METHOD TO REVIEW CLAIM (WHICH STATUS = NEW), THEN MOVE TO ReviewClaimOption FXML
    @FXML
    private Button reviewClaimButton;
    public  Button getReviewClaimButton() {
        return reviewClaimButton;
    }
    public void reviewClaim(){
        List<Claim> currentSurveyor = DBUtil.surveyorReviewClaim();

        if (currentSurveyor != null && !currentSurveyor.isEmpty()) {
            // Display the claim information
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Review Claim Information");
            alert.setHeaderText("Claim Retrieved Successfully");
            alert.setContentText(currentSurveyor.toString());
            alert.showAndWait();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/InsuranceSurveyorGUI/ReviewClaimOption.fxml"));
                Parent adminDashboardRoot = loader.load();

                Node sourceNode = reviewClaimButton;

                // Get the primary stage from the current scene
                Stage primaryStage = (Stage) sourceNode.getScene().getWindow();

                primaryStage.setScene(new Scene(adminDashboardRoot));
                primaryStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No Claim Found");
            alert.setContentText("No claims need to be reviewed.");
            alert.showAndWait();
        }
    }

    @FXML
    private Button proposeClaimToManager;
    public  Button getProposeClaimToManager() {
        return proposeClaimToManager;
    }

    public void proposeClaimToManager() throws SQLException {
        List<Claim> currentSurveyor = DBUtil.surveyorProposeToManager();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Claim Information Valid");
        alert.setHeaderText("Claim Proposed Sucessfully");
        //alert.setContentText(currentSurveyor.toString());
        alert.showAndWait();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/InsuranceSurveyorGUI/InsuranceSurveyor.fxml"));
            Parent adminDashboardRoot = loader.load();

            Node sourceNode = proposeClaimToManager;

            // Get the primary stage from the current scene
            Stage primaryStage = (Stage) sourceNode.getScene().getWindow();

            primaryStage.setScene(new Scene(adminDashboardRoot));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private Button requireMoreClaimInformation;
    public void requireMoreClaimInformation() throws SQLException {
        List<Claim> currentSurveyor = DBUtil.surveyorRequireClaimInformation();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Claim Information Invalid");
        alert.setHeaderText("Require More Information Successfully");
        //alert.setContentText(currentSurveyor.toString());
        alert.showAndWait();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/InsuranceSurveyorGUI/InsuranceSurveyor.fxml"));
            Parent adminDashboardRoot = loader.load();

            Node sourceNode = requireMoreClaimInformation;

            // Get the primary stage from the current scene
            Stage primaryStage = (Stage) sourceNode.getScene().getWindow();

            primaryStage.setScene(new Scene(adminDashboardRoot));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //PART OF FILTER CLAIM FUNCTION
    @FXML
    private Button filterClaimOption;
    @FXML
    private MenuButton statusSelection;
    @FXML
    private MenuButton policyHolderSelection;
    @FXML
    Button findButton;

    private ClaimStatus getStatus(String status) {
        if (status.equals("NEW")) {
            return ClaimStatus.NEW;
        } else if (status.equals("PROCESSING")) {
            return ClaimStatus.PROCESSING;
        } else if (status.equals("APPROVED")) {
            return ClaimStatus.APPROVED;
        } else if (status.equals("REJECTED")) {
            return ClaimStatus.REJECTED;
        } else {
            throw new IllegalArgumentException("Unknown role: " + status);
        }
    }


    public void initialize() throws SQLException {
        List<String> options = DBUtil.selectPolicyHolderName();
        if (options != null) {
            for (String name : options) {
                MenuItem menuItem = new MenuItem(name);
                menuItem.setOnAction(event -> policyHolderSelection.setText(name));
                policyHolderSelection.getItems().add(menuItem);
            }
        }
    }

    //MenuButton for selecting claim amount range

    public void createFilterClaimFXML() throws SQLException {
        // Create root pane
        AnchorPane root = new AnchorPane();
        root.setPrefSize(600, 400);

        // Create and configure "Filter Claim" label
        Label filterClaimLabel = new Label("Filter Claim");
        filterClaimLabel.setLayoutX(246);
        filterClaimLabel.setTextFill(Color.web("#ec1919"));
        filterClaimLabel.setFont(new Font("Calibri Bold", 24));

        // Create and configure "Status" label
        Label statusLabel = new Label("1. Status");
        statusLabel.setLayoutX(38);
        statusLabel.setLayoutY(73);
        statusLabel.setTextFill(Color.web("#b71212"));
        statusLabel.setFont(new Font(20));

        // Create and configure "Status" menu button
        statusSelection = new MenuButton("Select Status");
        statusSelection.setLayoutX(278);
        statusSelection.setLayoutY(72);
        MenuItem newStatus = new MenuItem("NEW");
        MenuItem processingStatus = new MenuItem("PROCESSING");
        MenuItem approvedStatus = new MenuItem("APPROVED");
        MenuItem rejectedStatus = new MenuItem("REJECTED");
        statusSelection.getItems().addAll(newStatus, processingStatus, approvedStatus, rejectedStatus);

        // Set action listeners for status menu items
        newStatus.setOnAction(event -> statusSelection.setText(newStatus.getText()));
        processingStatus.setOnAction(event -> statusSelection.setText(processingStatus.getText()));
        approvedStatus.setOnAction(event -> statusSelection.setText(approvedStatus.getText()));
        rejectedStatus.setOnAction(event -> statusSelection.setText(rejectedStatus.getText()));

        // Create and configure "Policy Holder" label
        Label policyHolderLabel = new Label("2. Policy Holder");
        policyHolderLabel.setLayoutX(38);
        policyHolderLabel.setLayoutY(159);
        policyHolderLabel.setTextFill(Color.web("#b71212"));
        policyHolderLabel.setFont(new Font(20));

        // Create and configure "Policy Holder" menu button
        policyHolderSelection = new MenuButton("Select Policy Holder");
        policyHolderSelection.setLayoutX(278);
        policyHolderSelection.setLayoutY(158);

        // Create and configure "Find" button
        findButton = new Button("Find");
        findButton.setLayoutX(278);
        findButton.setLayoutY(312);
        findButton.setOnAction(event -> {
            try {
                findFilterClaim();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        // Add all components to the root pane
//        root.getChildren().addAll(
//                filterClaimLabel,
//                statusLabel,
//                statusSelection,
//                policyHolderLabel,
//                policyHolderSelection,
//                amountRangeLabel,
//                amountRangeSelection,
//                findButton
//        );

        root.getChildren().addAll(
                filterClaimLabel,
                statusLabel,
                statusSelection,
                policyHolderLabel,
                policyHolderSelection,
                findButton
        );
        initialize();
        filterClaimOption.getScene().setRoot(root);
    }

    //Method to retrieve filter claim
    private void findFilterClaim() throws SQLException {
        String statusSelect = statusSelection.getText(); // Get selected card holder
        String policyHolderSelect = policyHolderSelection.getText();

        List<String> filterClaim = DBUtil.surveyorGetFilterClaim(statusSelect, policyHolderSelect/*, amountRangeSelect*/);
        if (filterClaim != null) {
            // Display the claim information
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Claim Information");
            alert.setHeaderText("Claim Retrieved Successfully");
            alert.setContentText(filterClaim.toString());
            alert.showAndWait();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/InsuranceSurveyorGUI/ViewFilterClaim.fxml"));
                Parent adminDashboardRoot = loader.load();

                Node sourceNode = findButton; // Use any node from the current scene

                // Get the primary stage from the source node's scene
                Stage primaryStage = (Stage) sourceNode.getScene().getWindow();

                primaryStage.setScene(new Scene(adminDashboardRoot));
                primaryStage.show();
            } catch (IOException e) {
                e.printStackTrace();
                // Handle any errors loading the admin dashboard FXML
            }
        }else{
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Claim Information");
            alert1.setHeaderText("Claim Unuccessfully");
//            alert1.setContentText(filterClaim.toString());
            alert1.showAndWait();
        }
    }


    //PART OF FILTER CUSTOMER FUNCTION
    @FXML
    private Button filterCustomerOption;
    @FXML
    private MenuButton roleSelectionButton;
    @FXML
    private MenuButton emailSelectionButton;
    @FXML
    private MenuButton fullNameSelectionButton;
    @FXML
    Button findCustomerButton;
    private String selectedRole; // Add a member variable to store the selected role

    public void createFilterCustomerFXML() throws SQLException {
        // Create root pane
        AnchorPane root = new AnchorPane();
        root.setPrefSize(600, 400);

        // Create and configure "Filter Claim" label
        Label filterCustomerLabel = new Label("Filter Customer");
        filterCustomerLabel.setLayoutX(246);
        filterCustomerLabel.setTextFill(Color.web("#ec1919"));
        filterCustomerLabel.setFont(new Font("Calibri Bold", 24));

        // Create and configure "Status" label
        Label roleLabel = new Label("1. Role");
        roleLabel.setLayoutX(38);
        roleLabel.setLayoutY(73);
        roleLabel.setTextFill(Color.web("#b71212"));
        roleLabel.setFont(new Font(20));

        // Create and configure "Role" menu button
        roleSelectionButton = new MenuButton("Select Role");
        roleSelectionButton.setLayoutX(278);
        roleSelectionButton.setLayoutY(72);
        MenuItem dependentSelect = new MenuItem("DEPENDENT");
        MenuItem policyOwnerSelect = new MenuItem("POLICY_OWNER");
        MenuItem policyHolderSelect = new MenuItem("POLICY_HOLDER");
        roleSelectionButton.getItems().addAll(dependentSelect, policyOwnerSelect, policyHolderSelect);

        // Set action listeners for status menu items
        dependentSelect.setOnAction(event -> roleSelectionButton.setText(dependentSelect.getText()));
        policyOwnerSelect.setOnAction(event -> roleSelectionButton.setText(policyOwnerSelect.getText()));
        policyHolderSelect.setOnAction(event -> roleSelectionButton.setText(policyHolderSelect.getText()));

        // Create and configure "Policy Holder" label
        Label emailLabel = new Label("2. Email");
        emailLabel.setLayoutX(38);
        emailLabel.setLayoutY(159);
        emailLabel.setTextFill(Color.web("#b71212"));
        emailLabel.setFont(new Font(20));

        // Create and configure "Policy Holder" menu button
        emailSelectionButton = new MenuButton("Select Email");
        emailSelectionButton.setLayoutX(278);
        emailSelectionButton.setLayoutY(158);

        // Create and configure "Claim Amount Range" label
        Label fullNameRangeLabel = new Label("3. Full Name");
        fullNameRangeLabel.setLayoutX(38);
        fullNameRangeLabel.setLayoutY(242);
        fullNameRangeLabel.setTextFill(Color.web("#b71212"));
        fullNameRangeLabel.setFont(new Font(20));

         //Create and configure "Claim Amount Range" menu button
        fullNameSelectionButton = new MenuButton("Select Full Name");
        fullNameSelectionButton.setLayoutX(277);
        fullNameSelectionButton.setLayoutY(241);

        // Create and configure "Find" button
        findCustomerButton = new Button("Find");
        findCustomerButton.setLayoutX(278);
        findCustomerButton.setLayoutY(312);
        findCustomerButton.setOnAction(event -> {
            try {
                findFilterCustomer();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        root.getChildren().addAll(
                filterCustomerLabel,
                roleLabel,
                roleSelectionButton,
                emailLabel,
                emailSelectionButton,
                fullNameRangeLabel,
                fullNameSelectionButton,

                findCustomerButton
        );
        initializeCustomer();
        filterClaimOption.getScene().setRoot(root);
    }

    public void initializeCustomer() throws SQLException {
        //String selectedRole = roleSelectionButton.getText();
        List<String> options = DBUtil.selectCustomerEmail();
        emailSelectionButton.getItems().clear();
        if (options != null) {
            for (String email : options) {
                MenuItem menuItem = new MenuItem(email);
                menuItem.setOnAction(event -> emailSelectionButton.setText(email));
                emailSelectionButton.getItems().add(menuItem);
            }
        }

        List<String> fullName = DBUtil.selectCustomerFullName();
        fullNameSelectionButton.getItems().clear();
        if (fullName != null) {
            for (String name : fullName) {
                MenuItem menuItem = new MenuItem(name);
                menuItem.setOnAction(event -> fullNameSelectionButton.setText(name));
                fullNameSelectionButton.getItems().add(menuItem);
            }
        }
    }

    //Method to retrieve filter customer
    private void findFilterCustomer() throws SQLException {
        String roleSelect = roleSelectionButton.getText(); // Get selected card holder
        String emailSelect = emailSelectionButton.getText();
        String fullNameSelect = fullNameSelectionButton.getText();


        List<String> filterCustomer = DBUtil.surveyorGetFilterCustomer(roleSelect, emailSelect, fullNameSelect);
        if (filterCustomer != null && !filterCustomer.isEmpty()) {
            // Display the claim information
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Customer Information");
            alert.setHeaderText("Customer Retrieved Successfully");
            alert.setContentText(filterCustomer.toString());
            alert.showAndWait();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/InsuranceSurveyorGUI/ViewFilterCustomer.fxml"));
                Parent adminDashboardRoot = loader.load();

                Node sourceNode = findCustomerButton; // Use any node from the current scene

                // Get the primary stage from the source node's scene
                Stage primaryStage = (Stage) sourceNode.getScene().getWindow();

                primaryStage.setScene(new Scene(adminDashboardRoot));
                primaryStage.show();
            } catch (IOException e) {
                e.printStackTrace();
                // Handle any errors loading the admin dashboard FXML
            }
        }else {
            // Display a warning alert if no customers were found
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No Customers Found");
            alert.setContentText("No customers were found based on the provided criteria.");
            alert.showAndWait();
        }
    }

//    private void findFilterCustomer() throws SQLException {
//        String roleSelect = roleSelectionButton.getText(); // Get selected card holder
//        String emailSelect = emailSelectionButton.getText();
//        String fullNameSelect = fullNameSelectionButton.getText();
//
//        List<String> filterCustomer = DBUtil.surveyorGetFilterCustomer(roleSelect, emailSelect, fullNameSelect);
//
//        if (filterCustomer != null && !filterCustomer.isEmpty()) {
//            // Display the claim information
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Customer Information");
//            alert.setHeaderText("Customer Retrieved Successfully");
//            alert.setContentText(filterCustomer.toString());
//            alert.showAndWait();
//            try {
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/InsuranceSurveyorGUI/ViewFilterCustomer.fxml"));
//                Parent adminDashboardRoot = loader.load();
//
//                Node sourceNode = findCustomerButton; // Use any node from the current scene
//
//                // Get the primary stage from the source node's scene
//                Stage primaryStage = (Stage) sourceNode.getScene().getWindow();
//
//                primaryStage.setScene(new Scene(adminDashboardRoot));
//                primaryStage.show();
//            } catch (IOException e) {
//                e.printStackTrace();
//                // Handle any errors loading the admin dashboard FXML
//            }
//        } else {
//            // Display a warning alert if no customers were found
//            Alert alert = new Alert(Alert.AlertType.WARNING);
//            alert.setTitle("Warning");
//            alert.setHeaderText("No Customers Found");
//            alert.setContentText("No customers were found based on the provided criteria.");
//            alert.showAndWait();
//        }
//    }



}
