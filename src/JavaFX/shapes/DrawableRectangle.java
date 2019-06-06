package JavaFX.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Pair;

/**
 * DrawableRectangle implements the Drawable interface. It`s used to implement the mouse related events.
 * @author Leonardo-Rocha, GabrielChiquetto
 */
public class DrawableRectangle extends Rectangle implements Drawable {

    private GraphicsContext canvasReference;
    private boolean isFilled = false; // TODO implement button to select this boolean.
    private Pair<Double, Double> initialPressCoordinate;

    public DrawableRectangle(Double x, Double y, Color color, GraphicsContext canvasReference) {
        super(x, y, 0 , 0);
        initialPressCoordinate = new Pair<>(x, y);
        setFill(color);
        setStroke(color);
        this.canvasReference = canvasReference;
    }

    @Override
    public void onMouseDragged(MouseEvent event) {
        Double x = event.getX();
        Double y = event.getY();
        originRelativeOrientation(x, y);
        //draw();
    }

    private void originRelativeOrientation(Double x, Double y){

        if(x >= initialPressCoordinate.getKey()) {
            setWidth(x - this.getX());

        }else {
            setWidth(initialPressCoordinate.getKey() - x);
            this.setX(x);

        }
        if(y >= initialPressCoordinate.getValue()) {
            setHeight(y - this.getY());

        }else{
            setHeight(initialPressCoordinate.getValue() - y);
            this.setY(y);
        }
    }

    @Override
    public void draw(){
         if(isFilled){
             canvasReference.setFill(getFill());
             canvasReference.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
         }else{
             canvasReference.setStroke(getStroke());
             canvasReference.strokeRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
         }
    }

    @Override
    public void onMouseReleased(MouseEvent event) {
        draw();
    }


    public void setToFill(boolean b){
        isFilled = b;
    }

    public void setToFill(){
        setToFill(true);
    }
}
