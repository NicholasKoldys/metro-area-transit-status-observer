package dev.nicholaskoldys.matso.controller;

import dev.nicholaskoldys.matso.database.Repository;
import dev.nicholaskoldys.matso.utility.Constants;
import dev.nicholaskoldys.matso.utility.CustomExceptions.EntryFieldException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import dev.nicholaskoldys.matso.SceneRouter;
import dev.nicholaskoldys.matso.model.Route;
import dev.nicholaskoldys.matso.utility.DateTimeService;

import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RoutesController implements Initializable {

    Repository repo = Repository.getInstance();

    Route selectedRoute;

    @FXML Label routeLabel;
    @FXML TextField searchTextField;
    @FXML TextField routeNumTextField;
    @FXML TextField startPointTextField;
    @FXML TextField endPointTextField;
    @FXML TextField stopsTotalTextField;
    @FXML TextField timeLengthTextField;
    @FXML TextField distanceTextField;
    @FXML TextField currentDelayTextField;
    @FXML TextField customersAvgTextField;

    @FXML TableView<Route> routeTableView;
    @FXML TableColumn routeNumCol;
    @FXML TableColumn startCol;
    @FXML TableColumn endCol;
    @FXML TableColumn stopsCol;
    @FXML TableColumn timeLengthCol;
    @FXML TableColumn distanceCol;
    @FXML TableColumn delayCol;
    @FXML TableColumn custAvgCol;

    @FXML Button searchButton;
    @FXML Button addButton;
    @FXML Button updateButton;
    @FXML Button resetButton;

    /**
     * In Initialize method, attempt to load Route and store into selectedRoute variable,
     * applying all Route attributes to textFields.
     * Enable Add and Update Buttons.
     */
    public void setRouteOnLoad() {
        selectedRoute = (Route) SceneRouter.objectHolder;
        if(selectedRoute != null) {
            routeTableView.getSelectionModel().getSelectedItem();
            routeNumTextField.setText(selectedRoute.getRouteNum());
            startPointTextField.setText(selectedRoute.getStartPoint());
            endPointTextField.setText(selectedRoute.getEndPoint());
            stopsTotalTextField.setText(Integer.toString(selectedRoute.getStopsTotal()));
            timeLengthTextField.setText(selectedRoute.getTimeLength().toString());
            distanceTextField.setText(Float.toString(selectedRoute.getDistance()));
            currentDelayTextField.setText(selectedRoute.getCurrentDelay().toString());
            customersAvgTextField.setText(Integer.toString(selectedRoute.getCustomersAvg()));

            routeLabel.setText("Route: " + selectedRoute.getRouteNum());
            addButton.setDisable(false);
            updateButton.setDisable(false);
        }
    }


    /**
     * Action Event to get row selected from routeTableView, load Route and store into selectedRoute variable,
     * applying all Route attributes to textFields.
     * Enable Add and Update Buttons.
     * @param event
     */
    @FXML
    private void tableViewSelectionAction(MouseEvent event) {
        if(routeTableView.getSelectionModel().getSelectedItem() != null) {

            /*
            *   Load Route object from the table.
            */
            selectedRoute = routeTableView.getSelectionModel().getSelectedItem();

            /*
             * Set textField strings and heading labels with data from selectedRoute Object
             */
            if(selectedRoute != null) {
                routeNumTextField.setText(selectedRoute.getRouteNum());
                startPointTextField.setText(selectedRoute.getStartPoint());
                endPointTextField.setText(selectedRoute.getEndPoint());
                stopsTotalTextField.setText(Integer.toString(selectedRoute.getStopsTotal()));
                timeLengthTextField.setText(selectedRoute.getTimeLength().toString());
                distanceTextField.setText(Float.toString(selectedRoute.getDistance()));
                currentDelayTextField.setText(selectedRoute.getCurrentDelay().toString());
                customersAvgTextField.setText(Integer.toString(selectedRoute.getCustomersAvg()));

                /*
                 * Enable buttons with loaded selectedRoute, ONLY Location to enable buttons
                 */
                routeLabel.setText("Route: " + selectedRoute.getRouteNum());
                addButton.setDisable(false);
                updateButton.setDisable(false);
            }
        }


    }


    /**
     * When Search Button is clicked, perform content search based on:
     *      RouteNum, StartPoint, EndPoint, Distance, CustomerAvg, and StopsTotal
     * @param event
     */
    @FXML
    private void searchButtonAction(ActionEvent event) {
        String searchStr = searchTextField.getText().trim().toUpperCase();
        routeTableView.setItems(repo.lookupRoutes(searchStr));
    }


    /**
     * When Add Button is clicked,
     * first test all entries into each textField,
     * second parse fields and set nulls where necessary,
     * third create a New Route Object and attempt to submit the Route to the database.
     * @param event
     */
    @FXML
    private void addButtonAction(ActionEvent event) {
        if (!addButton.isDisabled()) {

            /*
             *   Test all the textFields for Errors
             */
            if(isTextFieldsPassTests()) {

                /*
                 *   Normalize Route Num and correct to Database format, if missed by regex in tests
                 */
                String routeNumStr = routeNumTextField.getText().trim().toUpperCase();
                routeNumTextField.setText(routeNumStr);//set field to uppercase and no space version

                /*
                 *   Remove spaces in Stops Total, Time Length,  Distance,
                 *  C.Delay, C.Avg textField, if missed by regex in tests
                 */
                String stopsStr = stopsTotalTextField.getText().trim();
                stopsTotalTextField.setText(stopsStr);
                String lengthStr = timeLengthTextField.getText().trim();
                timeLengthTextField.setText(lengthStr);
                String distanceStr = distanceTextField.getText().trim();
                distanceTextField.setText(distanceStr);
                String delayStr = currentDelayTextField.getText().trim();
                currentDelayTextField.setText(delayStr);
                String customerStr = customersAvgTextField.getText().trim();
                customersAvgTextField.setText(customerStr);

                /*
                 * Create a New Route Object and make it the active Route on the Page (selectedRoute)
                 */
                selectedRoute = new Route(
                        routeNumTextField.getText(),
                        startPointTextField.getText(),
                        endPointTextField.getText(),
                        Integer.valueOf(stopsTotalTextField.getText()),
                        LocalTime.parse(timeLengthTextField.getText(), DateTimeService.OFFSET_LENGTH_FORMAT),
                        Float.valueOf(distanceTextField.getText()),
                        LocalTime.parse(currentDelayTextField.getText(), DateTimeService.OFFSET_LENGTH_FORMAT),
                        Integer.valueOf(customersAvgTextField.getText())
                );

                /*
                 * Attempt to submitRoute to database
                 */
                try {
                    if (repo.submitRoute(selectedRoute)) {
                        routeTableView.refresh();
                    } else {

                        /*
                         * If fails mark RouteNum textField with red background
                         */
                        routeNumTextField.setStyle("-fx-control-inner-background: #" + Constants.INCORRECT_COLOR);
                        throw new EntryFieldException("Warning::Bus Number is Incorrect::",
                                EntryFieldException.ALERT_TYPE.ROUTENUM_INCORRECT_FIELD_VALUE, routeNumTextField.getText());
                    }
                } catch (EntryFieldException tex) {
                    System.out.println(tex.getMessage());
                }
            }
        }
    }


    /**
     * When Update Button is clicked,
     * first test all entries into each textFields,
     * second parse fields and set nulls where necessary,
     * third create a New Bus Object and attempt to submit the Bus to the database.
     * @param event
     */
    @FXML
    private void updateButtonAction(ActionEvent event) {
        if (!updateButton.isDisabled()) {

            /*
             *   Test all the textFields for Errors
             */
            if (isTextFieldsPassTests()) {

                /*
                 *   Normalize Route Num and correct to Database format, if missed by regex in tests
                 */
                String routeNumStr = routeNumTextField.getText().trim().toUpperCase();
                routeNumTextField.setText(routeNumStr);//set field to uppercase and no space version

                /*
                 *   Remove spaces in Stops Total, Time Length,  Distance,
                 *  C.Delay, C.Avg textField, if missed by regex in tests
                 */
                String stopsStr = stopsTotalTextField.getText().trim();
                stopsTotalTextField.setText(stopsStr);
                String lengthStr = timeLengthTextField.getText().trim();
                timeLengthTextField.setText(lengthStr);
                String distanceStr = distanceTextField.getText().trim();
                distanceTextField.setText(distanceStr);
                String delayStr = currentDelayTextField.getText().trim();
                currentDelayTextField.setText(delayStr);
                String customerStr = customersAvgTextField.getText().trim();
                customersAvgTextField.setText(customerStr);

                /*
                 * Take active selectedRoute make a copy of previous data and update data for the original Object
                 *  (data can later be recovered, avoiding tableView updating false information if adding fails)
                 */
                Route oldSelectedRoute = selectedRoute.copy();
                selectedRoute.setRouteNum(routeNumTextField.getText());
                selectedRoute.setStartPoint(startPointTextField.getText());
                selectedRoute.setEndPoint(endPointTextField.getText());
                selectedRoute.setStopsTotal(Integer.valueOf(stopsTotalTextField.getText()));
                selectedRoute.setTimeLength(LocalTime.parse(timeLengthTextField.getText(), DateTimeService.OFFSET_LENGTH_FORMAT));
                selectedRoute.setDistance(Float.valueOf(distanceTextField.getText()));
                selectedRoute.setCurrentDelay(LocalTime.parse(currentDelayTextField.getText(), DateTimeService.OFFSET_LENGTH_FORMAT));
                selectedRoute.setCustomersAvg(Integer.valueOf(customersAvgTextField.getText()));

                /*
                 * Attempt to updateRoute in the database
                 */
                try {
                    if (repo.updateRoute(selectedRoute)) {
                        routeTableView.refresh();
                    } else {
                        throw new EntryFieldException("Warning::Route Number is Incorrect::",
                                EntryFieldException.ALERT_TYPE.ROUTENUM_INCORRECT_FIELD_VALUE, routeNumTextField.getText());
                    }
                } catch (EntryFieldException tex) {
                    System.out.println(tex.getMessage());

                    /*
                     * If fails mark textField with red background
                     * and reset object's Route fields to previous fields
                     */
                    routeNumTextField.setStyle("-fx-control-inner-background: #" + Constants.INCORRECT_COLOR);
                    selectedRoute.setRouteNum(oldSelectedRoute.getRouteNum());
                    selectedRoute.setStartPoint(oldSelectedRoute.getStartPoint());
                    selectedRoute.setEndPoint(oldSelectedRoute.getEndPoint());
                    selectedRoute.setStopsTotal(oldSelectedRoute.getStopsTotal());
                    selectedRoute.setTimeLength(oldSelectedRoute.getTimeLength());
                    selectedRoute.setDistance(oldSelectedRoute.getDistance());
                    selectedRoute.setCurrentDelay(oldSelectedRoute.getCurrentDelay());
                    selectedRoute.setCustomersAvg(oldSelectedRoute.getCustomersAvg());
                }
            }
        }
    }

    /*@FXML // delete not used for routes, all routes remain active -- just use update
    private void deleteButtonAction(ActionEvent event) {

    }*/

    /**
     * When Reset Button is clicked,
     * Set routeTableView with Route Observable Object List from Repository
     * Clear All textFields, setting them empty.
     * Disable button Update; button are only enabled with tableViewSelectionAction() method
     * @param event
     */
    @FXML
    private void resetButtonAction(ActionEvent event) {
        routeTableView.setItems(repo.getRouteList());
        routeTableView.refresh();
        setAllFieldsEmpty();
        setAllClickActions();
        updateButton.setDisable(true);
    }


    /**
     * Method called in updateButtonAction() and addButtonAction() methods,
     * Tests result in Pass or Fail, if failed then given textField is set to a red background and
     *      an appropriate Alert with the EntryFieldExceptions's AlertType is followed explaining the failure.
     *
     * Tests:
     *  All textFields for non-empty entries.
     *  Route Number textField follows "C###L" formatting.  C is a cardinal direction, # is a number, L is a letter
     *  Customer Average and Stops Total textFields allows only 1-11 digit Integers.
     *  Distance textField allows only 2 digit Integers and optional 2 decimal places. 99.99 = max distance
     *  Time Length and Current Delay textFields allows only 4 digit Time Intervals based in 24hr format. "HH:mm"
     *      Time Length must be greater than or equal to >=1 min Time interval.
     *
     * @return True if all tests are passed, false if a single test fails.
     */
    private boolean isTextFieldsPassTests() {
        try {
            /*
            * call getAllEmptyFields and use the List of strings of empty field names
            * to test the entries.
            * set background to red for empty textFields.
            */
            if (!getAllEmptyFields().isEmpty()) {
                List<String> emptyFields = getAllEmptyFields();
                for(String str : emptyFields) {
                    if(str.equals("Route Number")) {
                        routeNumTextField.setStyle("-fx-control-inner-background: #" + Constants.INCORRECT_COLOR);
                    }
                    if(str.equals("Start Point")) {
                        startPointTextField.setStyle("-fx-control-inner-background: #" + Constants.INCORRECT_COLOR);
                    }
                    if(str.equals("End Point")) {
                        endPointTextField.setStyle("-fx-control-inner-background: #" + Constants.INCORRECT_COLOR);
                    }
                    if(str.equals("Stops Total")) {
                        stopsTotalTextField.setStyle("-fx-control-inner-background: #" + Constants.INCORRECT_COLOR);
                    }
                    if(str.equals("Time Length")) {
                        timeLengthTextField.setStyle("-fx-control-inner-background: #" + Constants.INCORRECT_COLOR);
                    }
                    if(str.equals("Distance")) {
                        distanceTextField.setStyle("-fx-control-inner-background: #" + Constants.INCORRECT_COLOR);
                    }
                    if(str.equals("Current Delay")) {
                        currentDelayTextField.setStyle("-fx-control-inner-background: #" + Constants.INCORRECT_COLOR);
                    }
                    if(str.equals("Customers Average")) {
                        customersAvgTextField.setStyle("-fx-control-inner-background: #" + Constants.INCORRECT_COLOR);
                    }
                }
                throw new EntryFieldException(
                        "Warning::Missing Fields Detected for Route Submission::",
                        EntryFieldException.ALERT_TYPE.ROUTE_REQUIRED_FIELDS, getAllEmptyFields().toString());
            }

            /*
             * match Route Number textField against regex string
             * set background to red.
             */
            if (!routeNumTextField.getText().matches("[NSEW][0-9]{3}[A-Za-z]")) {
                routeNumTextField.setStyle("-fx-control-inner-background: #" + Constants.INCORRECT_COLOR);
                throw new EntryFieldException(
                        "Warning::Route Number Formatting is Incorrect::",
                        EntryFieldException.ALERT_TYPE.ROUTENUM_INCORRECT_FIELD_FORMAT, routeNumTextField.getText());
            }

            /*
             * match Stops Total textField against regex string
             * set background to red.
             */
            if (!stopsTotalTextField.getText().matches("[0-9]{1,11}")) {
                stopsTotalTextField.setStyle("-fx-control-inner-background: #" + Constants.INCORRECT_COLOR);
                throw new EntryFieldException(
                        "Warning::an Integer/Number value(s) is Incorrect::",
                        EntryFieldException.ALERT_TYPE.ROUTE_INTEGER_INCORRECT_FIELD_BOUNDS, stopsTotalTextField.getText());
            }

            /*
             * match Distance textField against regex string
             * set background to red.
             */
            if (!distanceTextField.getText().matches("[0-9]{0,2}(\\.[0-9]{0,2})?")) {
                distanceTextField.setStyle("-fx-control-inner-background: #" + Constants.INCORRECT_COLOR);
                throw new EntryFieldException(
                        "Warning::an Integer/Number value(s) is Incorrect::",
                        EntryFieldException.ALERT_TYPE.ROUTE_INTEGER_INCORRECT_FIELD_BOUNDS, distanceTextField.getText());
            }

            /*
             * match Customers Avg textField against regex string
             * set background to red.
             */
            if (!customersAvgTextField.getText().matches("[0-9]{1,11}")) {
                customersAvgTextField.setStyle("-fx-control-inner-background: #" + Constants.INCORRECT_COLOR);
                throw new EntryFieldException(
                        "Warning::an Integer/Number value(s) is Incorrect::",
                        EntryFieldException.ALERT_TYPE.ROUTE_INTEGER_INCORRECT_FIELD_BOUNDS, customersAvgTextField.getText());
            }

            /*
             * match Time Length textField against regex string
             * if passed test if lower than 1 min.
             * set background to red.
             */
            if (!timeLengthTextField.getText().matches("[0-2][0-9]:[0-5][0-9]")) {
                timeLengthTextField.setStyle("-fx-control-inner-background: #" + Constants.INCORRECT_COLOR);
                throw new EntryFieldException(
                        "Warning::an Time value(s) is Incorrect::",
                        EntryFieldException.ALERT_TYPE.ROUTE_TIME_INCORRECT_FIELD_BOUNDS, timeLengthTextField.getText());
            } else if (LocalTime.parse(timeLengthTextField.getText(), DateTimeService.OFFSET_LENGTH_FORMAT)
                    .isBefore(LocalTime.of(00, 01))) {
                timeLengthTextField.setStyle("-fx-control-inner-background: #" + Constants.INCORRECT_COLOR);
                throw new EntryFieldException(
                        "Warning::an Time value(s) is Incorrect::",
                        EntryFieldException.ALERT_TYPE.ROUTE_TIME_INCORRECT_FIELD_BOUNDS, timeLengthTextField.getText());
            }

            /*
             * match Current Delay textField against regex string
             * set background to red.
             */
            if (!currentDelayTextField.getText().matches("[0-2][0-9]:[0-5][0-9]")) {
                throw new EntryFieldException(
                        "Warning::an Time value(s) is Incorrect::",
                        EntryFieldException.ALERT_TYPE.ROUTE_TIME_INCORRECT_FIELD_BOUNDS, currentDelayTextField.getText());
            }
        } catch (EntryFieldException tex) {
            System.out.println(tex.getMessage());
            return false;
        }
        return true;
    }


    /**
     *
     * @return
     */
    private void setupTableView() {
        Platform.runLater(() -> {
            routeTableView.setItems(repo.getRouteList());
            routeNumCol.setCellValueFactory(new PropertyValueFactory<>("RouteNum"));
            startCol.setCellValueFactory(new PropertyValueFactory<>("StartPoint"));
            endCol.setCellValueFactory(new PropertyValueFactory<>("EndPoint"));
            stopsCol.setCellValueFactory(new PropertyValueFactory<>("StopsTotal"));
            timeLengthCol.setCellValueFactory(new PropertyValueFactory<>("TimeLength"));
            distanceCol.setCellValueFactory(new PropertyValueFactory<>("Distance"));
            delayCol.setCellValueFactory(new PropertyValueFactory<>("CurrentDelay"));
            custAvgCol.setCellValueFactory(new PropertyValueFactory<>("CustomersAvg"));
        });
    }


    /**
     *  Listener for 24hr Time Interval textFields.
     *  When text is changed or added to the textField, the cascading if statements disallow further typing
     *  if the string of entered text does not apply to the regex string.
     */
    private void timeFormatEnforceOnTextField(TextField field) {
        String input = field.getText();
        int maxLength = 4;// base 0 - 4
        String regexDateTime = "(?<hour>[0-2][0-9]):(?<min>[0-5][0-9])";
        if(!input.matches(regexDateTime)) {//base pattern
            if(input.length() != 0) {
                String correctedOutput = String.valueOf(input.toCharArray(), 0, input.length() - 1);
                char c = input.charAt(input.length() - 1);
                if(input.length()-1 > maxLength) {
                    field.setText(correctedOutput);
                } else if(input.length()-1 == 0) {
                    if(!String.valueOf(c).matches("[0-2]")) {
                        field.setText(correctedOutput);
                    }
                } else if(input.length()-1 == 1) {
                    char h1 = input.charAt(0);
                    if(h1 == '2' && !String.valueOf(c).matches("[0-3]")) {
                        field.setText(correctedOutput);
                    }
                } else if(input.length()-1 == 2) {
                    if(!String.valueOf(c).matches(":")) {
                        field.setText(correctedOutput);
                    }
                } else if(input.length()-1 == 3) {
                    if(!String.valueOf(c).matches("[0-5]")) {
                        field.setText(correctedOutput);
                    }
                } else if(!String.valueOf(c).matches("[0-9]")) {
                    field.setText(correctedOutput);
                }
            }
        }
    }


    /**
     * Capture all Empty textFields, store them in a list by a string identifier.
     * @return List of String Names of empty textFields:
     *      Route Number, Start Point, End Point, Stops Total, Time Length, Distance, Current Delay, Customer Average
     */
    private List<String> getAllEmptyFields() {
        List<String> emptyFields = new ArrayList<>();
        if (routeNumTextField.getText().isEmpty()) {
            emptyFields.add("Route Number");
        }
        if (startPointTextField.getText().isEmpty()) {
            emptyFields.add("Start Point");
        }
        if (endPointTextField.getText().isEmpty()) {
            emptyFields.add("End Point");
        }
        if (stopsTotalTextField.getText().isEmpty()) {
            emptyFields.add("Stops Total");
        }
        if (timeLengthTextField.getText().isEmpty()) {
            emptyFields.add("Time Length");
        }
        if (distanceTextField.getText().isEmpty()) {
            emptyFields.add("Distance");
        }
        if (currentDelayTextField.getText().isEmpty()) {
            emptyFields.add("Current Delay");
        }
        if (customersAvgTextField.getText().isEmpty()) {
            emptyFields.add("Customers Average");
        }
        return emptyFields;
    }


    /**
     * Set all textFields empty/clear
     */
    private void setAllFieldsEmpty() {
        routeNumTextField.clear();
        startPointTextField.clear();
        endPointTextField.clear();
        stopsTotalTextField.clear();
        timeLengthTextField.clear();
        distanceTextField.clear();
        currentDelayTextField.clear();
        customersAvgTextField.clear();
    }


    /**
     *  Set buttons with appropriate methods for Action Events,
     *  reset Style on textFields that are clicked, undoes the red background.
     */
    private void setAllClickActions() {
        routeTableView.setOnMouseClicked(event -> tableViewSelectionAction(event));
        searchButton.setOnAction(event -> searchButtonAction(event));
        addButton.setOnAction(event -> addButtonAction(event));
        updateButton.setOnAction(event -> updateButtonAction(event));
        resetButton.setOnAction(event -> resetButtonAction(event));
        routeNumTextField.setOnMouseClicked(event -> {
            if(routeNumTextField.isFocused()) {
                routeNumTextField.setStyle("");
            }
        });
        startPointTextField.setOnMouseClicked(event -> {
            if(startPointTextField.isFocused()) {
                startPointTextField.setStyle("");
            }
        });
        endPointTextField.setOnMouseClicked(event -> {
            if(endPointTextField.isFocused()) {
                endPointTextField.setStyle("");
            }
        });
        stopsTotalTextField.setOnMouseClicked(event -> {
            if(stopsTotalTextField.isFocused()) {
                stopsTotalTextField.setStyle("");
            }
        });
        timeLengthTextField.setOnMouseClicked(event -> {
            if(timeLengthTextField.isFocused()) {
                timeLengthTextField.setStyle("");
            }
        });
        distanceTextField.setOnMouseClicked(event -> {
            if(distanceTextField.isFocused()) {
                distanceTextField.setStyle("");
            }
        });
        currentDelayTextField.setOnMouseClicked(event -> {
            if(currentDelayTextField.isFocused()) {
                currentDelayTextField.setStyle("");
            }
        });
        customersAvgTextField.setOnMouseClicked(event -> {
            if(customersAvgTextField.isFocused()) {
                customersAvgTextField.setStyle("");
            }
        });
        timeLengthTextField.textProperty().addListener( (observable, oldValue, newValue) -> {
            timeFormatEnforceOnTextField(timeLengthTextField);
        });
        currentDelayTextField.textProperty().addListener( (observable, oldValue, newValue) -> {
            timeFormatEnforceOnTextField(currentDelayTextField);
        });
    }


    /**
     *  Initialize method for setting up the Route Manager Scene/Window/Page.
     *  Sets all buttons disabled, except reset and search.
     *  Applies setup methods.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        updateButton.setDisable(true);

        setRouteOnLoad();
        setupTableView();
        setAllClickActions();
    }
}