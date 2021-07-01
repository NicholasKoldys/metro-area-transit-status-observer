package dev.nicholaskoldys.matso.controller;

import dev.nicholaskoldys.matso.SceneRouter;
import dev.nicholaskoldys.matso.database.Repository;
import dev.nicholaskoldys.matso.utility.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML Label titleLabel;
    @FXML Label warningLabel;

    @FXML Label contactNameLabel;
    @FXML Label contactEmailLabel;
    @FXML Label contactPhoneLabel;
    @FXML Label contactAddressLabel;

    @FXML TextField nameTextField;
    @FXML Button loginButton;

    @FXML TextField passwordTextField;
    @FXML Button cancelButton;

    @FXML Button adminModeButton;

    /**
     * search active users against entered userName and password
     *
     * if correct load next scene and mark user as current
     * else throw alert box.
     *
     * @param event
     */
    @FXML
    private void loginButtonAction(ActionEvent event) {
        if (Repository.getInstance().userLogin(nameTextField.getText(),
                passwordTextField.getText())) {
            System.out.println("ACTIVE USER CONFIRMED");
            SceneRouter.getInstance().loadScene(Constants.SceneView.DASHBOARD_SCENE);
        } else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Incorrect Username or Password");
            a.setContentText("Please Enter a Correct Username and Password");
            a.showAndWait();
        }
    }

    /**
     * Remove all entered information
     *
     * @param event
     */
    @FXML
    private void cancelButtonAction(ActionEvent event) {
        nameTextField.clear();
        passwordTextField.clear();
    }


    /**
     * TEMPORARY ADMIN ACCESS MODE - Remove for future use.
     *
     */
    @FXML
    private void adminModeButtonAction(ActionEvent event) {
        nameTextField.setText("NKoldys");
        passwordTextField.setText("futurefunk");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameTextField.setPromptText("enter name");
        passwordTextField.setPromptText("enter password");
        loginButton.setOnAction(event -> loginButtonAction(event));
        cancelButton.setOnAction(event -> cancelButtonAction(event));
        adminModeButton.setOnAction(event -> adminModeButtonAction(event));

        /*
         * TODO TEMP LOGIN
         */
        nameTextField.setText("test");
        passwordTextField.setText("secret");
    }
}
