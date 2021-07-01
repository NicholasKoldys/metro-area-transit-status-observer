package dev.nicholaskoldys.matso.dao.implementation;

import dev.nicholaskoldys.matso.dao.UserDao;
import dev.nicholaskoldys.matso.database.DBConnection;
import dev.nicholaskoldys.matso.database.Repository;
import dev.nicholaskoldys.matso.utility.Constants;
import dev.nicholaskoldys.matso.utility.DateTimeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import dev.nicholaskoldys.matso.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static dev.nicholaskoldys.matso.utility.Constants.TableConstants.*;

public class UserDaoImpl implements UserDao {

    private final String SELECT_USER_BY_NAME =
            "SELECT " + USER_ID + ", "
                    + USER_NAME + ", "
                    + USER_LOGGEDIN + ", "
                    + USER_LASTLOGIN
            + " FROM " + TABLE_USER
            + " WHERE "  + USER_NAME + " = ?" //ACCEPTS USER_NAME
                    //ONLY ABLE TO SELECT ACTIVE USER
                    + " AND " + USER_ACTIVE + " = 1";


    private final String SELECT_USER_BY_ID =
            "SELECT " + USER_ID + ", "
                    + USER_NAME + ", "
                    + USER_LOGGEDIN + ", "
                    + USER_LASTLOGIN
            + " FROM " + TABLE_USER
            + " WHERE "  + USER_ID + " = ?" //ACCEPTS USER_ID
                //ONLY ABLE TO SELECT ACTIVE USER
                + " AND " + USER_ACTIVE + " = 1";


    private final String SELECT_ALL_USERS =
            "SELECT " + USER_ID + ", "
                    + USER_NAME + ", "
                    + USER_LOGGEDIN + ", "
                    + USER_LASTLOGIN
            + " FROM " + TABLE_USER
                    //ONLY ABLE SELECT ACTIVE USERS
            + " WHERE " + USER_ACTIVE + " = 1";


    private final String SELECT_ALL_USERS_REPORT =
            "SELECT * FROM " + TABLE_USER;


    // userId, userName, password, active, loggedIn, lastLogin, attempt, createDate, createdBy, lastUpdate, lastUpdateBy
    private final String INSERT_USER =
            "INSERT INTO " + TABLE_USER
                    + " (" + USER_NAME
                    + ", " + USER_PASSWORD
                    + ", " + USER_ACTIVE
                    + ", " + USER_LOGGEDIN
                    + ", " + USER_LASTLOGIN
                    + ", " + USER_ATTEMPT
                    + ", " + CREATE_DATE
                    + ", " + CREATED_BY
                    + ", " + LAST_UPDATE
                    + ", " + LAST_UPDATE_BY + ") "
                    + " VALUES (?, "
                    + Constants.EnvironmentVariables.ENCRYPTION_METHOD("?") + ", 1, 0, "
                    + Constants.EnvironmentVariables.CURRENT_TIMESTAMP_METHOD + ", 0, "
                    + Constants.EnvironmentVariables.CURRENT_TIMESTAMP_METHOD + ", ?, "
                    + Constants.EnvironmentVariables.CURRENT_TIMESTAMP_METHOD + ", ?)";


    private final String UPDATE_USER_NAME =
            "UPDATE " + TABLE_USER
                    + " SET "
                    + USER_NAME + " = ?, "  //ACCEPTS USER_NAME
                    + LAST_UPDATE + " = " + Constants.EnvironmentVariables.CURRENT_TIMESTAMP_METHOD + ", "
                    + LAST_UPDATE_BY + " = ?"
                    + " WHERE " + USER_ID + "= ?"; //ACCEPTS USER_ID


    private final String UPDATE_USER_PASSWORD =
            "UPDATE " + TABLE_USER
            + " SET "
                    + USER_PASSWORD + " = SHA1(?), " // ACCEPTS USER_PASSWORD
                    + LAST_UPDATE + " = " + Constants.EnvironmentVariables.CURRENT_TIMESTAMP_METHOD + ", "
                    + LAST_UPDATE_BY + " = ?" //ACCEPTS CURR_USER_NAME
            + " WHERE " + USER_ID + "= ?"; //ACCEPTS USER_ID


    /**
     * DELETE USER does not remove the user but sets the ACTIVE status to 0
     */
    private final String DELETE_USER =
            "UPDATE " + TABLE_USER
             + " SET "
                    + USER_ACTIVE + " = 0, "
                    + LAST_UPDATE + " = " + Constants.EnvironmentVariables.CURRENT_TIMESTAMP_METHOD + ", "
                    + LAST_UPDATE_BY + " = ?" //ACCEPTS CURR_USER_NAME
             + " WHERE " + USER_ID + " = ?"; //ACCEPTS USER_ID


    private final String TRY_LOGIN_ATTEMPT =
            "SELECT " + USER_NAME + ", "
                    + USER_PASSWORD
            + " FROM " + TABLE_USER
            + " WHERE "  + USER_NAME + " = ? " //ACCEPTS USER_NAME
                    + " AND " + USER_PASSWORD + " = " + Constants.EnvironmentVariables.ENCRYPTION_METHOD("?") + " " //ACCEPTS USER_PASSWORD
                    //ONLY ABLE SELECT ACTIVE USERS
                    + " AND " + USER_ACTIVE + " = 1 "
                    // ONLY LOGIN IF NOT LOGGED IN
                    + " AND " + USER_LOGGEDIN + " = 0 ";

    private final String SUCCESSFUL_LOGIN =
            "UPDATE " + TABLE_USER
            + " SET " + USER_LOGGEDIN + " = 0, "//changed to 0 for testing
                    + USER_LASTLOGIN + " = " + Constants.EnvironmentVariables.CURRENT_TIMESTAMP_METHOD
            + " WHERE " + USER_NAME + " = ? "
                    + " AND " + USER_PASSWORD + " = " + Constants.EnvironmentVariables.ENCRYPTION_METHOD("?");

    private final String TRY_LOGOUT_ATTEMPT =
            "UPDATE " + TABLE_USER
            + " SET " + USER_LOGGEDIN + " = 0"
            + " WHERE " + USER_ID + " = ?";


    private static UserDaoImpl INSTANCE = null;


    private UserDaoImpl() { }


    /**
     *
     * @return
     */
    public static UserDaoImpl getInstance() {

        if (INSTANCE == null) {
            synchronized (UserDaoImpl.class) {
                if(INSTANCE == null) {
                    INSTANCE = new UserDaoImpl();
                }
            }
        }

        if(INSTANCE != null) {
            return INSTANCE;
        } else {
            System.out.println("UserDaoImpl failed to create instance.");
            return null;
        }
    }

    public User getByName(String name) {
        try (PreparedStatement sqlStatement = DBConnection.getConnection().prepareStatement(SELECT_USER_BY_NAME)) {
            sqlStatement.setString(1, name);
            ResultSet results = sqlStatement.executeQuery();
            results.next();

            Timestamp lastLoginStamp = results.getTimestamp(USER_LASTLOGIN);
            LocalDateTime lastLoginLocal = null;
            if (lastLoginStamp != null) {
                lastLoginLocal = DateTimeService.fromUTC(lastLoginStamp.toLocalDateTime());
            }

            User user = new User(
                    results.getInt(USER_ID),
                    results.getString(USER_NAME),
                    results.getInt(USER_LOGGEDIN),
                    lastLoginLocal
            );
            return user;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("An error occurred while attempting to access the database with the SQL statement: " + SELECT_USER_BY_NAME);
        }
        return null;
    }


    @Override
    public User getById(int userId) {
        try (PreparedStatement sqlStatement = DBConnection.getConnection().prepareStatement(SELECT_USER_BY_ID)) {
            sqlStatement.setInt(1, userId);
            ResultSet results = sqlStatement.executeQuery();
            results.next();

            Timestamp lastLoginStamp = results.getTimestamp(USER_LASTLOGIN);
            LocalDateTime lastLoginLocal = null;
            if (lastLoginStamp != null) {
                lastLoginLocal = DateTimeService.fromUTC(lastLoginStamp.toLocalDateTime());
            }

            User user = new User(
                    results.getInt(USER_ID),
                    results.getString(USER_NAME),
                    results.getInt(USER_LOGGEDIN),
                    lastLoginLocal
            );
            return user;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("An error occurred while attempting to access the database with the SQL statement: " + SELECT_USER_BY_ID);
        }
        return null;
    }


    @Override
    public List<User> getAll() {
        List<User> userList = new ArrayList<>();
        /* SQL used in try blocks are automatically closed/cleaned/garbage collected by java */
        try (PreparedStatement sqlStatement = DBConnection.getConnection().prepareStatement(SELECT_ALL_USERS);
             ResultSet results = sqlStatement.executeQuery()) {

            while (results.next()) {

                Timestamp lastLoginStamp = results.getTimestamp(USER_LASTLOGIN);
                LocalDateTime lastLoginLocal = null;
                if (lastLoginStamp != null) {
                    lastLoginLocal = DateTimeService.fromUTC(lastLoginStamp.toLocalDateTime());
                }

                User user = new User(
                        results.getInt(USER_ID),
                        results.getString(USER_NAME),
                        results.getInt(USER_LOGGEDIN),
                        lastLoginLocal
                );
                userList.add(user);
            }

            return userList;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("An error occurred while attempting to access the database with the SQL statement: " + SELECT_ALL_USERS);
        }
        return null;
    }

    @Override
    public ObservableList<User> reportAll() {
        ObservableList<User> userObservableList = FXCollections.observableArrayList();
        try (PreparedStatement sqlStatement = DBConnection.getConnection().prepareStatement(SELECT_ALL_USERS_REPORT);
             ResultSet results = sqlStatement.executeQuery()) {

            while (results.next()) {

                Timestamp lastLoginStamp = results.getTimestamp(USER_LASTLOGIN);
                LocalDateTime lastLoginLocal = null;
                if (lastLoginStamp != null) {
                    lastLoginLocal = DateTimeService.fromUTC(lastLoginStamp.toLocalDateTime());
                }
                Timestamp createDateStamp = results.getTimestamp(CREATE_DATE);
                LocalDateTime createDateLocal = null;
                if (createDateStamp != null) {
                    createDateLocal = DateTimeService.fromUTC(createDateStamp.toLocalDateTime());
                }
                Timestamp updateStamp = results.getTimestamp(LAST_UPDATE);
                LocalDateTime updateLocal = null;
                if (updateStamp != null) {
                    updateLocal = DateTimeService.fromUTC(updateStamp.toLocalDateTime());
                }

                User user = new User(
                        results.getInt(USER_ID),
                        results.getString(USER_NAME),
                        results.getInt(USER_LOGGEDIN),
                        lastLoginLocal,
                        results.getInt(USER_ACTIVE),
                        results.getInt(USER_ATTEMPT),
                        createDateLocal,
                        results.getString(CREATED_BY),
                        updateLocal,
                        results.getString(LAST_UPDATE_BY)
                );
                userObservableList.add(user);
            }
            return userObservableList;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("An error occurred while attempting to access the database with the SQL statement: " + SELECT_ALL_USERS_REPORT);
        }
        return userObservableList;
    }

    @Override
    public boolean insert(User item) {
        // not in use
        return false;
    }

    @Override
    public boolean insert(String name, String password) {
        //check if user name exists
        try (PreparedStatement sqlStatement = DBConnection.getConnection().prepareStatement(INSERT_USER)) {

            sqlStatement.setString(1, name);
            sqlStatement.setString(2, password);
            // lastUpdate changed in constant's statement
            if(Repository.getInstance().getCurrentUser() != null) {
                sqlStatement.setString(3, Repository.getInstance().getCurrentUser().getUserName());
                sqlStatement.setString(4, Repository.getInstance().getCurrentUser().getUserName());
            } else {
                sqlStatement.setString(3, "ADMIN");
                sqlStatement.setString(4, "ADMIN");
            }

            // Changed to 0 for no added user -- need to find better way
            if (sqlStatement.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("An error occurred while attempting to access the database with the SQL statement: " + INSERT_USER);
        }
        return false;
    }


    @Override
    public boolean insertAll(List<User> items) {
        // not in use
        return false;
    }


    @Override
    public boolean update(User item) {
        // not it use
        return false;
    }


    @Override
    public boolean updateName(User item, String name) {
        // test if name exists already
        try (PreparedStatement sqlStatement = DBConnection.getConnection().prepareStatement(UPDATE_USER_NAME)) {
            sqlStatement.setString(1, name);
            // lastUpdate changed in constant's statement
            if(Repository.getInstance().getCurrentUser() != null) {
                sqlStatement.setString(2, Repository.getInstance().getCurrentUser().getUserName());
            } else {
                sqlStatement.setString(2, "ADMIN");
            }
            sqlStatement.setInt(3, item.getUserId());

            if (sqlStatement.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("An error occurred while attempting to access the database with the SQL statement: " + UPDATE_USER_NAME);
        }
        return false;
    }


    @Override
    public boolean updatePassword(User item, String password) {
        try (PreparedStatement sqlStatement = DBConnection.getConnection().prepareStatement(UPDATE_USER_PASSWORD)) {
            sqlStatement.setString(1, password);
            // lastUpdate changed in constant's statement
            //DateTimeService.toStampUTC(item.getLastBreakdown()));
            if(Repository.getInstance().getCurrentUser() != null) {
                sqlStatement.setString(2, Repository.getInstance().getCurrentUser().getUserName());
            } else {
                sqlStatement.setString(2, "ADMIN");
            }
            sqlStatement.setInt(3, item.getUserId());

            if (sqlStatement.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("An error occurred while attempting to access the database with the SQL statement: " + UPDATE_USER_PASSWORD);
        }
        return false;
    }


    @Override
    public boolean delete(User item) {
        try (PreparedStatement sqlStatement = DBConnection.getConnection().prepareStatement(DELETE_USER)) {
            // active changed in statement
            // lastUpdate changed in constant's statement
            if(Repository.getInstance().getCurrentUser() != null) {
                sqlStatement.setString(1, Repository.getInstance().getCurrentUser().getUserName());
            } else {
                sqlStatement.setString(1, "ADMIN");
            }
            sqlStatement.setInt(2, item.getUserId());

            if (sqlStatement.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("An error occurred while attempting to access the database with the SQL statement: " + DELETE_USER);
        }
        return false;
    }


    @Override
    public boolean loginAttempt(String name, String password) {
        try (PreparedStatement sqlStatement = DBConnection.getConnection().prepareStatement(TRY_LOGIN_ATTEMPT)) {
            sqlStatement.setString(1, name);
            sqlStatement.setString(2, password);
            ResultSet results = sqlStatement.executeQuery();

            if(results.next()) {
                System.out.println("Username and Password are correct.");
                if(loginUser(name, password)) {
                    return true;
                } else {
                    throw new SQLException("ERROR::LOGIN FAILED");
                }
            } else {
                System.out.println("Username and Password are incorrect.");
                return false;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("An error occurred while attempting to access the database with the SQL statement: " + TRY_LOGIN_ATTEMPT);
        }
        return false;
    }

    @Override
    public boolean loginUser(String name, String password) {
        try (PreparedStatement sqlStatement = DBConnection.getConnection().prepareStatement(SUCCESSFUL_LOGIN)) {
            sqlStatement.setString(1, name);
            sqlStatement.setString(2, password);

            if (sqlStatement.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("An error occurred while attempting to access the database with the SQL statement: " + SUCCESSFUL_LOGIN);
        }
        return false;
    }


    @Override
    public boolean logoutAttempt() {
        try (PreparedStatement sqlStatement = DBConnection.getConnection().prepareStatement(TRY_LOGOUT_ATTEMPT)) {
            sqlStatement.setInt(1, Repository.getInstance().getCurrentUser().getUserId());

            if (sqlStatement.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("An error occurred while attempting to access the database with the SQL statement: " + TRY_LOGOUT_ATTEMPT);
        }
        return false;
    }
}
