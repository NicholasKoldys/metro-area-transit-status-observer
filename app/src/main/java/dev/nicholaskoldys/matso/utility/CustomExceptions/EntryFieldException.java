package dev.nicholaskoldys.matso.utility.CustomExceptions;

import javafx.scene.control.Alert;

import java.time.LocalDateTime;

public class EntryFieldException extends Exception {


    /**
     * SPECIAL ALERT TYPES FOR SPECIFIC ERRORS ON SPECIFIC FIELDS.
     * -NON GENERIC
     */
    public enum ALERT_TYPE {
        BUS_REQUIRED_FIELDS,
        ROUTE_REQUIRED_FIELDS,
        BUSNUM_INCORRECT_FIELD_FORMAT,
        BUSNUM_INCORRECT_FIELD_VALUE,
        ROUTENUM_INCORRECT_FIELD_FORMAT,
        BUSROUTENUM_INCORRECT_FIELD_VALUE,
        ROUTENUM_INCORRECT_FIELD_VALUE,
        BUSBREAKDOWN_INCORRECT_FIELD_BOUNDS,
        BUS_INTEGER_INCORRECT_FIELD_BOUNDS,
        ROUTE_INTEGER_INCORRECT_FIELD_BOUNDS,
        ROUTE_TIME_INCORRECT_FIELD_BOUNDS;
    }

    public EntryFieldException(String errorMessage, ALERT_TYPE alert_type, String fieldValueOrFieldNames) {
        super(errorMessage);

        switch (alert_type) {

            case BUS_REQUIRED_FIELDS:
                RequiredFieldsAlert(
                        new String[]{
                                "Bus Number",
                                "Capacity",
                                "Maintenance State"
                        }, fieldValueOrFieldNames
                );
                break;
            case ROUTE_REQUIRED_FIELDS:
                RequiredFieldsAlert(
                        new String[]{
                                "Route Number",
                                "Start Point",
                                "End Point",
                                "Stops Total",
                                "Time Length",
                                "Distance",
                                "Current Delay",
                                "Customers Average"
                        }, fieldValueOrFieldNames
                );
                break;
            case BUSNUM_INCORRECT_FIELD_FORMAT:
                IncorrectFieldEntryFormatAlert(fieldValueOrFieldNames, "Bus Number", "###L", "3 Numbers/# and 1 Letter/L");
                break;
            case BUSNUM_INCORRECT_FIELD_VALUE:
                IncorrectFieldEntryExistingValueAlert(fieldValueOrFieldNames, "Bus Number", false);
                break;
            case ROUTENUM_INCORRECT_FIELD_FORMAT:
                IncorrectFieldEntryFormatAlert(fieldValueOrFieldNames, "Route Number", "C###L", "1 Cardinal Direction {N/S/E/W}/C and 3 Numbers/# and 1 Letter/L");
                break;
            case BUSROUTENUM_INCORRECT_FIELD_VALUE:
                IncorrectFieldEntryExistingValueAlert(fieldValueOrFieldNames, "Route Number", true);
                break;
            case ROUTENUM_INCORRECT_FIELD_VALUE:
                IncorrectFieldEntryExistingValueAlert(fieldValueOrFieldNames, "Route Number", false);
                break;
            case BUSBREAKDOWN_INCORRECT_FIELD_BOUNDS:
                String yearBound = "Year / YYYY: 1980-" + LocalDateTime.now().getYear();
                String monthBound ="Month / MM: 01-12";
                String dayBound ="Day / DD: 01-31";
                String hourBound ="Hour / HH: 00-24";
                String minBound ="Minute / MI: 00-59";
                String dateBound = yearBound + " " + monthBound + " " + dayBound + " " + hourBound + " " + minBound;
                IncorrectFieldEntryBoundsAlert(fieldValueOrFieldNames, "Last Breakdown", "YYYY-MM-DD HH:MI", dateBound);
                break;
            case BUS_INTEGER_INCORRECT_FIELD_BOUNDS:
                IncorrectFieldEntryBoundsAlert(fieldValueOrFieldNames, "Capacity", "Capacity: 0-11 digits", "");
                break;
            case ROUTE_INTEGER_INCORRECT_FIELD_BOUNDS:
                IncorrectFieldEntryBoundsAlert(fieldValueOrFieldNames, "Stops Total, Distance, and Customer Average", "Stops Total & Customer Average: 0-11 digits", "Distance: 2 digits and up to 2 decimal places");
                break;
            case ROUTE_TIME_INCORRECT_FIELD_BOUNDS:
                IncorrectFieldEntryBoundsAlert(fieldValueOrFieldNames, "Time Length and Current Delay", "HH:MI", "Time Length: 00:01-23:59\nCurrent Delay: 00:00-23:59\n(Time within a 24 hour period)");
                break;
        }
    }

    private void RequiredFieldsAlert(String[] requiredFieldsArray, String fieldNames) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        String requiredFields = "";
        for(String field : requiredFieldsArray) {
            requiredFields = requiredFields + "   " + field + ":\n";
        }
        a.setHeaderText("Please Correct the Missing Fields.\n\n\n"
                + "ALL the following fields must have a value:\n\n"
                + requiredFields + "\n");
        a.setContentText("Please review entries and correct missing fields: " + fieldNames);
        a.showAndWait();
    }

    private void IncorrectFieldEntryFormatAlert(String incorrectString, String textFieldName,
                                  String requiredFormat, String formatEnglishTranslation) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText("Please Enter a Valid " + textFieldName + "\n\n\n"
                + "The " + textFieldName + " field can only follow the designated format:\n\n"
                + "   " + requiredFormat + "\n"
                + "The " + textFieldName + " must contain " + formatEnglishTranslation +"\n\n");
        a.setContentText("Please review the " + textFieldName + " entry and correct: \n" + incorrectString
                +"\nAs it does not follow the proper format.");
        a.showAndWait();
    }

    private void IncorrectFieldEntryBoundsAlert(String incorrectString, String textFieldName,
                                                String requiredFormat, String requiredBounds) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText("Please Enter a Valid " + textFieldName + "\n\n\n"
                + "The " + textFieldName + " field can only be within the designated bounds:\n\n"
                + "   " + requiredFormat + "\n\n"
                + "   " + requiredBounds + "\n\n");
        a.setContentText("Please review the " + textFieldName + " entry and correct: \n" + incorrectString
                +"\nAs it does not follow the bounding rules.");
        a.showAndWait();
    }

    private void IncorrectFieldEntryExistingValueAlert(String incorrectString, String textFieldName, Boolean existing) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        String existingString;
        if(existing) {
            existingString = " field must have an existing value";
        } else {
            existingString = " field must NOT be an existing value";
        }
        a.setHeaderText("Please Correct the " + textFieldName + " Field.\n\n\n"
                + "The " + textFieldName + existingString + "\n\n");
        a.setContentText("Please review entries and correct the incorrect value: " + incorrectString);
        a.showAndWait();
    }
}
