package gui;

import models.Claim;
import models.ClaimStatus;
import models.User;
import utils.DBUtil;

import java.util.List;

public class ProviderController {
    private User provider;

    public ProviderController(User provider) {
        this.provider = provider;
    }

    public List<Claim> getClaims() {
        // Retrieve all claims for this provider from the database
        String filterCriteria = String.format("insured_person = '%s'", provider.getFullName());
        return DBUtil.getFilteredClaims();
    }

    public void proposeClaimToManager(Claim claim) {
        // Propose a claim to the manager for approval
        claim.setStatus(ClaimStatus.PROCESSING);
        DBUtil.updateClaim(claim);
    }

    public void requestMoreInfo(Claim claim) {
        // Request more information for a claim
        claim.setStatus(ClaimStatus.NEW);
        DBUtil.updateClaim(claim);
    }

    public List<User> getCustomers(String filterCriteria) {
        // Retrieve all customers based on a filter
        return DBUtil.getFilteredCustomers(filterCriteria);
    }
}
