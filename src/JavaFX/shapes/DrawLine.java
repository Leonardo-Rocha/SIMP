package JavaFX.shapes;

import JavaFX.Utils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Pair;

import static JavaFX.Main.HEIGHT;
import static JavaFX.Main.WIDTH;

public class DrawLine {
    private Canvas canvas;
    Line line;
    GraphicsContext graphicsContext;
    private Pair<Double, Double> initialPressCoordinate;

    public DrawLine(Stage primaryStage, GraphicsContext graphicsContext, double x, double y) {
        this.graphicsContext = graphicsContext;
        initialPressCoordinate = new Pair<>(x, y);
        Canvas newLayer = new Canvas(WIDTH, HEIGHT);
        Utils.configCanvasResize(primaryStage, newLayer);
        line = new Line(x, y, x, y);
    }

    public void onMouseDragged(MouseEvent event) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        //clears the layer canvas stroking the final line.
        context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        context.strokeLine(initialPressCoordinate.getKey(), initialPressCoordinate.getValue(),
                event.getX(), event.getSceneY());
    }

}
