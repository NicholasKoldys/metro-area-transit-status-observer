package dev.nicholaskoldys.matso.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import dev.nicholaskoldys.matso.SceneRouter;
import dev.nicholaskoldys.matso.database.Repository;
import dev.nicholaskoldys.matso.model.Bus;
import dev.nicholaskoldys.matso.model.Route;
import dev.nicholaskoldys.matso.utility.Constants;
import dev.nicholaskoldys.matso.utility.Constants.MaintenanceState;
import dev.nicholaskoldys.matso.utility.DateTimeService;
import dev.nicholaskoldys.matso.utility.CustomExceptions.EntryFieldException;
import dev.nicholaskoldys.matso.utility.SuggestionTextField;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import static javafx.collections.FXCollections.observableArrayList;

public class BusesController implements Initializable {

    Repository repo = Repository.getInstance();

    List<MaintenanceState> maintenanceStateList;
    ObservableList<MaintenanceState> maintenanceStateComboSelectionList;

    Bus selectedBus;
    Route selectedRoute;

    @FXML Label busLabel;
    @FXML Label routeLabel;
    @FXML TextField searchTextField;
    @FXML TextField busNumTextField;
    @FXML TextField capacityTextField;
    @FXML SuggestionTextField routeTextField;
    @FXML ComboBox maintenanceStateComboBox;
    @FXML SuggestionTextField lastBreakdownTextField;
    @FXML Hyperlink routeRecordHyperLink;

    @FXML TableView<Bus> busTableView;
    @FXML TableColumn busNumCol;
    @FXML TableColumn capacityCol;
    @FXML TableColumn routeCol;
    @FXML TableColumn maintenanceStateCol;
    @FXML TableColumn lastBreakdownCol;

    @FXML Button searchButton;
    @FXML Button addButton;
    @FXML Button updateButton;
    @FXML Button deleteButton;
    @FXML Button resetButton;

    /**
     * Action Event to get row selected from busTableView, load Bus and store into selectedTable variable,
     * applying all available Bus attributes to textFields.
     * Enable Add, Update, and Delete Buttons.
     * @param event
     */
    private void tableViewSelectionAction(MouseEvent event) {
        if(busTableView.getSelectionModel().getSelectedItem() != null) {

            /*
            * Load Bus object from the table, if bus object has a route load the route object as well
            */
            selectedBus = busTableView.getSelectionModel().getSelectedItem();
            selectedRoute = repo.lookupRoute(selectedBus.getRouteId());

            /*
            * Set textField strings and heading labels with data from selectedBus Object
            */
            if(selectedBus != null) {
                busNumTextField.setText(selectedBus.getBusNum());
                capacityTextField.setText(Integer.toString(selectedBus.getCapacity()));
                busLabel.setText("Bus: " + selectedBus.getBusNum());
                maintenanceStateComboBox.getSelectionModel().select(selectedBus.getMaintenanceState());
                if(selectedBus.getLastBreakdown() != null) {
                    lastBreakdownTextField.setText(selectedBus.getLastBreakdown().format(DateTimeService.LASTBREAKDOWN_FORMAT));
                } else {
                    lastBreakdownTextField.setText("");
                }
            }

            /*
            * Buses don't always carry route data, if they do set textField strings, heading labels, and route hyperlink
            * for the selectedBus Object's selectedRoute Object
            */
            if(selectedRoute != null) {
                routeRecordHyperLink.setDisable(false);
                routeTextField.setText(selectedRoute.getRouteNum());
                routeLabel.setText("Route: " + selectedRoute.getRouteNum());
            } else {
                routeRecordHyperLink.setDisable(true);
                routeTextField.setText("");
                routeLabel.setText("Route: n/a");
            }

            /*
            * Enable buttons with loaded selectedBus, ONLY Location to enable buttons
            */
            addButton.setDisable(false);
            updateButton.setDisable(false);
            deleteButton.setDisable(false);
        }
    }


    /**
     * When Search Button is clicked, perform content search based on:
     *      BusNum, Bus linked RouteNum, MaintenanceState, and Capacity
     *
     * @param event
     */
    @FXML
    private void searchButtonAction(ActionEvent event) {
        String searchStr = searchTextField.getText().trim().toUpperCase();
        busTableView.setItems(repo.lookupBuses(searchStr));
    }


    /**
     * Links a selectedBus' route to the Route Manager Scene/Page, the page will load the route on entry
     *
     * @param event
     */
    @FXML
    private void routeRecordHyperLinkAction(ActionEvent event) {
        if (!routeRecordHyperLink.isDisabled()) {
            if(!routeTextField.getText().isEmpty()) {
                selectedRoute = repo.lookupRoute(routeTextField.getText());
                SceneRouter.getInstance().loadScene(Constants.SceneView.ROUTES_SCENE, selectedRoute);
            }
        }
    }


    /**
     * When Add Button is clicked,
     * first test all entries into each textField,
     * second parse fields and set nulls where necessary,
     * third create a New Bus Object and attempt to submit the Bus to the database.
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
                 *   Normalize Bus Num and correct to Database format, if missed by regex in tests
                 */
                String busNumStr = busNumTextField.getText().trim().toUpperCase();
                busNumTextField.setText(busNumStr);//set field to uppercase and no space version

                /*
                 *   Remove spaces in Capacity textField, if missed by regex in tests
                 */
                String capacityStr = capacityTextField.getText().trim();
                capacityTextField.setText(capacityStr);

                /*
                 *   Route Text Field is allowed to be empty, so must check for null
                 * //routeNum = null -> does not replace text in text field, so empty string = "" will do.
                 */
                int routeId = 0;
                String routeNum = "";
                if (setSelectedRoute()) {
                    routeId = selectedRoute.getRouteId();
                    routeNum = selectedRoute.getRouteNum();
                }

                /*
                 *   Last Breakdown is allowed to be empty, so must check for null.
                 *   Normalize Last Breakdown and correct to Database format, if missed by regex in tests
                 *   //not necessary but included if changeListener in method 'setupLastBreakdownTestingTextField()' is not used.
                 */
                LocalDateTime lastBreakdownDate = null;
                if (!lastBreakdownTextField.getText().isEmpty()) {
                    String lastBreakdownDateStr = lastBreakdownTextField.getText().trim();
                    lastBreakdownTextField.setText(lastBreakdownDateStr);//set field to uppercase and no space version
                    lastBreakdownDate = LocalDateTime.parse(lastBreakdownTextField.getText(), DateTimeService.LASTBREAKDOWN_FORMAT);
                }

                /*
                 * Create a New Bus Object and make it the active Bus on the Page
                 */
                selectedBus = new Bus(
                        busNumTextField.getText(),
                        Integer.valueOf(capacityTextField.getText()),
                        routeId,
                        routeNum,
                        (MaintenanceState) maintenanceStateComboBox.getSelectionModel().getSelectedItem(),
                        lastBreakdownDate
                );

                /*
                 * Attempt to submitBus to database
                 */
                try {
                    if (repo.submitBus(selectedBus)) {
                        busTableView.refresh();
                    } else {

                        /*
                         * If fails mark BusNum textField with red background
                         */
                        busNumTextField.setStyle("-fx-control-inner-background: #" + Constants.INCORRECT_COLOR);
                        throw new EntryFieldException("Warning::Bus Number is Incorrect::",
                                EntryFieldException.ALERT_TYPE.BUSNUM_INCORRECT_FIELD_VALUE, busNumTextField.getText());
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
            if(isTextFieldsPassTests()) {

                /*
                 *   Normalize Bus Num and correct to Database format, if missed by regex in tests
                 */
                String busNumStr = busNumTextField.getText().trim().toUpperCase();
                busNumTextField.setText(busNumStr);//set field to uppercase and no space version

                /*
                 *   Remove spaces in Capacity textField, if missed by regex in tests
                 */
                String capacityStr = capacityTextField.getText().trim();
                capacityTextField.setText(capacityStr);

                /*
                 *   Route Text Field is allowed to be empty, so must check for null
                 * //routeNum = null -> does not replace text in text field, so empty string = "" will do.
                 */
                int routeId = 0;
                String routeNum = "";
                if (setSelectedRoute()) {
                    routeId = selectedRoute.getRouteId();
                    routeNum = selectedRoute.getRouteNum();
                }

                /*
                 *   Last Breakdown is allowed to be empty, so must check for null.
                 *   Normalize Last Breakdown and correct to Database format, if missed by regex in tests
                 *   //not necessary but included if changeListener in method 'setupLastBreakdownTestingTextField()' is not used.
                 */
                LocalDateTime lastBreakdownDate = null;
                if (!lastBreakdownTextField.getText().isEmpty()) {
                    String lastBreakdownDateStr = lastBreakdownTextField.getText().trim();
                    lastBreakdownTextField.setText(lastBreakdownDateStr);//set field to uppercase and no space version
                    lastBreakdownDate = LocalDateTime.parse(lastBreakdownTextField.getText(), DateTimeService.LASTBREAKDOWN_FORMAT);
                }

                /*
                 * Take active selectedBus make a copy of previous data and update data for the original Object
                 *  (data can later be recovered, avoiding tableView updating false information if adding fails)
                 */
                Bus oldSelectedBus = selectedBus.copy();
                selectedBus.setBusNum(busNumTextField.getText());
                selectedBus.setCapacity(Integer.valueOf(capacityTextField.getText()));
                selectedBus.setRouteId(routeId);
                selectedBus.setRouteNum(routeNum);
                selectedBus.setMaintenanceState((MaintenanceState) maintenanceStateComboBox.getSelectionModel().getSelectedItem());
                selectedBus.setLastBreakdown(lastBreakdownDate);

                /*
                 * Attempt to updateBus in the database
                 */
                try {
                    if(repo.updateBus(selectedBus)) {
                        busTableView.refresh();
                    } else {
                        throw new EntryFieldException("Warning::Bus Number is Incorrect::",
                                EntryFieldException.ALERT_TYPE.BUSNUM_INCORRECT_FIELD_VALUE, busNumTextField.getText());
                    }
                } catch (EntryFieldException tex) {
                    System.out.println(tex.getMessage());

                    /*
                     * If fails mark textField with red background
                     * and reset object's Bus fields to previous fields
                     */
                    busNumTextField.setStyle("-fx-control-inner-background: #" + Constants.INCORRECT_COLOR);
                    selectedBus.setBusNum(oldSelectedBus.getBusNum());
                    selectedBus.setCapacity(oldSelectedBus.getCapacity());
                    selectedBus.setRouteId(oldSelectedBus.getRouteId());
                    selectedBus.setRouteNum(oldSelectedBus.getRouteNum());
                    selectedBus.setMaintenanceState(oldSelectedBus.getMaintenanceState());
                    selectedBus.setLastBreakdown(oldSelectedBus.getLastBreakdown());
                }
            }
        }
    }


    /**
     * When Delete Button is clicked,
     * Send Alert Confirmation Window, if confirmed set Bus to be deleted.
     * @param event
     */
    @FXML
    private void deleteButtonAction(ActionEvent event) {
        if(selectedBus != null) {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setContentText(
                    "You selected to DELETE the selected bus: \n\n"
                            + selectedBus.getBusNum()
                            + "\n\n"
                            + "Delete the Bus by Pressing Ok..");
            a.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {
                        repo.deleteBus(selectedBus);
                    });
        }
    }


    /**
     * When Reset Button is clicked,
     * Set busTableView with Bus Observable Object List from Repository
     * Clear All textFields, setting them empty.
     * Disable buttons Update and Delete; buttons are only enabled with tableViewSelectionAction() method
     * @param event
     */
    @FXML
    private void resetButtonAction(ActionEvent event) {
        busTableView.setItems(repo.getBusList());
        busTableView.refresh();
        setAllFieldsEmpty();
        setAllClickActions();
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }


    /**
     * Method called in updateButtonAction() and addButtonAction() methods,
     * Tests result in Pass or Fail, if failed then given textField is set to a red background and
     *      an appropriate Alert with the EntryFieldExceptions's AlertType is followed explaining the failure.
     *
     * Tests:
     *  Bus Number, Capacity, Maintenance State Fields for non-empty entries.
     *  Bus Number textField follows "###L" formatting.  # is a number, L is a letter
     *  Capacity textField allows only 1-11 digit Integers.
     *  Route textField follows "C###L" formatting.   C is a cardinal direction, # is a number, L is a letter
     *  Route textField must match an existing routeNum. Before testing, the textField is normalized and set to the textField
     *  Last Breakdown textField follows "yyyy-MM-dd HH:mm" formatting. y is years, M is months, d is days, H is hours, m is minutes
     *
     * @return True if all tests are passed, false if a single test fails.
     */
    private boolean isTextFieldsPassTests() {
        try {
            /*
            * call getAllEmptyFields and use the List of strings of empty field names
            * to test the Bus Number, Capacity, and Maintenance State for entries.
            * set background to red for empty textFields.
            */
            if (!getAllEmptyFields().isEmpty()) {
                List<String> emptyFields = getAllEmptyFields();
                for (String str : emptyFields) {
                    if (str.equals("Bus Number")) {
                        busNumTextField.setStyle("-fx-control-inner-background: #" + Constants.INCORRECT_COLOR);
                    }
                    if (str.equals("Capacity")) {
                        capacityTextField.setStyle("-fx-control-inner-background: #" + Constants.INCORRECT_COLOR);
                    }
                }
                throw new EntryFieldException(
                        "Warning::Missing Fields Detected for Bus Submission::",
                        EntryFieldException.ALERT_TYPE.BUS_REQUIRED_FIELDS, getAllEmptyFields().toString());
            }

            /*
            * match Bus Number textField against regex string
            * set background to red.
            */
            if (!busNumTextField.getText().matches("[0-9]{3}[A-Za-z]")) {
                busNumTextField.setStyle("-fx-control-inner-background: #" + Constants.INCORRECT_COLOR);
                throw new EntryFieldException(
                        "Warning::Bus Number Formatting is Incorrect::",
                        EntryFieldException.ALERT_TYPE.BUSNUM_INCORRECT_FIELD_FORMAT, busNumTextField.getText());
            }

            /*
             * match Capacity textField against regex string
             * set background to red.
             */
            if (!capacityTextField.getText().matches("[0-9]{1,11}")) {
                capacityTextField.setStyle("-fx-control-inner-background: #" + Constants.INCORRECT_COLOR);
                throw new EntryFieldException(
                        "Warning::an Integer/Number value(s) is Incorrect::",
                        EntryFieldException.ALERT_TYPE.BUS_INTEGER_INCORRECT_FIELD_BOUNDS, capacityTextField.getText());
            }

            /*
             * match Route textField against regex string, if not empty (route allows nulls)
             * set background to red.
             */
            if (!routeTextField.getText().matches("[NSEWnsew][0-9]{3}[A-Za-z]")
                    && !routeTextField.getText().isEmpty()) {
                routeTextField.setStyle("-fx-control-inner-background: #" + Constants.INCORRECT_COLOR);
                throw new EntryFieldException(
                        "Warning::Route Number Formatting is Incorrect::",
                        EntryFieldException.ALERT_TYPE.ROUTENUM_INCORRECT_FIELD_FORMAT, routeTextField.getText());
            }

            /*
             * normalize Route textField, set is back to textField.
             * attempt to load route into selectedRoute from string value entry.
             * if selectedRoute comes up NULL, throw exception and set background red.
             */
            if (!routeTextField.getText().isEmpty()) {
                String routeNumStr = routeTextField.getText().trim().toUpperCase();
                routeTextField.setText(routeNumStr);//set field to uppercase and no space version if incorrect
                selectedRoute = repo.lookupRoute(routeNumStr);
                if (selectedRoute == null) {
                    routeTextField.setStyle("-fx-control-inner-background: #" + Constants.INCORRECT_COLOR);
                    throw new EntryFieldException("Warning::Route Number is Incorrect::",
                            EntryFieldException.ALERT_TYPE.BUSROUTENUM_INCORRECT_FIELD_VALUE, routeTextField.getText());
                }
            }

            /*
             * match Last Breakdown textField against regex string, if not empty (lastBreakdown allows nulls)
             * set background to red.
             */
            if (!lastBreakdownTextField.getText().isEmpty()) {
                if (!lastBreakdownTextField.getText().matches("(?<year>[0-9]{4})-(?<month>[0-9]{2})-(?<day>[0-9]{2})\\s(?<hour>[0-9]{2}):(?<min>[0-9]{2})")) {
                    lastBreakdownTextField.setStyle("-fx-control-inner-background: #" + Constants.INCORRECT_COLOR);
                    throw new EntryFieldException(
                            "Warning::Bus' Last Breakdown Bounds Do Not Match With Required Bounds::",
                            EntryFieldException.ALERT_TYPE.BUSBREAKDOWN_INCORRECT_FIELD_BOUNDS, lastBreakdownTextField.getText());
                }
            }
        } catch (EntryFieldException tex) {
            System.out.println(tex.getMessage());
            return false;
        }
        return true;
    }


    /**
     * Maintenance State ComboBox contains 3 values:
     *      OK , SERVICE , OUT_OF_ORDER
     *
     * values are stored in constants in utility.Constants class.
     * values are stored into an ObservableArrayList to be used with a ComboBox.
     */
    private void setupMaintenanceStateComboBox() {
        maintenanceStateList = new ArrayList<>();
        for (MaintenanceState state : MaintenanceState.values()) {
            maintenanceStateList.add(state);
        }
        maintenanceStateComboSelectionList = FXCollections.observableArrayList(maintenanceStateList);
        maintenanceStateComboBox.setItems(maintenanceStateComboSelectionList);
    }


    /**
     *  Listener for DateTime textFields.
     *  When text is changed or added to the textField, the cascading if statements disallow further typing
     *  if the string of entered text does not apply to the regex string.
     */
    private void dateTimeFormatEnforceOnTextField(TextField field) {
            String input = field.getText();
            int maxLength = 15;
            String regexDateTime = "(?<year>[0-9]{4})-(?<month>[0-1][0-9])-(?<day>[0-3][0-9])\\s(?<hour>[0-2][0-9]):(?<min>[0-5][0-9])";
            if(!input.matches(regexDateTime)) {//base pattern
                if(input.length() != 0) {
                    String correctedOutput = String.valueOf(input.toCharArray(), 0, input.length() - 1);
                    char c = input.charAt(input.length() - 1);
                    if(input.length()-1 > maxLength) {
                        field.setText(correctedOutput);
                    } else if(input.length()-1 == 4
                            || input.length()-1 == 7) {
                        if(!String.valueOf(c).matches("-")) {
                            field.setText(correctedOutput);
                        }
                    } else if(input.length()-1 == 5) {
                        if(!String.valueOf(c).matches("[0-1]")) {
                            field.setText(correctedOutput);
                        }
                    } else if(input.length()-1 == 6) {
                        char h1 = input.charAt(5);
                        if(h1 == '1' && !String.valueOf(c).matches("[0-2]")) {
                            field.setText(correctedOutput);
                        }
                    } else if(input.length()-1 == 8) {
                        if(!String.valueOf(c).matches("[0-3]")) {
                            field.setText(correctedOutput);
                        }
                    } else if(input.length()-1 == 9) {
                        char h1 = input.charAt(8);
                        if(h1 == '3' && !String.valueOf(c).matches("[0-1]")) {
                            field.setText(correctedOutput);
                        }
                    } else if(input.length()-1 == 10) {
                        if (!String.valueOf(c).matches("\\s")) {
                            //c = 'T';
                            //correctedOutput = correctedOutput + c;
                            field.setText(correctedOutput);
                        }
                    } else if(input.length()-1 == 11) {
                        if(!String.valueOf(c).matches("[0-2]")) {
                            field.setText(correctedOutput);
                        }
                    } else if(input.length()-1 == 12) {
                        char h1 = input.charAt(11);
                        if(h1 == '2' && !String.valueOf(c).matches("[0-3]")) {
                            field.setText(correctedOutput);
                        }
                    } else if(input.length()-1 == 13
                            || input.length()-1 == 16) {
                        if (!String.valueOf(c).matches(":")) {
                            field.setText(correctedOutput);
                        }
                    } else if(input.length()-1 == 14) {
                        if(!String.valueOf(c).matches("[0-5]")) {
                            field.setText(correctedOutput);
                        }
                    } else if(input.length()-1 == 15) {
                        char h1 = input.charAt(11);
                        char h2 = input.charAt(12);
                        char m1 = input.charAt(14);
                        if(h1 == '0' && h2 == '0' && m1 == '0' && !String.valueOf(c).matches("[1-9]")) {
                            field.setText(correctedOutput);
                        }
                    } else if(!String.valueOf(c).matches("[0-9]")) {
                        field.setText(correctedOutput);
                    }
                }
            }
    }

    /**
     *  In a background JavaFX thread, setup the busTableView by
     *  loading a list of Bus objects from Repository and applying column
     *  cellValueFactories for each observable attribute, on the available columns
     *  defined in the .fxml file.
     * @return
     */
    private void setupTableView() {
        Platform.runLater(() -> {
            busTableView.setItems(repo.getBusList());
            busNumCol.setCellValueFactory(new PropertyValueFactory<>("BusNum"));
            capacityCol.setCellValueFactory(new PropertyValueFactory<>("Capacity"));
            routeCol.setCellValueFactory(new PropertyValueFactory<>("RouteNum"));
            maintenanceStateCol.setCellValueFactory(new PropertyValueFactory<>("MaintenanceState"));
            lastBreakdownCol.setCellValueFactory(new PropertyValueFactory<>("LastBreakdown"));
        });
    }


    /**
     * Capture all Empty textFields, store them in a list by a string identifier.
     * @return List of String Names of empty textFields:
     *      Bus Number, Capacity, or Maintenance State
     */
    private List<String> getAllEmptyFields() {
        List<String> emptyFields = new ArrayList<>();
        if (busNumTextField.getText().isEmpty()) {
            emptyFields.add("Bus Number");
        }
        if (capacityTextField.getText().isEmpty()) {
            emptyFields.add("Capacity");
        }
        if (maintenanceStateComboBox.getSelectionModel().getSelectedItem() == null) {
            emptyFields.add("Maintenance State");
        }
        return emptyFields;
    }


    /**
     *  Set all textFields empty/clear
     */
    private void setAllFieldsEmpty() {
        searchTextField.clear();
        busNumTextField.clear();
        capacityTextField.clear();
        routeTextField.clear();
        maintenanceStateComboBox.getSelectionModel().clearSelection();
        lastBreakdownTextField.clear();
        routeRecordHyperLink.setDisable(true);
    }


    /**
     *  Set buttons with appropriate methods for Action Events,
     *  reset Style on textFields that are clicked, undoes the red background.
     */
    private void setAllClickActions() {
        busTableView.setOnMouseClicked(event -> tableViewSelectionAction(event));
        searchButton.setOnAction(event -> searchButtonAction(event));
        routeRecordHyperLink.setOnAction(event -> routeRecordHyperLinkAction(event));
        addButton.setOnAction(event -> addButtonAction(event));
        updateButton.setOnAction(event -> updateButtonAction(event));
        deleteButton.setOnAction(event -> deleteButtonAction(event));
        resetButton.setOnAction(event -> resetButtonAction(event));
        busNumTextField.setOnMouseClicked(event -> {
            if(busNumTextField.isFocused()) {
                busNumTextField.setStyle("");
            }
        });
        capacityTextField.setOnMouseClicked(event -> {
            if(capacityTextField.isFocused()) {
                capacityTextField.setStyle("");
            }
        });
        routeTextField.setOnMouseClicked(event -> {
            if(routeTextField.isFocused()) {
                routeTextField.setStyle("");
            }
        });
        maintenanceStateComboBox.setOnMouseClicked(event -> {
            if(maintenanceStateComboBox.isFocused()) {
                maintenanceStateComboBox.setStyle("");
            }
        });
        lastBreakdownTextField.setOnMouseClicked(event -> {
            lastBreakdownTextField.showSuggestionMenu();
            if(lastBreakdownTextField.isFocused()) {
                lastBreakdownTextField.setStyle("");
            }
        });
        lastBreakdownTextField.textProperty().addListener( (observable, oldValue, newValue) -> {
            dateTimeFormatEnforceOnTextField(lastBreakdownTextField);
        });
    }


    /**
     *  Set the variable selectedRoute with a Route named in the routeTextField.
     * @return true if selectedRoute equates to a existing route, false if it does not.
     */
    private boolean setSelectedRoute() {
        if(!routeTextField.getText().isEmpty()) {
            selectedRoute = repo.lookupRoute(routeTextField.getText());
            if(selectedRoute != null) {
                return true;
            }
        }
        return false;
    }


    /**
     *  Initialize method for setting up the Bus Manager Scene/Window/Page.
     *  Sets all buttons disabled, except reset and search.
     *  Applies setup methods.
     *  Creates AutoComplete Menus for routeTextField and lastBreakdownTextField
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        routeRecordHyperLink.setDisable(true);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);

        /*
         * Setup Methods
         */
        setupMaintenanceStateComboBox();
        //setupLastBreakdownTestingTextField();
        setupTableView();
        setAllClickActions();


        /*
         * Setup Suggestions for AutoComplete
         */
        Platform.runLater(() -> {

            /*
            * Suggest only existing routes from repo's route list
            */
            LinkedList<String> routeNums = new LinkedList<>();
            for( Route route : repo.getRouteList()) {
                routeNums.add(route.getRouteNum());
            }
            routeTextField.setSuggestionList(routeNums);

            /*
            * Suggest only today's date
            */
            LinkedList<String> dateNow = new LinkedList<>();
            LocalDateTime now = LocalDateTime.now();
            dateNow.add(now.format(DateTimeService.LASTBREAKDOWN_FORMAT));
            lastBreakdownTextField.setSuggestionList(dateNow);
        });
    }
}
