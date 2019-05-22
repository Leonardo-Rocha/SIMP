package JavaFX;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class SIMPController {

    @FXML
    private Canvas canvas;

    @FXML
    private TextField brushSize;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private CheckBox eraser;

    public void initialize() {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        canvas.setOnMouseDragged(e -> {

        });
    }

    public void onSave() {
        try {
            //TODO change this parameters to allow a file overwrite.
            Image snapshot = canvas.snapshot(null, null);
            //TODO change this so the user can change the name, the path and the format.
            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png",
                    new File("paint.png"));
        } catch (IOException e) {
            e.printStackTrace();
            //TODO print this to the user.
            System.out.println("Failed to save image.");
        }
    }

    public void onExit() {
        Platform.exit();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public TextField getBrushSize() {
        return brushSize;
    }

    public ColorPicker getColorPicker() {
        return colorPicker;
    }

    public CheckBox getEraser() {
        return eraser;
    }
}
