package models;

public class LoginSession {
    private static LoginSession instance;
    private User currentUser;

    public LoginSession() {
        // Private constructor to prevent instantiation
    }

    public static LoginSession getInstance() {
        if (instance == null) {
            instance = new LoginSession();
        }
        return instance;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

}
