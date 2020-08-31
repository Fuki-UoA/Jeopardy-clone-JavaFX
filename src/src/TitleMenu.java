package src;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This class is a border pane for title menu
 * of Jeopardy.
 */
public class TitleMenu extends BorderPane {

    StackPane _title;
    int _winning = 0;
    Color _color;

    public TitleMenu(Stage stage, Color color){
        _color = color;

        //Initialise title
        setTitle();

        //Initialise vbox which includes three buttons
        VBox vbox = new VBox(10);
        vbox.setPrefWidth(100);
        this.setBackground(new Background(new BackgroundFill(_color, CornerRadii.EMPTY, Insets.EMPTY)));

        //Initialise required buttons
        Button printBoard = new Button("Print question board");
        printBoard.setMinWidth(vbox.getPrefWidth());

        Button askQuestion = new Button("Ask a question");
        askQuestion.setMinWidth(vbox.getPrefWidth());

        Button reset = new Button("Reset");
        reset.setPrefWidth(vbox.getPrefWidth());

        Button quit = new Button("Quit");
        quit.setMinWidth(vbox.getPrefWidth());

        //Initialise text for current winning
        Text t = new Text("Current winning: " + _winning);

        //Add functionality to the buttons

        printBoard.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                BorderPane qb = new QuestionBoardMenu(stage, _color);
                Scene scene = new Scene(qb, 800, 600);
                stage.setScene(scene);
            }
        });

        quit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.close();
            }
        });

        //Add the buttons to vbox.
        vbox.getChildren().addAll(printBoard, askQuestion, reset, quit);
        vbox.setAlignment(Pos.CENTER);

        //Create new StackPane
        StackPane bottom = new StackPane();
        bottom.setPrefHeight(20);
        bottom.getChildren().add(t);

        //Set nodes to this object.
        this.setCenter(vbox);
        this.setTop(_title);
        this.setBottom(bottom);
    }

    private void setTitle(){
        _title = new StackPane();
        _title.setPrefHeight(200);
    }

    public void setWinning(int winning){
        _winning = winning;
    }
}
