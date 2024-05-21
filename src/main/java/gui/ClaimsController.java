    package gui;

    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.fxml.FXML;
    import javafx.scene.control.TableColumn;
    import javafx.scene.control.TableView;
    import javafx.scene.control.cell.PropertyValueFactory;
    import models.Claim;
    import models.ClaimStatus;

    import java.util.Date;
    import java.util.List;

    public class ClaimsController {

        @FXML
        private TableView<Claim> claimsTableView;
        @FXML
        private TableColumn<Claim, String> idColumn;
        @FXML
        private TableColumn<Claim, Date> claimDateColumn;
        @FXML
        private TableColumn<Claim, String> insuredPersonColumn;
        @FXML
        private TableColumn<Claim, String> cardNumberColumn;
        @FXML
        private TableColumn<Claim, Date> examDateColumn;
        @FXML
        private TableColumn<Claim, List<String>> documentsColumn;
        @FXML
        private TableColumn<Claim, Double> claimAmountColumn;
        @FXML
        private TableColumn<Claim, ClaimStatus> statusColumn;
        @FXML
        private TableColumn<Claim, String> receiverBankColumn;
        @FXML
        private TableColumn<Claim, String> receiverNameColumn;
        @FXML
        private TableColumn<Claim, String> receiverNumberColumn;
        @FXML
        private TableColumn<Claim, String> policyHolderNameColumn;

        private ObservableList<Claim> claimList;

        @FXML
        public void initialize() {
            // Set up the columns in the table
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            claimDateColumn.setCellValueFactory(new PropertyValueFactory<>("claimDate"));
            insuredPersonColumn.setCellValueFactory(new PropertyValueFactory<>("insuredPerson"));
            cardNumberColumn.setCellValueFactory(new PropertyValueFactory<>("cardNumber"));
            examDateColumn.setCellValueFactory(new PropertyValueFactory<>("examDate"));
            documentsColumn.setCellValueFactory(new PropertyValueFactory<>("documents"));
            claimAmountColumn.setCellValueFactory(new PropertyValueFactory<>("claimAmount"));
            statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
            receiverBankColumn.setCellValueFactory(new PropertyValueFactory<>("receiverBank"));
            receiverNameColumn.setCellValueFactory(new PropertyValueFactory<>("receiverName"));
            receiverNumberColumn.setCellValueFactory(new PropertyValueFactory<>("receiverNumber"));
            policyHolderNameColumn.setCellValueFactory(new PropertyValueFactory<>("policyHolderName"));

            // Load dummy data
            loadClaimData();

            // Add the data to the table
            claimsTableView.setItems(claimList);
        }

        private void loadClaimData() {
            claimList = FXCollections.observableArrayList(

            );
        }
    }
