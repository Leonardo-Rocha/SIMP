package JavaFX.shapes;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
		// TODO Auto-generated method stub
		width = event.getX() - initialPressCoordinate.getKey();
		height = event.getY() - initialPressCoordinate.getValue();
		rectangle.setWidth(width);
		rectangle.setHeight(height);
		
	}


	@Override
	public void onMouseReleased(MouseEvent event) {
		// TODO Auto-generated method stub
	}

}
