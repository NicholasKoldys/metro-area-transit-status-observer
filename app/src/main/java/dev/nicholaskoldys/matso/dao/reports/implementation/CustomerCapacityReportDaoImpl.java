package dev.nicholaskoldys.matso.dao.reports.implementation;

import dev.nicholaskoldys.matso.dao.reports.DaoReportBase;
import dev.nicholaskoldys.matso.database.DBConnection;
import dev.nicholaskoldys.matso.model.reports.CustomerCapacityReport;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static dev.nicholaskoldys.matso.utility.Constants.TableConstants.*;

public class CustomerCapacityReportDaoImpl implements DaoReportBase<CustomerCapacityReport> {

    public static final String ROUTE_SUMCAPACITY = "ROUTE_CAPACITY";

    private final String SELECT_ROUTES_TOTAL_CAPACITY_WITH_CUSTOMERSAVG =
            "SELECT " + ROUTE_NUM + ", "
                    + ROUTE_CUSTOMERSAVG + ", "
                    + "SUM(" + BUS_CAPACITY + ")" + " AS " + ROUTE_SUMCAPACITY
            + " FROM " + TABLE_ROUTES + " LEFT OUTER JOIN " + TABLE_BUSES
                    + " ON " + TABLE_ROUTES + "." + ROUTE_ID
                    + " = " + TABLE_BUSES + "." + ROUTE_ID
            + " WHERE " + BUS_ACTIVE + " = 1"
            + " GROUP BY " + ROUTE_NUM + ", " + ROUTE_CUSTOMERSAVG;


    private final String SELECT_ROUTES_TOTAL_CAPACITY_LESSORGREATER_CUSTOMERAVG =
            "SELECT " + ROUTE_NUM + ", "
                    + ROUTE_CUSTOMERSAVG + ", "
                    + "SUM(" + BUS_CAPACITY + ")" + " AS " + ROUTE_SUMCAPACITY
            + " FROM " + TABLE_ROUTES + " INNER JOIN " + TABLE_BUSES
                    + " ON " + TABLE_ROUTES + "." + ROUTE_ID
                        + " = " + TABLE_BUSES + "." + ROUTE_ID
            + " WHERE " + BUS_ACTIVE + " = 1 "
            + " GROUP BY " + ROUTE_NUM + ", " + ROUTE_CUSTOMERSAVG
            + " HAVING " + "SUM(" + BUS_CAPACITY + ")" + " ? " + ROUTE_CUSTOMERSAVG;


    private static CustomerCapacityReportDaoImpl INSTANCE = null;


    private CustomerCapacityReportDaoImpl() {}

    /**
     *
     * @return
     */
    public static CustomerCapacityReportDaoImpl getInstance() {

        if (INSTANCE == null) {
            synchronized (CustomerCapacityReportDaoImpl.class) {
                if(INSTANCE == null) {
                    INSTANCE = new CustomerCapacityReportDaoImpl();
                }
            }
        }
        if(INSTANCE != null) {
            return INSTANCE;
        } else {
            System.out.println("CustomerCapacityReportDaoImpl failed to create instance.");
            return null;
        }
    }

    @Override
    public List<CustomerCapacityReport> queryReport() {

        List<CustomerCapacityReport> reportList = new ArrayList<>();

        try (PreparedStatement sqlStatement = DBConnection.getConnection().prepareStatement(SELECT_ROUTES_TOTAL_CAPACITY_WITH_CUSTOMERSAVG)) {

            ResultSet results = sqlStatement.executeQuery();

            while(results.next()) {
                CustomerCapacityReport route = new CustomerCapacityReport(
                        results.getString(ROUTE_NUM),
                        results.getInt(ROUTE_CUSTOMERSAVG),
                        results.getInt(ROUTE_SUMCAPACITY)
                );
                reportList.add(route);
            }
            return reportList;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("An error occurred while attempting to access the database with the SQL statement: " + SELECT_ROUTES_TOTAL_CAPACITY_WITH_CUSTOMERSAVG);
        }
        return reportList;
    }


    public List<CustomerCapacityReport> queryLessThanReport() {

        List<CustomerCapacityReport> reportList = new ArrayList<>();

        try (PreparedStatement sqlStatement = DBConnection.getConnection().prepareStatement(SELECT_ROUTES_TOTAL_CAPACITY_LESSORGREATER_CUSTOMERAVG)) {

            sqlStatement.setString(1, "<");
            ResultSet results = sqlStatement.executeQuery();

            while(results.next()) {
                CustomerCapacityReport route = new CustomerCapacityReport(
                        results.getString(ROUTE_NUM),
                        results.getInt(ROUTE_CUSTOMERSAVG),
                        results.getInt(ROUTE_SUMCAPACITY)
                );
                reportList.add(route);
            }
            return reportList;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("An error occurred while attempting to access the database with the SQL statement: " + SELECT_ROUTES_TOTAL_CAPACITY_LESSORGREATER_CUSTOMERAVG);
        }
        return reportList;
    }

    public List<CustomerCapacityReport> queryGreaterThanOrEqualReport() {

        List<CustomerCapacityReport> reportList = new ArrayList<>();
        try (PreparedStatement sqlStatement = DBConnection.getConnection().prepareStatement(SELECT_ROUTES_TOTAL_CAPACITY_LESSORGREATER_CUSTOMERAVG)) {

            sqlStatement.setString(1, ">=");
            ResultSet results = sqlStatement.executeQuery();

            while(results.next()) {
                CustomerCapacityReport route = new CustomerCapacityReport(
                        results.getString(ROUTE_NUM),
                        results.getInt(ROUTE_CUSTOMERSAVG),
                        results.getInt(ROUTE_SUMCAPACITY)
                );
                reportList.add(route);
            }
            return reportList;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("An error occurred while attempting to access the database with the SQL statement: " + SELECT_ROUTES_TOTAL_CAPACITY_LESSORGREATER_CUSTOMERAVG);
        }
        return reportList;
    }
}
