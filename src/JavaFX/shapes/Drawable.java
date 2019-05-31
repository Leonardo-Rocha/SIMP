package JavaFX.shapes;

import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Shape;

/**
 * Drawable interface. Defines the mouse related events.
 * @author Leonardo-Rocha, GabrielChiquetto
 */
public interface Drawable {

	public void draw();

	public void onMouseDragged(MouseEvent event);
	
	public void onMouseReleased(MouseEvent event);

	public Shape getShape();
}
