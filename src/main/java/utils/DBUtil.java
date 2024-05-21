package utils;

import models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DBUtil {
    private static final String URL = "jdbc:postgresql://aws-0-ap-southeast-1.pooler.supabase.com:5432/postgres";
    private static final String USER = "postgres.tfusvrojiuczultagudt";
    private static final String PASSWORD = "fURTHERpROGRAMMING78";

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("Connected to the database."); // Debug statement
        return conn;
    }

    // Get Claims for Policy Holder
    public static List<Claim> getClaimsForPolicyHolder(String policyHolderName) throws SQLException {
        List<Claim> claims = new ArrayList<>();
        String sql = "SELECT * FROM claims WHERE policyHolder_name = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, policyHolderName);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Claim claim = new Claim(
                        rs.getString("id"),
                        rs.getDate("claim_date").toLocalDate(), // Convert to LocalDate
                        rs.getString("insured_person"),
                        rs.getString("card_number"),
                        rs.getDate("exam_date").toLocalDate(),
                        rs.getFloat("claim_amount"),
                        rs.getString("status"),
                        rs.getString("receiver_bank"),
                        rs.getString("receiver_name"),
                        rs.getString("receiver_number"),
                        rs.getString("policyHolder_name")
                );
                claims.add(claim);
            }
        }
        return claims;
    }

    public static PolicyHolder fetchPolicyHolderDetails(String userId) throws SQLException {
        String query = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Map ResultSet to PolicyHolder object
                    PolicyHolder policyHolder = new PolicyHolder();
                    policyHolder.setId(resultSet.getString("id"));
                    policyHolder.setFullName(resultSet.getString("full_name"));
                    // ... set other properties from the ResultSet
                    return policyHolder;
                }
            }
        }
        return null; // PolicyHolder not found
    }

    public static List<Claim> getAllClaims() {
        List<Claim> claims = new ArrayList<>();
        String query = "SELECT * FROM claims";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Claim claim = new Claim(
                        resultSet.getString("id"),
                        resultSet.getDate("claim_date"),
                        resultSet.getString("insured_person"),
                        resultSet.getString("card_number"),
                        resultSet.getDate("exam_date"),
                        resultSet.getDouble("claim_amount"),
                        ClaimStatus.valueOf(resultSet.getString("status")),
                        resultSet.getString("receiver_bank"),
                        resultSet.getString("receiver_name"),
                        resultSet.getString("receiver_number"),
                        resultSet.getString("policyHolder_name")
                );
                claims.add(claim);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return claims;
    }

    public static void approveClaim(String claimId) {
        String query = "UPDATE claims SET status = 'APPROVED' WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, claimId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void rejectClaim(String claimId) {
        String query = "UPDATE claims SET status = 'REJECTED' WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, claimId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User getUserByEmailAndPassword(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        UserRole.valueOf(rs.getString("role"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                users.add(new User(
                        rs.getString("id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        UserRole.valueOf(rs.getString("role"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public static void addUser(User user) {
        String query = "INSERT INTO users (id, full_name, email, password, role) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getFullName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPassword());
            pstmt.setString(5, user.getRole().name());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateUser(User user) {
        String query = "UPDATE users SET full_name = ?, email = ?, password = ?, role = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, user.getFullName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getRole().name());
            pstmt.setString(5, user.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

     //PolicyOwner remove Beneficiary
    public static void PO_deleteUser(String userId, UserRole role) {
        String query = "DELETE FROM users WHERE id = ? AND role = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, role.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Policy Owner update Beneficiary
    public static void PO_updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET full_Name = ?, email = ?, password = ?, role = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getFullName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, String.valueOf(user.getRole())); // Assuming role is stored as a string
            pstmt.setString(5, user.getId()); // Setting ID as a string

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User updated successfully.");
            } else {
                System.out.println("No user found with the specified ID and role.");
            }
        }
    }
    public static void deleteUser(String userId) {
        String query = "DELETE FROM users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<User> getFilteredCustomers(String filterCriteria) {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users WHERE " + filterCriteria;
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                users.add(new User(
                        rs.getString("id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        UserRole.valueOf(rs.getString("role"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public static List<String> getPolicyHolders() throws SQLException {
        List<String> policyHolders = new ArrayList<>();
        String query = "SELECT u.id, u.full_name " +
                "FROM users u " +
                "LEFT JOIN \"InsuranceCard\" ic ON u.id = ic.policy_holder_id " +
                "WHERE ic.policy_holder_id IS NULL AND u.role = 'POLICY_HOLDER'";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String fullName = rs.getString("full_name");
                String id = rs.getString("id");
                policyHolders.add(fullName + " - " + id);
            }
        }
        return policyHolders;
    }

    public static List<Claim> getFilteredClaims() {
        List<Claim> claims = new ArrayList<>();
        String query = "SELECT claims.* FROM claims INNER JOIN users ON claims.insured_person = users.full_name";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                claims.add(new Claim(
                        rs.getString("id"),
                        rs.getDate("claim_date"),
                        rs.getString("insured_person"),
                        rs.getString("card_number"),
                        rs.getDate("exam_date"),
                        rs.getDouble("claim_amount"),
                        ClaimStatus.valueOf(rs.getString("status")),
                        rs.getString("receiver_bank"),
                        rs.getString("receiver_name"),
                        rs.getString("receiver_number"),
                        rs.getString("policyHolder_name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return claims;
    }

    public static void updateClaim(Claim claim) {
        String query = "UPDATE claims SET claim_date = ?, insured_person = ?, card_number = ?, exam_date = ?, claim_amount = ?, status = ?, receiver_bank = ?, receiver_name = ?, receiver_number = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDate(1, new java.sql.Date(claim.getClaimDate().getTime()));
            pstmt.setString(2, claim.getInsuredPerson());
            pstmt.setString(3, claim.getCardNumber());
            pstmt.setDate(4, new java.sql.Date(claim.getExamDate().getTime()));
            pstmt.setDouble(5, claim.getClaimAmount());
            pstmt.setString(6, claim.getStatus().name());
            pstmt.setString(7, claim.getReceiverBank());
            pstmt.setString(8, claim.getReceiverName());
            pstmt.setString(9, claim.getReceiverNumber());
            pstmt.setString(10, claim.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static double calculateTotalClaimedAmount() {
        double totalClaimedAmount = 0;
        String query = "SELECT SUM(claim_amount) AS total FROM claims WHERE status = 'DONE'";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                totalClaimedAmount = rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalClaimedAmount;
    }

    public static List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT id, full_name, email, role FROM users";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                customers.add(new Customer(
                        rs.getString("id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    public static List<Surveyor> getAllSurveyors() {
        List<Surveyor> surveyors = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role = 'SURVEYOR'";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                surveyors.add(new Surveyor(
                        rs.getString("id"),
                        rs.getString("full_name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return surveyors;
    }

    public static String getPrefixByUserRole(UserRole role) {
        switch (role) {
            case POLICY_HOLDER:
                return "h";
            case POLICY_OWNER:
                return "o";
            case INSURANCE_SURVEYOR:
                return "s";
            case INSURANCE_MANAGER:
                return "m";
            case DEPENDENT:
                return "d";
            case SYSTEM_ADMIN:
                return "a";
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }

    public static String getLargestIdByUserRole(UserRole role) {
        String prefix = getPrefixByUserRole(role);
        String query = "SELECT MAX(CAST(SUBSTRING(id FROM LENGTH(?) + 1) AS INTEGER)) AS max_id " +
                "FROM users " +
                "WHERE role = ? AND SUBSTRING(id FROM LENGTH(?) + 1) ~ '^[0-9]+$'";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, prefix);
            pstmt.setString(2, role.toString());
            pstmt.setString(3, prefix);
            ResultSet rs = pstmt.executeQuery();
            int maxId = 0; // Default maxId if no records found
            if (rs.next()) {
                // Get the max_id from the result set
                maxId = rs.getInt("max_id");
                // Check if max_id was null (i.e., no records found)
                if (rs.wasNull()) {
                    maxId = 0; // No records found, so initialize to 0
                }
            }
            // Increment the numeric part of the ID
            maxId++;
            // Format the new ID with the prefix and zero-padded numeric part
            String largestId = String.format("%s%02d", prefix, maxId);
            return largestId;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String generateUniqueRandomCardNumber() {
        int numRetries = 0; // Track retry attempts
        int maxRetries = 10; // Set a maximum number of retries (optional)
        String cardNumber;
        do {
            cardNumber = generateRandomCardNumber();
            if (isCardNumberInUse(cardNumber)) {
                numRetries++;
                if (numRetries >= maxRetries) {
                    throw new RuntimeException("Failed to generate unique card number after " + maxRetries + " attempts.");
                }
            }
        } while (isCardNumberInUse(cardNumber));
        return cardNumber;
    }

    public static String generateRandomCardNumber() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private static boolean isCardNumberInUse(String cardNumber) {
        String query = "SELECT COUNT(*) FROM \"InsuranceCard\" WHERE card_number = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, cardNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Check if count is greater than 0 (card exists)
            } else {
                // Table might be empty, consider this a unique card number
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database errors (consider logging or throwing exception)
            return false; // Assume failure on error
        }
    }

    public static void addInsuranceCard(InsuranceCard card) {
        String query = "INSERT INTO \"InsuranceCard\" (card_number, policy_holder_id, policy_owner_id, expire_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, card.getCardNumber()); // Use getters from the object
            pstmt.setString(2, card.getCardHolder());
            pstmt.setString(3, card.getPolicyOwner()); // Assuming policyOwnerId exists
            java.util.Date utilDate = card.getExpirationDate();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            pstmt.setDate(4, sqlDate);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve Dependent from the database
    public static List<String> getDependent() throws SQLException {
        List<String> dependent = new ArrayList<>();
        String query = "SELECT u.id, u.full_name " +
                "FROM users u " +
                "WHERE u.role = 'DEPENDENT'";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String fullName = rs.getString("full_name");
                String id = rs.getString("id");
                dependent.add(fullName + " - " + id);
            }
        }
        return dependent;
    }

    // Method to retrieve Insurance Surveyor from the database
    public static List<String> getInsuranceSurveyor() throws SQLException {
        List<String> insuranceSurveyor = new ArrayList<>();
        String query = "SELECT u.id, u.full_name " +
                "FROM users u " +
                "WHERE u.role = 'INSURANCE_SURVEYOR'";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String fullName = rs.getString("full_name");
                String id = rs.getString("id");
                insuranceSurveyor.add(fullName + " - " + id);
            }
        }
        return insuranceSurveyor;
    }

    // Method to retrieve Insurance Manager from the database
    public static List<String> getInsuranceManager() throws SQLException {
        List<String> insuranceManager = new ArrayList<>();
        String query = "SELECT u.id, u.full_name " +
                "FROM users u " +
                "WHERE u.role = 'INSURANCE_MANAGER'";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String fullName = rs.getString("full_name");
                String id = rs.getString("id");
                insuranceManager.add(fullName + " - " + id);
            }
        }
        return insuranceManager;
    }

    // Method to retrieve policy owners from the database
    public static List<String> getPolicyOwners() throws SQLException {
        List<String> policyOwners = new ArrayList<>();
        String query = "SELECT u.id, u.full_name " +
                "FROM users u " +
                "WHERE u.role = 'POLICY_OWNER'";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String fullName = rs.getString("full_name");
                String id = rs.getString("id");
                policyOwners.add(fullName + " - " + id);
            }
        }
        return policyOwners;
    }

    // Get claims for Policy Owner
    public static List<Claim> getClaimsForPolicyOwner() throws SQLException {
        List<Claim> claims = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            String query = "SELECT claims.* FROM claims JOIN InsuranceCard i ON claims.card_number = i.card_number " +
                    "JOIN users u ON i.policy_holder_id = u.id";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                claims.add(new Claim(
                        resultSet.getString("id"),
                        resultSet.getDate("claim_date"),
                        resultSet.getString("insured_person"),
                        resultSet.getString("card_number"),
                        resultSet.getDate("exam_date"),
                        resultSet.getDouble("claim_amount"),
                        ClaimStatus.valueOf(resultSet.getString("status")),
                        resultSet.getString("receiver_bank"),
                        resultSet.getString("receiver_name"),
                        resultSet.getString("receiver_number")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception to propagate it
        } finally {
            // Close resources in reverse order of creation
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return claims;
    }

    // Use to review claim (Insurance Surveyor) before deciding to propose to manager or require more information
    public static List<Claim> surveyorReviewClaim() {
        List<Claim> claims = new ArrayList<>();
        String query = "SELECT * FROM claims WHERE status = 'NEW'";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                claims.add(new Claim(
                        rs.getString("id"),
                        rs.getDate("claim_date"),
                        rs.getString("insured_person"),
                        rs.getString("card_number"),
                        rs.getDate("exam_date"),
                        rs.getDouble("claim_amount"),
                        ClaimStatus.valueOf(rs.getString("status")),
                        rs.getString("receiver_bank"),
                        rs.getString("receiver_name"),
                        rs.getString("receiver_number")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return claims;
    }

    // Use to propose claim to manager (Insurance Surveyor)
    public static List<Claim> surveyorProposeToManager() {
        List<Claim> claims = new ArrayList<>();
        String query = "UPDATE claims SET status = 'PROCESSING' WHERE status = 'NEW'";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                claims.add(new Claim(
                        rs.getString("id"),
                        rs.getDate("claim_date"),
                        rs.getString("insured_person"),
                        rs.getString("card_number"),
                        rs.getDate("exam_date"),
                        rs.getDouble("claim_amount"),
                        ClaimStatus.valueOf(rs.getString("status")),
                        rs.getString("receiver_bank"),
                        rs.getString("receiver_name"),
                        rs.getString("receiver_number")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return claims;
    }

    public static Customer getCustomerById(String id) {
        String query = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Customer(
                        rs.getString("id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("role")
                );
            } else {
                System.out.println("No user found with ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Claim> getClaimsToExamine() {
        List<Claim> claims = new ArrayList<>();
        String query = "SELECT * FROM claims WHERE status = 'waiting for examination'";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Claim claim = new Claim(
                        resultSet.getString("id"),
                        resultSet.getDate("claim_date"),
                        resultSet.getString("insured_person"),
                        resultSet.getString("card_number"),
                        resultSet.getDate("exam_date"),
                        resultSet.getDouble("claim_amount"),
                        ClaimStatus.valueOf(resultSet.getString("status")),
                        resultSet.getString("receiver_bank"),
                        resultSet.getString("receiver_name"),
                        resultSet.getString("receiver_number"),
                        resultSet.getString("policyHolderName")
                );
                claims.add(claim);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return claims;
    }

    public static List<String> getPH() throws SQLException {
        List<String> policyHolders = new ArrayList<>();
        String query = "SELECT u.id, u.full_name " +
                "FROM users u " +
                "WHERE u.role = 'POLICY_HOLDER'";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String fullName = rs.getString("full_name");
                String id = rs.getString("id");
                policyHolders.add(fullName + " - " + id);
            }
        }
        return policyHolders;
    }

    public static User getUser(String id) {
        String query = "SELECT * FROM users WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        UserRole.valueOf(rs.getString("role"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection to PostgreSQL has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    //Use to filter claim (INSURANCE SURVEYOR)
    public static List<String> surveyorGetFilterClaim(String status, String policyHolderName /*, String amountRange*/) throws SQLException {
        List<String> filterClaim = new ArrayList<>();

        // Base query
        String query = "SELECT * FROM claims WHERE 1=1";

        // Dynamically build the query
        switch (status){
            case "NEW":
                query += " AND \"status\" = 'NEW'";
                break;
            case "PROCESSING":
                query += " AND \"status\" = 'PROCESSING'";
                break;
            case "APPROVED":
                query += " AND \"status\" = 'APPROVED'";
                break;
            case "REJECTED":
                query += " AND \"status\" = 'REJECTED'";
                break;
            default:
                throw new IllegalArgumentException("Invalid status: " + status);
        }

        boolean hasPolicyHolderName = policyHolderName != null && !policyHolderName.isEmpty();
        if (hasPolicyHolderName) {
            query += " AND \"policyHolder_name\" = ?";
        }
        // Debug: Print the final query
        System.out.println("Final query: " + query);

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set the parameter for policyHolderName if it is present
            if (hasPolicyHolderName) {
                pstmt.setString(1, policyHolderName);
            }
            // Debug: Print the prepared statement parameters
            System.out.println("Parameters:");
            if (hasPolicyHolderName) {
                System.out.println("1: " + policyHolderName);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    filterClaim.add(String.valueOf(new Claim(
                            rs.getString("id"),
                            rs.getDate("claim_date"),
                            rs.getString("insured_person"),
                            rs.getString("card_number"),
                            rs.getDate("exam_date"),
                            rs.getDouble("claim_amount"),
                            ClaimStatus.valueOf(rs.getString("status")),
                            rs.getString("receiver_bank"),
                            rs.getString("receiver_name"),
                            rs.getString("receiver_number"),
                            rs.getString("policyHolder_name")
                    )));
                }
            }
        }
        return filterClaim;
    }

    //Use to load policy holder name in table for function 'Filter Claim' (INSURANCE SURVEYOR)
    public static List<String> selectPolicyHolderName() throws SQLException {
        List<String> policyHolders = new ArrayList<>();
        String query = "SELECT \"policyHolder_name\" FROM claims";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String fullName = rs.getString("policyHolder_name");
                policyHolders.add(fullName);
            }
        }
        return policyHolders;
    }

    //(INSURANCE SURVEYOR) Filter Customer
    public static List<String> selectCustomerEmail() throws SQLException {
        List<String> emails = new ArrayList<>();
        String roleD = "DEPENDENT";
        String rolePO = "POLICY OWNER";
        String rolePH = "POLICY HOLDER";

        // Base query
        String query = "SELECT email FROM users WHERE 1=1";

        // Add condition based on the role
        if ("DEPENDENT".equalsIgnoreCase(roleD)) {
            query += " AND email LIKE 'd%'";
        }else if("POLICY OWNER".equalsIgnoreCase(rolePO)){
            query += " AND email LIKE 'o%'";
        }else if("POLICY HOLDER".equalsIgnoreCase(rolePH)){
            query += " AND email LIKE 'h%'";
        }else{
            throw new IllegalArgumentException("Invalid email");
        }

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String email = rs.getString("email");
                emails.add(email);
            }
        }
        return emails;
    }

    public static List<String> selectCustomerFullName() throws SQLException {
        List<String> fullName = new ArrayList<>();
        String roleD = "DEPENDENT";
        String rolePO = "POLICY OWNER";
        String rolePH = "POLICY HOLDER";

        // Base query
        String query = "SELECT full_name FROM users WHERE 1=1";

        // Add condition based on the role
        if ("DEPENDENT".equalsIgnoreCase(roleD)) {
            query += " AND role = 'DEPENDENT' ";
        }else if("POLICY OWNER".equalsIgnoreCase(rolePO)){
            query += " AND role = 'POLICY OWNER' ";
        }else if("POLICY HOLDER".equalsIgnoreCase(rolePH)){
            query += " AND role = 'POLICY HOLDER' ";
        }else{
            throw new IllegalArgumentException("Invalid email");
        }

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String full_name = rs.getString("full_name");
                fullName.add(full_name);
            }
        }
        return fullName;
    }

    public static List<String> surveyorGetFilterCustomer(String role, String email , String fullName) throws SQLException {
        List<String> filterCustomer = new ArrayList<>();

        // Base query
        String query = "SELECT * FROM users WHERE 1=1";

        boolean hasRole = role != null && !role.isEmpty();
        if (hasRole) {
            query += " AND \"role\" = ?";
        }

        boolean hasEmail = email != null && !email.isEmpty();
        if (hasEmail) {
            query += " AND \"email\" = ?";
        }

        boolean hasFullName = fullName != null && !fullName.isEmpty();
        if (hasFullName) {
            query += " AND \"full_name\" = ?";
        }

        // Debug: Print the final query
        System.out.println("Final query: " + query);

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set the parameters if they are present
            if (hasRole) {
                pstmt.setString(1, role);
            }
            if (hasEmail) {
                pstmt.setString(2, email);
            }
            if (hasFullName) {
                pstmt.setString(3, fullName);
            }

            // Debug: Print the prepared statement parameters
            System.out.println("Parameters:");
            if (hasRole) {
                System.out.println("1: " + role);
            }
            if (hasEmail) {
                System.out.println("2: " + email);
            }
            if (hasFullName) {
                System.out.println("3: " + fullName);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    filterCustomer.add(String.valueOf(new User(
                            rs.getString("id"),
                            rs.getString("full_name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            UserRole.valueOf(rs.getString("role"))
                    )));
                }
            }
        }
        return filterCustomer;
    }
    public static void updateIC(String cardNumber, LocalDate expireDate){
        String query = "UPDATE \"InsuranceCard\" SET expire_date = ? WHERE card_number = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDate(1, java.sql.Date.valueOf(expireDate));
            pstmt.setString(2, cardNumber);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
