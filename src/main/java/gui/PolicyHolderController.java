package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import models.Claim;
import models.PolicyHolder;

import java.io.IOException;
import java.util.List;

public class PolicyHolderController {
    private PolicyHolder policyHolder; // Store the PolicyHolder object

    public PolicyHolderController(PolicyHolder policyHolder) {
        this.policyHolder = policyHolder;
    }

    @FXML
    public void openPolicyHolderDashboard(Button loginButton) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/PolicyHolderGUI/PolicyHolderDashboard.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Access the controller and set the policyHolder
            PolicyHolderDashboardController dashboardController = loader.getController();
            dashboardController.setPolicyHolder(policyHolder); // Pass to the dashboard controller

            // Get any node from the current scene
            Node sourceNode = loginButton;

            // Get the primary stage from the source node's scene
            Stage primaryStage = (Stage) sourceNode.getScene().getWindow();
            primaryStage.setTitle("Policy Holder Dashboard");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any errors loading the dashboardFXML
        }
    }

    public List<Claim> getClaims() {
        return policyHolder.getClaims();
    }

    public void addClaim(Claim claim) {
        policyHolder.getClaims().add(claim);
    }

    public void updateClaim(Claim updatedClaim) {
        List<Claim> claims = policyHolder.getClaims();
        for (int i = 0; i < claims.size(); i++) {
            if (claims.get(i).getId().equals(updatedClaim.getId())) {
                claims.set(i, updatedClaim);
                break;
            }
        }
    }

    public void deleteClaim(String claimId) {
        List<Claim> claims = policyHolder.getClaims();
        claims.removeIf(claim -> claim.getId().equals(claimId));
    }
}
