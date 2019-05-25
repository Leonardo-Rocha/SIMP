package JavaFX;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class SIMPController {

    /**
     * FXML Canvas.
     */
    @FXML
    private Canvas canvas;

    /**
     * FXML BrushSize text field.
     */
    @FXML
    private Slider brushSizeSlider;

    /**
     * FXML BrushSize text field.
     */
    @FXML
    private TextField brushSizeText;

    /**
     * FXML Color picker.
     */
    @FXML
    private ColorPicker colorPicker;

    /**
     * FXML Eraser checkbox.
     */
    @FXML
    private ToggleButton eraser;

    /**
     * FXML Undo button.
     */
    @FXML
    private Button undo;

    /**
     * FXML Redo button.
     */
    @FXML
    private Button redo;

    /**
     * FXML fillColor button.
     */
    @FXML
    private ToggleButton fillColor;

    /**
     * FXML Redo button.
     */
    @FXML
    private ToggleButton pencil;

    /**
     * FXML Redo button.
     */
    @FXML
    private ToggleButton text;

    /**
     * Canvas graphicsContext.
     */
    private GraphicsContext graphicsContext;

    /**
     * File chooser reference.
     */
    private FileChooser fileChooser;

    /**
     * Application primaryStage.
     */
    private Stage primaryStage;

    /**
     * Current file being used.
     */
    private File currentFile;

    /**
     * Previous move DrawAction List to be used on Undo action.
     */
    private List<DrawAction> previousMove;

    /**
     * Next move DrawAction List to be used on Redo action.
     */
    private List<DrawAction> nextMove;

    /**
     * Toggle Buttons map to keep only one button selected.
     */
    private Map<String, ToggleButton> toggleButtons;

    /**
     * Special signature method initialize. Instantiates some variables and bind the mouse actions.
     */
    public void initialize() {
        previousMove = new ArrayList<>();
        nextMove = new ArrayList<>();

        graphicsContext = canvas.getGraphicsContext2D();
        canvas.setOnDragDetected(e -> canvas.startFullDrag());
        pencil.setSelected(true);
        onBrushSizeTextChanged();

        //OnMouseDragged lambda.
        canvas.setOnMouseDragged(e -> {
            //TODO enhance the fillRect and the drawAction(Undo + Redo) precision.
            double size = brushSizeSlider.getValue();
            double x = e.getX() - size / 2;
            double y = e.getY() - size / 2;

            if (pencil.isSelected()) {
                Color fillColor = colorPicker.getValue();
                graphicsContext.setFill(fillColor);
                graphicsContext.fillRect(x, y, size, size);
                addDrawAction(fillColor, size, x, y);
            } else if (eraser.isSelected()) {
                graphicsContext.clearRect(x, y, size, size);
                addDrawAction(null, size, x, y);
            }
        });

        canvas.setOnMouseClicked(e -> {
            double x = e.getX();
            double y = e.getY();
            //TODO make these actions to support undo/redo
            if (fillColor.isSelected()) {
                //TODO fillColor logic
            } else if (text.isSelected()) {
                //TODO improve this input dialog - add title, icon, etc.
                String input = JOptionPane.showInputDialog("Enter text: ");
                //TODO add textSize and alignment.
                graphicsContext.setFill(colorPicker.getValue());
                graphicsContext.fillText(input, x, y);
            } //TODO add cases for the different shapes.
        });

        canvas.setOnMouseDragEntered(e -> nextMove.clear());
        canvas.setOnMouseDragReleased(e -> previousMove = nextMove);

        fileChooser = new FileChooser();
        setExtensionFilters();

        toggleButtons = new HashMap<>();
        toggleButtons.put("pencil", pencil);
        toggleButtons.put("eraser", eraser);
        toggleButtons.put("fillColor", fillColor);
        toggleButtons.put("text", text);
    }

    /**
     * Set the extension filters in the file chooser so the user can select only the allowed file extensions.
     */
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

    /**
     * New button action. Ask the user to save or not the current changes and then clear the canvas.
     */
    public void onNew(ActionEvent actionEvent) {
        //TODO implement logic - if there's an open file ask to save changes or not before clearing the canvas.
    }

    /**
     * Open button action. Opens the file chooser and draw the chosen image in the canvas.
     */
    public void onOpen(ActionEvent actionEvent) {
        try {
            currentFile = fileChooser.showOpenDialog(primaryStage);
            BufferedImage bufferedImage;
            Image image;
            if (currentFile != null) {
                bufferedImage = ImageIO.read(currentFile);
                image = SwingFXUtils.toFXImage(bufferedImage, null);
                graphicsContext.drawImage(image, 0, 0);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to read image.");
        }
    }

    /**
     * Save button action. If the image was open or already saved, save the changes on it.
     * Otherwise, call onSaveAs().
     */
    public void onSave() {
        if (currentFile != null) {
            try {
                Image snapshot = canvas.snapshot(null, null);
                ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), getFileFormat(), currentFile);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to save image.");
            }
        } else
            onSaveAs();
    }

    /**
     * SaveAs button action. Opens the file chooser and save the image in the chosen directory.
     */
    public void onSaveAs() {
        try {
            //TODO change this parameters to allow a file overwrite.
            Image snapshot = canvas.snapshot(null, null);
            fileChooser.setTitle("Save As");
            currentFile = fileChooser.showSaveDialog(primaryStage);
            if (currentFile != null)
                ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), getFileFormat(), currentFile);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to save image.");
        }
    }

    /**
     * Exit button action. Calls Platform.exit() to close the program.
     */
    public void onExit() {
        Platform.exit();
    }

    /**
     * Undo button action. Undo the last draw movement.
     */
    public void onUndo() {
        for (DrawAction drawAction : previousMove) {
            graphicsContext.clearRect(drawAction.x, drawAction.y, drawAction.size, drawAction.size);
        }
    }

    /**
     * Redo button action. Redo the last draw movement.
     */
    public void onRedo() {
        for (DrawAction drawAction : nextMove) {
            graphicsContext.setFill(drawAction.fillColor);
            graphicsContext.fillRect(drawAction.x, drawAction.y, drawAction.size, drawAction.size);
        }
    }

    public void onBrushSizeSliderChanged() {
        //TODO enhance this method - format the text and bind the method to correct slider actions.
        double value = brushSizeSlider.getValue();
        String text = String.valueOf(value);
        brushSizeText.setText(text);
    }

    public void onBrushSizeTextChanged() {
        double value = Double.parseDouble(brushSizeText.getText());
        brushSizeSlider.setValue(value);
    }

    public void onPencilSelected() {
        deselectOtherToggles("pencil");
    }

    public void onEraserSelected() {
        deselectOtherToggles("eraser");
    }

    public void onFillColorSelected() {
        deselectOtherToggles("fillColor");
    }

    public void onTextSelected() {
        deselectOtherToggles("text");
    }

    /**
     * Loop through the toggleButtons map and deselect all the toggles, except the selected one.
     *
     * @param selectedToggle most recently selected ToggleButton.
     */
    void deselectOtherToggles(String selectedToggle) {
        for (Map.Entry m : toggleButtons.entrySet()) {
            if (m.getKey() != selectedToggle) {
                ToggleButton toggleButton = (ToggleButton) m.getValue();
                toggleButton.setSelected(false);
            }
        }
    }

    /**
     * Parse the file name.
     *
     * @return the file format/extension.
     */
    private String getFileFormat() {
        String[] parsedFileName = currentFile.getName().split("\\.");
        String formatName = "png";

        if (parsedFileName.length > 1)
            formatName = parsedFileName[1];

        return formatName;
    }

    /**
     * Add a DrawAction to the nextMove list.
     *
     * @param fillColor color set on the ColorPicker.
     * @param bIsDraw   true if the action is a draw, false otherwise.
     * @param bIsErase  true if the action is a draw, false otherwise.
     * @param size      size of the rectangle.
     * @param x         position x of the rectangle in the canvas.
     * @param y         position y of the rectangle in the canvas.
     */
    public void addDrawAction(Color fillColor, double size, double x, double y) {
        DrawAction drawAction = new DrawAction(fillColor, size, x, y);
        nextMove.add(drawAction);
    }

    /**
     * Canvas getter.
     *
     * @return canvas.
     */
    public Canvas getCanvas() {
        return canvas;
    }

    /**
     * Stage setter.
     *
     * @param primaryStage reference to set.
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * DrawAction data structure to store all mouse drag actions.
     */
    private class DrawAction {
        //TODO add an enum to handle text, fillColor and shapes
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
}
