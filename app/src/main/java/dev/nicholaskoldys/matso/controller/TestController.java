package dev.nicholaskoldys.matso.controller;

import dev.nicholaskoldys.matso.model.Route;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import dev.nicholaskoldys.matso.dao.implementation.RouteDaoImpl;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class TestController implements Initializable {

    @FXML private Label label1;
    @FXML private Label label2;

    @FXML private TextField field1;

    @FXML private Button searchBtn;
    @FXML private Button editBtn;
    @FXML private Button dashBtn;
    @FXML private Button getBtn;

    @FXML
    public void setGetBtn(ActionEvent event) {

//        if(UserDaoImpl.getInstance().loginAttempt("test2", "test2")) {
//            String str = label1.getText() + "\n PASSED";
//            label1.setText(str);
//        } else {
//            String str = label1.getText() + "\n FAILED";
//            label1.setText(str);
//        }

        Route ro = RouteDaoImpl.getInstance().getById(1);
        ro.setRouteNum("01A");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        label1.setText("This is the Date right now " + LocalDateTime.now());

        label2.setText("Hello World");

        field1.textProperty().addListener((observable, oldValue, newValue) -> {
                String input = field1.getText();

                String regex = "[0-2][0][0-2][0-9]-[0-1][0-9]";

                if (!input.matches(regex)) {
                    if (input.length() != 1) {
                        String correctedOutput = String.valueOf(input.toCharArray(), 0, input.length() - 1);
                        char c = input.charAt(input.length() - 1);
                            field1.setText(correctedOutput);
                    }
                }
        });


//        field1.textProperty().addListener((observable, oldValue, newValue) -> {
//            String input = field1.getText();
//
//            //String regexDecial = "[0-9]{0,4}(\\.??[0-9]{0,4}?)";
//            String regexDecial = "[0-9]{0,4}(\\.[0-9]{0,4})?";
//            //String regexNum = "[0-9]{1,11}";
//            if(!input.matches(regexDecial)) {
//                if(input.length() != 0) {
//                    String correctedOutput = String.valueOf(input.toCharArray(), 0, input.length() - 1);
//                    char c = input.charAt(input.length() - 1);
//                    if (input.length()-1 > 7) {
//                        field1.setText(correctedOutput);
//                    } else if (input.length()-1 == 4) {
//                        if(!String.valueOf(c).matches("\\.") || !input.contains(".")) {
//                            field1.setText(correctedOutput);
//                        }
//                        field1.setText(correctedOutput);
//                    } else if (!String.valueOf(c).matches("[0-9]")) {
//                        field1.setText(correctedOutput);
//                    }
//                }
//            }
//        });


//        field1.textProperty().addListener( (observable, oldValue, newValue) -> {
//            String input = field1.getText();
//            int maxLength = 15;
//            String regexDateTime = "(?<year>[0-9]{4})-(?<month>[0-9]{2})-(?<day>[0-9]{2})\\s(?<hour>[0-9]{2}):(?<min>[0-9]{2})";
//            //Pattern dateTimePattern = Pattern.compile(regexDateTime);
//            //Matcher matcher =  dateTimePattern.matcher(input);
//
//            if(!input.matches(regexDateTime)) {//base pattern
//                if(input.length() != 0) {
//                    String correctedOutput = String.valueOf(input.toCharArray(), 0, input.length() - 1);
//                    char c = input.charAt(input.length() - 1);
//                    if(input.length()-1 > maxLength) {
//                        field1.setText(correctedOutput);
//                    } else if(input.length()-1 == 4
//                            || input.length()-1 == 7) {
//                        if(!String.valueOf(c).matches("-")) {
//                            field1.setText(correctedOutput);
//                        }
//                    } else if(input.length()-1 == 10) {
//                        if(!String.valueOf(c).matches("\\s")) {
//                            //c = 'T';
//                            //correctedOutput = correctedOutput + c;
//                            field1.setText(correctedOutput);
//                        }
//                    } else if(input.length()-1 == 13
//                                || input.length()-1 == 16) {
//                        if(!String.valueOf(c).matches(":")) {
//                            field1.setText(correctedOutput);
//                        }
//                    } else if(!String.valueOf(c).matches("[0-9]")) {
//                        field1.setText(correctedOutput);
//                    }
//                }
//            }
//        });



        // else if(input.length()-1 == 15) {
        //                        char h1 = input.charAt(11);
        //                        char h2 = input.charAt(12);
        //                        char m1 = input.charAt(14);
        //                        if(h1 == '0' && h2 == '0' && m1 == '0' && !String.valueOf(c).matches("[1-9]")) {
        //                            lastBreakdownTextField.setText(correctedOutput);
        //                        }
    }
}
