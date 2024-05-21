package models;

public enum UserRole {
    POLICY_HOLDER("POLICY_HOLDER"),
    DEPENDENT("DEPENDENT"),
    POLICY_OWNER("POLICY_OWNER"),
    INSURANCE_SURVEYOR("INSURANCE_SURVEYOR"),
    INSURANCE_MANAGER("INSURANCE_MANAGER"),
    SYSTEM_ADMIN("SYSTEM_ADMIN"),
    CUSTOMER("CUSTOMER");

    private final String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return roleName;
    }
}
