package dev.nicholaskoldys.matso.controller;

import dev.nicholaskoldys.matso.database.Repository;
import dev.nicholaskoldys.matso.model.BusChart;
import dev.nicholaskoldys.matso.model.Route;
import dev.nicholaskoldys.matso.model.reports.CustomerCapacityReport;
import dev.nicholaskoldys.matso.utility.Constants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import dev.nicholaskoldys.matso.model.Bus;
import dev.nicholaskoldys.matso.model.User;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {

    ObservableList<String> reportsList = FXCollections.observableArrayList(Constants.AVAILABLE_REPORTS);

    Repository repo = Repository.getInstance();

    @FXML ChoiceBox reportChoiceBox;

    @FXML Tab tab1;
    TableView reportTable;
    @FXML BarChart reportChart;

    @FXML Label reportLabel;

    @FXML Button searchButton;
    @FXML TextField searchTextField;


    @FXML
    private void searchButtonAction(ActionEvent event) {
        String searchStr = searchTextField.getText().trim().toUpperCase();
        String tableSelected = reportChoiceBox.getSelectionModel().getSelectedItem().toString();
        switch (tableSelected) {
            case "Buses" :
                reportTable.setItems(repo.lookupReportBuses(searchStr));
                break;
            case "Routes" :
                reportTable.setItems(repo.lookupReportRoutes(searchStr));
                break;
            case "Bus Chart" :
                break;
            case "Routes Below Capacity" :
                break;
            case "Routes Above Capacity" :
                break;
            case "Users" :
                reportTable.setItems(repo.lookupReportUsers(searchStr));
                break;
        }
    }

    private void loadUserReport() {
        reportLabel.setText("User Report");
        reportTable = new TableView<User>();
        tab1.setContent(reportTable);
        reportTable.setItems(repo.getUserReportList());

        TableColumn<User, Integer> userId = new TableColumn("ID");
        userId.setCellValueFactory(new PropertyValueFactory<>("UserId"));
        reportTable.getColumns().add(userId);

        TableColumn<User, String> userName = new TableColumn("Name");
        userName.setCellValueFactory(new PropertyValueFactory<>("UserName"));
        reportTable.getColumns().add(userName);

        TableColumn<User, Integer> loggedIn = new TableColumn("LoggedIn");
        loggedIn.setCellValueFactory(new PropertyValueFactory<>("LoggedIn"));
        reportTable.getColumns().add(loggedIn);

        TableColumn<User, LocalDateTime> lastLogin = new TableColumn("LastLogin");
        lastLogin.setCellValueFactory(new PropertyValueFactory<>("LastLogin"));
        reportTable.getColumns().add(lastLogin);

        TableColumn<User, Integer> active = new TableColumn("Active");
        active.setCellValueFactory(new PropertyValueFactory<>("Active"));
        reportTable.getColumns().add(active);

        TableColumn<User, Integer> attempt = new TableColumn("Attempt");
        attempt.setCellValueFactory(new PropertyValueFactory<>("Attempt"));
        reportTable.getColumns().add(attempt);

        TableColumn<User, LocalDateTime> createDate = new TableColumn("Created Date");
        createDate.setCellValueFactory(new PropertyValueFactory<>("CreateDate"));
        reportTable.getColumns().add(createDate);

        TableColumn<User, String> createdUser = new TableColumn("Created By User");
        createdUser.setCellValueFactory(new PropertyValueFactory<>("CreateUserName"));
        reportTable.getColumns().add(createdUser);

        TableColumn<User, LocalDateTime> updateDate = new TableColumn("LastUpdate Date");
        updateDate.setCellValueFactory(new PropertyValueFactory<>("LastUpdateBy"));
        reportTable.getColumns().add(updateDate);

        TableColumn<User, String> updateUser = new TableColumn("LastUpdated By User");
        updateUser.setCellValueFactory(new PropertyValueFactory<>("UpdateUserName"));
        reportTable.getColumns().add(updateUser);
    }

    private void loadBusReport() {
        reportLabel.setText("Bus Report");
        reportTable = new TableView<Bus>();
        tab1.setContent(reportTable);
        reportTable.setItems(repo.getBusReportList());

        TableColumn<Bus, Integer> busId = new TableColumn("ID");
        busId.setCellValueFactory(new PropertyValueFactory<>("BusId"));
        reportTable.getColumns().add(busId);

        TableColumn<Bus, String> busNum = new TableColumn("Number");
        busNum.setCellValueFactory(new PropertyValueFactory<>("BusNum"));
        reportTable.getColumns().add(busNum);

        TableColumn<Bus, Integer> capacity = new TableColumn("Capacity");
        capacity.setCellValueFactory(new PropertyValueFactory<>("Capacity"));
        reportTable.getColumns().add(capacity);

        TableColumn<Bus, Integer> routeId = new TableColumn("Route ID");
        routeId.setCellValueFactory(new PropertyValueFactory<>("RouteId"));
        reportTable.getColumns().add(routeId);

        TableColumn<Bus, String> routeNum = new TableColumn("Route Number");
        routeNum.setCellValueFactory(new PropertyValueFactory<>("RouteNum"));
        reportTable.getColumns().add(routeNum);

        TableColumn<Bus, String> maintenanceState = new TableColumn("Maintenance State");
        maintenanceState.setCellValueFactory(new PropertyValueFactory<>("MaintenanceState"));
        reportTable.getColumns().add(maintenanceState);

        TableColumn<Bus, LocalDateTime> lastBreakdown = new TableColumn("Last Breakdown Date");
        lastBreakdown.setCellValueFactory(new PropertyValueFactory<>("LastBreakdown"));
        reportTable.getColumns().add(lastBreakdown);

        TableColumn<Bus, Integer> active = new TableColumn("Active");
        active.setCellValueFactory(new PropertyValueFactory<>("Active"));
        reportTable.getColumns().add(active);

        TableColumn<Bus, LocalDateTime> createDate = new TableColumn("Created Date");
        createDate.setCellValueFactory(new PropertyValueFactory<>("CreateDate"));
        reportTable.getColumns().add(createDate);

        TableColumn<Bus, String> createdUser = new TableColumn("Created By User");
        createdUser.setCellValueFactory(new PropertyValueFactory<>("CreateUserName"));
        reportTable.getColumns().add(createdUser);

        TableColumn<Bus, LocalDateTime> updateDate = new TableColumn("LastUpdate Date");
        updateDate.setCellValueFactory(new PropertyValueFactory<>("LastUpdateBy"));
        reportTable.getColumns().add(updateDate);

        TableColumn<Bus, String> updateUser = new TableColumn("LastUpdated By User");
        updateUser.setCellValueFactory(new PropertyValueFactory<>("UpdateUserName"));
        reportTable.getColumns().add(updateUser);
    }

    private void loadRouteReport() {
        reportLabel.setText("Route Report");
        reportTable = new TableView<Route>();
        tab1.setContent(reportTable);
        reportTable.setItems(repo.getRouteReportList());

        TableColumn<Route, Integer> routeId = new TableColumn("ID");
        routeId.setCellValueFactory(new PropertyValueFactory<>("RouteId"));
        reportTable.getColumns().add(routeId);

        TableColumn<Route, String> routeNum = new TableColumn("Number");
        routeNum.setCellValueFactory(new PropertyValueFactory<>("RouteNum"));
        reportTable.getColumns().add(routeNum);

        TableColumn<Route, String> startPoint = new TableColumn("Start Point");
        startPoint.setCellValueFactory(new PropertyValueFactory<>("StartPoint"));
        reportTable.getColumns().add(startPoint);

        TableColumn<Route, String> endPoint = new TableColumn("End Point");
        endPoint.setCellValueFactory(new PropertyValueFactory<>("EndPoint"));
        reportTable.getColumns().add(endPoint);

        TableColumn<Route, Integer> stopsTotal = new TableColumn("Total Stops");
        stopsTotal.setCellValueFactory(new PropertyValueFactory<>("StopsTotal"));
        reportTable.getColumns().add(stopsTotal);

        TableColumn<Route, LocalTime> timeLength = new TableColumn("Time Length");
        timeLength.setCellValueFactory(new PropertyValueFactory<>("TimeLength"));
        reportTable.getColumns().add(timeLength);

        TableColumn<Route, Float> distance = new TableColumn("Distance");
        distance.setCellValueFactory(new PropertyValueFactory<>("Distance"));
        reportTable.getColumns().add(distance);

        TableColumn<Route, LocalTime> currentDelay = new TableColumn("Current Delay");
        currentDelay.setCellValueFactory(new PropertyValueFactory<>("CurrentDelay"));
        reportTable.getColumns().add(currentDelay);

        TableColumn<Route, Integer> customerAvg = new TableColumn("Customer Average");
        customerAvg.setCellValueFactory(new PropertyValueFactory<>("CustomersAvg"));
        reportTable.getColumns().add(customerAvg);

        TableColumn<Route, LocalDateTime> createDate = new TableColumn("Created Date");
        createDate.setCellValueFactory(new PropertyValueFactory<>("CreateDate"));
        reportTable.getColumns().add(createDate);

        TableColumn<Route, String> createdUser = new TableColumn("Created By User");
        createdUser.setCellValueFactory(new PropertyValueFactory<>("CreateUserName"));
        reportTable.getColumns().add(createdUser);

        TableColumn<Route, LocalDateTime> updateDate = new TableColumn("LastUpdate Date");
        updateDate.setCellValueFactory(new PropertyValueFactory<>("LastUpdateBy"));
        reportTable.getColumns().add(updateDate);

        TableColumn<Route, String> updateUser = new TableColumn("LastUpdated By User");
        updateUser.setCellValueFactory(new PropertyValueFactory<>("UpdateUserName"));
        reportTable.getColumns().add(updateUser);
    }

    private void loadBusChart() {
        reportLabel.setText("Bus Chart Report");
        reportTable = new TableView<BusChart>();
        tab1.setContent(reportTable);
        reportTable.setItems(repo.getBusChartReportList());

        TableColumn<Route, Integer> routeId = new TableColumn("Route ID");
        routeId.setCellValueFactory(new PropertyValueFactory<>("RouteId"));
        reportTable.getColumns().add(routeId);

        TableColumn<Route, String> routeNum = new TableColumn("Route Number");
        routeNum.setCellValueFactory(new PropertyValueFactory<>("RouteNum"));
        reportTable.getColumns().add(routeNum);

        TableColumn<Route, Integer> busId = new TableColumn("Bus ID");
        busId.setCellValueFactory(new PropertyValueFactory<>("BusId"));
        reportTable.getColumns().add(busId);

        TableColumn<Route, String> busNum = new TableColumn("Bus Number");
        busNum.setCellValueFactory(new PropertyValueFactory<>("BusNum"));
        reportTable.getColumns().add(busNum);
    }

    private void loadRouteBelowCapacityReport() {
        reportLabel.setText("Routes Below Capacity Report");
        reportTable = new TableView<CustomerCapacityReport>();
        tab1.setContent(reportTable);

        ObservableList<CustomerCapacityReport> lessThanReport = FXCollections.observableArrayList();
        for(CustomerCapacityReport report : repo.getCustomerCapacityReportList()) {
            if(report.getTotalCapacity() < report.getCustomerAvg()) {
                lessThanReport.add(report);
            }
        }
        reportTable.setItems(lessThanReport);

        TableColumn<CustomerCapacityReport, String> routeNum = new TableColumn("Route Number");
        routeNum.setCellValueFactory(new PropertyValueFactory<>("RouteNum"));
        reportTable.getColumns().add(routeNum);

        TableColumn<CustomerCapacityReport, Integer> customerAvg = new TableColumn("Customer Amount Average");
        customerAvg.setCellValueFactory(new PropertyValueFactory<>("CustomerAvg"));
        reportTable.getColumns().add(customerAvg);

        TableColumn<CustomerCapacityReport, Integer> totalCapacity = new TableColumn("Total Bus Capacity Available");
        totalCapacity.setCellValueFactory(new PropertyValueFactory<>("TotalCapacity"));
        reportTable.getColumns().add(totalCapacity);
    }

    private void loadRouteAboveCapacityReport() {
        reportLabel.setText("Routes Above Capacity Report");
        reportTable = new TableView<CustomerCapacityReport>();
        tab1.setContent(reportTable);

        ObservableList<CustomerCapacityReport> greaterThanOrEqualReport = FXCollections.observableArrayList();
        for(CustomerCapacityReport report : repo.getCustomerCapacityReportList()) {
            if(report.getTotalCapacity() >= report.getCustomerAvg()) {
                greaterThanOrEqualReport.add(report);
            }
        }
        reportTable.setItems(greaterThanOrEqualReport);

        TableColumn<CustomerCapacityReport, String> routeNum = new TableColumn("Route Number");
        routeNum.setCellValueFactory(new PropertyValueFactory<>("RouteNum"));
        reportTable.getColumns().add(routeNum);

        TableColumn<CustomerCapacityReport, Integer> customerAvg = new TableColumn("Customer Amount Average");
        customerAvg.setCellValueFactory(new PropertyValueFactory<>("CustomerAvg"));
        reportTable.getColumns().add(customerAvg);

        TableColumn<CustomerCapacityReport, Integer> totalCapacity = new TableColumn("Total Bus Capacity Available");
        totalCapacity.setCellValueFactory(new PropertyValueFactory<>("TotalCapacity"));
        reportTable.getColumns().add(totalCapacity);
    }

    private void setupReportsChoiceBox(ObservableList<String> reportNames) {
        reportChoiceBox.setItems(reportNames);
        reportChoiceBox.setValue("Please Select a Report...");
    }

    private void reportsChoiceBoxListener() {
        loadSelectedReport(reportChoiceBox.getSelectionModel().getSelectedItem().toString());
    }

    /**
     * Switch statement to call the report load methods.
     *
     * @param report
     */
    private void loadSelectedReport(String report) {
        switch (report) {
            case "Buses" :
                loadBusReport();
                break;
            case "Routes" :
                loadRouteReport();
                break;
            case "Bus Chart" :
                loadBusChart();
                break;
            case "Routes Below Capacity" :
                loadRouteBelowCapacityReport();
                break;
            case "Routes Above Capacity" :
                loadRouteAboveCapacityReport();
                break;
            case "Users" :
                loadUserReport();
                break;
        }
    }

    /**
     *  Set buttons with appropriate methods for Action Events,
     *  reset Style on textFields that are clicked, undoes the red background.
     */
    private void setAllClickActions() {
        reportChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> reportsChoiceBoxListener());
        searchButton.setOnAction(event -> searchButtonAction(event));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        repo.resetReports();
        setupReportsChoiceBox(reportsList);
        setAllClickActions();
    }
}
