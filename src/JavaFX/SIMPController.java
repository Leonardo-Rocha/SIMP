package JavaFX;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
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
import javax.imageio.ImageWriter;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class SIMPController {

    @FXML
    private Canvas canvas;

    @FXML
    private TextField brushSize;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private CheckBox eraser;

    private GraphicsContext graphicsContext;

    private FileChooser fileChooser;

    private Stage stage;

    private File currentFile;

    public void initialize() {
        graphicsContext = canvas.getGraphicsContext2D();

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
        setExtensionFilters();
    }

    private void setExtensionFilters() {
        //TODO fix other formats not working(already tried jpg, jpeg, bmp).
        String[] extensions = {"png"};
        FileChooser.ExtensionFilter extensionFilter;
        for (String format : extensions) {
            String extension = "*." + format;
            extensionFilter =
                    new FileChooser.ExtensionFilter(format.toUpperCase() + " files (" + extension + ")",
                            extension);
            fileChooser.getExtensionFilters().add(extensionFilter);
        }
    }

    public void onNew(ActionEvent actionEvent) {
        //TODO implement logic - if there's an open file ask to save changes or not before clearing the canvas.
    }

    public void onOpen(ActionEvent actionEvent) {
        try {
            currentFile = fileChooser.showOpenDialog(stage);
            BufferedImage bufferedImage;
            Image image;
            if (currentFile != null) {
                bufferedImage = ImageIO.read(currentFile);
                image = SwingFXUtils.toFXImage(bufferedImage, null );
                graphicsContext.drawImage(image, 0, 0);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to read image.");
        }
    }

    public void onSave() {
        if (currentFile != null) {
            try {
                Image snapshot = canvas.snapshot(null, null);
                ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), getFileFormat(), currentFile);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to save image.");
            }
        }
        else
            onSaveAs();
    }

    public void onSaveAs() {
        try {
            //TODO change this parameters to allow a file overwrite.
            Image snapshot = canvas.snapshot(null, null);
            fileChooser.setTitle("Save As");
            currentFile = fileChooser.showSaveDialog(stage);
            if (currentFile != null)
                ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), getFileFormat(), currentFile);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Failed to save image.");
        }
    }

    private String getFileFormat() {
        String[] parsedFileName = currentFile.getName().split("\\.");
        String formatName = "png";

        if (parsedFileName.length > 1)
            formatName = parsedFileName[1];

        return formatName;
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
