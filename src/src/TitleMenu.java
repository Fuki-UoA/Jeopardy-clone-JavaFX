package src;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * This class is a border pane for title menu
 * of Jeopardy.
 */
public class TitleMenu extends BorderPane {

    public TitleMenu(){
        //Initialise vbox which includes three buttons
        VBox vbox = new VBox(10);
        vbox.setPrefWidth(100);
        this.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        //Initialise required buttons
        Button printBoard = new Button("Print question board");
        printBoard.setMinWidth(vbox.getPrefWidth());

        Button askQuestion = new Button("Ask a question");
        askQuestion.setMinWidth(vbox.getPrefWidth());

        Button reset = new Button("Reset");

        Button quit = new Button("Quit");
        quit.setMinWidth(vbox.getPrefWidth());

        //Add the buttons to vbox.
        vbox.getChildren().addAll(printBoard, askQuestion, quit);
        vbox.setAlignment(Pos.CENTER);

        //Set the vbox to pane.
        this.setCenter(vbox);
    }
}
