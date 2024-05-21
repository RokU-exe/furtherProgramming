package models;

import java.util.List;

public class PolicyHolder extends Customer {
    private List<Dependent> dependents;
    private List<Claim> claims;


    // Constructors
    public PolicyHolder(String id, String fullName, String email, String password, UserRole role, InsuranceCard insuranceCard, List<Claim> claims, List<Dependent> dependents) {
        super(id, fullName, email, password, role, insuranceCard, claims);
        this.dependents = dependents;
        this.claims = claims;
    }

    // Getters and Setters for dependent and claims
    public List<Dependent> getDependents() {
        return dependents;
    }

    public void setDependents(List<Dependent> dependents) {
        this.dependents = dependents;
    }

    public PolicyHolder() {
    }

    public List<Claim> getClaims() {
        return claims;
    }

    public void setClaims(List<Claim> claims) {
        this.claims = claims;
    }

    public double calculateTotalCost(double rate) {
        double cost = rate;
        for (Dependent dependent : dependents) {
            cost += rate * 0.6;
        }
        return cost;
    }

    @Override
    public String toString() {
        return "models.PolicyHolder{" +
                "dependents=" + dependents +
                ", " + super.toString() +
                '}';
    }
}
