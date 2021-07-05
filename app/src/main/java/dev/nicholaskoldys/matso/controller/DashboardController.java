package dev.nicholaskoldys.matso.controller;

import dev.nicholaskoldys.matso.model.Route;
import dev.nicholaskoldys.matso.model.reports.CustomerCapacityReport;
import dev.nicholaskoldys.matso.utility.Constants;
import dev.nicholaskoldys.matso.utility.DateTimeService;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import dev.nicholaskoldys.matso.database.DBConnection;
import dev.nicholaskoldys.matso.database.Repository;
import dev.nicholaskoldys.matso.model.Bus;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

public class DashboardController implements Initializable {

    Repository repo = Repository.getInstance();

    @FXML Label currentDateLabel;
    @FXML Label currentTimeLabel;

    @FXML TableView busTableView;
    @FXML TableColumn busNumCol;
    @FXML TableColumn routeCol;
    @FXML TableColumn maintenanceStateCol;

    @FXML TableView routeTableView;
    @FXML TableColumn routeNumCol;
    @FXML TableColumn timeLengthCol;
    @FXML TableColumn distanceCol;
    @FXML TableColumn delayCol;

    @FXML PieChart busMaintenanceStatusPieChart;

    @FXML ScatterChart customerAvgAndBusCapacityScatterChart;
    @FXML NumberAxis customerAvgAxis;
    @FXML NumberAxis totalCapacityAxis;

    @FXML StackedBarChart routeTimeStatsBarChart;
    @FXML CategoryAxis routeNumAxis;
    @FXML NumberAxis timeDurationAxis;

    @FXML TextField searchTextField;
    @FXML Button searchBtn;


    private void setupPieChart() {
        busMaintenanceStatusPieChart.getData().clear();

        ObservableList<Bus> busList = repo.getBusList();

        List<PieChart.Data> pieChartData = new ArrayList<>();

        if(!busList.isEmpty()) {
            Map<Enum<Constants.MaintenanceState>, Integer> maintenanceCountList = new HashMap<>();
            for(Bus bus : busList) {
                if(bus != null) {
                    Enum<Constants.MaintenanceState> state = bus.getMaintenanceState();
                    if (state != null) {
                        if (!maintenanceCountList.containsKey(state)) {
                            maintenanceCountList.put(state, 1);
                        } else {
                            maintenanceCountList.replace(
                                    state,
                                    maintenanceCountList.get(state) + 1
                            );
                        }
                    }
                }
            }

            for (Map.Entry<Enum<Constants.MaintenanceState>, Integer> state : maintenanceCountList.entrySet()) {
                PieChart.Data newPiePiece = new PieChart.Data(
                        state.getKey().toString(), state.getValue()
                );
                newPiePiece.setName(state.getValue() + " - " + state.getKey().toString() + " buses");
                pieChartData.add(newPiePiece);
            }
            busMaintenanceStatusPieChart.getData().addAll(pieChartData);
        }
    }


    private void setupBarChart() {
        routeTimeStatsBarChart.setTitle("Route Length and Delay Times");
        routeTimeStatsBarChart.setAnimated(false);//bugged must disable to show all xAxis
        routeTimeStatsBarChart.getData().clear();

        routeNumAxis = new CategoryAxis();
        timeDurationAxis = new NumberAxis();

        routeTimeStatsBarChart.getXAxis().setLabel("Route Numbers");
        routeTimeStatsBarChart.getYAxis().setLabel("Time Duration - min.");

        ObservableList<Route> routeList = repo.getRouteList();

        List<XYChart.Series> routeTimeGroup = new ArrayList<>();

        if(!routeList.isEmpty()) {
            XYChart.Series lengthSet = new XYChart.Series<>();
            lengthSet.setName("Length");

            XYChart.Series delaySet = new XYChart.Series<>();
            delaySet.setName("Delay");

            for(Route route : routeList) {
                int length = (route.getTimeLength().toSecondOfDay() / 60);
                int delay = (route.getCurrentDelay().toSecondOfDay() / 60);
                lengthSet.getData().add(
                        new XYChart.Data<String, Number>(route.getRouteNum(), length)
                );
                delaySet.getData().add(
                        new XYChart.Data<String, Number>(route.getRouteNum(), delay)
                );
            }
            routeTimeGroup.add(delaySet);
            routeTimeGroup.add(lengthSet);
            routeTimeStatsBarChart.getData().addAll(routeTimeGroup);
        }
    }


    private void setupScatterChart() {
        customerAvgAndBusCapacityScatterChart.setTitle("Customer Capacity Per Route");
        customerAvgAndBusCapacityScatterChart.getData().clear();

        customerAvgAndBusCapacityScatterChart.getXAxis().setLabel("Customer Avg");
        customerAvgAndBusCapacityScatterChart.getYAxis().setLabel("Total Capacity");

        ObservableList<CustomerCapacityReport> routeList = FXCollections.observableArrayList();
        routeList = repo.getCustomerCapacityReportList();

        List<XYChart.Series> scatterPoints = new ArrayList<>();

        if(!routeList.isEmpty()) {
            for (CustomerCapacityReport routeNum : routeList) {

                XYChart.Series dataSet = new XYChart.Series<>();
                dataSet.setName(routeNum.getRouteNum());
                dataSet.getData().add(
                        new XYChart.Data(routeNum.getCustomerAvg(), routeNum.getTotalCapacity())
                );
                scatterPoints.add(dataSet);
            }
            customerAvgAndBusCapacityScatterChart.getData().addAll(scatterPoints);
        }
    }


    @FXML
    private void searchButtonAction(ActionEvent event) {
        String searchStr = searchTextField.getText().trim().toUpperCase();
        busTableView.setItems(repo.lookupBuses(searchStr));
        routeTableView.setItems(repo.lookupRoutes(searchStr));
    }


    private void setupTableView() {
        Platform.runLater(() -> {
            busTableView.setItems(repo.getBusList());
            busNumCol.setCellValueFactory(new PropertyValueFactory<>("BusNum"));
            routeCol.setCellValueFactory(new PropertyValueFactory<>("RouteNum"));
            maintenanceStateCol.setCellValueFactory(new PropertyValueFactory<>("MaintenanceState"));

            routeTableView.setItems(repo.getRouteList());
            routeNumCol.setCellValueFactory(new PropertyValueFactory<>("RouteNum"));
            timeLengthCol.setCellValueFactory(new PropertyValueFactory<>("TimeLength"));
            distanceCol.setCellValueFactory(new PropertyValueFactory<>("Distance"));
            delayCol.setCellValueFactory(new PropertyValueFactory<>("CurrentDelay"));
        });
    }


    private void setAllClickActions() {
        searchBtn.setOnAction(event -> searchButtonAction(event));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        repo.resetDashReports();
        setAllClickActions();
        setupTableView();

        Platform.runLater(() -> {
            setupPieChart();
            setupBarChart();
            setupScatterChart();
        });

        currentDateLabel.setText("Date: " + LocalDateTime.now().format(DateTimeService.DASHDATE_FORMAT));
        currentTimeLabel.setText("Time: " + LocalDateTime.now().format(DateTimeService.DASHTIME_FORMAT));

        AnimationTimer timer = new AnimationTimer() {
                private long tick;
                private int i = 0;
                //private long longTick;
                @Override
                public void handle(long now) {
                    if (tick == 0L) {
                        tick = now;
                    } else {
                        long diff1 = now - tick;
                        if (diff1 >= 30_500_000_000L) {//30.5s
                            currentDateLabel.setText("Date: " + LocalDateTime.now().format(DateTimeService.DASHDATE_FORMAT));
                            currentTimeLabel.setText("Time: " + LocalDateTime.now().format(DateTimeService.DASHTIME_FORMAT));
                            System.out.println("Database Connection Keep Alive " + i++);
                            DBConnection.getConnection();
                            tick = now;
                        }

                    }
                }
        };
        timer.start();
    }
}
