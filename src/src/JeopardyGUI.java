package src;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class JeopardyGUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Jeopardy");

        BorderPane pane = new BorderPane();

        Scene scene = new Scene(pane, 1000, 800);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
