package JavaFX;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

public abstract class FXMLController {
    /**
     * FXML pencil toggle button.
     */
    @FXML
    protected ToggleButton pencil;

    /**
     * FXML eraser toggle button.
     */
    @FXML
    protected ToggleButton eraser;

    /**
     * FXML fillColor toggle button.
     */
    @FXML
    protected ToggleButton fillColor;

    /**
     * FXML insertText toggle button.
     */
    @FXML
    protected ToggleButton insertText;

    /**
     * FXML straightLine toggle button.
     */
    @FXML
    protected ToggleButton straightLine;

    /**
     * FXML square toggle button.
     */
    @FXML
    protected ToggleButton rectangle;

    /**
     * FXML circle toggle button.
     */
    @FXML
    protected ToggleButton circle;

    /**
     * FXML StackPane to hold tempCanvas.
     */
    @FXML
    protected StackPane canvasBackground;

    /**
     * FXML Canvas.
     */
    @FXML
    protected Canvas mainCanvas;

    /**
     * FXML menu of recent files.
     */
    @FXML
    protected Menu recentFilesMenu;

    /**
     * FXML BrushSize Slider.
     */
    @FXML
    protected Slider brushSizeSlider;

    /**
     * FXML BrushSize Text field.
     */
    @FXML
    protected TextField brushSizeText;

    /**
     * FXML Color picker.
     */
    @FXML
    protected ColorPicker colorPicker;

    protected boolean shapeSelected(){
        return rectangle.isSelected() || circle.isSelected() || straightLine.isSelected();
    }
}
