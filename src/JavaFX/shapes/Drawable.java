package JavaFX.shapes;

import javafx.scene.input.MouseEvent;

public interface Drawable {

	public void onMouseDragged(MouseEvent event);
	
	public void onMouseReleased(MouseEvent event);
}
