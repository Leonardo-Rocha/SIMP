package JavaFX;

import javafx.scene.paint.Color;

/**
 * DrawAction data structure to store all mouse drag actions.
 */
class DrawAction {
    //TODO add an enum to handle insertText, fillColor and shapes
    Color fillColor;
    double size;
    double x;
    double y;

    DrawAction(Color fillColor, double size, double x, double y) {
        this.fillColor = fillColor;
        this.size = size;
        this.x = x;
        this.y = y;
    }
}
