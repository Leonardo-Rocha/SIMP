package JavaFX.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;
import javafx.util.Pair;

public class DrawableEllipse extends Ellipse implements Drawable{

    private GraphicsContext canvasReference;
    private boolean isFilled = false;
    private Pair<Double, Double> initialPressCoordinate;

    public DrawableEllipse(Double centerX, Double centerY, GraphicsContext canvasReference, Color color){
        super(centerX, centerY, 0, 0);
        initialPressCoordinate = new Pair<>(centerX, centerY);
        this.canvasReference = canvasReference;
        this.setFill(color);
        this.setStroke(color);
        System.out.println("x:" + initialPressCoordinate.getKey());
    }

    public DrawableEllipse(Double centerX,Double centerY, Double radiusX , Double radiusY, GraphicsContext canvasReference, Color color){
        super(centerX, centerY, radiusX, radiusY);
        this.canvasReference = canvasReference;
        this.setFill(color);
        this.setStroke(color);
        initialPressCoordinate = new Pair<>(centerX, centerY);
    }


    @Override
    public void draw() {        
        if (isFilled){
            canvasReference.setFill(getFill());
            canvasReference.fillOval(getCenterX(), getCenterY(), 2*getRadiusX(), 2*getRadiusY());
        }else{
            canvasReference.setStroke(getStroke());
            canvasReference.strokeOval(getCenterX(), getCenterY(), 2*getRadiusX(), 2*getRadiusY());
        }
    }

    @Override
    public void onMouseDragged(MouseEvent event) {
        Double x = event.getX();
        Double y = event.getY();
        originRelativeOrientation(x, y);
        //draw(); TODO implement previewLayer for preview drawing.
    }
    private void originRelativeOrientation(Double x, Double y){
        Double originX = initialPressCoordinate.getKey(), originY = initialPressCoordinate.getValue();
        if(x >= originX) {
            setRadiusX((x - originX)/2.0);
        }else {
            setRadiusX((originX - x) / 2.0);
            setCenterX(x);
        }
        if(y >= originY) {
            setRadiusY((y - originY) / 2.0);
        }else{
            setRadiusY((originY - y) / 2.0);
            setCenterY(y);
        }
    }
    @Override
    public void onMouseReleased(MouseEvent event) {
        draw();
    }

    @Override
    public Shape getShape() {
        return this;
    }

    public void setToFill(boolean b){
        isFilled = b;
    }

    public void setToFill(){
        setToFill(true);
    }
}
