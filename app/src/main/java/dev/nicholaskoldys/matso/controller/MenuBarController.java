package dev.nicholaskoldys.matso.controller;

import dev.nicholaskoldys.matso.SceneRouter;
import dev.nicholaskoldys.matso.database.Repository;
import dev.nicholaskoldys.matso.utility.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuBarController extends MenuBar implements Initializable {

    @FXML Menu dashboardItem;
    Label dashboardMenuLabel;
    @FXML Menu manageMenu;
    @FXML MenuItem busesItem;
    @FXML MenuItem routesItem;
    @FXML Menu reportsItem;
    Label reportsMenuLabel;
    @FXML MenuItem aboutItem;

    /**
     *  The Controller is currently used to control the access of menubar options on a user whitelist basis.
     *  The MenuBar is included in scenes via fxml included controller <MenuBarController fx:id="menuBar"/>
     *  With this inclusion the menu bar is created through its constructor.
     */
    /* TODO must find way to add controller without code as the call in
            Dashboard.fxml -> <MenuBarController fx:id="menuBar"/> */
    public MenuBarController() {
        URL path = SceneRouter.class.getResource("/view/MenuBar.fxml");
        FXMLLoader loader = new FXMLLoader(path);
        loader.setRoot(MenuBarController.this);
        loader.setController(MenuBarController.this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mouse Event Only, for the label "Dashboard" in the menu bar
     *
     * @param event
     */
    private void menuItemDashboardAction(MouseEvent event) {
        System.out.println("Dashboard Menu");
        SceneRouter.getInstance().loadScene(Constants.SceneView.DASHBOARD_SCENE);
    }

    /**
     *
     */
    public void menuItemManageBusesAction(ActionEvent event) {
        SceneRouter.getInstance().loadScene(Constants.SceneView.BUSES_SCENE);
    }

    /**
     *
     */
    public void menuItemManageRoutesAction(ActionEvent event) {
        SceneRouter.getInstance().loadScene(Constants.SceneView.ROUTES_SCENE);
    }

    /**
     * Mouse Event Only, for the label "Reports" in the menu bar
     * @param event
     */
    public void menuItemReportsAction(MouseEvent event) {
        SceneRouter.getInstance().loadScene(Constants.SceneView.REPORTS_SCENE);
    }

    /*public void menuItemAboutAction(ActionEvent event) {
        System.out.println("Temporary Test Page DELETE ME");
        SceneRouter.getInstance().loadScene(Constants.SceneView.TEST);
    }*/


    /**
     * This function is TEMPORARY and should not be used in a production environment
     * This function grabs the currentUser and checks the it across a List of Names or whitelist of users
     * that are able to access certain methods/functions of the application.
     */
    public void loadUsersMenu() {

        dashboardMenuLabel = new Label("Dashboard");
        dashboardItem.setText(""); //Remove placeholder text so label is visible
        dashboardItem.setGraphic(dashboardMenuLabel);
        dashboardMenuLabel.setOnMouseClicked(event -> menuItemDashboardAction(event));

        reportsMenuLabel = new Label("Reports");
        reportsItem.setText(""); //Remove placeholder text so label is visible
        reportsItem.setGraphic(reportsMenuLabel);
        reportsMenuLabel.setOnMouseClicked(event -> menuItemReportsAction(event));

        /*aboutItem.setOnAction(event -> menuItemAboutAction(event));*/

        for(int i=0; i < Constants.WHITELIST_ADMIN.length; i++) {
            if(Constants.WHITELIST_ADMIN[i].equals((Repository.getInstance().getCurrentUser().getUserName().toUpperCase()))) {
                busesItem.setOnAction(event -> menuItemManageBusesAction(event));
                routesItem.setOnAction(event -> menuItemManageRoutesAction(event));
            } else {
                manageMenu.setVisible(false);
                busesItem.setVisible(false);
                routesItem.setVisible(false);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadUsersMenu();
    }
}
