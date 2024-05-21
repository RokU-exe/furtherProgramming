package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Customer;
import utils.DBUtil;

import java.io.IOException;

public class RetrieveUserController {

    @FXML
    private TextField userIdField;

    @FXML
    private TextArea userInfoArea;

    @FXML
    private Button backButton;

    @FXML
    private Button logoutButton;

    @FXML
    private void handleRetrieveUser() {
        String userId = userIdField.getText();
        if (userId.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a user ID.");
            return;
        }

        try {
            Customer customer = DBUtil.getCustomerById(userId);
            if (customer != null) {
                userInfoArea.setText("ID: " + customer.getId() + "\nName: " + customer.getFullName() + "\nEmail: " + customer.getEmail() + "\nRole: " + customer.getRole() + "\nPassword: "+ customer.getPassword());
                System.out.println("Customer retrieved: " + customer); // Debug statement
            } else {
                userInfoArea.setText("User not found.");
                System.out.println("Customer not found with ID: " + userId); // Debug statement
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while retrieving the user.");
            e.printStackTrace(); // Debug statement
        }
    }

    @FXML
    private void handleBack() throws IOException {
        navigateToPage("/gui/ManagerGUI/Manager.fxml");
    }

    @FXML
    private void handleLogout() throws IOException {
        navigateToPage("/gui/Login.fxml");
    }

    private void navigateToPage(String fxmlFile) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Unable to load the requested page: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
