package JavaFX;

import JavaFX.Brush;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;


public class Pencil implements Brush {
    private GraphicsContext graphicsContext;
    private Double clickX;
    private Double clickY;
    private Color color;

    public Pencil(Double clickX, Double clickY, Color color, GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
        this.clickX = clickX;
        this.clickY = clickY;
        this.color = color;

    }

    @Override
    public void draw() {
        graphicsContext.setStroke(color);
        graphicsContext.lineTo(clickX, clickY);
        graphicsContext.stroke();
        graphicsContext.closePath();
        graphicsContext.beginPath();
        graphicsContext.moveTo(clickX, clickY);
    }

    @Override
    public void onMouseDragged(MouseEvent event) {
        graphicsContext.setStroke(color);
        graphicsContext.lineTo(event.getX(), event.getY());
        graphicsContext.stroke();
        graphicsContext.closePath();
        graphicsContext.beginPath();
        graphicsContext.moveTo(event.getX(), event.getY());
    }

    @Override
    public void onMousePressed(MouseEvent event) {
        graphicsContext.moveTo(clickX, clickY);
        draw();
    }

    @Override
    public void onMouseReleased(MouseEvent event) {

    }
}
