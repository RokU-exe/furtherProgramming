package gui;

public class SharedData {
    private static SharedData instance;
    private String selectedPolicyHolder;
    private String selectedPolicyOwner;
    private String selectedDependent;
    private String selectedInsuranceSurveyor; // New field
    private String selectedInsuranceManager;  // New field
    private boolean isUpdatingPolicyHolder;
    private boolean isUpdatingDependent;
    private boolean isUpdatingInsuranceSurveyor; // New field
    private boolean isUpdatingInsuranceManager;  // New field

    private SharedData() {}

    public static SharedData getInstance() {
        if (instance == null) {
            instance = new SharedData();
        }
        return instance;
    }

    public String getSelectedPolicyHolder() {
        return selectedPolicyHolder;
    }

    public void setSelectedPolicyHolder(String selectedPolicyHolder) {
        this.selectedPolicyHolder = selectedPolicyHolder;
        this.isUpdatingPolicyHolder = true;
        this.isUpdatingDependent = false;
        this.isUpdatingInsuranceSurveyor = false;
        this.isUpdatingInsuranceManager = false;
    }

    public String getSelectedPolicyOwner() {
        return selectedPolicyOwner;
    }

    public void setSelectedPolicyOwner(String selectedPolicyOwner) {
        this.selectedPolicyOwner = selectedPolicyOwner;
        this.isUpdatingPolicyHolder = false;
        this.isUpdatingDependent = false;
        this.isUpdatingInsuranceSurveyor = false;
        this.isUpdatingInsuranceManager = false;
    }

    public String getSelectedDependent() {
        return selectedDependent;
    }

    public void setSelectedDependent(String selectedDependent) {
        this.selectedDependent = selectedDependent;
        this.isUpdatingPolicyHolder = false;
        this.isUpdatingDependent = true;
        this.isUpdatingInsuranceSurveyor = false;
        this.isUpdatingInsuranceManager = false;
    }

    public String getSelectedInsuranceSurveyor() {
        return selectedInsuranceSurveyor;
    }

    public void setSelectedInsuranceSurveyor(String selectedInsuranceSurveyor) {
        this.selectedInsuranceSurveyor = selectedInsuranceSurveyor;
        this.isUpdatingPolicyHolder = false;
        this.isUpdatingDependent = false;
        this.isUpdatingInsuranceSurveyor = true;
        this.isUpdatingInsuranceManager = false;
    }

    public String getSelectedInsuranceManager() {
        return selectedInsuranceManager;
    }

    public void setSelectedInsuranceManager(String selectedInsuranceManager) {
        this.selectedInsuranceManager = selectedInsuranceManager;
        this.isUpdatingPolicyHolder = false;
        this.isUpdatingDependent = false;
        this.isUpdatingInsuranceSurveyor = false;
        this.isUpdatingInsuranceManager = true;
    }

    public boolean isUpdatingPolicyHolder() {
        return isUpdatingPolicyHolder;
    }

    public boolean isUpdatingDependent() {
        return isUpdatingDependent;
    }

    public boolean isUpdatingInsuranceSurveyor() {
        return isUpdatingInsuranceSurveyor;
    }

    public boolean isUpdatingInsuranceManager() {
        return isUpdatingInsuranceManager;
    }
}


