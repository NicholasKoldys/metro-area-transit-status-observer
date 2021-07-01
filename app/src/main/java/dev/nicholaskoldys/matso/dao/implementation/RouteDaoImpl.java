package dev.nicholaskoldys.matso.dao.implementation;

import dev.nicholaskoldys.matso.dao.RouteDao;
import dev.nicholaskoldys.matso.database.DBConnection;
import dev.nicholaskoldys.matso.database.Repository;
import dev.nicholaskoldys.matso.model.BusChart;
import dev.nicholaskoldys.matso.model.Route;
import dev.nicholaskoldys.matso.utility.Constants;
import dev.nicholaskoldys.matso.utility.DateTimeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static dev.nicholaskoldys.matso.utility.Constants.TableConstants.*;

public class RouteDaoImpl implements RouteDao {

    private final String SELECT_ROUTE_BY_ID =
            "SELECT " + ROUTE_ID + ", "
                    + ROUTE_NUM + ", "
                    + ROUTE_STARTPOINT + ", "
                    + ROUTE_ENDPOINT + ", "
                    + ROUTE_STOPSTOTAL + ", "
                    + ROUTE_TIMELENGTH + ", "
                    + ROUTE_DISTANCE + ", "
                    + ROUTE_CURRENTDELAY + ", "
                    + ROUTE_CUSTOMERSAVG
                    + " FROM " + TABLE_ROUTES
                    + " WHERE " + ROUTE_ID + " = ?"; //ACCEPTS ROUTE_ID


    private final String SELECT_ALL_ROUTES =
            "SELECT " + ROUTE_ID + ", "
                    + ROUTE_NUM + ", "
                    + ROUTE_STARTPOINT + ", "
                    + ROUTE_ENDPOINT + ", "
                    + ROUTE_STOPSTOTAL + ", "
                    + ROUTE_TIMELENGTH + ", "
                    + ROUTE_DISTANCE + ", "
                    + ROUTE_CURRENTDELAY + ", "
                    + ROUTE_CUSTOMERSAVG
                    + " FROM " + TABLE_ROUTES;


    private final String SELECT_ALL_ROUTES_REPORT =
            "SELECT * FROM " + TABLE_ROUTES;


    private final String INSERT_ROUTE =
            "INSERT INTO " + TABLE_ROUTES
                    + " (" + ROUTE_NUM
                    + ", " + ROUTE_STARTPOINT
                    + ", " + ROUTE_ENDPOINT
                    + ", " + ROUTE_STOPSTOTAL
                    + ", " + ROUTE_TIMELENGTH
                    + ", " + ROUTE_DISTANCE
                    + ", " + ROUTE_CURRENTDELAY
                    + ", " + ROUTE_CUSTOMERSAVG
                    + ", " + CREATE_DATE
                    + ", " + CREATED_BY
                    + ", " + LAST_UPDATE
                    + ", " + LAST_UPDATE_BY + ") "
                    + " VALUES ("
                    + " ?," //routeNum 1
                    + " ?," //startPoint 2
                    + " ?," //endPoint 3
                    + " ?," //stopsTotal 4
                    + " ?," //timeLength 5
                    + " ?," //distance 6
                    + " ?," //currentDelay 7
                    + " ?," //customersAvg 8
                    + " " + Constants.EnvironmentVariables.CURRENT_TIMESTAMP_METHOD + ", ?, " + Constants.EnvironmentVariables.CURRENT_TIMESTAMP_METHOD + ", ?)"; //Create + lastUpdate //ACCEPTS CURR_USER_NAME 9+10


    private final String UPDATE_ROUTE =
            "UPDATE " + TABLE_ROUTES
                    + " SET "
                    + ROUTE_NUM + " = ?, " //1
                    + ROUTE_STARTPOINT + " = ?, " //2
                    + ROUTE_ENDPOINT + " = ?, " //3
                    + ROUTE_STOPSTOTAL + " = ?, " //4
                    + ROUTE_TIMELENGTH + " = ?, " //5
                    + ROUTE_DISTANCE + " = ?, " //6
                    + ROUTE_CURRENTDELAY + " = ?, " //7
                    + ROUTE_CUSTOMERSAVG + " = ?, " //8
                    + LAST_UPDATE + " = " + Constants.EnvironmentVariables.CURRENT_TIMESTAMP_METHOD + ", "
                    + LAST_UPDATE_BY + " = ?" //ACCEPTS CURR_USER_NAME 9
                    + " WHERE " + ROUTE_ID + " = ?"; //ACCEPTS ROUTE_ID 10


    public final String SELECT_ALL_CHART_REPORT =
            "SELECT " + TABLE_BUSCHART + "." + ROUTE_ID + " AS chartRouteId, "
                    + TABLE_ROUTES + "." + ROUTE_NUM + " AS routesRouteNum, "
                    + TABLE_BUSCHART + "." + BUS_ID + " AS chartBusId, "
                    + TABLE_BUSES + "." + BUS_NUM + " AS busesBusNum"
            + " FROM " + TABLE_BUSCHART + ", " + TABLE_ROUTES + ", " + TABLE_BUSES
            + " WHERE " + TABLE_BUSCHART + "." + ROUTE_ID + " IS NOT NULL"
                    + " AND " + TABLE_BUSCHART + "." + BUS_ID + " IS NOT NULL"
                    + " AND " + TABLE_BUSCHART + "." + BUS_ID + " = "
                                + TABLE_BUSES + "." + BUS_ID
                    + " AND " + TABLE_BUSCHART + "." + ROUTE_ID + " = "
                                + TABLE_ROUTES + "." + ROUTE_ID;


    private static RouteDaoImpl INSTANCE = null;


    private RouteDaoImpl() { }


    /**
     *
     * @return
     */
    public static RouteDaoImpl getInstance() {

        if (INSTANCE == null) {
            synchronized (RouteDaoImpl.class) {
                if(INSTANCE == null) {
                    INSTANCE = new RouteDaoImpl();
                }
            }
        }

        if(INSTANCE != null) {
            return INSTANCE;
        } else {
            System.out.println("RouteDaoImpl failed to create instance.");
            return null;
        }
    }

    @Override
    public Route getById(int routeId) {

        try (PreparedStatement sqlStatement = DBConnection.getConnection().prepareStatement(SELECT_ROUTE_BY_ID)) {

            sqlStatement.setInt(1, routeId);
            ResultSet results = sqlStatement.executeQuery();

            results.next();
            Route route = new Route(
                    results.getInt(ROUTE_ID),
                    results.getString(ROUTE_NUM),
                    results.getString(ROUTE_STARTPOINT),
                    results.getString(ROUTE_ENDPOINT),
                    results.getInt(ROUTE_STOPSTOTAL),
                    results.getTime(ROUTE_TIMELENGTH).toLocalTime(),
                    results.getFloat(ROUTE_DISTANCE),
                    results.getTime(ROUTE_CURRENTDELAY).toLocalTime(),
                    results.getInt(ROUTE_CUSTOMERSAVG)
            );
            return route;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("An error occurred while attempting to access the database with the SQL statement: " + SELECT_ROUTE_BY_ID);
        }
        return null;
    }

    @Override
    public List<Route> getAll() {

        List<Route> routeList = new ArrayList<>();
        /* SQL used in try blocks are automatically closed/cleaned/garbage collected by java */
        try (PreparedStatement sqlStatement = DBConnection.getConnection().prepareStatement(SELECT_ALL_ROUTES);
             ResultSet results = sqlStatement.executeQuery()) {

            while (results.next()) {

                Route route = new Route(
                        results.getInt(ROUTE_ID),
                        results.getString(ROUTE_NUM),
                        results.getString(ROUTE_STARTPOINT),
                        results.getString(ROUTE_ENDPOINT),
                        results.getInt(ROUTE_STOPSTOTAL),
                        results.getTime(ROUTE_TIMELENGTH).toLocalTime(),
                        results.getFloat(ROUTE_DISTANCE),
                        results.getTime(ROUTE_CURRENTDELAY).toLocalTime(),
                        results.getInt(ROUTE_CUSTOMERSAVG)
                );
                routeList.add(route);
            }

            return routeList;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("An error occurred while attempting to access the database with the SQL statement: " + SELECT_ALL_ROUTES);
        }
        return routeList;
    }

    @Override
    public ObservableList<Route> reportAll() {

        ObservableList<Route> routeObservableList = FXCollections.observableArrayList();
        try (PreparedStatement sqlStatement = DBConnection.getConnection().prepareStatement(SELECT_ALL_ROUTES_REPORT);
             ResultSet results = sqlStatement.executeQuery()) {

            while (results.next()) {

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

                Route route = new Route(
                        results.getInt(ROUTE_ID),
                        results.getString(ROUTE_NUM),
                        results.getString(ROUTE_STARTPOINT),
                        results.getString(ROUTE_ENDPOINT),
                        results.getInt(ROUTE_STOPSTOTAL),
                        results.getTime(ROUTE_TIMELENGTH).toLocalTime(),
                        results.getFloat(ROUTE_DISTANCE),
                        results.getTime(ROUTE_CURRENTDELAY).toLocalTime(),
                        results.getInt(ROUTE_CUSTOMERSAVG),
                        createDateLocal,
                        results.getString(CREATED_BY),
                        updateDateLocal,
                        results.getString(LAST_UPDATE_BY)
                );
                routeObservableList.add(route);
            }
            return routeObservableList;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("An error occurred while attempting to access the database with the SQL statement: " + SELECT_ALL_ROUTES_REPORT);
        }
        return routeObservableList;
    }

    @Override
    public ObservableList<BusChart> getChart() {

        ObservableList<BusChart> busChartObservableList = FXCollections.observableArrayList();
        try (PreparedStatement sqlStatement = DBConnection.getConnection().prepareStatement(SELECT_ALL_CHART_REPORT);
             ResultSet results = sqlStatement.executeQuery()) {

            while (results.next()) {

                BusChart chart = new BusChart(
                        results.getInt("chartRouteId"),
                        results.getString("routesRouteNum"),
                        results.getInt("chartBusId"),
                        results.getString("busesBusNum")
                );
                busChartObservableList.add(chart);
            }
            return busChartObservableList;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("An error occurred while attempting to access the database with the SQL statement: " + SELECT_ALL_CHART_REPORT);
        }
        return busChartObservableList;
    }

    @Override
    public boolean insert(Route item) {

        try (PreparedStatement sqlStatement = DBConnection.getConnection().prepareStatement(INSERT_ROUTE)) {

            sqlStatement.setString(1, item.getRouteNum());
            sqlStatement.setString(2, item.getStartPoint());
            sqlStatement.setString(3, item.getEndPoint());
            sqlStatement.setInt(4, item.getStopsTotal());
            sqlStatement.setTime(5, java.sql.Time.valueOf(item.getTimeLength()));
            sqlStatement.setFloat(6, item.getDistance());
            sqlStatement.setTime(7, java.sql.Time.valueOf(item.getCurrentDelay()));
            sqlStatement.setInt(8, item.getCustomersAvg());
            //createDate changed in constant's statement
            // lastUpdate changed in constant's statement
            if(Repository.getInstance().getCurrentUser() != null) {
                sqlStatement.setString(9, Repository.getInstance().getCurrentUser().getUserName());
                sqlStatement.setString(10, Repository.getInstance().getCurrentUser().getUserName());
            } else {
                sqlStatement.setString(9, "ADMIN");
                sqlStatement.setString(10, "ADMIN");
            }

            if (sqlStatement.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("An error occurred while attempting to access the database with the SQL statement: " + INSERT_ROUTE);
        }
        return false;
    }

    @Override
    public boolean insertAll(List<Route> items) {
        //not used
        System.out.println("InsertAll Routes - has not yet been implemented - is an empty method");
        return false;
    }

    @Override
    public boolean update(Route item) {

        try (PreparedStatement sqlStatement = DBConnection.getConnection().prepareStatement(UPDATE_ROUTE)) {

            sqlStatement.setString(1, item.getRouteNum());
            sqlStatement.setString(2, item.getStartPoint());
            sqlStatement.setString(3, item.getEndPoint());
            sqlStatement.setInt(4, item.getStopsTotal());
            sqlStatement.setTime(5, java.sql.Time.valueOf(item.getTimeLength()));
            sqlStatement.setFloat(6, item.getDistance());
            sqlStatement.setTime(7, java.sql.Time.valueOf(item.getCurrentDelay()));
            sqlStatement.setInt(8, item.getCustomersAvg());
            // lastUpdate changed in constant's statement
            if(Repository.getInstance().getCurrentUser() != null) {
                sqlStatement.setString(9, Repository.getInstance().getCurrentUser().getUserName());
            } else {
                sqlStatement.setString(9, "ADMIN");
            }
            sqlStatement.setInt(10, item.getRouteId());

            if (sqlStatement.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("An error occurred while attempting to access the database with the SQL statement: " + UPDATE_ROUTE);
        }
        return false;
    }

    @Override
    public boolean delete(Route item) {
        //NOT USED -- routes never deleted or set inactive
        System.out.println("Delete Route - Should never be used. It is an empty method.");
        return false;
    }
}
