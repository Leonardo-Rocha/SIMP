package JavaFX.shapes;

import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Shape;

public interface Drawable {

	public void onMouseDragged(MouseEvent event);
	
	public void onMouseReleased(MouseEvent event);

	public Shape getShape();
}
