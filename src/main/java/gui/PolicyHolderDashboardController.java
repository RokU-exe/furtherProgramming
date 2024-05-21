package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Claim;
import models.PolicyHolder;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.cell.PropertyValueFactory;
import utils.DBUtil;

public class PolicyHolderDashboardController {
    @FXML
    private ListView<Claim> claimListView;
    @FXML
    private TableView<Claim> claimTableView;
    @FXML
    private Button viewClaimDetailsButton;
    @FXML
    private Button fileNewClaimButton;
    @FXML
    private Button updateClaimButton;
    @FXML
    private Button accountSettingsButton;
    @FXML
    private Label claimIdLabel, claimDateLabel, claimDescriptionLabel;
    @FXML
    private Button navigateButton;
    @FXML
    private PolicyHolder policyHolder;

    public PolicyHolderDashboardController() {
    }

    public <URL> void initialize(URL url, ResourceBundle resources) {
        // This method is called after FXML loading is complete
        if (policyHolder != null) {
            loadClaimData(); // Load and display claims in the ListView
        }
    }

    public void setPolicyHolder(PolicyHolder policyHolder) {
        this.policyHolder = policyHolder;
        if (claimListView != null) { // Check if initialized
            loadClaimData();
        }
    }

    private void loadClaimData() {
        if (policyHolder != null) {
            List<Claim> claims = policyHolder.getClaims();// Fetch the claims
            claimListView.getItems().addAll(claims); // Populate the ListView
        }
    }
    
    @FXML
    private void handleViewClaimDetails() throws SQLException {
        Claim selectedClaim = claimListView.getSelectionModel().getSelectedItem();
        if (selectedClaim != null) {
            claimTableView.getItems().clear(); // Clear previous details

            // Fetch the complete claim details from the database
            List<Claim> claimDetails = DBUtil.getClaimsForPolicyHolder(policyHolder.getFullName());

            // Define and configure the table columns
            if (claimTableView.getColumns().isEmpty()) {
                TableColumn<Claim, String> idCol = new TableColumn<>("ID");
                idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
                TableColumn<Claim, LocalDate> dateCol = new TableColumn<>("Claim Date"); // Use LocalDate for claimDate
                dateCol.setCellValueFactory(new PropertyValueFactory<>("claimDate"));
                TableColumn<Claim, String> personCol = new TableColumn<>("Insured Person");
                personCol.setCellValueFactory(new PropertyValueFactory<>("insuredPerson"));

                claimTableView.getColumns().addAll(idCol, dateCol, personCol);
            }

            claimTableView.getItems().add(selectedClaim);
        }
    }

    @FXML
    private void handleFileNewClaim() {
        // Open a new window/dialog for filing a new claim
    }

    @FXML
    private void handleUpdateClaim() {
        Claim selectedClaim = claimListView.getSelectionModel().getSelectedItem();
        if (selectedClaim != null) {
            // Open a new window/dialog for updating the selected claim
        }
    }

    @FXML
    private void handleAccountSettings() {
        // Open a new window/dialog for account settings
    }

    // Navigation between pages
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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
    @FXML
    private void handleLogout() {
        navigateToPage("/gui/Login.fxml");
    }
}
