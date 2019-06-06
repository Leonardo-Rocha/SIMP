package JavaFX;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Stack;


public class BucketPainter  {

    private Painter painter;
    private Double x;
    private Double y;
    private Color targetColor;
    private WritableImage updateable;


    public BucketPainter(Double x, Double y, Color targetColor, WritableImage image) {
        this.x = x;
        this.y = y;

        updateable = new WritableImage(
                image.getPixelReader(),
                (int) image.getWidth(),
                (int) image.getHeight()
        );
        this.targetColor = targetColor;
        painter = new Painter(updateable, targetColor);


    }

    public WritableImage paint(){
        System.out.println("Ordering to paint");
        painter.unleash(new Point2D(x,   y), targetColor);
        return updateable;
    }

    class Painter {
        private final WritableImage image;
        private final Color colorToFill;
        private Color startingColor;
        private  final Double tolerance = 0.3;

        public Painter(WritableImage image, Color colorToFill) {
            this.image = image;
            this.colorToFill = colorToFill;

        }

        public void unleash(Point2D start, Color color) {
            PixelReader reader = image.getPixelReader();
            PixelWriter writer = image.getPixelWriter();

            Stack<Point2D> stack = new Stack<>();
            stack.push(start);
            this.startingColor = reader.getColor(x.intValue(), y.intValue());
            while (!stack.isEmpty()) {
                Point2D point = stack.pop();
                int x = (int) point.getX();
                int y = (int) point.getY();
                if (!filled(reader, x, y)) {
                    writer.setColor(x, y, color);

                    push(stack, x - 1, y - 1);
                    push(stack, x - 1, y    );
                    push(stack, x - 1, y + 1);
                    push(stack, x    , y + 1);
                    push(stack, x + 1, y + 1);
                    push(stack, x + 1, y    );
                    push(stack, x + 1, y - 1);
                    push(stack, x,     y - 1);
                }
            }
        }

        private void push(Stack<Point2D> stack, int x, int y) {
            if (x < 0 || x > image.getWidth() -1 ||
                    y < 0 || y > image.getHeight() -1) {
                return;
            }

            stack.push(new Point2D(x, y));
        }

        private boolean filled(PixelReader reader, int x, int y) {
            Color color = reader.getColor(x, y);
            boolean b = withinTolerance(colorToFill, color, tolerance) || !withinTolerance(color, startingColor, tolerance);
            return b;
        }

        private boolean withinTolerance(Color a, Color b, double epsilon) {
            return
                    withinTolerance(a.getRed(),   b.getRed(),   epsilon) &&
                            withinTolerance(a.getGreen(), b.getGreen(), epsilon) &&
                            withinTolerance(a.getBlue(),  b.getBlue(),  epsilon);
        }

        private boolean withinTolerance(double a, double b, double epsilon) {
            return Math.abs(a - b) < epsilon;
        }
    }
}