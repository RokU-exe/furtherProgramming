package models;

import java.util.Date;

public class ActionHistory {
    private String id;
    private String userId;
    private String action;
    private Date actionDate;

    // Constructors, getters, and setters
    public ActionHistory(String id, String userId, String action, Date actionDate) {
        this.id = id;
        this.userId = userId;
        this.action = action;
        this.actionDate = actionDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    @Override
    public String toString() {
        return "models.ActionHistory{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", action='" + action + '\'' +
                ", actionDate=" + actionDate +
                '}';
    }
}
