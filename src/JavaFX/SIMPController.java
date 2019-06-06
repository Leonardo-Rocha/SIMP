package JavaFX;

import JavaFX.shapes.*;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;

import static javax.swing.JOptionPane.NO_OPTION;
import static javax.swing.JOptionPane.YES_NO_CANCEL_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;

/**
 * SIMPController controls all interactions between the user and the GUI.
 *
 * @author Leonardo-Rocha, GabrielChiquetto
 */
public class SIMPController extends FXMLController{



    /**
     * Canvas graphicsContext.
     */
    private GraphicsContext graphicsContext;

    /**
     * File chooser reference.
     */
    private FileChooser fileChooser;

    /**
     * Map for recent files opened.
     */
    private Map<String, File> recentFiles;

    /**
     * Application primaryStage.
     */
    private Stage primaryStage;

    /**
     * Current file being used.
     */
    private File currentFile;

    /**
     * Drawable shape interface.
     */
    private Drawable drawable;

    private Brush brush;

    /**
     * A Writable image to print the temporary canvas undo history.
     */
    WritableImage undoSnapshot;

    /**
     * A Writable image to print the temporary canvas redo history.
     */
    WritableImage redoSnapshot;

    /**
     * Special signature method initialize. Instantiates some variables and bind the mouse actions.
     */ //TODO Refactor duplicated code.
    public void initialize() {

        graphicsContext = mainCanvas.getGraphicsContext2D();

        mainCanvas.setOnDragDetected(e -> mainCanvas.startFullDrag());
        //Starts the app with the pencil selected.
        pencil.setSelected(true);
        colorPicker.setValue(Color.BLACK);
        onBrushSizeTextChanged();
        graphicsContext.setLineCap(StrokeLineCap.ROUND);
        graphicsContext.setLineJoin(StrokeLineJoin.ROUND);

        //Bind brushSizeSlider to text.
        brushSizeSlider.valueProperty().addListener(e -> onBrushSizeSliderChanged());

        //OnMouseDragged lambda.
        mainCanvas.setOnMouseDragged(e -> {
            //TODO draw in different layers.
            double size = brushSizeSlider.getValue();
            double x = e.getX();
            double y = e.getY();
            Color color = colorPicker.getValue();
            graphicsContext.setLineWidth(size);
            if (pencil.isSelected()) {
                graphicsContext.setStroke(color);
                graphicsContext.lineTo(x, y);
                graphicsContext.stroke();
                graphicsContext.closePath();
                graphicsContext.beginPath();
                graphicsContext.moveTo(x, y);
            } else if (eraser.isSelected()) {
                graphicsContext.setStroke(Color.WHITE);
                graphicsContext.lineTo(x, y);
                graphicsContext.stroke();
                graphicsContext.closePath();
                graphicsContext.beginPath();
                graphicsContext.moveTo(x, y);
            } else if (shapeSelected()) {
                graphicsContext.moveTo(x, y);
                drawable.onMouseDragged(e);
            }
        });

        //onMouseClicked lambda
        mainCanvas.setOnMousePressed(e -> {
            try {
                undoSnapshot = takeSnapshot();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            double x = e.getX();
            double y = e.getY();
            Color color = colorPicker.getValue();
            if (pencil.isSelected()) {
                brush = new Pencil(x, y, color, graphicsContext);
                brush.onMousePressed(e);
            }else if(eraser.isSelected()){
                brush = new Pencil(x, y, Color.WHITE, graphicsContext);
                brush.onMousePressed(e);
            }else if (fillColor.isSelected()) {
                BucketPainter painter = new BucketPainter(x, y, color, undoSnapshot);
                printInCanvas(painter.paint());
            } else if (insertText.isSelected()) {
                //TODO improve this input dialog - add title, icon, etc.
                String input = JOptionPane.showInputDialog("Enter insertText: ");
                //TODO add textSize and alignment.
                graphicsContext.setFill(color);
                graphicsContext.fillText(input, x, y);
            } else if(shapeSelected()){
                createDrawableShape(x, y, color);
            }
        });

        mainCanvas.setOnMouseDragReleased(this::attemptDraw);

        fileChooser = new FileChooser();
        setExtensionFilters();
        currentFile = new File("Untitled");
    }

    private void createDrawableShape(double x, double y, Color color) {
        if (straightLine.isSelected()) {
            graphicsContext.beginPath();
            graphicsContext.moveTo(x, y);
            drawable = new DrawableLine(graphicsContext, x, y, color );
        } else if (rectangle.isSelected()) {
            graphicsContext.beginPath();
            graphicsContext.moveTo(x, y);
            drawable = new DrawableRectangle(x, y, color, graphicsContext);
        } else if (circle.isSelected()) {
            graphicsContext.beginPath();
            graphicsContext.moveTo(x, y);
            drawable = new DrawableEllipse(x, y, graphicsContext, color);
        }
    }

    private void attemptDraw(MouseDragEvent e) {
        if (drawable != null && shapeSelected()) {
            drawable.onMouseReleased(e);
            graphicsContext.closePath();
            graphicsContext.beginPath();
        }
    }


    /**
	 *  Method to save the current canvas image and put it on the desired Writable Image.
	 */
	private WritableImage takeSnapshot() throws IOException {
	    return mainCanvas.snapshot(new SnapshotParameters(), null);
	}


	/**
	 * Create and populate the recent files menu.
	 */
    public void setupRecentFilesMenu() {
        if (recentFiles != null) {
            recentFilesMenu.getItems().clear();
            recentFiles.forEach((k, v) -> {
                MenuItem menuItem = new MenuItem(k);
                menuItem.setOnAction(actionEvent -> {
                    try {
                        openImage(v);
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                });
                recentFilesMenu.getItems().add(menuItem);
            });
        }
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
     * New button action. Ask the user to save or not the current changes and then clear the tempCanvas.
     *
     * @param actionEvent onAction event.
     */
    public void onNew(ActionEvent actionEvent) {
        if (currentFile != null) {
            int answer = JOptionPane.showConfirmDialog(null,
                    "Do you want to save changes to the open file? ",
                    "SIMP", YES_NO_CANCEL_OPTION);
            if (answer == YES_OPTION) {
                onSaveAs();
                clearCanvas();
            } else if (answer == NO_OPTION) {
                clearCanvas();
            }
        }
    }

    private void clearCanvas() {
        graphicsContext.clearRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());
    }

    /**
     * Open button action. Opens the file chooser and draw the chosen image in the tempCanvas.
     *
     * @param actionEvent onAction event.
     */
    public void onOpen(ActionEvent actionEvent) {
        try {
            currentFile = fileChooser.showOpenDialog(primaryStage);
            openImage(currentFile);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to read image.");
        }
    }

    private void openImage(File file) throws IOException {
        BufferedImage bufferedImage;
        Image image;
        if (file != null) {
            clearCanvas();
            bufferedImage = ImageIO.read(file);
            image = SwingFXUtils.toFXImage(bufferedImage, null);
            clearCanvas();
            graphicsContext.drawImage(image, 0, 0);
            Utils.addFileToProperties(file);
            setupRecentFilesMenu();
        }
    }

    /**
     * Save button action. If the image was open or already saved, save the changes on it.
     * Otherwise, call onSaveAs().
     */
    public void onSave() {
        if (currentFile != null) {
            try {
                Image snapshot = mainCanvas.snapshot(new SnapshotParameters(), null);
                ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), getFileFormat(), currentFile);
                Utils.addFileToProperties(currentFile);
                setupRecentFilesMenu();
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
            Image snapshot = mainCanvas.snapshot(new SnapshotParameters(), null);
            fileChooser.setTitle("Save As");
            currentFile = fileChooser.showSaveDialog(primaryStage);
            if (currentFile != null) {
                ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), getFileFormat(), currentFile);
                Utils.addFileToProperties(currentFile);
                setupRecentFilesMenu();
            }
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
    public void onUndo() throws IOException {
        redoSnapshot = takeSnapshot();
    	printInCanvas(undoSnapshot);
    }

	/**
	 * Print the target image in the main canvas.
	 * @param input	image to be printed. 
	 */
	private void printInCanvas(WritableImage input) {
		clearCanvas();
		graphicsContext.drawImage(input, 0, 0);
	}
	
    /**
     * Redo button action. Redo the last draw movement.
     */
    public void onRedo() throws IOException {
        undoSnapshot = takeSnapshot();
    	printInCanvas(redoSnapshot);
    }

    /**
     * Brush Size Slider action. Updates the text according to the slider value.
     */
    public void onBrushSizeSliderChanged() {
        double value = brushSizeSlider.getValue();
        DecimalFormat df = new DecimalFormat("#");
        brushSizeText.setText(df.format(value));
    }

    /**
     * Brush Size Text action. Updates the slider according to the text value.
     */
    public void onBrushSizeTextChanged() {
        double value = Double.parseDouble(brushSizeText.getText());
        brushSizeSlider.setValue(value);
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
     * Stage setter.
     *
     * @param primaryStage reference to set.
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Log of recent files.
     *
     * @return Map of the recent files.
     */
    public Map<String, File> getRecentFiles() {
        return recentFiles;
    }

    public void setRecentFiles(Map<String, File> recentFiles) {
        this.recentFiles = recentFiles;
    }

    public Canvas getMainCanvas() {
        return mainCanvas;
    }

    public void setMainCanvas(Canvas mainCanvas) {
        this.mainCanvas = mainCanvas;
    }

    public Canvas getTempCanvas() {
        return mainCanvas;
    }

    public void setTempCanvas(Canvas tempCanvas) {
        this.mainCanvas = tempCanvas;
    }
}
