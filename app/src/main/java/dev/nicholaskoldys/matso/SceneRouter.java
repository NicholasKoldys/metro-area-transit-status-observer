package dev.nicholaskoldys.matso;

import dev.nicholaskoldys.matso.utility.Constants;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneRouter {

    private static Stage mainWindow;
    public static SceneRouter instance = null;
    public static Object objectHolder;


    /**
     * Router - all scene changes processed in this class
     *
     * @param mainStage internal method.
     */
    private SceneRouter(Stage mainStage) {
        setMainStage(mainStage);
    }


    /**
     * Allow one instance
     *
     * @param mainStage Main method start init.
     * @return
     */
    public static SceneRouter getInstance(Stage mainStage) {

        if (instance == null) {
            synchronized (SceneRouter.class) {
                if(instance == null) {
                    instance = new SceneRouter(mainStage);
                }
            }
        }
        if(instance != null) {
            return instance;
        } else {
            System.out.println("Application ran into an issue in scene routing");
            return null;
        }
    }


    /**
     * @return Scene Route Instance
     */
    public static SceneRouter getInstance() {

        if(instance != null) {
            return instance;
        } else {
            System.out.println("Application has not been created in Main 'Start' Method");
            return null;
        }
    }


    /**
     * Set MainStage from MATSOApplicationMain Init Method
     *
     * @param mainStage to load scenes into.
     */
    private void setMainStage(Stage mainStage) {
        mainWindow = mainStage;
    }


    /**
     * @param sceneView to load into stage.
     */
    public void loadScene(Enum<Constants.SceneView> sceneView) {
        objectHolder = null;
        try {
            Parent root = FXMLLoader.load(
                    SceneRouter.class.getResource("/view/" + sceneView.toString() + ".fxml"));

            Scene scene = new Scene(root);
            mainWindow.setTitle("MATSO > " + sceneView.toString());
            mainWindow.setScene(scene);
            mainWindow.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }


    /**
     * LoadScene with object value reference. - used in BusController for Route loading
     *
     * @param sceneView
     * @param passObject used for passing objects to scene. - referenced with SceneRoute.instance.objectHolder;
     */
    public void loadScene(Enum<Constants.SceneView> sceneView, Object passObject) {
        objectHolder = passObject;
        try {
            Parent root = FXMLLoader.load(
                    SceneRouter.class.getResource("/view/" + sceneView.toString() + ".fxml"));

            Scene scene = new Scene(root);
            mainWindow.setTitle("MATSO > " + sceneView.toString());
            mainWindow.setScene(scene);
            mainWindow.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
