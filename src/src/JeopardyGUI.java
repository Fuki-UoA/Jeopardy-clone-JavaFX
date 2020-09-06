package src;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class JeopardyGUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Jeopardy");

        JeopardyLogic logic = null;

        try {
            logic = new JeopardyLogic();
        } catch (IOException e) {
        }

        TitleMenu pane = new TitleMenu(primaryStage, Color.color(0.43,0.39,0.39), logic);

        String style = getClass().getResource("style.css").toExternalForm();
        pane.getStylesheets().addAll(style);

        Scene scene = new Scene(pane, 800, 600);
        pane.setRootScene(scene);

        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
