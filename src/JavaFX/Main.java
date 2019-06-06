package JavaFX;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application class. Used to wrap things up and launch the application.
 * @author Leonardo-Rocha, Gabriel Chiquetto.
 */
public class Main extends Application {

    /**
     * Scene preferred width.
     */
    public static final double WIDTH = 896;
    /**
     * Scene preferred height.
     */
    public static final double HEIGHT = 504;
    /**
     * Application title.
     */
    private static final String TITLE = "SIMP";

    /**
     * Controller reference.
     */
    SIMPController controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/JavaFX/SIMP.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        controller.setRecentFiles(Utils.readPropertiesFile());
        controller.setPrimaryStage(primaryStage);
        controller.setupRecentFilesMenu();
        Utils.configCanvasResize(primaryStage, controller.getMainCanvas());
        Utils.configCanvasResize(primaryStage, controller.getTempCanvas());

        primaryStage.setTitle(TITLE);
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));

        primaryStage.show();

    }

    public static void main(String[] args)
    {
        launch(args);
    }
}

