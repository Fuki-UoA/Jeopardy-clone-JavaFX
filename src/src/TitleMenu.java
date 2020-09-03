package src;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;

/**
 * This class is a border pane for title menu
 * of Jeopardy.
 */
public class TitleMenu extends BorderPane {

    private StackPane _title;
    private int _winning = 0;
    private Color _color;
    private Scene _rootScene;
    private Text _titleFont = null;
    private JeopardyLogic _logic;

    public TitleMenu(Stage stage, Color color){
        _color = color;

        //Initialise title
        setTitle();
        setTitleFont();

        //Initialise vbox which includes three buttons
        VBox vbox = new VBox(10);
        vbox.setPrefWidth(100);
        this.setBackground(new Background(new BackgroundFill(_color, CornerRadii.EMPTY, Insets.EMPTY)));

        try {
            runScript();
        } catch (IOException e) {
        }

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
                SubMenu qb = new QuestionBoardMenu(stage, _color);
                qb.setTitlemenu(_rootScene);
                Scene scene = new Scene(qb, 800, 600);
                stage.setScene(scene);
            }
        });

        askQuestion.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                SubMenu aq = new AskQuestionMenu(stage, _color);
                aq.setTitlemenu(_rootScene);
                Scene scene = new Scene(aq, 800, 600);
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
        this.setTop(_titleFont);

        BorderPane.setAlignment(_titleFont, Pos.TOP_CENTER);
        BorderPane.setMargin(_titleFont, new Insets(50,0,0,0));

        this.setBottom(bottom);
    }

    private void setTitle(){
        _title = new StackPane();
        _title.setPrefHeight(200);
    }

    private void setTitleFont(){
        if(_titleFont == null){
            String fontFile = ".."+ File.separator + "fonts" + File.separator + "gyparody hv.ttf";
            InputStream fontStream = TitleMenu.class.getResourceAsStream(fontFile);
            Font font = null;
            if(fontStream != null){
                font = Font.loadFont(fontStream, 150);
            }

            _titleFont = new Text("Jeopardy!");
            _titleFont.setFont(font);
        }
    }

    private void runScript() throws IOException {
        //Initialise logic script file
        ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", "echo Welcome to Jeopardy! | festival --tts");

        Process p = null;
        try {
            p = pb.start();
        } catch (IOException e) {

        }

    }

    public void setWinning(int winning){
        _winning = winning;
    }

    public void setRootScene(Scene rootScene){
        _rootScene = rootScene;
    }

    public void setGameLogic(JeopardyLogic logic){
        _logic = logic;
    }
}
