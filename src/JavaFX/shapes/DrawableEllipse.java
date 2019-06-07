package JavaFX.shapes;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.util.Pair;

public class DrawableEllipse extends Ellipse implements Drawable{

    private boolean isFilled = false;
    private Pair<Double, Double> initialPressCoordinate;
    private GraphicsContext graphicsContext;

    public DrawableEllipse(Double centerX, Double centerY, GraphicsContext graphicsContext, Color color){
        super(centerX, centerY, 0, 0);
        initialPressCoordinate = new Pair<>(centerX, centerY);
        this.setFill(color);
        this.setStroke(color);
        this.graphicsContext = graphicsContext;
    }

    public DrawableEllipse(Double centerX,Double centerY, Double radiusX , Double radiusY, GraphicsContext graphicsContext, Color color){
        super(centerX, centerY, radiusX, radiusY);
        this.setFill(color);
        this.setStroke(color);
        initialPressCoordinate = new Pair<>(centerX, centerY);
        this.graphicsContext = graphicsContext;
    }


    @Override
    public void draw() {        
        if (isFilled){
            graphicsContext.setFill(getFill());
            graphicsContext.fillOval(getCenterX(), getCenterY(), 2*getRadiusX(), 2*getRadiusY());
        }else{
            graphicsContext.setStroke(getStroke());
            graphicsContext.strokeOval(getCenterX(), getCenterY(), 2*getRadiusX(), 2*getRadiusY());
        }
    }

    @Override
    public void onMouseDragged(MouseEvent event) {
        Double x = event.getX();
        Double y = event.getY();
        originRelativeOrientation(x, y);
        //draw(); //TODO implement previewLayer for preview drawing.
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
    public void setToFill(boolean b){
        isFilled = b;
    }

    public void setToFill(){
        setToFill(true);
    }
}
