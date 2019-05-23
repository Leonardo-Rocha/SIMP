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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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

    private FileChooser fileChooser;

    private Stage stage;

    public void initialize() {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        canvas.setOnMouseDragged(e -> {
            double size = Double.parseDouble(brushSize.getText());
            double x = e.getX() - size / 2;
            double y = e.getY() - size / 2;

            if (eraser.isSelected())
                graphicsContext.clearRect(x, y, size, size);
            else {
                graphicsContext.setFill(colorPicker.getValue());
                graphicsContext.fillRect(x, y, size, size);
            }
        });

        fileChooser = new FileChooser();
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

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
