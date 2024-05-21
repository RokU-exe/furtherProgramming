package gui;

//import gui.coolEffects.TextAnimation;
import controllers.SystemAdmin;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.*;
import utils.DBUtil;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    public  Button getLoginButton() {
        return loginButton;
    }

    User getUser(){
        User user  = new User();
        String email = emailField.getText();
        String password = passwordField.getText();
        user = DBUtil.getUserByEmailAndPassword(email, password);
        return user;
    }

    public void validShow(){
        User user = getUser();
        String roleName = getRoleDisplayName(user.getRole());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(roleName + " Login");
        alert.setHeaderText("Login Successful!");
        alert.setContentText("You have successfully logged in as a " + roleName + ".");
        alert.showAndWait();
    }

    private String getRoleDisplayName(UserRole role) {
        switch (role) {
            case POLICY_OWNER:
                return "Policy Owner";
            case POLICY_HOLDER:
                return "Policy Holder";
            case DEPENDENT:
                return "Dependent";
            case INSURANCE_SURVEYOR:
                return "Insurance Surveyor";
            case INSURANCE_MANAGER:
                return "Insurance Manager";
            case SYSTEM_ADMIN:
                return "System Admin";
            default:
                return "Unknown Role";
        }
    }
    @FXML
    public void handleLogin() throws IOException, SQLException {
        User user = getUser();
        Dependent d = new Dependent();
        if (user != null) {
            switch (user.getRole()) {
//                case UserRole.POLICY_HOLDER -> {
//                    // Fetch the PolicyHolder details
//                    PolicyHolder policyHolder = DBUtil.fetchPolicyHolderDetails(user.getId());
//                    if (policyHolder != null) {
//                        validShow();
//                        PolicyHolderController controller = new PolicyHolderController(policyHolder);
//                        controller.openPolicyHolderDashboard(getLoginButton()); // Open the dashboard
//                        LoginSession.getInstance().setCurrentUser(user);
//                    } else {
//                        // Handle the case where no policy holder is found (e.g., show an error)
//                    }
//                }
                case UserRole.DEPENDENT -> {
                    validShow();
                    Dependent dependent = new Dependent(user.getId(), user.getFullName(), user.getEmail(), user.getPassword(), UserRole.DEPENDENT, d.getInsuranceCard(), null, d.getPolicyHolderId());
                    DependentController controller = new DependentController(dependent);
                    controller.openDependentDashboard(getLoginButton());
                    LoginSession.getInstance().setCurrentUser(user);
                }
//                case UserRole.POLICY_OWNER -> {validShow();
//                    PolicyOwner po = new PolicyOwner(user.getId(), user.getFullName(), user.getEmail(), user.getPassword(), UserRole.DEPENDENT);
//                    PolicyOwnerController controller = new PolicyOwnerController(po);
//                    controller.openPolicyOwnerDashboard(getLoginButton());
//                }
                case UserRole.INSURANCE_SURVEYOR -> {
                    validShow();
                    InsuranceSurveyorController controller = new InsuranceSurveyorController();
                    controller.openSurveyorDashboard(getLoginButton());
                }
                case UserRole.INSURANCE_MANAGER ->{validShow(); openManagerDashboard(user);}
                case UserRole.SYSTEM_ADMIN -> {
                    validShow();
                    SystemAdmin systemAdmin = new SystemAdmin(user.getId(), user.getFullName(), user.getEmail(), user.getPassword(), UserRole.SYSTEM_ADMIN);
                    SystemAdminController controller = new SystemAdminController(systemAdmin);
                    controller.openAdminDashboard(getLoginButton());
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText("Invalid Credentials");
            alert.setContentText("Please check your email and password.");
            alert.showAndWait();
        }
    }

    private void openSurveyorDashboard(User user) {
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

    @FXML
    private void openManagerDashboard(User user) {
        // Open the Insurance Manager Dashboard
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ManagerGUI/Manager.fxml"));
            Parent managerDashboardRoot = loader.load();
            Scene managerDashboardScene = new Scene(managerDashboardRoot);

            Node sourceNode = loginButton;

            Stage primaryStage = (Stage) sourceNode.getScene().getWindow();

            primaryStage.setScene(managerDashboardScene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any errors loading the manager dashboard FXML
        }
    }
}
