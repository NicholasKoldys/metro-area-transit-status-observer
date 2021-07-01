package dev.nicholaskoldys.matso.dao.implementation;

import dev.nicholaskoldys.matso.dao.BusDao;
import dev.nicholaskoldys.matso.database.DBConnection;
import dev.nicholaskoldys.matso.database.Repository;
import dev.nicholaskoldys.matso.utility.Constants;
import dev.nicholaskoldys.matso.utility.DateTimeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import dev.nicholaskoldys.matso.model.Bus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static dev.nicholaskoldys.matso.utility.Constants.TableConstants.*;

public class BusDaoImpl implements BusDao {

    private final String SELECT_BUS_BY_ID =
            "SELECT " + BUS_ID + ", "
                    + BUS_NUM + ", "
                    + BUS_CAPACITY + ", "
                    + TABLE_BUSES + "." + BUS_ROUTEID + ", "
                    + BUS_MAINTENANCESTATE + ", "
                    + BUS_LASTBREAKDOWN + ", "
                    + ROUTE_NUM
            + " FROM " + TABLE_BUSES + " LEFT OUTER JOIN " + TABLE_ROUTES
                + " ON " + TABLE_BUSES + "." + BUS_ROUTEID
                    + " = " + TABLE_ROUTES + "." + ROUTE_ID
            + " WHERE " + BUS_ID + " = ?" //ACCEPTS BUS_ID
                  //ONLY ABLE TO SELECT ACTIVE BUSES
                + " AND " + BUS_ACTIVE + " = 1";


    private final String SELECT_ALL_BUSES =
            "SELECT " + BUS_ID + ", "
                    + BUS_NUM + ", "
                    + BUS_CAPACITY + ", "
                    + TABLE_BUSES + "." + BUS_ROUTEID + ", "
                    + BUS_MAINTENANCESTATE + ", "
                    + BUS_LASTBREAKDOWN + ", "
                    + ROUTE_NUM
            + " FROM " + TABLE_BUSES + " LEFT OUTER JOIN " + TABLE_ROUTES
                + " ON " + TABLE_BUSES + "." + BUS_ROUTEID
                    + " = " + TABLE_ROUTES + "." + ROUTE_ID
                    //ONLY ABLE SELECT ACTIVE BUSES
            + " WHERE " + BUS_ACTIVE + " = 1";



    private final String SELECT_ALL_BUSES_REPORT =
            "SELECT " + TABLE_BUSES + ".*, " + ROUTE_NUM
            + " FROM " + TABLE_BUSES + " LEFT OUTER JOIN " + TABLE_ROUTES
            + " ON " + TABLE_BUSES + "." + BUS_ROUTEID
                    + " = " + TABLE_ROUTES + "." + ROUTE_ID;


    // userId, userName, password, active, loggedIn, lastLogin, attempt, createDate, createdBy, lastUpdate, lastUpdateBy
    private final String INSERT_BUS =
            "INSERT INTO " + TABLE_BUSES
                    + " (" + BUS_NUM
                    + ", " + BUS_ACTIVE
                    + ", " + BUS_CAPACITY
                    + ", " + BUS_ROUTEID
                    + ", " + BUS_MAINTENANCESTATE
                    + ", " + BUS_LASTBREAKDOWN
                    + ", " + CREATE_DATE
                    + ", " + CREATED_BY
                    + ", " + LAST_UPDATE
                    + ", " + LAST_UPDATE_BY + ") "
            + " VALUES ("
                    + " ?," //busNum
                    + " 1," //active
                    + " ?," //capacity
                    + " ?," //routeId
                    + " ?," //maintenanceState
                    + " ?," //lastBreakdown
                    + Constants.EnvironmentVariables.CURRENT_TIMESTAMP_METHOD + ", ?, " + Constants.EnvironmentVariables.CURRENT_TIMESTAMP_METHOD + ", ?)"; //Create + lastUpdate //ACCEPTS CURR_USER_NAME


    private final String UPDATE_BUS =
            "UPDATE " + TABLE_BUSES
             + " SET "
                    + BUS_NUM + " = ?, " //ACCEPTS BUS_NUM
                    + BUS_CAPACITY + " = ?, " //ACCEPTS BUS_CAPACITY
                    + BUS_ROUTEID + " = ?, " //ACCEPTS BUS_ROUTEID
                    + BUS_MAINTENANCESTATE + " = ?, " //ACCEPTS BUS_MAINTENANCESTATE
                    + BUS_LASTBREAKDOWN + " =?, " //ACCEPTS BUS_LASTBREAKDOWN
                    + LAST_UPDATE + " = " + Constants.EnvironmentVariables.CURRENT_TIMESTAMP_METHOD + ", "
                    + LAST_UPDATE_BY + " = ?" //ACCEPTS CURR_USER_NAME
             + " WHERE " + BUS_ID + "= ?"; //ACCEPTS BUS_ID


    /**
     * DELETE USER does not remove the user but sets the ACTIVE status to 0
     */
    private final String DELETE_BUS =
            "UPDATE " + TABLE_BUSES
            + " SET "
                    + BUS_ACTIVE + " = 0, "
                    + LAST_UPDATE + " = " + Constants.EnvironmentVariables.CURRENT_TIMESTAMP_METHOD + ", "
                    + LAST_UPDATE_BY + " = ?" //ACCEPTS CURR_USER_NAME
            + " WHERE " + BUS_ID + " = ?"; //ACCEPTS BUS_ID



    private static BusDaoImpl INSTANCE = null;


    private BusDaoImpl() { }


    /**
     *
     * @return
     */
    public static BusDaoImpl getInstance() {

        if (INSTANCE == null) {
            synchronized (BusDaoImpl.class) {
                if(INSTANCE == null) {
                    INSTANCE = new BusDaoImpl();
                }
            }
        }
        if(INSTANCE != null) {
            return INSTANCE;
        } else {
            System.out.println("BusDaoImpl failed to create instance.");
            return null;
        }
    }

    @Override
    public Bus getById(int lookupBusId) {
        try (PreparedStatement sqlStatement = DBConnection.getConnection().prepareStatement(SELECT_BUS_BY_ID)) {

            sqlStatement.setInt(1, lookupBusId);
            ResultSet results = sqlStatement.executeQuery();

            if(results.next()) {

                Timestamp lastBreakdownStamp = results.getTimestamp(BUS_LASTBREAKDOWN);

                LocalDateTime lastBreakdownLocal = null;
                if (lastBreakdownStamp != null) {
                    lastBreakdownLocal = DateTimeService.fromUTC(lastBreakdownStamp.toLocalDateTime());
                }


                Bus bus = new Bus(
                        results.getInt(BUS_ID),
                        results.getString(BUS_NUM),
                        results.getInt(BUS_CAPACITY),
                        results.getInt(BUS_ROUTEID),
                        results.getString(ROUTE_NUM),
                        Constants.MaintenanceState.valueOf(results.getString(BUS_MAINTENANCESTATE)),
                        lastBreakdownLocal
                );
                return bus;
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("An error occurred while attempting to access the database with the SQL statement: " + SELECT_ALL_BUSES);
        }
        return null;
    }

    @Override
    public List<Bus> getAll() {

        List<Bus> busList = new ArrayList<>();
        /* SQL used in try blocks are automatically closed/cleaned/garbage collected by java */
        try (PreparedStatement sqlStatement = DBConnection.getConnection().prepareStatement(SELECT_ALL_BUSES);
             ResultSet results = sqlStatement.executeQuery()) {

            while (results.next()) {
                Timestamp lastBreakdownStamp = results.getTimestamp(BUS_LASTBREAKDOWN);

                LocalDateTime lastBreakdownLocal = null;
                if (lastBreakdownStamp != null) {
                    lastBreakdownLocal = DateTimeService.fromUTC(lastBreakdownStamp.toLocalDateTime());
                }

                Bus bus = new Bus(
                        results.getInt(BUS_ID),
                        results.getString(BUS_NUM),
                        results.getInt(BUS_CAPACITY),
                        results.getInt(BUS_ROUTEID),
                        results.getString(ROUTE_NUM),
                        Constants.MaintenanceState.valueOf(results.getString(BUS_MAINTENANCESTATE)),
                        lastBreakdownLocal
                );
                busList.add(bus);
            }
            return busList;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("An error occurred while attempting to access the database with the SQL statement: " + SELECT_ALL_BUSES);
        }
        return busList;
    }

    @Override
    public ObservableList<Bus> reportAll() {

        ObservableList<Bus> busObservableList = FXCollections.observableArrayList();
        try (PreparedStatement sqlStatement = DBConnection.getConnection().prepareStatement(SELECT_ALL_BUSES_REPORT);
             ResultSet results = sqlStatement.executeQuery()) {

            while (results.next()) {

                Timestamp lastBreakdownStamp = results.getTimestamp(BUS_LASTBREAKDOWN);
                LocalDateTime lastBreakdownLocal = null;
                if (lastBreakdownStamp != null) {
                    lastBreakdownLocal = DateTimeService.fromUTC(lastBreakdownStamp.toLocalDateTime());
                }
                Timestamp createDateStamp = results.getTimestamp(CREATE_DATE);
                LocalDateTime createDateLocal = null;
                if (createDateStamp != null) {
                    createDateLocal = DateTimeService.fromUTC(createDateStamp.toLocalDateTime());
                }
                Timestamp updateDateStamp = results.getTimestamp(LAST_UPDATE);
                LocalDateTime updateDateLocal = null;
                if (updateDateStamp != null) {
                    updateDateLocal = DateTimeService.fromUTC(updateDateStamp.toLocalDateTime());
                }

                Bus bus = new Bus(
                        results.getInt(BUS_ID),
                        results.getString(BUS_NUM),
                        results.getInt(BUS_CAPACITY),
                        results.getInt(BUS_ROUTEID),
                        results.getString(ROUTE_NUM),
                        Constants.MaintenanceState.valueOf(results.getString(BUS_MAINTENANCESTATE)),
                        lastBreakdownLocal,
                        results.getInt(BUS_ACTIVE),
                        createDateLocal,
                        results.getString(CREATED_BY),
                        updateDateLocal,
                        results.getString(LAST_UPDATE_BY)
                );
                busObservableList.add(bus);
            }
            return busObservableList;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("An error occurred while attempting to access the database with the SQL statement: " + SELECT_ALL_BUSES_REPORT);
        }
        return busObservableList;
    }

    @Override
    public boolean insert(Bus item) {

        try (PreparedStatement sqlStatement = DBConnection.getConnection().prepareStatement(INSERT_BUS)) {

            sqlStatement.setString(1, item.getBusNum());
            //active status set in statement
            sqlStatement.setInt(2, item.getCapacity());
            //insert Null if 0, int cant be null - does not translate to sql
            if (item.getRouteId() == 0) {
                sqlStatement.setNull(3, java.sql.Types.INTEGER);
            } else {
                sqlStatement.setInt(3, item.getRouteId());
            }
            //convert String to Constant Enum
            sqlStatement.setString(4, item.getMaintenanceState().toString());
            if(item.getLastBreakdown() == null) {
                sqlStatement.setNull(5, java.sql.Types.TIMESTAMP);
            } else {
                sqlStatement.setTimestamp(5, DateTimeService.toStampUTC(item.getLastBreakdown()));
            }
            //createDate changed in constant's statement
            //lastUpdate changed in constant's statement
            if(Repository.getInstance().getCurrentUser() != null) {
                sqlStatement.setString(6, Repository.getInstance().getCurrentUser().getUserName());
                sqlStatement.setString(7, Repository.getInstance().getCurrentUser().getUserName());
            } else {
                sqlStatement.setString(6, "ADMIN");
                sqlStatement.setString(7, "ADMIN");
            }

            if (sqlStatement.executeUpdate() == 1) {
                return true;
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("An error occurred while attempting to access the database with the SQL statement: " + INSERT_BUS);
        }
        return false;
    }

    @Override
    public boolean insertAll(List<Bus> items) {
        // not used yet
        System.out.println("InsertAll Buses - is an empty method");
        return false;
    }

    @Override
    public boolean update(Bus item) {
        try (PreparedStatement sqlStatement = DBConnection.getConnection().prepareStatement(UPDATE_BUS)) {

            sqlStatement.setString(1, item.getBusNum());
            sqlStatement.setInt(2, item.getCapacity());

            //insert Null if 0, int cant be null - does not translate to sql
            if (item.getRouteId() == 0) {
                sqlStatement.setNull(3, java.sql.Types.INTEGER);
            } else {
                sqlStatement.setInt(3, item.getRouteId());
            }

            //convert String to Constant Enum
            sqlStatement.setString(4, item.getMaintenanceState().toString());
            if(item.getLastBreakdown() == null) {
                sqlStatement.setNull(5, java.sql.Types.TIMESTAMP);
            } else {
                sqlStatement.setTimestamp(5, DateTimeService.toStampUTC(item.getLastBreakdown()));
            }

            //lastUpdate changed in constant's statement
            if(Repository.getInstance().getCurrentUser() != null) {
                sqlStatement.setString(6, Repository.getInstance().getCurrentUser().getUserName());
            } else {
                sqlStatement.setString(6, "ADMIN");
            }

            sqlStatement.setInt(7, item.getBusId());

            if (sqlStatement.executeUpdate() == 1) {
                return true;
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("An error occurred while attempting to access the database with the SQL statement: " + UPDATE_BUS);
        }
        return false;
    }

    @Override
    public boolean delete(Bus item) {
        try (PreparedStatement sqlStatement = DBConnection.getConnection().prepareStatement(DELETE_BUS)) {

            //active changed in statement
            //lastUpdate changed in constant's statement
            if(Repository.getInstance().getCurrentUser() != null) {
                sqlStatement.setString(1, Repository.getInstance().getCurrentUser().getUserName());
            } else {
                sqlStatement.setString(1, "ADMIN");
            }
            sqlStatement.setInt(2, item.getBusId());

            if (sqlStatement.executeUpdate() == 1) {
                return true;
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("An error occurred while attempting to access the database with the SQL statement: " + DELETE_BUS);
        }
        return false;
    }
}
