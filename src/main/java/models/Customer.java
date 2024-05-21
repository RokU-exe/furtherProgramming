package models;

import java.util.List;

public class Customer extends User {
    private InsuranceCard insuranceCard;
    private List<Claim> claims;

    public Customer(String id, String fullName, String email, String password, UserRole role, InsuranceCard insuranceCard, List<Claim> claims) {
        super(id, fullName, email, password, role);
        this.insuranceCard = insuranceCard;
        this.claims = claims;
    }

    public Customer(String id, String fullName, String email, String role) {
        super(id, fullName, email, null, UserRole.valueOf(role));
    }

    public Customer(String id, String fullName, String email) {
        super(id, fullName, email, null, UserRole.CUSTOMER);
    }

    public Customer() {
    }

    public Customer(String id, String fullName) {
        super(id, fullName, null, null, UserRole.CUSTOMER);
    }

    public InsuranceCard getInsuranceCard() {
        return insuranceCard;
    }

    public void setInsuranceCard(InsuranceCard insuranceCard) {
        this.insuranceCard = insuranceCard;
    }

    public List<Claim> getClaims() {
        return claims;
    }

    public void setClaims(List<Claim> claims) {
        this.claims = claims;
    }

    @Override
    public String toString() {
        return "Customer{" + super.toString() +
                "insuranceCard=" + insuranceCard +
                ", claims=" + claims +
                '}';
    }
}
