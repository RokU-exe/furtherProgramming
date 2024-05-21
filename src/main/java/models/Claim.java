package models;

import java.util.Date;

public class Claim {
    private String id;
    private Date claimDate;
    private String insuredPerson;
    private String cardNumber;
    private Date examDate;
    private Double claimAmount;
    private ClaimStatus status;
    private String receiverBank;
    private String receiverName;
    private String receiverNumber;
    private String policyHolderName;

    // Constructor with all parameters
    public Claim(String id, Date claimDate, String insuredPerson, String cardNumber, Date examDate,
                 Double claimAmount, ClaimStatus status, String receiverBank, String receiverName,
                 String receiverNumber, String policyHolderName) {
        this.id = id;
        this.claimDate = claimDate;
        this.insuredPerson = insuredPerson;
        this.cardNumber = cardNumber;
        this.examDate = examDate;
        this.claimAmount = claimAmount;
        this.status = status;
        this.receiverBank = receiverBank;
        this.receiverName = receiverName;
        this.receiverNumber = receiverNumber;
        this.policyHolderName = policyHolderName;
    }

    // Simplified Constructor
    public Claim(String id, String insuredPerson, ClaimStatus status) {
        this.id = id;
        this.insuredPerson = insuredPerson;
        this.status = status;
    }

    public Claim(String id, java.sql.Date claimDate, String insuredPerson, String cardNumber, java.sql.Date examDate, double claimAmount, ClaimStatus status, String receiverBank, String receiverName, String receiverNumber) {
    }


    public Claim(String id, LocalDate claimDate, String insuredPerson, String cardNumber, LocalDate examDate, float claimAmount, String status, String receiverBank, String receiverName, String receiverNumber, String policyHolderName) {
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(Date claimDate) {
        this.claimDate = claimDate;
    }

    public String getInsuredPerson() {
        return insuredPerson;
    }

    public void setInsuredPerson(String insuredPerson) {
        this.insuredPerson = insuredPerson;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    public Double getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(Double claimAmount) {
        this.claimAmount = claimAmount;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
    }

    public String getReceiverBank() {
        return receiverBank;
    }

    public void setReceiverBank(String receiverBank) {
        this.receiverBank = receiverBank;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverNumber() {
        return receiverNumber;
    }

    public void setReceiverNumber(String receiverNumber) {
        this.receiverNumber = receiverNumber;
    }

    public String getPolicyHolderName() {
        return policyHolderName;
    }

    public void setPolicyHolderName(String policyHolderName) {
        this.policyHolderName = policyHolderName;
    }

    @Override
    public String toString() {
        return "Claim{" +
                "id='" + id + '\'' +
                ", claimDate=" + claimDate +
                ", insuredPerson='" + insuredPerson + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", examDate=" + examDate +
                ", claimAmount=" + claimAmount +
                ", status=" + status +
                ", receiverBank='" + receiverBank + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", receiverNumber='" + receiverNumber + '\'' +
                ", policyHolderName='" + policyHolderName + '\'' +
                '}';
    }
}
