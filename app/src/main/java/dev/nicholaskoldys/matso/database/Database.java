package dev.nicholaskoldys.matso.database;

import dev.nicholaskoldys.matso.dao.implementation.BusDaoImpl;
import dev.nicholaskoldys.matso.dao.implementation.RouteDaoImpl;
import dev.nicholaskoldys.matso.dao.implementation.UserDaoImpl;
import dev.nicholaskoldys.matso.dao.reports.implementation.CustomerCapacityReportDaoImpl;

public class Database {

    private UserDaoImpl userDao;
    private BusDaoImpl busDao;
    private RouteDaoImpl routeDao;
    private CustomerCapacityReportDaoImpl customerCapacityReportDao;

    private static Database INSTANCE;

    private Database() {
        this.userDao = UserDao();
        this.busDao = BusDao();
        this.routeDao = RouteDao();
        this.customerCapacityReportDao = CustomerCapacityReportDao();
    }

    public static Database getInstance(){
        if (INSTANCE == null) {
            synchronized (DBConnection.class) {
                if(INSTANCE == null) {
                    INSTANCE = new Database();
                }
            }
        }
        if(INSTANCE != null) {
            return INSTANCE;
        } else {
            System.out.println("Database Object failed to be created");
            return null;
        }
    }

    public UserDaoImpl UserDao() {
        if (userDao == null) {
            synchronized (DBConnection.class) {
                if(userDao == null) {
                    userDao = UserDaoImpl.getInstance();
                }
            }
        }
        if(userDao != null) {
            return userDao;
        } else {
            System.out.println("User Database Object failed to be created");
            return null;
        }
    }

    public BusDaoImpl BusDao() {
        if (busDao == null) {
            synchronized (DBConnection.class) {
                if(busDao == null) {
                    busDao = BusDaoImpl.getInstance();
                }
            }
        }
        if(busDao != null) {
            return busDao;
        } else {
            System.out.println("Bus Database Object failed to be created");
            return null;
        }
    }

    public RouteDaoImpl RouteDao(){
        if (routeDao == null) {
            synchronized (DBConnection.class) {
                if(routeDao == null) {
                    routeDao = RouteDaoImpl.getInstance();
                }
            }
        }
        if(routeDao != null) {
            return routeDao;
        } else {
            System.out.println("Route Database Object failed to be created");
            return null;
        }
    }

    public CustomerCapacityReportDaoImpl CustomerCapacityReportDao(){
        if (customerCapacityReportDao == null) {
            synchronized (DBConnection.class) {
                if(customerCapacityReportDao == null) {
                    customerCapacityReportDao = CustomerCapacityReportDaoImpl.getInstance();
                }
            }
        }
        if(customerCapacityReportDao != null) {
            return customerCapacityReportDao;
        } else {
            System.out.println("CustomerCapacityReport Database Object failed to be created");
            return null;
        }
    }
}
