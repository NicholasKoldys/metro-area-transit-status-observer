package dev.nicholaskoldys.matso;

import dev.nicholaskoldys.matso.database.DBConnection;
import dev.nicholaskoldys.matso.database.Database;
import dev.nicholaskoldys.matso.database.DatabaseInitializer;
import dev.nicholaskoldys.matso.database.Repository;
import dev.nicholaskoldys.matso.utility.Constants;
import javafx.application.Application;
import javafx.stage.Stage;

public class MATSOApplicationMain extends Application {

    private Database database;

    /**
     * Init performs initialization methods:
     *     setup Database Connection.
     *     setup Database and Repository.
     *
     * @throws Exception
     */
    @Override
    public void init() throws Exception {

        super.init();
        DBConnection.setConnection();
        DatabaseInitializer.initializeDatabase();
        database = Database.getInstance();
    }


    /**
     * Load the default Scene - defined in Constants.
     *
     * @param primaryStage pass stage to Scene Router
     */
    @Override
    public void start(Stage primaryStage) {

        SceneRouter.getInstance(primaryStage).loadScene(Constants.APP_START_SCENE);
    }


    /**
     * Logout CurrentUser in loggedIn.
     * Disconnect Database.connection
     */
    @Override
    public void stop() {

        Repository.getInstance().logoutUser();
        DBConnection.disconnect();
    }


    /**
     * Launch the main stage of the application.
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
