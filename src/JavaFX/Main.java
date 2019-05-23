package JavaFX;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {

    private static final double WIDTH = 896;
    private static final double HEIGHT = 504;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/JavaFX/SIMP.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("SIMP");
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));

        SIMPController controller = loader.getController();
        controller.setStage(primaryStage);
        Canvas canvas = controller.getCanvas();
        canvas.setWidth(WIDTH);
        canvas.setHeight(HEIGHT);

        canvas.widthProperty().bind(primaryStage.widthProperty());
        canvas.heightProperty().bind(primaryStage.heightProperty());

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
