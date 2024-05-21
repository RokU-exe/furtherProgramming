package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Claim;
import models.ClaimStatus;
import models.Customer;
import models.Surveyor;
import utils.DBUtil;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ManagerController implements Initializable {

    @FXML
    private TableView<Claim> claimsTable;

    @FXML
    private TableColumn<Claim, String> claimIdColumn;

    @FXML
    private TableColumn<Claim, String> insuredPersonColumn;

    @FXML
    private TableColumn<Claim, ClaimStatus> statusColumn;

    private ObservableList<Claim> claimsData = FXCollections.observableArrayList();

    @FXML
    private Label contentLabel;

    @FXML
    private Button logoutButton, viewClaimsButton, viewCustomersButton, viewSurveyorsButton, approveClaimButton, rejectClaimButton, navigateButton;

    @FXML
    private VBox contentArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        claimIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        insuredPersonColumn.setCellValueFactory(new PropertyValueFactory<>("insuredPerson"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadClaimsData();
    }

    private void loadClaimsData() {
        List<Claim> claims = DBUtil.getAllClaims();
        if (claims != null && !claims.isEmpty()) {
            claimsData.setAll(claims);
            claimsTable.setItems(claimsData);
        } else {
            System.out.println("No data to display");
        }
    }

    @FXML
    private void handleLogout() {
        navigateToPage("/gui/Login.fxml");
    }

    @FXML
    private void handleViewClaims() {
        loadClaimsData();
        StringBuilder claimsInfo = new StringBuilder("Claims:\n\n");
        for (Claim claim : claimsData) {
            claimsInfo.append(String.format("ID: %s, Insured Person: %s, Status: %s\n",
                    claim.getId(), claim.getInsuredPerson(), claim.getStatus()));
        }
        contentLabel.setText(claimsInfo.toString());
    }

    @FXML
    private void handleViewCustomers() {
        List<Customer> customers = DBUtil.getAllCustomers();
        StringBuilder customersInfo = new StringBuilder("Customers:\n\n");
        for (Customer customer : customers) {
            customersInfo.append(String.format("ID: %s, Name: %s, Role: %s\n",
                    customer.getId(), customer.getFullName(), customer.getRole()));
        }
        contentLabel.setText(customersInfo.toString());
    }

    @FXML
    private void handleViewSurveyors() {
        List<Surveyor> surveyors = DBUtil.getAllSurveyors();
        StringBuilder surveyorsInfo = new StringBuilder("Surveyors:\n\n");
        for (Surveyor surveyor : surveyors) {
            surveyorsInfo.append(String.format("ID: %s, Name: %s\n",
                    surveyor.getId(), surveyor.getName()));
        }
        contentLabel.setText(surveyorsInfo.toString());
    }

    @FXML
    private void handleApproveClaim() {
        Claim selectedClaim = getSelectedClaim();
        if (selectedClaim != null) {
            DBUtil.approveClaim(selectedClaim.getId());
            refreshClaims();
        } else {
            showAlert(Alert.AlertType.ERROR, "No Claim Selected", "Please select a claim to approve.");
        }
    }

    @FXML
    private void handleRejectClaim() {
        Claim selectedClaim = getSelectedClaim();
        if (selectedClaim != null) {
            DBUtil.rejectClaim(selectedClaim.getId());
            refreshClaims();
        } else {
            showAlert(Alert.AlertType.ERROR, "No Claim Selected", "Please select a claim to reject.");
        }
    }

    @FXML
    private void handleExamineClaims() throws IOException {
        navigateToPage("/gui/ManagerGUI/ExamClaims.fxml");
    }

    @FXML
    private void handleRetrieveUser() throws IOException {
        navigateToPage("/gui/ManagerGUI/RetrieveUser.fxml");
    }

    private void navigateToPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) navigateButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Unable to load the requested page: " + e.getMessage());
        }
    }

    private Claim getSelectedClaim() {
        return claimsTable.getSelectionModel().getSelectedItem();
    }

    private void refreshClaims() {
        handleViewClaims();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
