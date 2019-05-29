package JavaFX;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Utils {

    private static final String PROPERTIES_PATH = "/config.properties";

    private static Properties properties;

    private static File configFile;

    private static FileOutputStream fileOutputStream;

    public static Map<String, File> recentFiles;

    private static int lastValidIndex;

    /**
     * Config canvas automatic resize according to root size.
     *
     * @param primaryStage stage reference.
     */
    public static void configCanvasResize(Stage primaryStage, Canvas canvas) {
        canvas.widthProperty().bind(primaryStage.widthProperty());
        canvas.heightProperty().bind(primaryStage.heightProperty());
    }

    /**
     * Write the files path to a .properties.
     *
     * @param mapToSave map of files to save on the .properties.
     */
    public static void createPropertiesFile(Map<String, File> mapToSave) {
        try {
            recentFiles = mapToSave;
            fileOutputStream = new FileOutputStream(configFile);
            properties = new Properties();
            int i = 0;
            for (File item : mapToSave.values()) {
                properties.setProperty("recentFile" + i++, item.getAbsolutePath());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read the properties file.
     *
     * @return a map containing the name and the path of the files.
     */
    public static Map<String, File> readPropertiesFile() {
        recentFiles = new HashMap<>();
        try {
            // Find document folder and write file inside
            configFile = new File(System.getenv("HOMEPATH") + PROPERTIES_PATH);
            if (!configFile.exists() || configFile.length() <= 0) {
                createPropertiesFile(recentFiles);
                return recentFiles;
            } else {
                properties = new Properties();
            }

            FileInputStream fileLoaded = new FileInputStream(configFile);

            if (fileLoaded == null)
                throw new IOException("Error while reading file.");

            properties.load(fileLoaded);

            String property = "";
            for (int i = 1; property != null; i++) {
                property = properties.getProperty("recentFile" + i);
                if (property != null) {
                    File file = new File(property);
                    recentFiles.put(file.getName(), file);
                    lastValidIndex = i;
                }
            }
        } catch (FileNotFoundException e) {
            //TODO show more significant message.
            System.out.println("File not found.");
            Platform.exit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return recentFiles;
    }

    /**
     * Add a new file to configuration file.
     *
     * @param newFile
     */
    public static void addFileToProperties(File newFile) throws IOException {

        if (newFile != null && !recentFiles.containsKey(newFile.getName())){
            properties.setProperty("recentFile" + ++lastValidIndex, newFile.getAbsolutePath());
            recentFiles.put(newFile.getName(), newFile);
            properties.store(new FileOutputStream(configFile), "");
        } else{
            System.out.println("File == null or key already inserted in map.");
        }
    }
}
