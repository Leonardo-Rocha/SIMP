package JavaFX;

import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

public class Utils {

    /**
     * Config canvas automatic resize according to root size.
     * @param primaryStage stage reference.
     */
    public static void configCanvasResize(Stage primaryStage, Canvas canvas) {
        //TODO check if this is necessary
        //canvas.setWidth(WIDTH);
        //canvas.setHeight(HEIGHT);
        canvas.widthProperty().bind(primaryStage.widthProperty());
        canvas.heightProperty().bind(primaryStage.heightProperty());
    }
}
