package dev.nicholaskoldys.matso.model;

import java.time.LocalDateTime;

/**
 *
 * @author nicho
 */
public class User {


    private int userId;
    private String userName;
    private int loggedIn;
    private LocalDateTime lastLogin;
    private int active;
    private int attempt;
    private LocalDateTime createDate;
    private String createUserName;
    private LocalDateTime lastUpdateBy;
    private String updateUserName;


    public User(int userId, String userName, int loggedIn, LocalDateTime lastLogin) {
        this.userId = userId;
        this.userName = userName;
        this.loggedIn = loggedIn;
        this.lastLogin = lastLogin;
    }

    public User(int userId, String userName, int loggedIn, LocalDateTime lastLogin, int active, int attempt, LocalDateTime createDate, String createUserName, LocalDateTime lastUpdateBy, String updateUserName) {
        this.userId = userId;
        this.userName = userName;
        this.loggedIn = loggedIn;
        this.lastLogin = lastLogin;
        this.active = active;
        this.attempt = attempt;
        this.createDate = createDate;
        this.createUserName = createUserName;
        this.lastUpdateBy = lastUpdateBy;
        this.updateUserName = updateUserName;
    }


    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getLoggedIn() {
        return loggedIn;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public int getActive() {
        return active;
    }

    public int getAttempt() {
        return attempt;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public LocalDateTime getLastUpdateBy() {
        return lastUpdateBy;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }
}
