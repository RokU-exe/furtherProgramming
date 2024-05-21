package models;

import java.util.List;

public class Provider extends User{
    public Provider(String id, String fullName, String email, String password, UserRole role, InsuranceCard insuranceCard, List<Claim> claims) {
        super(id, fullName, email, password, role);
    }

    public Provider(){}

    @Override
    public String toString() {
        return "Provider{" + super.toString() + "}";
    }
}
