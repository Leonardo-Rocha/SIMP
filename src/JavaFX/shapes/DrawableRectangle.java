package JavaFX.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Pair;

public class DrawableRectangle implements Drawable {
	
	private Double width;
	private Double height;
    private Pair<Double, Double> initialPressCoordinate; 
	private Rectangle rectangle; 
	private Color color;
	
	public DrawableRectangle(Double x, Double y, Color color) {
		initialPressCoordinate = new Pair<> (x,y); 
		rectangle = new Rectangle(x, y);
		rectangle.setFill(color);
	}
	
	@Override
	public void onMouseDragged(MouseEvent event) {
		double x = initialPressCoordinate.getKey();
		double y = initialPressCoordinate.getValue();
		width = event.getX() - x;
		height = event.getY() - y;
		rectangle.setWidth(width);
		rectangle.setHeight(height);
	}


	@Override
	public void onMouseReleased(MouseEvent event) {
	}

	@Override
	public Shape getShape() {
		return rectangle;
	}
}
