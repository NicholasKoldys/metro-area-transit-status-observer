package dev.nicholaskoldys.matso.database;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import dev.nicholaskoldys.matso.dao.implementation.BusDaoImpl;
import dev.nicholaskoldys.matso.dao.implementation.RouteDaoImpl;
import dev.nicholaskoldys.matso.dao.implementation.UserDaoImpl;
import dev.nicholaskoldys.matso.dao.reports.implementation.CustomerCapacityReportDaoImpl;
import dev.nicholaskoldys.matso.model.Bus;
import dev.nicholaskoldys.matso.model.BusChart;
import dev.nicholaskoldys.matso.model.Route;
import dev.nicholaskoldys.matso.model.User;
import dev.nicholaskoldys.matso.model.reports.CustomerCapacityReport;
import java.util.List;

public class Repository {

    private Database db;
    private UserDaoImpl userDao;
    private BusDaoImpl busDao;
    private RouteDaoImpl routeDao;

    private CustomerCapacityReportDaoImpl customerCapacityReportDao;

    private ObservableList<User> userList = FXCollections.observableArrayList();
    private ObservableList<Bus> busList = FXCollections.observableArrayList();
    private ObservableList<Route> routeList = FXCollections.observableArrayList();

    private ObservableList<CustomerCapacityReport> ccReportList = FXCollections.observableArrayList();

    private ObservableList<User> userReportList = FXCollections.observableArrayList();
    private ObservableList<Bus> busReportList = FXCollections.observableArrayList();
    private ObservableList<Route> routeReportList = FXCollections.observableArrayList();
    private ObservableList<BusChart> busChartReportList = FXCollections.observableArrayList();

    private User currentUser;

    private static Repository INSTANCE;


    /**
     * Repository Stores ObservableLists that have been populated form Database Queries.
     * This Class should be the goto for all application data.
     */
    private Repository() {
        db = Database.getInstance();
        userDao = db.UserDao();
        busDao = db.BusDao();
        routeDao = db.RouteDao();

        customerCapacityReportDao = db.CustomerCapacityReportDao();

        Platform.runLater(() -> {
            userList.setAll(userDao.getAll());
            routeList.setAll(routeDao.getAll());
            busList.setAll(busDao.getAll());
            ccReportList.setAll(customerCapacityReportDao.queryReport());
        });
    }


    /**
     * Allow one instance.
     *
     * @return
     */
    public static Repository getInstance() {
        if (INSTANCE == null) {
            synchronized (Repository.class) {
                if(INSTANCE == null) {
                    INSTANCE = new Repository();
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


    /* -------------------------------------------------------------------------
     *  Get List Methods
     * -------------------------------------------------------------------------
     */
    public ObservableList<User> getUserList() {
        if(userList == null || userList.isEmpty()) {
            setUserList(userDao.getAll());
        }
        return userList;
    }


    public ObservableList<Bus> getBusList() {
        if(busList == null || busList.isEmpty()) {
            setBusList(busDao.getAll());
        }
        return busList;
    }


    public ObservableList<Route> getRouteList() {
        if(routeList == null || routeList.isEmpty()) {
            setRouteList(routeDao.getAll());
        }
        return routeList;
    }


    public ObservableList<CustomerCapacityReport> getCustomerCapacityReportList() {
        if(ccReportList == null || ccReportList.isEmpty()) {
            setCustomerCapacityReportList(customerCapacityReportDao.queryReport());
        }
        return ccReportList;
    }


    public ObservableList<User> getUserReportList() {
        if(userReportList == null || userReportList.isEmpty()) {
            setUserReportList(userDao.reportAll());
        }
        return userReportList;
    }


    public ObservableList<Bus> getBusReportList() {
        if(busReportList == null || busReportList.isEmpty()) {
            setBusReportList(busDao.reportAll());
        }
        return busReportList;
    }


    public ObservableList<Route> getRouteReportList() {
        if(routeReportList == null || routeReportList.isEmpty()) {
            setRouteReportList(routeDao.reportAll());
        }
        return routeReportList;
    }


    public ObservableList<BusChart> getBusChartReportList() {
        if(busChartReportList == null || busChartReportList.isEmpty()) {
            setBusChartReportList(routeDao.getChart());
        }
        return busChartReportList;
    }


    /* -------------------------------------------------------------------------
     *  Set List Methods
     * -------------------------------------------------------------------------
     */
    public void setUserList(List<User> userList) {
        this.userList.setAll(userList);
    }

    public void setBusList(List<Bus> busList) {
        this.busList.setAll(busList);
    }

    public void setRouteList(List<Route> routeList) {
        this.routeList.setAll(routeList);
    }

    public void setCustomerCapacityReportList(List<CustomerCapacityReport> reportList) {
        this.ccReportList.setAll(reportList);
    }

    public void setUserReportList(List<User> userReportList) {
        this.userReportList.setAll(userReportList);
    }

    public void setBusReportList(List<Bus> busReportList) {
        this.busReportList.setAll(busReportList);
    }

    public void setRouteReportList(List<Route> routeReportList) {
        this.routeReportList.setAll(routeReportList);
    }

    public void setBusChartReportList(List<BusChart> busChartReportList) {
        this.busChartReportList.setAll(busChartReportList);
    }


    /**
     *  After Every Dashboard Scene Reload the method is called to update DashReports.
     */
    public void resetDashReports() {
        setCustomerCapacityReportList(customerCapacityReportDao.queryReport());
    }


    /**
     *  After Every Report Scene Reload the method is called to update Reports.
     */
    public void resetReports() {
        setUserReportList(userDao.reportAll());
        setBusReportList(busDao.reportAll());
        setRouteReportList(routeDao.reportAll());
        setBusChartReportList(routeDao.getChart());
    }


    /* -------------------------------------------------------------------------
     *  Object Submit Methods for Database Insert
     * -------------------------------------------------------------------------
     */
    public boolean submitUser(String userName, String password) {
        User existingUser = lookupUser(userName);
        if(existingUser != null) {
            return false;
        } else if(userDao.insert(userName, password)) {
            setUserList(userDao.getAll());
            return true;
        }
        return false;
    }


    public boolean submitBus(Bus bus) {
        Bus existingBus = lookupBus(bus.getBusNum());
        if(existingBus != null
                && existingBus.getBusId() != bus.getBusId()) {
            return false;
        } else if(busDao.insert(bus)) {
            setBusList(busDao.getAll());
            bus.setBusId(lookupBus(bus.getBusNum()).getBusId());
            return true;
        }
        return false;
    }


    public boolean submitRoute(Route route) {
        Route existingRoute = lookupRoute(route.getRouteNum());
        if(existingRoute != null
                && existingRoute.getRouteId() != route.getRouteId()) {
            return false;
        } else if(routeDao.insert(route)) {
            setRouteList(routeDao.getAll());
            route.setRouteId(lookupRoute(route.getRouteNum()).getRouteId());
            return true;
        }
        return false;
    }


    /* -------------------------------------------------------------------------
     *  Object Update Methods for Database Update
     * -------------------------------------------------------------------------
     */
    public boolean updateUserName(User user, String userName) {
        User existingUser = lookupUser(userName);
        if(existingUser != null
                && existingUser.getUserId() != user.getUserId()) {
            return false;
        } else if(userDao.updateName(user, userName)) {
            setUserList(userDao.getAll());
            return true;
        }
        return false;
    }


    public boolean updateUserPassword(User user, String password) {
        if (false) {//todo: change true to password rules
            return false;
        } else if(!userDao.updatePassword(user, password)) {
            return false;
        }
        return true;
    }


    public boolean updateBus(Bus bus) {
        Bus existingBus = lookupBus(bus.getBusNum());
        if(existingBus != null
                && existingBus.getBusId() != bus.getBusId()) {
            return false;
        } else if(busDao.update(bus)) {
            setBusList(busDao.getAll());
            return true;
        }
        return false;
    }


    public boolean updateRoute(Route route) {
        Route existingRoute = lookupRoute(route.getRouteNum());
        if(existingRoute != null
                && existingRoute.getRouteId() != route.getRouteId()) {
            return false;
        } else if(routeDao.update(route)) {
            setRouteList(routeDao.getAll());
            return true;
        }
        return false;
    }


    /* -------------------------------------------------------------------------
     *  Object Delete Methods for Database Delete/Set InActive
     * -------------------------------------------------------------------------
     */
    public boolean deleteUser(User user) {
        if(userDao.delete(user)) {
           setUserList(userDao.getAll());
            return true;
        }
        return false;
    }


    public boolean deleteBus(Bus bus) {
        if(busDao.delete(bus)) {
            setBusList(busDao.getAll());
            return true;
        }
        return false;
    }


    /*// delete not used for routes, all routes remain active -- just use update
    public boolean deleteRoute(Route route) {
        return routeDao.delete(route);
    }*/


    /* -------------------------------------------------------------------------
     *  Search Methods for Loaded List
     * -------------------------------------------------------------------------
     */
    public User lookupUser(String userName) {
        for(User user : userList) {
            if(user.getUserName().equals(userName)) {
                return user;
            }
        }
        return null;
    }


    public User lookupUser(int userId) {
        for(User user : userList) {
            if(user.getUserId() == (userId)) {
                return user;
            }
        }
        return null;
    }


    public Bus lookupBus(String busNum) {
        for(Bus bus : busList) {
            if(bus.getBusNum().equals(busNum)) {
                return bus;
            }
        }
        return null;
    }


    public Bus lookupBus(int busId) {
        for(Bus bus : busList) {
            if(bus.getBusId() == busId) {
                return bus;
            }
        }
        return null;
    }


    public Route lookupRoute(String routeNum) {
        for(Route route : routeList) {
            if(route.getRouteNum().equals(routeNum)) {
                return route;
            }
        }
        return null;
    }


    public Route lookupRoute(int routeId) {
        for(Route route : routeList) {
            if(route.getRouteId() == routeId) {
                return route;
            }
        }
        return null;
    }


    public ObservableList<User> lookupUsers(String containStr) {
        ObservableList<User> userList = FXCollections.observableArrayList();
        for(User user : this.userList) {
            if(user.getUserName().contains(containStr)) {
                if(!userList.contains(user)) {
                    userList.add(user);
                }
            }
        }
        return userList;
    }


    public ObservableList<User> lookupReportUsers(String containStr) {
        ObservableList<User> userList = FXCollections.observableArrayList();
        for(User user : userReportList) {
            if(user.getUserName().contains(containStr)) {
                if(!userList.contains(user)) {
                    userList.add(user);
                }
            }
        }
        return userList;
    }


    /**
     * Create a list for graphical interfaces that require an ObservableList.
     * Attempt to load Buses that contain the specified containStr, used in SearchButton Methods.
     * Search attributes contain:
     *      BusNum, MaintenanceState, Bus linked RouteNum, and Capacity
     * @param containStr
     * @return List of Buses that contain the containStr String.
     */
    public ObservableList<Bus> lookupBuses(String containStr) {
        ObservableList<Bus> searchList = FXCollections.observableArrayList();
        for(Bus bus : busList) {
            if(bus.getBusNum().toUpperCase().contains(containStr)
                    || bus.getMaintenanceState().toString().contains(containStr)) {
                if(!searchList.contains(bus)) {
                    searchList.add(bus);
                }
            }
            if(bus.getRouteNum() != null) {
                if(bus.getRouteNum().contains(containStr)) {
                    if (!searchList.contains(bus)) {
                        searchList.add(bus);
                    }
                }
            }
            if(containStr.matches("[0-9]+\\W")) {
                int check = Integer.valueOf(containStr);
                if(bus.getCapacity() == check) {
                    if(!searchList.contains(bus)) {
                        searchList.add(bus);
                    }
                }
            }
        }
        return searchList;
    }


    public ObservableList<Bus> lookupReportBuses(String containStr) {
        ObservableList<Bus> searchList = FXCollections.observableArrayList();
        for(Bus bus : busReportList) {
            if(bus.getBusNum().toUpperCase().contains(containStr)
                    || bus.getMaintenanceState().toString().contains(containStr)) {
                if(!searchList.contains(bus)) {
                    searchList.add(bus);
                }
            }
            if(bus.getRouteNum() != null) {
                if(bus.getRouteNum().contains(containStr)) {
                    if (!searchList.contains(bus)) {
                        searchList.add(bus);
                    }
                }
            }
            if(containStr.matches("[0-9]+\\W")) {
                int check = Integer.valueOf(containStr);
                if(bus.getCapacity() == check) {
                    if(!searchList.contains(bus)) {
                        searchList.add(bus);
                    }
                }
            }
        }
        return searchList;
    }


    /**
     * Create a list for graphical interfaces that require an ObservableList.
     * Attempt to load Routes that contain the specified containStr, used in SearchButton Methods.
     * Search attributes contain:
     *      RouteNum, StartPoint, EndPoint, Distance, CustomerAvg, and StopsTotal
     * @param containStr
     * @return List of routes that contain the containStr String.
     */
    public ObservableList<Route> lookupRoutes(String containStr) {
        ObservableList<Route> searchList = FXCollections.observableArrayList();
        for(Route route : this.routeList) {
            if(route.getRouteNum().toUpperCase().contains(containStr)
                    || route.getStartPoint().toUpperCase().contains(containStr)
                    || route.getEndPoint().toUpperCase().contains(containStr)) {
                if(!searchList.contains(route)) {
                    searchList.add(route);
                }
            }
            if(containStr.matches("[0-9]+")) {
                int check = Integer.valueOf(containStr);
                if(route.getDistance() == check
                        || route.getCustomersAvg() == check
                        || route.getStopsTotal() == check) {
                    if(!searchList.contains(route)) {
                        searchList.add(route);
                    }
                }
            }
        }
        return searchList;
    }


    public ObservableList<Route> lookupReportRoutes(String containStr) {
        ObservableList<Route> searchList = FXCollections.observableArrayList();
        for(Route route : routeReportList) {
            if(route.getRouteNum().toUpperCase().contains(containStr)
                    || route.getStartPoint().toUpperCase().contains(containStr)
                    || route.getEndPoint().toUpperCase().contains(containStr)) {
                if(!searchList.contains(route)) {
                    searchList.add(route);
                }
            }
            if(containStr.matches("[0-9]+")) {
                int check = Integer.valueOf(containStr);
                if(route.getDistance() == check
                        || route.getCustomersAvg() == check
                        || route.getStopsTotal() == check) {
                    if(!searchList.contains(route)) {
                        searchList.add(route);
                    }
                }
            }
        }
        return searchList;
    }


    /* -------------------------------------------------------------------------
     *  Current User Methods
     * -------------------------------------------------------------------------
     */
    private void setCurrentLogin(User loggedInUser) {
        this.currentUser = loggedInUser;
    }


    public User getCurrentUser() {
        if(currentUser == null) {
            return null;
        }
        return currentUser;
    }


    /**
     * TEMPORARY - should not be used in a production environment.
     * The function should be the first thing called before any of the application initializes.
     *
     * This function as a temporary measure, copys the userName from a list and assigns it as loggedIn.
     * Then updates database with logged in status.
     * @return bool
     */
    public Boolean userLogin(String name, String password) {
        if(userDao.loginAttempt(name, password)) {
            User loggedInUser = userDao.getByName(name);
            if(loggedInUser != null) {
                setCurrentLogin(loggedInUser);
                return true;
            }
        }
        return false;
    }


    /**
     * Set LoggedIn status.
     *
     * @return bool
     */
    public Boolean logoutUser() {
        if(currentUser != null) {
            return userDao.logoutAttempt();
        }
        return false;
    }
}
