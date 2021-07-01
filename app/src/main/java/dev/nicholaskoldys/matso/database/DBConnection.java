package dev.nicholaskoldys.matso.database;

// import com.mysql.jdbc.Connection;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    /**
     * CONSTANTS used in the connection methods
     */
    /* private final static String SERVER_NAME = "3.227.166.251";
     public final static String DATABASE_NAME = "U05iV3";
     private final static String DB_URL = "jdbc:mysql://" + SERVER_NAME + "/" + DATABASE_NAME;
     private final static String USERNAME = "U05iV3"; //
     private final static String PASSWORD = "53688515843";
     private final static String DRIVER = "com.mysql.jdbc.Driver";*/
    public final static String DATABASE_NAME = "DB_MATSO";// MUST be all caps
    private final static String HSQL_DB_TYPE = "file:";
    private final static String DB_URL = "jdbc:hsqldb:" + HSQL_DB_TYPE + DATABASE_NAME + "/";
    private final static String USERNAME = "sa";
    private final static String PASSWORD = "";
    private final static String DRIVER = "org.hsqldb.jdbc.JDBCDriver";
    private static Connection connectionInstance = null;

    /**
     *  Method to return Connection made through connectToDatabase method
     */
    public static Connection getConnection() {

        if (connectionInstance == null) {
            synchronized (DBConnection.class) {
                if(connectionInstance == null) {
                    setConnection();
                    return connectionInstance;
                }
            }
        }

        if(connectionInstance != null) {
            try {
                if (connectionInstance.isClosed()) {
                    setConnection();
                }
                return connectionInstance;
            } catch (SQLException ex) {
                ex.printStackTrace();
                System.out.println(ex.getMessage());
            }
        } else {
            System.out.println("DatabaseConnection failed to create instance.");
        }
        return null;
    }


    /**
     *  Method to connect to the database and name the instance for
     *  simple method calls.
     */
    public static void setConnection() {

        try {
            Class.forName(DRIVER);
        } catch (Exception ex) {
            System.out.println(
                    "The class: " + DRIVER + " was not found."
                            + "\nFAILED in class " + MethodHandles.lookup().getClass()
                            + "\n" + ex.getMessage());
        }

        try {
            connectionInstance = (Connection) DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            System.out.println("Database Connection Successful");
        } catch (Exception ex) {
            System.out.println(
                    "URL, Username, and Password : " + DB_URL
                            + "\nFAILED in class " + MethodHandles.lookup().getClass()
                            + "\n" + ex.getMessage());
        }
    }


    /**
     *  Method to remove instance and close the connection to the database.
     */
    public static void disconnect() {

        try {
            connectionInstance.close();
            System.out.println("Database Disconnect Successful");
        } catch (Exception ex) {
            System.out.println(
                    "Unable to disconnect from Database connection."
                            + "\nFAILED in class " + MethodHandles.lookup().getClass()
                            + "\n" + ex.getMessage());
        }
    }
}
