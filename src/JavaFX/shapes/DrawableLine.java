package JavaFX.shapes;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.util.Pair;

/**
 * DrawableLine implements the Drawable interface. It`s used to implement the mouse related events.
 * @author Leonardo-Rocha, GabrielChiquetto
 */
public class DrawableLine extends Line implements Drawable {
    GraphicsContext graphicsContext;
    private Pair<Double, Double> initialPressCoordinate;

    public DrawableLine( GraphicsContext graphicsContext, Double x, Double y, Color color) {
        super();
        setStartX(x);
        setStartY(y);
        this.graphicsContext = graphicsContext;
        initialPressCoordinate = new Pair<>(x, y);
        setStroke(color);
    }

    @Override
    public void draw() {
        graphicsContext.setStroke(getStroke());
        graphicsContext.strokeLine(getStartX(), getStartY(), getEndX(), getEndY());
    }

    @Override
    public void onMouseDragged(MouseEvent event) {
        setEndX(event.getX());
        setEndY(event.getY());
    }

    @Override
    public void onMouseReleased(MouseEvent event) {
        draw();
    }






}
