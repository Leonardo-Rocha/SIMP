package JavaFX;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

public class Main extends Application {

    /** Scene preferred width. */
    private static final double WIDTH = 896;
    /** Scene preferred height. */
    private static final double HEIGHT = 504;
    /** Application title. */
    private static final String TITLE = "SIMP";

    /** Controller reference. */
    SIMPController controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/JavaFX/SIMP.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        controller.setPrimaryStage(primaryStage);

        primaryStage.setTitle(TITLE);
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));

        configCanvasResize(primaryStage);

        primaryStage.show();
    }

    /**
     * Config canvas automatic resize according to root size.
     * @param primaryStage stage reference.
     */
    private void configCanvasResize(Stage primaryStage) {
        Canvas canvas = controller.getCanvas();
        canvas.setWidth(WIDTH);
        canvas.setHeight(HEIGHT);

        canvas.widthProperty().bind(primaryStage.widthProperty());
        canvas.heightProperty().bind(primaryStage.heightProperty());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
