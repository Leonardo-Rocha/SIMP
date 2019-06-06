package JavaFX;

import javafx.scene.input.MouseEvent;

public interface Brush {

    public void draw();

    public void onMouseDragged(MouseEvent event);

    public void onMousePressed(MouseEvent event);

    public void onMouseReleased(MouseEvent event);
}
