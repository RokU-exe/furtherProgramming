package models;

import java.util.List;

public class InsuranceSurveyor extends Provider{
    public InsuranceSurveyor(String id, String fullName, String email, String password, UserRole role, InsuranceCard insuranceCard, List<Claim> claims, String policyHolderId) {
        super(id, fullName, email, password, role, insuranceCard, claims);
    }

    public InsuranceSurveyor(){
        super();
    }

    public String toString() {
        return "Insurance Surveyor{" + super.toString() + "}";
    }

}
