package gui;

import controllers.CustomerManagerImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import models.InsuranceCard;
import models.User;
import models.UserRole;
import utils.DBUtil;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class SystemAdminController implements Initializable {


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    private CustomerManagerImpl.SystemAdmin systemAdmin;
    public SystemAdminController() {
        // Default constructor
    }


    public SystemAdminController(CustomerManagerImpl.SystemAdmin systemAdmin) {
        this.systemAdmin = systemAdmin;
    }

    public List<User> getUsers() {
        // Retrieve all users from the database
        return DBUtil.getAllUsers();
    }
    @FXML
    private Button add;
    @FXML
    private TextField email;
    @FXML
    private TextField fullname;
    @FXML
    private TextField password;
    @FXML
    private MenuButton role;
    @FXML
    private Button addIc;

    private UserRole getRole(String role) {
        if (role.equals("Policy Owner")) {
            return UserRole.POLICY_OWNER;
        } else if (role.equals("Policy Holder")) {
            return UserRole.POLICY_HOLDER;
        } else if (role.equals("Dependent")) {
            return UserRole.DEPENDENT;
        } else if (role.equals("Insurance Surveyor")) {
            return UserRole.INSURANCE_SURVEYOR;
        } else if (role.equals("Insurance Manager")) {
            return UserRole.INSURANCE_MANAGER;
        } else {
            // Handle the case when the role is not recognized
            // For example, you can throw an IllegalArgumentException
            throw new IllegalArgumentException("Unknown role: " + role);
        }
    }

    public void addUser() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/SystemAdminGUI/AddUser.fxml"));
            Parent adminDashboardRoot = loader.load();

            Node sourceNode = add; // Use any node from the current scene

            // Get the primary stage from the source node's scene
            Stage primaryStage = (Stage) sourceNode.getScene().getWindow();

            primaryStage.setScene(new Scene(adminDashboardRoot));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any errors loading the admin dashboard FXML
        }
    }
    @FXML
    private void selectRole(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String roleName = menuItem.getText();

        // Set the selected role as the text of the MenuButton
        role.setText(roleName);
    }
    @FXML
    private Button doneAdding;

   //add the user, go back to dashboard
   public void doneAddUser() {
       String roleName = role.getText();
       System.out.println(roleName);
       // Check if a role is selected
       if (roleName.equals("Select Role")) {
           System.out.println("Please select a valid role.");
           return;
       }

       UserRole userRole = getRole(roleName);
       String id = DBUtil.getLargestIdByUserRole(userRole);
       if (id == null) {
           // Handle case when no matching records are found
           System.out.println("No matching records found for role: " + roleName);
           return;
       }
       User user = new User(id, fullname.getText(), email.getText(), password.getText(), userRole);

       // Add a new user to the database
       DBUtil.addUser(user);
       Alert alert = new Alert(Alert.AlertType.INFORMATION);

       alert.setTitle("Add " + roleName);
       alert.setHeaderText(roleName + " Added Successfully!");
       alert.setContentText(user.toString());
       alert.showAndWait();

       dashBoard(doneAdding);
   }

    public double calculateTotalClaimedAmount() {
        // Sum up the successfully claimed amount with different parameters
        return DBUtil.calculateTotalClaimedAmount();
    }

    @FXML
    private MenuButton policyHolderMenuButton;

    @FXML
    private MenuButton policyOwnerMenuButton;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button doneAddingIC;

    @FXML
    private void initializeUIComponents() throws SQLException {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(600, 400);
        root.setStyle("-fx-background-color: #CDE8E5;");

        Label titleLabel = new Label("ADD NEW INSURANCE CARD");
        titleLabel.setLayoutX(128);
        titleLabel.setPrefHeight(118);
        titleLabel.setPrefWidth(497);
        titleLabel.setStyle("-fx-background-color: #CDE8E5;");
        titleLabel.setFont(new Font("Calibri", 24));
        titleLabel.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView logoImageView = new ImageView(new Image("https://1000logos.net/wp-content/uploads/2019/07/RMIT-Logo.png"));
        logoImageView.setFitHeight(88);
        logoImageView.setFitWidth(286);
        logoImageView.setPreserveRatio(true);
        logoImageView.setPickOnBounds(true);

        ImageView blendImageView = new ImageView(new Image("https://tse1.mm.bing.net/th?id=OIP.QqEXi7j5Z0ZMFu8pLgTxzAHaHa&amp;pid=Api&amp;P=0&amp;h=180"));
        blendImageView.setFitHeight(150);
        blendImageView.setFitWidth(200);
        blendImageView.setLayoutX(450);
        blendImageView.setLayoutY(242);
        blendImageView.setPreserveRatio(true);
        blendImageView.setPickOnBounds(true);
        blendImageView.setBlendMode(javafx.scene.effect.BlendMode.MULTIPLY);

        Label cardHolderLabel = new Label("Card Holder:");
        cardHolderLabel.setLayoutX(90);
        cardHolderLabel.setLayoutY(144);
        cardHolderLabel.setFont(new Font("Calibri", 18));

        Label policyOwnerLabel = new Label("Policy Owner:");
        policyOwnerLabel.setLayoutX(92);
        policyOwnerLabel.setLayoutY(215);
        policyOwnerLabel.setFont(new Font("Calibri", 18));

        Label expireDateLabel = new Label("Expire date:");
        expireDateLabel.setLayoutX(92);
        expireDateLabel.setLayoutY(283);
        expireDateLabel.setFont(new Font("Calibri", 18));

        policyHolderMenuButton = new MenuButton("Select Policy Holder");
        policyHolderMenuButton.setLayoutX(226);
        policyHolderMenuButton.setLayoutY(142);
        policyHolderMenuButton.setPrefHeight(26);
        policyHolderMenuButton.setPrefWidth(150);

        policyOwnerMenuButton = new MenuButton("Select Policy Owner");
        policyOwnerMenuButton.setLayoutX(227);
        policyOwnerMenuButton.setLayoutY(213);
        policyOwnerMenuButton.setPrefHeight(26);
        policyOwnerMenuButton.setPrefWidth(150);

        datePicker = new DatePicker();
        datePicker.setLayoutX(227);
        datePicker.setLayoutY(282);

        doneAddingIC = new Button("ADD");
        doneAddingIC.setLayoutX(502);
        doneAddingIC.setLayoutY(168);
        doneAddingIC.setPrefHeight(47);
        doneAddingIC.setPrefWidth(106);
        doneAddingIC.setFont(new Font("Calibri", 18));
        doneAddingIC.setOnAction(event -> doneAddInsuranceCard());

        root.getChildren().addAll(titleLabel, logoImageView, blendImageView, cardHolderLabel, policyOwnerLabel, expireDateLabel, policyHolderMenuButton, policyOwnerMenuButton, datePicker, doneAddingIC);

        populateMenuButtons();
        addIc.getScene().setRoot(root);
    }
    // Helper method to extract ID from the text (modify based on your actual format)
    private String extractIdFromText(String text) {
        if (text == null || text.isEmpty()) {
            return null; // Handle empty text
        }
        // Assuming the ID is at the end, separated by a space or hyphen
        int indexOfSeparator = text.lastIndexOf(" ") + 1; // Adjust for different separators
        if (indexOfSeparator > 0) {
            return text.substring(indexOfSeparator);
        } else {
            return text; // If no separator found, return the entire text (might need adjustment)
        }
    }
    private void doneAddInsuranceCard() {
        String cardHolder = extractIdFromText(policyHolderMenuButton.getText()); // Get selected card holder
        String policyOwner = extractIdFromText(policyOwnerMenuButton.getText()); // Get selected policy owner
        LocalDate expireDate = datePicker.getValue(); // Get selected expiration date

        // Convert LocalDate to java.util.Date (legacy approach)
        java.util.Date utilExpireDate = java.util.Date.from(expireDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Convert java.util.Date to java.sql.Date
        Date sqlExpireDate = new Date(utilExpireDate.getTime());

        String cardNumber = DBUtil.generateUniqueRandomCardNumber();
        InsuranceCard newIC = new InsuranceCard(cardNumber,cardHolder, policyOwner, sqlExpireDate);

        DBUtil.addInsuranceCard(newIC);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Add Insurance Card");
        alert.setHeaderText("Card: "+ cardNumber + " Added Successfully!");
        alert.setContentText(newIC.toString());
        alert.showAndWait();
        dashBoard(doneAddingIC);
    }

    private void populateMenuButtons() throws SQLException {
        List<String> policyHolders = DBUtil.getPolicyHolders();
        List<String> policyOwners = DBUtil.getPolicyOwners();

        policyHolderMenuButton.getItems().clear();
        policyOwnerMenuButton.getItems().clear();

        for (String holder : policyHolders) {
            MenuItem menuItem = new MenuItem(holder);
            menuItem.setOnAction(event -> policyHolderMenuButton.setText(holder));
            policyHolderMenuButton.getItems().add(menuItem);
        }

        for (String owner : policyOwners) {
            MenuItem menuItem = new MenuItem(owner);
            menuItem.setOnAction(event -> policyOwnerMenuButton.setText(owner));
            policyOwnerMenuButton.getItems().add(menuItem);
        }
    }

    @FXML
// Open the Admin Dashboard
    public void openAdminDashboard(Button loginButton) {
        dashBoard(loginButton);
    }

    @FXML
    private Button ph;
    @FXML
    public void policyHolderMenu(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/SystemAdminGUI/sysPolicyHolderMenu.fxml"));
            Parent adminDashboardRoot = loader.load();

            Node sourceNode = ph; // Use any node from the current scene

            // Get the primary stage from the source node's scene
            Stage primaryStage = (Stage) sourceNode.getScene().getWindow();

            primaryStage.setScene(new Scene(adminDashboardRoot));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any errors loading the admin dashboard FXML
        }
    }

    @FXML
    private Button de;
    public void dependentMenu(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/SystemAdminGUI/sysDependentMenu.fxml"));
            Parent adminDashboardRoot = loader.load();

            Node sourceNode = de; // Use any node from the current scene

            // Get the primary stage from the source node's scene
            Stage primaryStage = (Stage) sourceNode.getScene().getWindow();

            primaryStage.setScene(new Scene(adminDashboardRoot));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any errors loading the admin dashboard FXML
        }
    }
    @FXML
    private Button po;
    public void policyOwnerMenu(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/SystemAdminGUI/sysPolicyOwnerMenu.fxml"));
            Parent adminDashboardRoot = loader.load();

            Node sourceNode = po; // Use any node from the current scene

            // Get the primary stage from the source node's scene
            Stage primaryStage = (Stage) sourceNode.getScene().getWindow();

            primaryStage.setScene(new Scene(adminDashboardRoot));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any errors loading the admin dashboard FXML
        }
    }
    @FXML
    private Button is;
    public void insuranceSurveyorMenu(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/SystemAdminGUI/sysInsuranceSurveyorMenu.fxml"));
            Parent adminDashboardRoot = loader.load();

            Node sourceNode = is; // Use any node from the current scene

            // Get the primary stage from the source node's scene
            Stage primaryStage = (Stage) sourceNode.getScene().getWindow();

            primaryStage.setScene(new Scene(adminDashboardRoot));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any errors loading the admin dashboard FXML
        }
    }
    @FXML
    private Button im;
    public void insuranceManagerMenu(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/SystemAdminGUI/sysInsuranceManagerMenu.fxml"));
            Parent adminDashboardRoot = loader.load();

            Node sourceNode = im; // Use any node from the current scene

            // Get the primary stage from the source node's scene
            Stage primaryStage = (Stage) sourceNode.getScene().getWindow();

            primaryStage.setScene(new Scene(adminDashboardRoot));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any errors loading the admin dashboard FXML
        }
    }

    @FXML
    private Button ic;
    public void insuranceCardMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/SystemAdminGUI/sysInsuranceCardMenu.fxml"));
            Parent adminDashboardRoot = loader.load();

            Node sourceNode = ic; // Use any node from the current scene

            // Get the primary stage from the source node's scene
            Stage primaryStage = (Stage) sourceNode.getScene().getWindow();

            primaryStage.setScene(new Scene(adminDashboardRoot));
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle any errors loading the admin dashboard FXML
        }
    }
    @FXML
    private Button back;
    public void backtoDashboard() {
        dashBoard(back);
    }
    @FXML
    private Button read;
    // Read dependent
    @FXML
    private MenuButton readIC;
    @FXML
    private Button readIC1;

    @FXML
    private void readInsuranceCard() throws SQLException {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(600, 400);
        root.setStyle("-fx-background-color: #CDE8E5;");

        Label titleLabel = new Label("READ INSURANCE CARD");
        titleLabel.setLayoutX(128);
        titleLabel.setPrefHeight(118);
        titleLabel.setPrefWidth(497);
        titleLabel.setStyle("-fx-background-color: #CDE8E5;");
        titleLabel.setFont(new Font("Calibri", 24));
        titleLabel.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView logoImageView = new ImageView(new Image("https://1000logos.net/wp-content/uploads/2019/07/RMIT-Logo.png"));
        logoImageView.setFitHeight(88);
        logoImageView.setFitWidth(286);
        logoImageView.setPreserveRatio(true);
        logoImageView.setPickOnBounds(true);

        ImageView blendImageView = new ImageView(new Image("https://tse1.mm.bing.net/th?id=OIP.QqEXi7j5Z0ZMFu8pLgTxzAHaHa&amp;pid=Api&amp;P=0&amp;h=180"));
        blendImageView.setFitHeight(150);
        blendImageView.setFitWidth(200);
        blendImageView.setLayoutX(450);
        blendImageView.setLayoutY(242);
        blendImageView.setPreserveRatio(true);
        blendImageView.setPickOnBounds(true);
        blendImageView.setBlendMode(javafx.scene.effect.BlendMode.MULTIPLY);

        Label policyOwnerLabel = new Label("Insurance Card:");
        policyOwnerLabel.setLayoutX(77);
        policyOwnerLabel.setLayoutY(126);
        policyOwnerLabel.setFont(new Font("Calibri", 18));

        readIC = new MenuButton("Select Insurance Card");
        readIC.setLayoutX(248);
        readIC.setLayoutY(124);
        readIC.setPrefHeight(26);
        readIC.setPrefWidth(367);
        populatereadIC();

        readIC1 = new Button("READ");
        readIC1.setLayoutX(179);
        readIC1.setLayoutY(303);
        readIC1.setPrefHeight(47);
        readIC1.setPrefWidth(106);
        readIC1.setFont(new Font("Calibri", 18));
        readIC1.setOnAction(event -> doneReadingIC());

        root.getChildren().addAll(titleLabel, logoImageView, blendImageView, policyOwnerLabel, readIC, readIC1);


        read.getScene().setRoot(root);
    }
    private void populatereadIC() throws SQLException {
        List<String> insuranceCard = DBUtil.getICForMenu();
        readIC.getItems().clear();

        for (String is : insuranceCard) {
            MenuItem menuItem = new MenuItem(is);
            menuItem.setOnAction(event -> readIC.setText(is));
            readIC.getItems().add(menuItem);
        }
    }
    public void doneReadingIC(){
        InsuranceCard user = DBUtil.getIC(extractCardNumber(readIC.getText()));
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Read User:");
        alert.setHeaderText("InsuranceCard: "+ user.getCardNumber());
        alert.setContentText(user.toString());
        alert.showAndWait();
        dashBoard(readIC1);
    }

    @FXML
    private MenuButton readD;
    @FXML
    private Button readD1;

    @FXML
    private void readDependent() throws SQLException {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(600, 400);
        root.setStyle("-fx-background-color: #CDE8E5;");

        Label titleLabel = new Label("READ DEPENDENT");
        titleLabel.setLayoutX(128);
        titleLabel.setPrefHeight(118);
        titleLabel.setPrefWidth(497);
        titleLabel.setStyle("-fx-background-color: #CDE8E5;");
        titleLabel.setFont(new Font("Calibri", 24));
        titleLabel.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView logoImageView = new ImageView(new Image("https://1000logos.net/wp-content/uploads/2019/07/RMIT-Logo.png"));
        logoImageView.setFitHeight(88);
        logoImageView.setFitWidth(286);
        logoImageView.setPreserveRatio(true);
        logoImageView.setPickOnBounds(true);

        ImageView blendImageView = new ImageView(new Image("https://tse1.mm.bing.net/th?id=OIP.QqEXi7j5Z0ZMFu8pLgTxzAHaHa&amp;pid=Api&amp;P=0&amp;h=180"));
        blendImageView.setFitHeight(150);
        blendImageView.setFitWidth(200);
        blendImageView.setLayoutX(450);
        blendImageView.setLayoutY(242);
        blendImageView.setPreserveRatio(true);
        blendImageView.setPickOnBounds(true);
        blendImageView.setBlendMode(javafx.scene.effect.BlendMode.MULTIPLY);

        Label policyOwnerLabel = new Label("Dependent:");
        policyOwnerLabel.setLayoutX(77);
        policyOwnerLabel.setLayoutY(126);
        policyOwnerLabel.setFont(new Font("Calibri", 18));

        readD = new MenuButton("Select Dependent");
        readD.setLayoutX(248);
        readD.setLayoutY(124);
        readD.setPrefHeight(26);
        readD.setPrefWidth(367);

        readD1 = new Button("READ");
        readD1.setLayoutX(179);
        readD1.setLayoutY(303);
        readD1.setPrefHeight(47);
        readD1.setPrefWidth(106);
        readD1.setFont(new Font("Calibri", 18));
        readD1.setOnAction(event -> doneReadingDependent());

        root.getChildren().addAll(titleLabel, logoImageView, blendImageView, policyOwnerLabel, readD, readD1);

        populatereadD();
        read.getScene().setRoot(root);
    }
    private void populatereadD() throws SQLException {
        List<String> dependent = DBUtil.getDependent();
        readD.getItems().clear();

        for (String is : dependent) {
            MenuItem menuItem = new MenuItem(is);
            menuItem.setOnAction(event -> readD.setText(is));
            readD.getItems().add(menuItem);
        }
    }
    public void doneReadingDependent(){
        User user = DBUtil.getUser(extractIdFromText(readD.getText()));
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Read User:");
        alert.setHeaderText("User "+ user.getFullName());
        alert.setContentText(user.toString());
        alert.showAndWait();
        dashBoard(readD1);
    }
    // Read insurance surveyor
    @FXML
    private MenuButton ReadIS1;
    @FXML
    private Button readIS;
    @FXML
    private void readInsuranceSurveyor() throws SQLException {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(600, 400);
        root.setStyle("-fx-background-color: #CDE8E5;");

        Label titleLabel = new Label("READ INSURANCE SURVEYOR");
        titleLabel.setLayoutX(128);
        titleLabel.setPrefHeight(118);
        titleLabel.setPrefWidth(497);
        titleLabel.setStyle("-fx-background-color: #CDE8E5;");
        titleLabel.setFont(new Font("Calibri", 24));
        titleLabel.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView logoImageView = new ImageView(new Image("https://1000logos.net/wp-content/uploads/2019/07/RMIT-Logo.png"));
        logoImageView.setFitHeight(88);
        logoImageView.setFitWidth(286);
        logoImageView.setPreserveRatio(true);
        logoImageView.setPickOnBounds(true);

        ImageView blendImageView = new ImageView(new Image("https://tse1.mm.bing.net/th?id=OIP.QqEXi7j5Z0ZMFu8pLgTxzAHaHa&amp;pid=Api&amp;P=0&amp;h=180"));
        blendImageView.setFitHeight(150);
        blendImageView.setFitWidth(200);
        blendImageView.setLayoutX(450);
        blendImageView.setLayoutY(242);
        blendImageView.setPreserveRatio(true);
        blendImageView.setPickOnBounds(true);
        blendImageView.setBlendMode(javafx.scene.effect.BlendMode.MULTIPLY);

        Label policyOwnerLabel = new Label("Insurance Surveyor:");
        policyOwnerLabel.setLayoutX(77);
        policyOwnerLabel.setLayoutY(126);
        policyOwnerLabel.setFont(new Font("Calibri", 18));

        ReadIS1 = new MenuButton("Select Insurance Surveyor");
        ReadIS1.setLayoutX(248);
        ReadIS1.setLayoutY(124);
        ReadIS1.setPrefHeight(26);
        ReadIS1.setPrefWidth(367);

        readIS = new Button("READ");
        readIS.setLayoutX(179);
        readIS.setLayoutY(303);
        readIS.setPrefHeight(47);
        readIS.setPrefWidth(106);
        readIS.setFont(new Font("Calibri", 18));
        readIS.setOnAction(event -> doneReadingInsuranceSurveyor());

        root.getChildren().addAll(titleLabel, logoImageView, blendImageView, policyOwnerLabel, ReadIS1, readIS);

        populatereadIS();
        read.getScene().setRoot(root);
    }
    private void populatereadIS() throws SQLException {
        List<String> insuranceSurveyor = DBUtil.getInsuranceSurveyor();
        ReadIS1.getItems().clear();

        for (String is : insuranceSurveyor) {
            MenuItem menuItem = new MenuItem(is);
            menuItem.setOnAction(event -> ReadIS1.setText(is));
            ReadIS1.getItems().add(menuItem);
        }
    }
    public void doneReadingInsuranceSurveyor(){
        User user = DBUtil.getUser(extractIdFromText(ReadIS1.getText()));
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Read User:");
        alert.setHeaderText("User "+ user.getFullName());
        alert.setContentText(user.toString());
        alert.showAndWait();
        dashBoard(readIS);
    }
    //Read Insurance Manager
    @FXML
    private MenuButton readIM;
    @FXML
    private Button readIM1;

    @FXML
    private void readInsuranceManager() throws SQLException {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(600, 400);
        root.setStyle("-fx-background-color: #CDE8E5;");

        Label titleLabel = new Label("READ INSURANCE MANAGER");
        titleLabel.setLayoutX(128);
        titleLabel.setPrefHeight(118);
        titleLabel.setPrefWidth(497);
        titleLabel.setStyle("-fx-background-color: #CDE8E5;");
        titleLabel.setFont(new Font("Calibri", 24));
        titleLabel.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView logoImageView = new ImageView(new Image("https://1000logos.net/wp-content/uploads/2019/07/RMIT-Logo.png"));
        logoImageView.setFitHeight(88);
        logoImageView.setFitWidth(286);
        logoImageView.setPreserveRatio(true);
        logoImageView.setPickOnBounds(true);

        ImageView blendImageView = new ImageView(new Image("https://tse1.mm.bing.net/th?id=OIP.QqEXi7j5Z0ZMFu8pLgTxzAHaHa&amp;pid=Api&amp;P=0&amp;h=180"));
        blendImageView.setFitHeight(150);
        blendImageView.setFitWidth(200);
        blendImageView.setLayoutX(450);
        blendImageView.setLayoutY(242);
        blendImageView.setPreserveRatio(true);
        blendImageView.setPickOnBounds(true);
        blendImageView.setBlendMode(javafx.scene.effect.BlendMode.MULTIPLY);

        Label policyOwnerLabel = new Label("Insurance Manager:");
        policyOwnerLabel.setLayoutX(77);
        policyOwnerLabel.setLayoutY(126);
        policyOwnerLabel.setFont(new Font("Calibri", 18));

        readIM = new MenuButton("Select Insurance Manager");
        readIM.setLayoutX(248);
        readIM.setLayoutY(124);
        readIM.setPrefHeight(26);
        readIM.setPrefWidth(367);

        readIM1 = new Button("READ");
        readIM1.setLayoutX(179);
        readIM1.setLayoutY(303);
        readIM1.setPrefHeight(47);
        readIM1.setPrefWidth(106);
        readIM1.setFont(new Font("Calibri", 18));
        readIM1.setOnAction(event -> doneReadingInsuranceManager());

        root.getChildren().addAll(titleLabel, logoImageView, blendImageView, policyOwnerLabel, readIM, readIM1);

        populatereadIM();
        read.getScene().setRoot(root);
    }
    private void populatereadIM() throws SQLException {
        List<String> insuranceManager = DBUtil.getInsuranceManager();

        readIM.getItems().clear();

        for (String im : insuranceManager) {
            MenuItem menuItem = new MenuItem(im);
            menuItem.setOnAction(event -> readIM.setText(im));
            readIM.getItems().add(menuItem);
        }
    }
    public void doneReadingInsuranceManager(){
        User user = DBUtil.getUser(extractIdFromText(readIM.getText()));
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Read User:");
        alert.setHeaderText("User "+ user.getFullName());
        alert.setContentText(user.toString());
        alert.showAndWait();
        dashBoard(readIM1);
    }
    // Read policy owners
    @FXML
    private Button readPO; // Button to activate reading
    @FXML
    private MenuButton readPO1; // menu button to choose which policy holder to read
    @FXML
    private void readPolicyOwner() throws SQLException {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(600, 400);
        root.setStyle("-fx-background-color: #CDE8E5;");

        Label titleLabel = new Label("READ POLICY OWNER");
        titleLabel.setLayoutX(128);
        titleLabel.setPrefHeight(118);
        titleLabel.setPrefWidth(497);
        titleLabel.setStyle("-fx-background-color: #CDE8E5;");
        titleLabel.setFont(new Font("Calibri", 24));
        titleLabel.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView logoImageView = new ImageView(new Image("https://1000logos.net/wp-content/uploads/2019/07/RMIT-Logo.png"));
        logoImageView.setFitHeight(88);
        logoImageView.setFitWidth(286);
        logoImageView.setPreserveRatio(true);
        logoImageView.setPickOnBounds(true);

        ImageView blendImageView = new ImageView(new Image("https://tse1.mm.bing.net/th?id=OIP.QqEXi7j5Z0ZMFu8pLgTxzAHaHa&amp;pid=Api&amp;P=0&amp;h=180"));
        blendImageView.setFitHeight(150);
        blendImageView.setFitWidth(200);
        blendImageView.setLayoutX(450);
        blendImageView.setLayoutY(242);
        blendImageView.setPreserveRatio(true);
        blendImageView.setPickOnBounds(true);
        blendImageView.setBlendMode(javafx.scene.effect.BlendMode.MULTIPLY);

        Label policyOwnerLabel = new Label("Policy Owner:");
        policyOwnerLabel.setLayoutX(77);
        policyOwnerLabel.setLayoutY(126);
        policyOwnerLabel.setFont(new Font("Calibri", 18));

        readPO1 = new MenuButton("Select Policy Owner");
        readPO1.setLayoutX(248);
        readPO1.setLayoutY(124);
        readPO1.setPrefHeight(26);
        readPO1.setPrefWidth(367);

        readPO = new Button("READ");
        readPO.setLayoutX(179);
        readPO.setLayoutY(303);
        readPO.setPrefHeight(47);
        readPO.setPrefWidth(106);
        readPO.setFont(new Font("Calibri", 18));
        readPO.setOnAction(event -> doneReadingPolicyOwner());

        root.getChildren().addAll(titleLabel, logoImageView, blendImageView, policyOwnerLabel, readPO1, readPO);

        populatereadPO1();
        read.getScene().setRoot(root);
    }
    private void populatereadPO1() throws SQLException {
        List<String> policyOwners = DBUtil.getPolicyOwners();

        readPO1.getItems().clear();

        for (String owner : policyOwners) {
            MenuItem menuItem = new MenuItem(owner);
            menuItem.setOnAction(event -> readPO1.setText(owner));
            readPO1.getItems().add(menuItem);
        }
    }
    public void doneReadingPolicyOwner(){
        User user = DBUtil.getUser(extractIdFromText(readPO1.getText()));
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Read User:");
        alert.setHeaderText("User "+ user.getFullName());
        alert.setContentText(user.toString());
        alert.showAndWait();
        dashBoard(readPO);

    }
    // Read Policy Holder
    @FXML
    private MenuButton readPH1; // menu button to choose which policy holder to read
    @FXML
    private Button readPH; // Button to activate reading
    @FXML
    private void readPolicyHolder() throws SQLException {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(600, 400);
        root.setStyle("-fx-background-color: #CDE8E5;");

        Label titleLabel = new Label("READ POLICY HOLDER");
        titleLabel.setLayoutX(128);
        titleLabel.setPrefHeight(118);
        titleLabel.setPrefWidth(497);
        titleLabel.setStyle("-fx-background-color: #CDE8E5;");
        titleLabel.setFont(new Font("Calibri", 24));
        titleLabel.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView logoImageView = new ImageView(new Image("https://1000logos.net/wp-content/uploads/2019/07/RMIT-Logo.png"));
        logoImageView.setFitHeight(88);
        logoImageView.setFitWidth(286);
        logoImageView.setPreserveRatio(true);
        logoImageView.setPickOnBounds(true);

        ImageView blendImageView = new ImageView(new Image("https://tse1.mm.bing.net/th?id=OIP.QqEXi7j5Z0ZMFu8pLgTxzAHaHa&amp;pid=Api&amp;P=0&amp;h=180"));
        blendImageView.setFitHeight(150);
        blendImageView.setFitWidth(200);
        blendImageView.setLayoutX(450);
        blendImageView.setLayoutY(242);
        blendImageView.setPreserveRatio(true);
        blendImageView.setPickOnBounds(true);
        blendImageView.setBlendMode(javafx.scene.effect.BlendMode.MULTIPLY);

        Label policyHolderLabel = new Label("Policy Holder:");
        policyHolderLabel.setLayoutX(77);
        policyHolderLabel.setLayoutY(126);
        policyHolderLabel.setFont(new Font("Calibri", 18));

        readPH1 = new MenuButton("Select Policy Holder");
        readPH1.setLayoutX(248);
        readPH1.setLayoutY(124);
        readPH1.setPrefHeight(26);
        readPH1.setPrefWidth(367);

        readPH = new Button("READ");
        readPH.setLayoutX(179);
        readPH.setLayoutY(303);
        readPH.setPrefHeight(47);
        readPH.setPrefWidth(106);
        readPH.setFont(new Font("Calibri", 18));
        readPH.setOnAction(event -> doneReadingPolicyHolder());

        root.getChildren().addAll(titleLabel, logoImageView, blendImageView, policyHolderLabel, readPH1, readPH);

        populatereadPH1();
        read.getScene().setRoot(root);
    }
    private void populatereadPH1() throws SQLException {
        List<String> policyHolders = DBUtil.getPH();

        readPH1.getItems().clear();

        for (String holder : policyHolders) {
            MenuItem menuItem = new MenuItem(holder);
            menuItem.setOnAction(event -> readPH1.setText(holder));
            readPH1.getItems().add(menuItem);
        }
    }
    public void doneReadingPolicyHolder(){
        User user = DBUtil.getUser(extractIdFromText(readPH1.getText()));
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Read User:");
        alert.setHeaderText("User "+ user.getFullName());
        alert.setContentText(user.toString());
        alert.showAndWait();
        dashBoard(readPH);
    }
    ////////////////////////UPDATE SECTION/////////////////////////////////////////////////////////////////////////////////////
   @FXML
   private Button update;
    @FXML
    private Button upPH;
    @FXML
    private MenuButton upPH1;
    @FXML
    private Button doneUpdating;
// updating policy holder
    @FXML
    private void updatePolicyHolder() throws SQLException {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(600, 400);
        root.setStyle("-fx-background-color: #CDE8E5;");

        Label titleLabel = new Label("UPDATE POLICY HOLDER");
        titleLabel.setLayoutX(128);
        titleLabel.setPrefHeight(118);
        titleLabel.setPrefWidth(497);
        titleLabel.setStyle("-fx-background-color: #CDE8E5;");
        titleLabel.setFont(new Font("Calibri", 24));
        titleLabel.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView logoImageView = new ImageView(new Image("https://1000logos.net/wp-content/uploads/2019/07/RMIT-Logo.png"));
        logoImageView.setFitHeight(88);
        logoImageView.setFitWidth(286);
        logoImageView.setPreserveRatio(true);
        logoImageView.setPickOnBounds(true);

        ImageView blendImageView = new ImageView(new Image("https://tse1.mm.bing.net/th?id=OIP.QqEXi7j5Z0ZMFu8pLgTxzAHaHa&amp;pid=Api&amp;P=0&amp;h=180"));
        blendImageView.setFitHeight(150);
        blendImageView.setFitWidth(200);
        blendImageView.setLayoutX(450);
        blendImageView.setLayoutY(242);
        blendImageView.setPreserveRatio(true);
        blendImageView.setPickOnBounds(true);
        blendImageView.setBlendMode(javafx.scene.effect.BlendMode.MULTIPLY);

        Label policyHolderLabel = new Label("Policy Holder:");
        policyHolderLabel.setLayoutX(77);
        policyHolderLabel.setLayoutY(126);
        policyHolderLabel.setFont(new Font("Calibri", 18));
        upPH1 = new MenuButton("Select Policy Holder");
        upPH1.setLayoutX(248);
        upPH1.setLayoutY(124);
        upPH1.setPrefHeight(26);
        upPH1.setPrefWidth(367);

        upPH = new Button("UPDATE");
        upPH.setLayoutX(179);
        upPH.setLayoutY(303);
        upPH.setPrefHeight(47);
        upPH.setPrefWidth(106);
        upPH.setFont(new Font("Calibri", 18));
        upPH.setOnAction(event -> goToUpdate());

        root.getChildren().addAll(titleLabel, logoImageView, blendImageView, policyHolderLabel, upPH1, upPH);

        populateUpPH1();
        update.getScene().setRoot(root);
    }
    private void populateUpPH1() throws SQLException {
        List<String> policyHolders = DBUtil.getPH();

        upPH1.getItems().clear();

        for (String holder : policyHolders) {
            MenuItem menuItem = new MenuItem(holder);
            menuItem.setOnAction(event -> {
                upPH1.setText(holder);
                SharedData.getInstance().setSelectedPolicyHolder(holder);
            });
            upPH1.getItems().add(menuItem);
        }
    }

    public void goToUpdate(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/SystemAdminGUI/UpdateUser.fxml"));
            Parent adminDashboardRoot = loader.load();

            Node sourceNode = upPH; // Use any node from the current scene

            // Get the primary stage from the source node's scene
            Stage primaryStage = (Stage) sourceNode.getScene().getWindow();

            primaryStage.setScene(new Scene(adminDashboardRoot));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any errors loading the admin dashboard FXML
        }
    }
    @FXML
    private TextField upemail;
    @FXML
    private TextField upfullname;
    @FXML
    private TextField uppassword;

    public void doneUpdatingPH() {
        SharedData sharedData = SharedData.getInstance();
        String selectedEntity;
        if (sharedData.isUpdatingPolicyHolder()) {
            selectedEntity = sharedData.getSelectedPolicyHolder();
        } else if (sharedData.isUpdatingDependent()) {
            selectedEntity = sharedData.getSelectedDependent();
        } else if (sharedData.isUpdatingInsuranceSurveyor()) {
            selectedEntity = sharedData.getSelectedInsuranceSurveyor();
        } else if (sharedData.isUpdatingInsuranceManager()) {
            selectedEntity = sharedData.getSelectedInsuranceManager();
        } else {
            selectedEntity = sharedData.getSelectedPolicyOwner();
        }

        if (selectedEntity != null) {
            User user = DBUtil.getUser(extractIdFromText(selectedEntity));
            String name = upfullname.getText();
            String email = upemail.getText();
            String password = uppassword.getText();

            if (name != null && !name.isEmpty()) {
                user.setFullName(name);
            }
            if (email != null && !email.isEmpty()) {
                user.setEmail(email);
            }
            if (password != null && !password.isEmpty()) {
                user.setPassword(password);
            }
            DBUtil.updateUser(user);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Updated User:");
            alert.setHeaderText("User " + user.getId());
            alert.setContentText(user.toString());
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No selection");
            alert.setContentText("Please select a policy holder, policy owner, dependent, insurance surveyor, or insurance manager to update.");
            alert.showAndWait();
        }
        dashBoard(upPH);
    }


    //update Policy Owner
    @FXML
    private MenuButton upPO1;

    @FXML
    private void updatePolicyOwner() throws SQLException {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(600, 400);
        root.setStyle("-fx-background-color: #CDE8E5;");

        Label titleLabel = new Label("UPDATE POLICY OWNER");
        titleLabel.setLayoutX(128);
        titleLabel.setPrefHeight(118);
        titleLabel.setPrefWidth(497);
        titleLabel.setStyle("-fx-background-color: #CDE8E5;");
        titleLabel.setFont(new Font("Calibri", 24));
        titleLabel.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView logoImageView = new ImageView(new Image("https://1000logos.net/wp-content/uploads/2019/07/RMIT-Logo.png"));
        logoImageView.setFitHeight(88);
        logoImageView.setFitWidth(286);
        logoImageView.setPreserveRatio(true);
        logoImageView.setPickOnBounds(true);

        ImageView blendImageView = new ImageView(new Image("https://tse1.mm.bing.net/th?id=OIP.QqEXi7j5Z0ZMFu8pLgTxzAHaHa&amp;pid=Api&amp;P=0&amp;h=180"));
        blendImageView.setFitHeight(150);
        blendImageView.setFitWidth(200);
        blendImageView.setLayoutX(450);
        blendImageView.setLayoutY(242);
        blendImageView.setPreserveRatio(true);
        blendImageView.setPickOnBounds(true);
        blendImageView.setBlendMode(javafx.scene.effect.BlendMode.MULTIPLY);

        Label policyHolderLabel = new Label("Policy Owners:");
        policyHolderLabel.setLayoutX(77);
        policyHolderLabel.setLayoutY(126);
        policyHolderLabel.setFont(new Font("Calibri", 18));
        upPO1 = new MenuButton("Select Policy Owner");
        upPO1.setLayoutX(248);
        upPO1.setLayoutY(124);
        upPO1.setPrefHeight(26);
        upPO1.setPrefWidth(367);

        upPH = new Button("UPDATE");
        upPH.setLayoutX(179);
        upPH.setLayoutY(303);
        upPH.setPrefHeight(47);
        upPH.setPrefWidth(106);
        upPH.setFont(new Font("Calibri", 18));
        upPH.setOnAction(event -> goToUpdate());

        root.getChildren().addAll(titleLabel, logoImageView, blendImageView, policyHolderLabel, upPO1, upPH);

        populateUpPO1();
        update.getScene().setRoot(root);
    }
    private void populateUpPO1() throws SQLException {
        List<String> policyOwners = DBUtil.getPolicyOwners(); // Assuming you have a method to get policy owners

        upPO1.getItems().clear(); // Assuming upPO1 is your MenuButton for policy owners

        for (String owner : policyOwners) {
            MenuItem menuItem = new MenuItem(owner);
            menuItem.setOnAction(event -> {
                upPO1.setText(owner);
                SharedData.getInstance().setSelectedPolicyOwner(owner);
            });
            upPO1.getItems().add(menuItem);
        }
    }
    // update Dependent
    @FXML
    private MenuButton upDependent;
    @FXML
    private void updateDependent() throws SQLException {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(600, 400);
        root.setStyle("-fx-background-color: #CDE8E5;");

        Label titleLabel = new Label("UPDATE DELETE");
        titleLabel.setLayoutX(128);
        titleLabel.setPrefHeight(118);
        titleLabel.setPrefWidth(497);
        titleLabel.setStyle("-fx-background-color: #CDE8E5;");
        titleLabel.setFont(new Font("Calibri", 24));
        titleLabel.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView logoImageView = new ImageView(new Image("https://1000logos.net/wp-content/uploads/2019/07/RMIT-Logo.png"));
        logoImageView.setFitHeight(88);
        logoImageView.setFitWidth(286);
        logoImageView.setPreserveRatio(true);
        logoImageView.setPickOnBounds(true);

        ImageView blendImageView = new ImageView(new Image("https://tse1.mm.bing.net/th?id=OIP.QqEXi7j5Z0ZMFu8pLgTxzAHaHa&amp;pid=Api&amp;P=0&amp;h=180"));
        blendImageView.setFitHeight(150);
        blendImageView.setFitWidth(200);
        blendImageView.setLayoutX(450);
        blendImageView.setLayoutY(242);
        blendImageView.setPreserveRatio(true);
        blendImageView.setPickOnBounds(true);
        blendImageView.setBlendMode(javafx.scene.effect.BlendMode.MULTIPLY);

        Label policyHolderLabel = new Label("Dependent:");
        policyHolderLabel.setLayoutX(77);
        policyHolderLabel.setLayoutY(126);
        policyHolderLabel.setFont(new Font("Calibri", 18));
        upDependent = new MenuButton("Select Dependent");
        upDependent.setLayoutX(248);
        upDependent.setLayoutY(124);
        upDependent.setPrefHeight(26);
        upDependent.setPrefWidth(367);

        upPH = new Button("UPDATE");
        upPH.setLayoutX(179);
        upPH.setLayoutY(303);
        upPH.setPrefHeight(47);
        upPH.setPrefWidth(106);
        upPH.setFont(new Font("Calibri", 18));
        upPH.setOnAction(event -> goToUpdate());

        root.getChildren().addAll(titleLabel, logoImageView, blendImageView, policyHolderLabel, upDependent, upPH);

        populateUpDependent();
        update.getScene().setRoot(root);
    }

    private void populateUpDependent() throws SQLException {
        List<String> dependents = DBUtil.getDependent();

        upDependent.getItems().clear();

        for (String dependent : dependents) {
            MenuItem menuItem = new MenuItem(dependent);
            menuItem.setOnAction(event -> {
                upDependent.setText(dependent);
                SharedData.getInstance().setSelectedDependent(dependent);
            });
            upDependent.getItems().add(menuItem);
        }
    }
    @FXML
    private MenuButton upInsuranceSurveyor;
    @FXML
    private void updateInsuranceSurveyor() throws SQLException {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(600, 400);
        root.setStyle("-fx-background-color: #CDE8E5;");

        Label titleLabel = new Label("UPDATE INSURANCE SURVEYOR");
        titleLabel.setLayoutX(128);
        titleLabel.setPrefHeight(118);
        titleLabel.setPrefWidth(497);
        titleLabel.setStyle("-fx-background-color: #CDE8E5;");
        titleLabel.setFont(new Font("Calibri", 24));
        titleLabel.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView logoImageView = new ImageView(new Image("https://1000logos.net/wp-content/uploads/2019/07/RMIT-Logo.png"));
        logoImageView.setFitHeight(88);
        logoImageView.setFitWidth(286);
        logoImageView.setPreserveRatio(true);
        logoImageView.setPickOnBounds(true);

        ImageView blendImageView = new ImageView(new Image("https://tse1.mm.bing.net/th?id=OIP.QqEXi7j5Z0ZMFu8pLgTxzAHaHa&amp;pid=Api&amp;P=0&amp;h=180"));
        blendImageView.setFitHeight(150);
        blendImageView.setFitWidth(200);
        blendImageView.setLayoutX(450);
        blendImageView.setLayoutY(242);
        blendImageView.setPreserveRatio(true);
        blendImageView.setPickOnBounds(true);
        blendImageView.setBlendMode(javafx.scene.effect.BlendMode.MULTIPLY);

        Label policyHolderLabel = new Label("Insurance Surveyor:");
        policyHolderLabel.setLayoutX(77);
        policyHolderLabel.setLayoutY(126);
        policyHolderLabel.setFont(new Font("Calibri", 18));
        upInsuranceSurveyor = new MenuButton("Select Insurance Surveyor");
        upInsuranceSurveyor.setLayoutX(248);
        upInsuranceSurveyor.setLayoutY(124);
        upInsuranceSurveyor.setPrefHeight(26);
        upInsuranceSurveyor.setPrefWidth(367);

        upPH = new Button("UPDATE");
        upPH.setLayoutX(179);
        upPH.setLayoutY(303);
        upPH.setPrefHeight(47);
        upPH.setPrefWidth(106);
        upPH.setFont(new Font("Calibri", 18));
        upPH.setOnAction(event -> goToUpdate());

        root.getChildren().addAll(titleLabel, logoImageView, blendImageView, policyHolderLabel, upInsuranceSurveyor, upPH);

        populateUpInsuranceSurveyor();
        update.getScene().setRoot(root);
    }
    private void populateUpInsuranceSurveyor() throws SQLException {
        List<String> surveyors = DBUtil.getInsuranceSurveyor(); // Assuming you have a method to get insurance surveyors

        upInsuranceSurveyor.getItems().clear(); // Assuming upInsuranceSurveyor is your MenuButton for insurance surveyors

        for (String surveyor : surveyors) {
            MenuItem menuItem = new MenuItem(surveyor);
            menuItem.setOnAction(event -> {
                upInsuranceSurveyor.setText(surveyor);
                SharedData.getInstance().setSelectedInsuranceSurveyor(surveyor);
            });
            upInsuranceSurveyor.getItems().add(menuItem);
        }
    }
    //update insurance manager
    @FXML
    private MenuButton upInsuranceManager;
    @FXML
    private void updateInsuranceMaanger() throws SQLException {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(600, 400);
        root.setStyle("-fx-background-color: #CDE8E5;");

        Label titleLabel = new Label("UPDATE INSURANCE MANAGER");
        titleLabel.setLayoutX(128);
        titleLabel.setPrefHeight(118);
        titleLabel.setPrefWidth(497);
        titleLabel.setStyle("-fx-background-color: #CDE8E5;");
        titleLabel.setFont(new Font("Calibri", 24));
        titleLabel.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView logoImageView = new ImageView(new Image("https://1000logos.net/wp-content/uploads/2019/07/RMIT-Logo.png"));
        logoImageView.setFitHeight(88);
        logoImageView.setFitWidth(286);
        logoImageView.setPreserveRatio(true);
        logoImageView.setPickOnBounds(true);

        ImageView blendImageView = new ImageView(new Image("https://tse1.mm.bing.net/th?id=OIP.QqEXi7j5Z0ZMFu8pLgTxzAHaHa&amp;pid=Api&amp;P=0&amp;h=180"));
        blendImageView.setFitHeight(150);
        blendImageView.setFitWidth(200);
        blendImageView.setLayoutX(450);
        blendImageView.setLayoutY(242);
        blendImageView.setPreserveRatio(true);
        blendImageView.setPickOnBounds(true);
        blendImageView.setBlendMode(javafx.scene.effect.BlendMode.MULTIPLY);

        Label policyHolderLabel = new Label("Insurance Manager:");
        policyHolderLabel.setLayoutX(77);
        policyHolderLabel.setLayoutY(126);
        policyHolderLabel.setFont(new Font("Calibri", 18));
        upInsuranceManager = new MenuButton("Select Insurance Manager");
        upInsuranceManager.setLayoutX(248);
        upInsuranceManager.setLayoutY(124);
        upInsuranceManager.setPrefHeight(26);
        upInsuranceManager.setPrefWidth(367);

        upPH = new Button("UPDATE");
        upPH.setLayoutX(179);
        upPH.setLayoutY(303);
        upPH.setPrefHeight(47);
        upPH.setPrefWidth(106);
        upPH.setFont(new Font("Calibri", 18));
        upPH.setOnAction(event -> goToUpdate());

        root.getChildren().addAll(titleLabel, logoImageView, blendImageView, policyHolderLabel, upInsuranceManager, upPH);

        populateUpInsuranceManager();
        update.getScene().setRoot(root);
    }


    private void populateUpInsuranceManager() throws SQLException {
        List<String> managers = DBUtil.getInsuranceManager(); // Assuming you have a method to get insurance managers

        upInsuranceManager.getItems().clear(); // Assuming upInsuranceManager is your MenuButton for insurance managers

        for (String manager : managers) {
            MenuItem menuItem = new MenuItem(manager);
            menuItem.setOnAction(event -> {
                upInsuranceManager.setText(manager);
                SharedData.getInstance().setSelectedInsuranceManager(manager);
            });
            upInsuranceManager.getItems().add(menuItem);
        }
    }

    public static String extractCardNumber(String insuranceCardInfoString) {
        // Split the string by the separator " - "
        String[] parts = insuranceCardInfoString.split(" - ");

        // The card number is the first part
        return parts[0];
    }
    /////////////////////////DELETE USER//////////////////////////////////////////////////
    @FXML
    private Button delete;
    @FXML
    private Button dePH;
    @FXML
    private MenuButton dePH1;

    // delete policy holder
    @FXML
    private void deletePolicyHolder() throws SQLException {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(600, 400);
        root.setStyle("-fx-background-color: #CDE8E5;");

        Label titleLabel = new Label("DELETE POLICY HOLDER");
        titleLabel.setLayoutX(128);
        titleLabel.setPrefHeight(118);
        titleLabel.setPrefWidth(497);
        titleLabel.setStyle("-fx-background-color: #CDE8E5;");
        titleLabel.setFont(new Font("Calibri", 24));
        titleLabel.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView logoImageView = new ImageView(new Image("https://1000logos.net/wp-content/uploads/2019/07/RMIT-Logo.png"));
        logoImageView.setFitHeight(88);
        logoImageView.setFitWidth(286);
        logoImageView.setPreserveRatio(true);
        logoImageView.setPickOnBounds(true);

        ImageView blendImageView = new ImageView(new Image("https://tse1.mm.bing.net/th?id=OIP.QqEXi7j5Z0ZMFu8pLgTxzAHaHa&amp;pid=Api&amp;P=0&amp;h=180"));
        blendImageView.setFitHeight(150);
        blendImageView.setFitWidth(200);
        blendImageView.setLayoutX(450);
        blendImageView.setLayoutY(242);
        blendImageView.setPreserveRatio(true);
        blendImageView.setPickOnBounds(true);
        blendImageView.setBlendMode(javafx.scene.effect.BlendMode.MULTIPLY);

        Label policyHolderLabel = new Label("Policy Holder:");
        policyHolderLabel.setLayoutX(77);
        policyHolderLabel.setLayoutY(126);
        policyHolderLabel.setFont(new Font("Calibri", 18));
        dePH1 = new MenuButton("Select Policy Holder");
        dePH1.setLayoutX(248);
        dePH1.setLayoutY(124);
        dePH1.setPrefHeight(26);
        dePH1.setPrefWidth(367);

        dePH = new Button("DELETE");
        dePH.setLayoutX(179);
        dePH.setLayoutY(303);
        dePH.setPrefHeight(47);
        dePH.setPrefWidth(106);
        dePH.setFont(new Font("Calibri", 18));
        dePH.setOnAction(event -> doneDeletingPH());

        root.getChildren().addAll(titleLabel, logoImageView, blendImageView, policyHolderLabel, dePH1, dePH);

        populateDePH1();
        delete.getScene().setRoot(root);
    }
    private void populateDePH1() throws SQLException {
        List<String> policyHolders = DBUtil.getPH();

        dePH1.getItems().clear();

        for (String holder : policyHolders) {
            MenuItem menuItem = new MenuItem(holder);
            menuItem.setOnAction(event -> {
                dePH1.setText(holder);
            });
            dePH1.getItems().add(menuItem);
        }
    }

    public void doneDeletingPH() {
            User user = DBUtil.getUser(extractIdFromText(dePH1.getText()));
            DBUtil.deleteUser(user.getId());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Delete User:");
            alert.setHeaderText("User " + user.getId());
            alert.setContentText(user.toString());
            alert.showAndWait();
            dashBoard(dePH);
    }


    //delete Policy Owner
    @FXML
    private Button dePO;
    @FXML
    private MenuButton dePO1;

    @FXML
    private void deletePolicyOwner() throws SQLException {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(600, 400);
        root.setStyle("-fx-background-color: #CDE8E5;");

        Label titleLabel = new Label("DELETE POLICY OWNER");
        titleLabel.setLayoutX(128);
        titleLabel.setPrefHeight(118);
        titleLabel.setPrefWidth(497);
        titleLabel.setStyle("-fx-background-color: #CDE8E5;");
        titleLabel.setFont(new Font("Calibri", 24));
        titleLabel.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView logoImageView = new ImageView(new Image("https://1000logos.net/wp-content/uploads/2019/07/RMIT-Logo.png"));
        logoImageView.setFitHeight(88);
        logoImageView.setFitWidth(286);
        logoImageView.setPreserveRatio(true);
        logoImageView.setPickOnBounds(true);

        ImageView blendImageView = new ImageView(new Image("https://tse1.mm.bing.net/th?id=OIP.QqEXi7j5Z0ZMFu8pLgTxzAHaHa&amp;pid=Api&amp;P=0&amp;h=180"));
        blendImageView.setFitHeight(150);
        blendImageView.setFitWidth(200);
        blendImageView.setLayoutX(450);
        blendImageView.setLayoutY(242);
        blendImageView.setPreserveRatio(true);
        blendImageView.setPickOnBounds(true);
        blendImageView.setBlendMode(javafx.scene.effect.BlendMode.MULTIPLY);

        Label policyHolderLabel = new Label("Policy Owners:");
        policyHolderLabel.setLayoutX(77);
        policyHolderLabel.setLayoutY(126);
        policyHolderLabel.setFont(new Font("Calibri", 18));
        dePO1 = new MenuButton("Select Policy Owner");
        dePO1.setLayoutX(248);
        dePO1.setLayoutY(124);
        dePO1.setPrefHeight(26);
        dePO1.setPrefWidth(367);

        dePO = new Button("DELETE");
        dePO.setLayoutX(179);
        dePO.setLayoutY(303);
        dePO.setPrefHeight(47);
        dePO.setPrefWidth(106);
        dePO.setFont(new Font("Calibri", 18));
        dePO.setOnAction(event -> doneDeletingPO());

        root.getChildren().addAll(titleLabel, logoImageView, blendImageView, policyHolderLabel, dePO1, dePO);

        populatedePO1();
        delete.getScene().setRoot(root);
    }
    public void doneDeletingPO() {
        User user = DBUtil.getUser(extractIdFromText(dePO1.getText()));
        DBUtil.deleteUser(user.getId());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Delete User:");
        alert.setHeaderText("User " + user.getId());
        alert.setContentText(user.toString());
        alert.showAndWait();
        dashBoard(dePO);
    }

    private void populatedePO1() throws SQLException {
        List<String> policyOwners = DBUtil.getPolicyOwners(); // Assuming you have a method to get policy owners

        dePO1.getItems().clear(); // Assuming upPO1 is your MenuButton for policy owners

        for (String owner : policyOwners) {
            MenuItem menuItem = new MenuItem(owner);
            menuItem.setOnAction(event -> {
                dePO1.setText(owner);
            });
            dePO1.getItems().add(menuItem);
        }
    }
    // delete Dependent
    @FXML
    private MenuButton deD;
    @FXML
    private Button deD1;
    @FXML
    private void deleteDependent() throws SQLException {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(600, 400);
        root.setStyle("-fx-background-color: #CDE8E5;");

        Label titleLabel = new Label("DELETE DEPENDENT");
        titleLabel.setLayoutX(128);
        titleLabel.setPrefHeight(118);
        titleLabel.setPrefWidth(497);
        titleLabel.setStyle("-fx-background-color: #CDE8E5;");
        titleLabel.setFont(new Font("Calibri", 24));
        titleLabel.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView logoImageView = new ImageView(new Image("https://1000logos.net/wp-content/uploads/2019/07/RMIT-Logo.png"));
        logoImageView.setFitHeight(88);
        logoImageView.setFitWidth(286);
        logoImageView.setPreserveRatio(true);
        logoImageView.setPickOnBounds(true);

        ImageView blendImageView = new ImageView(new Image("https://tse1.mm.bing.net/th?id=OIP.QqEXi7j5Z0ZMFu8pLgTxzAHaHa&amp;pid=Api&amp;P=0&amp;h=180"));
        blendImageView.setFitHeight(150);
        blendImageView.setFitWidth(200);
        blendImageView.setLayoutX(450);
        blendImageView.setLayoutY(242);
        blendImageView.setPreserveRatio(true);
        blendImageView.setPickOnBounds(true);
        blendImageView.setBlendMode(javafx.scene.effect.BlendMode.MULTIPLY);

        Label policyHolderLabel = new Label("Dependent:");
        policyHolderLabel.setLayoutX(77);
        policyHolderLabel.setLayoutY(126);
        policyHolderLabel.setFont(new Font("Calibri", 18));
        deD = new MenuButton("Select Dependent");
        deD.setLayoutX(248);
        deD.setLayoutY(124);
        deD.setPrefHeight(26);
        deD.setPrefWidth(367);

        deD1 = new Button("DELETE");
        deD1.setLayoutX(179);
        deD1.setLayoutY(303);
        deD1.setPrefHeight(47);
        deD1.setPrefWidth(106);
        deD1.setFont(new Font("Calibri", 18));
        deD1.setOnAction(event -> doneDeletingDe());

        root.getChildren().addAll(titleLabel, logoImageView, blendImageView, policyHolderLabel, deD, deD1);

        populatedeDependent();
        delete.getScene().setRoot(root);
    }

    private void populatedeDependent() throws SQLException {
        List<String> dependents = DBUtil.getDependent();

        deD.getItems().clear();

        for (String dependent : dependents) {
            MenuItem menuItem = new MenuItem(dependent);
            menuItem.setOnAction(event -> {
                deD.setText(dependent);
            });
            deD.getItems().add(menuItem);
        }
    }
    public void doneDeletingDe() {
        User user = DBUtil.getUser(extractIdFromText(deD.getText()));
        DBUtil.deleteUser(user.getId());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Delete User:");
        alert.setHeaderText("User " + user.getId());
        alert.setContentText(user.toString());
        alert.showAndWait();
        dashBoard(deD1);
    }
    @FXML
    private MenuButton deInsuranceSurveyor;
    @FXML
    private Button deInsuranceSurveyor1;
    @FXML
    private void deleteInsuranceSurveyor() throws SQLException {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(600, 400);
        root.setStyle("-fx-background-color: #CDE8E5;");

        Label titleLabel = new Label("DELETE INSURANCE SURVEYOR");
        titleLabel.setLayoutX(128);
        titleLabel.setPrefHeight(118);
        titleLabel.setPrefWidth(497);
        titleLabel.setStyle("-fx-background-color: #CDE8E5;");
        titleLabel.setFont(new Font("Calibri", 24));
        titleLabel.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView logoImageView = new ImageView(new Image("https://1000logos.net/wp-content/uploads/2019/07/RMIT-Logo.png"));
        logoImageView.setFitHeight(88);
        logoImageView.setFitWidth(286);
        logoImageView.setPreserveRatio(true);
        logoImageView.setPickOnBounds(true);

        ImageView blendImageView = new ImageView(new Image("https://tse1.mm.bing.net/th?id=OIP.QqEXi7j5Z0ZMFu8pLgTxzAHaHa&amp;pid=Api&amp;P=0&amp;h=180"));
        blendImageView.setFitHeight(150);
        blendImageView.setFitWidth(200);
        blendImageView.setLayoutX(450);
        blendImageView.setLayoutY(242);
        blendImageView.setPreserveRatio(true);
        blendImageView.setPickOnBounds(true);
        blendImageView.setBlendMode(javafx.scene.effect.BlendMode.MULTIPLY);

        Label policyHolderLabel = new Label("Insurance Surveyor:");
        policyHolderLabel.setLayoutX(77);
        policyHolderLabel.setLayoutY(126);
        policyHolderLabel.setFont(new Font("Calibri", 18));
        deInsuranceSurveyor = new MenuButton("Select Insurance Surveyor");
        deInsuranceSurveyor.setLayoutX(248);
        deInsuranceSurveyor.setLayoutY(124);
        deInsuranceSurveyor.setPrefHeight(26);
        deInsuranceSurveyor.setPrefWidth(367);

        deInsuranceSurveyor1 = new Button("DELETE");
        deInsuranceSurveyor1.setLayoutX(179);
        deInsuranceSurveyor1.setLayoutY(303);
        deInsuranceSurveyor1.setPrefHeight(47);
        deInsuranceSurveyor1.setPrefWidth(106);
        deInsuranceSurveyor1.setFont(new Font("Calibri", 18));
        deInsuranceSurveyor1.setOnAction(event -> doneDeletingIS());

        root.getChildren().addAll(titleLabel, logoImageView, blendImageView, policyHolderLabel, deInsuranceSurveyor, deInsuranceSurveyor1);

        populatedeInsuranceSurveyor();
        delete.getScene().setRoot(root);
    }
    private void populatedeInsuranceSurveyor() throws SQLException {
        List<String> surveyors = DBUtil.getInsuranceSurveyor(); // Assuming you have a method to get insurance surveyors

        deInsuranceSurveyor.getItems().clear(); // Assuming upInsuranceSurveyor is your MenuButton for insurance surveyors

        for (String surveyor : surveyors) {
            MenuItem menuItem = new MenuItem(surveyor);
            menuItem.setOnAction(event -> {
                deInsuranceSurveyor.setText(surveyor);
            });
            deInsuranceSurveyor.getItems().add(menuItem);
        }
    }
    public void doneDeletingIS() {
        User user = DBUtil.getUser(extractIdFromText(deInsuranceSurveyor.getText()));
        DBUtil.deleteUser(user.getId());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Delete User:");
        alert.setHeaderText("User " + user.getId());
        alert.setContentText(user.toString());
        alert.showAndWait();
        dashBoard(deInsuranceSurveyor1);
    }
    //update insurance manager
    @FXML
    private MenuButton deInsuranceManager;
    @FXML
    private Button deInsuranceManager1;
    @FXML
    private void deInsuranceMaanger() throws SQLException {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(600, 400);
        root.setStyle("-fx-background-color: #CDE8E5;");

        Label titleLabel = new Label("DELETE INSURANCE MANAGER");
        titleLabel.setLayoutX(128);
        titleLabel.setPrefHeight(118);
        titleLabel.setPrefWidth(497);
        titleLabel.setStyle("-fx-background-color: #CDE8E5;");
        titleLabel.setFont(new Font("Calibri", 24));
        titleLabel.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView logoImageView = new ImageView(new Image("https://1000logos.net/wp-content/uploads/2019/07/RMIT-Logo.png"));
        logoImageView.setFitHeight(88);
        logoImageView.setFitWidth(286);
        logoImageView.setPreserveRatio(true);
        logoImageView.setPickOnBounds(true);

        ImageView blendImageView = new ImageView(new Image("https://tse1.mm.bing.net/th?id=OIP.QqEXi7j5Z0ZMFu8pLgTxzAHaHa&amp;pid=Api&amp;P=0&amp;h=180"));
        blendImageView.setFitHeight(150);
        blendImageView.setFitWidth(200);
        blendImageView.setLayoutX(450);
        blendImageView.setLayoutY(242);
        blendImageView.setPreserveRatio(true);
        blendImageView.setPickOnBounds(true);
        blendImageView.setBlendMode(javafx.scene.effect.BlendMode.MULTIPLY);

        Label policyHolderLabel = new Label("Insurance Manager:");
        policyHolderLabel.setLayoutX(77);
        policyHolderLabel.setLayoutY(126);
        policyHolderLabel.setFont(new Font("Calibri", 18));

        deInsuranceManager = new MenuButton("Select Insurance Manager");
        deInsuranceManager.setLayoutX(248);
        deInsuranceManager.setLayoutY(124);
        deInsuranceManager.setPrefHeight(26);
        deInsuranceManager.setPrefWidth(367);

        deInsuranceManager1 = new Button("DELETE");
        deInsuranceManager1.setLayoutX(179);
        deInsuranceManager1.setLayoutY(303);
        deInsuranceManager1.setPrefHeight(47);
        deInsuranceManager1.setPrefWidth(106);
        deInsuranceManager1.setFont(new Font("Calibri", 18));
        deInsuranceManager1.setOnAction(event -> doneDeletingIM());

        root.getChildren().addAll(titleLabel, logoImageView, blendImageView, policyHolderLabel, deInsuranceManager, deInsuranceManager1);

        populatedeInsuranceManager();
        delete.getScene().setRoot(root);
    }


    private void populatedeInsuranceManager() throws SQLException {
        List<String> managers = DBUtil.getInsuranceManager(); // Assuming you have a method to get insurance managers

        deInsuranceManager.getItems().clear(); // Assuming upInsuranceManager is your MenuButton for insurance managers

        for (String manager : managers) {
            MenuItem menuItem = new MenuItem(manager);
            menuItem.setOnAction(event ->
                deInsuranceManager.setText(manager));
            deInsuranceManager.getItems().add(menuItem);
        }
    }
    public void doneDeletingIM() {
        User user = DBUtil.getUser(extractIdFromText(deInsuranceManager.getText()));
        DBUtil.deleteUser(user.getId());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Delete User:");
        alert.setHeaderText("User " + user.getId());
        alert.setContentText(user.toString());
        alert.showAndWait();
        dashBoard(deInsuranceManager1);
    }

    private void dashBoard(Button readIM1) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/SystemAdminGUI/systemAdminDashboard.fxml"));
            Parent adminDashboardRoot = loader.load();

            Node sourceNode = readIM1; // Use any node from the current scene

            // Get the primary stage from the source node's scene
            Stage primaryStage = (Stage) sourceNode.getScene().getWindow();

            primaryStage.setScene(new Scene(adminDashboardRoot));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any errors loading the admin dashboard FXML
        }
    }
    /////////////////UPDATE INSURANCE CARD////////////////////////////////

    @FXML
    private Button upIC;
    @FXML
    private MenuButton upIC1;
    @FXML
    private DatePicker expireDatePicker;
        @FXML
        private void updateIC() throws SQLException {
            AnchorPane root = new AnchorPane();
            root.setPrefSize(600, 400);
            root.setStyle("-fx-background-color: #CDE8E5;");

            // Create and configure the label
            Label titleLabel = new Label("UPDATE INSURANCE CARD");
            titleLabel.setFont(new Font("Calibri", 24));
            titleLabel.setStyle("-fx-background-color: #CDE8E5;");
            titleLabel.setPrefSize(497, 118);
            titleLabel.setLayoutX(128);
            titleLabel.setAlignment(javafx.geometry.Pos.CENTER);

            // Create and configure the first ImageView
            ImageView logoImageView = new ImageView();
            logoImageView.setFitHeight(88);
            logoImageView.setFitWidth(286);
            logoImageView.setPreserveRatio(true);
            logoImageView.setImage(new Image("https://1000logos.net/wp-content/uploads/2019/07/RMIT-Logo.png"));

            // Create and configure the second ImageView
            ImageView blendImageView = new ImageView();
            blendImageView.setFitHeight(150);
            blendImageView.setFitWidth(200);
            blendImageView.setLayoutX(450);
            blendImageView.setLayoutY(242);
            blendImageView.setPreserveRatio(true);
            blendImageView.setImage(new Image("https://tse1.mm.bing.net/th?id=OIP.QqEXi7j5Z0ZMFu8pLgTxzAHaHa&amp;pid=Api&amp;P=0&amp;h=180"));
            blendImageView.setBlendMode(javafx.scene.effect.BlendMode.MULTIPLY);
            blendImageView.setEffect(new Blend());

            // Create and configure the back button
            Button backButton = new Button("Back to Dashboard");
            backButton.setFont(new Font(14));
            backButton.setLayoutX(525);
            backButton.setLayoutY(5);
            backButton.setOnAction(e -> backtoDashboard());

            Label policyHolderLabel = new Label("Insurance Card:");
            policyHolderLabel.setLayoutX(77);
            policyHolderLabel.setLayoutY(126);
            policyHolderLabel.setFont(new Font("Calibri", 18));

            upIC1 = new MenuButton("Select Insurance Card");
            upIC1.setLayoutX(248);
            upIC1.setLayoutY(124);
            upIC1.setPrefHeight(26);
            upIC1.setPrefWidth(367);

            // Create and configure the expire date label
            Label expireDateLabel = new Label("Expire Date:");
            expireDateLabel.setFont(new Font("Calibri", 18));
            expireDateLabel.setLayoutX(83);
            expireDateLabel.setLayoutY(259);

            // Create and configure the update button
            upIC = new Button("UPDATE");
            upIC.setFont(new Font("Calibri", 18));
            upIC.setPrefSize(106, 47);
            upIC.setLayoutX(508);
            upIC.setLayoutY(235);
            upIC.setOnAction(e -> doneUpdateIC());

            // Create and configure the DatePicker
            expireDatePicker = new DatePicker();
            expireDatePicker.setLayoutX(243);
            expireDatePicker.setLayoutY(257);

            // Add all components to the root pane
            root.getChildren().addAll(titleLabel, logoImageView, blendImageView, backButton, policyHolderLabel, upIC1, expireDateLabel, upIC, expireDatePicker);
            populateupIC1();
            update.getScene().setRoot(root);
        }

        private void populateupIC1(){
            List<String> card = DBUtil.getICForMenu(); // Assuming you have a method to get insurance managers

            upIC1.getItems().clear(); // Assuming upInsuranceManager is your MenuButton for insurance managers

            for (String ic : card) {
                MenuItem menuItem = new MenuItem(ic);
                menuItem.setOnAction(event -> upIC1.setText(ic));
                upIC1.getItems().add(menuItem);
            }
        }

    public void doneUpdateIC() {
        String user = extractCardNumber(upIC1.getText());
        LocalDate expireDate = expireDatePicker.getValue(); // Get selected expiration date
        DBUtil.updateIC(user, expireDate);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Update Insurance Card:");
        alert.setHeaderText("InsuranceCard: "+ extractCardNumber(upIC1.getText()));
        alert.setContentText(user.toString());
        alert.showAndWait();
        dashBoard(upIC);
    }
}
