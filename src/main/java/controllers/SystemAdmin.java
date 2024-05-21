package controllers;

import models.User;
import models.UserRole;

public class SystemAdmin extends User {
    public SystemAdmin(String id, String fullName, String email, String password, UserRole role) {
        super(id, fullName, email, password, role);
    }
    // Additional methods specific to System Admins
}
