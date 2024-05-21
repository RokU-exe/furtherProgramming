package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Claim;
import utils.DBUtil;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ExamClaimsController implements Initializable {

    @FXML
    private TableView<Claim> claimsTable;

    @FXML
    private TableColumn<Claim, String> claimIdColumn;

    @FXML
    private TableColumn<Claim, String> policyHolderColumn;

    @FXML
    private TableColumn<Claim, String> statusColumn;

    @FXML
    private Button backButton;

    @FXML
    private Button logoutButton;

    private ObservableList<Claim> claimsData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        claimIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        policyHolderColumn.setCellValueFactory(new PropertyValueFactory<>("policyHolderName"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        claimsTable.setItems(claimsData);

        loadClaimsToExamine();
    }

    private void loadClaimsToExamine() {
        List<Claim> claims = DBUtil.getClaimsToExamine();
        if (claims != null) {
            claimsData.setAll(claims);
            System.out.println("Claims loaded: " + claims.size());
            for (Claim claim : claims) {
                System.out.println(claim); // Print each claim to verify its content
            }
        } else {
            System.out.println("No claims to examine.");
        }
    }

    @FXML
    private void handleBack() {
        navigateToPage("/gui/ManagerGUI/Manager.fxml");
    }

    @FXML
    private void handleLogout() {
        navigateToPage("/gui/Login.fxml");
    }

    private void navigateToPage(String fxmlFile) {
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
