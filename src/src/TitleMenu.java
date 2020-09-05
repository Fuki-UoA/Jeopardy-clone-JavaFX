package src;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class is a border pane for title menu
 * of Jeopardy.
 */
public class TitleMenu extends BorderPane implements Observer{

    //Panes
    private StackPane _title;
    private  QuestionBoard _questionBoard;

    //Scenes for pages.
    private Scene _rootScene;
    private Scene _QuestionBoardScene;
    private Scene _AskQuestionScene;

    private Stage _stage;

    private int _winning = 0;
    private Color _color;

    private Text _titleFont = null;
    private Text _winningText;

    private JeopardyLogic _logic;
    private List<Button> _buttons;
    private String _buttonColor;

    public TitleMenu(Stage stage, Color color){
        _color = color;
        _stage = stage;

        //Initialise title
        setTitle();
        setTitleFont();

        //Initialise vbox which includes three buttons
        VBox vbox = new VBox(10);
        vbox.setPrefWidth(250);
        this.setBackground(new Background(new BackgroundFill(_color, CornerRadii.EMPTY, Insets.EMPTY)));

        try {
            runScript();
        } catch (IOException e) {
        }

        //Initialise required buttons
        _buttons = new ArrayList<>();

        Button askQuestion = new Button("Play a game");
        _buttons.add(askQuestion);

        Button reset = new Button("Reset");
        _buttons.add(reset);

        Button quit = new Button("Quit");
        _buttons.add(quit);

        for(Button button : _buttons){
            button.setMinWidth(vbox.getPrefWidth());
            button.setPrefHeight(50);
        }

        //Initialise text for current winning
        _winningText = new Text("Current winning: " + _winning);

        //Add functionality to the buttons

        askQuestion.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.setScene(_AskQuestionScene);
            }
        });

        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Reset game");
                alert.setHeaderText("Are you sure you want to reset the game?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    _logic.reset();
                } else {
                    // do nothing
                }
            }
        });

        quit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Quitting game");
                alert.setHeaderText("Are you sure you want to quit?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    stage.close();
                } else {
                    // do nothing
                }
            }
        });

        //Add the buttons to vbox.
        vbox.getChildren().addAll(askQuestion, reset, quit);
        vbox.setAlignment(Pos.CENTER);

        //Set nodes to this object.
        this.setCenter(vbox);
        this.setTop(_titleFont);

        BorderPane.setAlignment(_titleFont, Pos.TOP_CENTER);
        BorderPane.setMargin(_titleFont, new Insets(50,0,0,0));
        BorderPane.setAlignment(_winningText, Pos.CENTER);

        this.setBottom(_winningText);
    }

    private void setTitle(){
        _title = new StackPane();
        _title.setPrefHeight(200);
    }

    private void setTitleFont(){
        if(_titleFont == null){
            FontLoader font = new FontLoader(".."+ File.separator + "fonts" + File.separator + "gyparody hv.ttf", 150.0);
            _titleFont = new Text("Jeopardy!");
            _titleFont.setFont(font.getFont());
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

    @Override
    public void update(){
        _winning = _logic.getWinning();
        _winningText = new Text("Current winning: " + _winning);
        BorderPane.setAlignment(_winningText, Pos.CENTER);

        this.setBottom(_winningText);
    }

    public void setRootScene(Scene rootScene){
        _rootScene = rootScene;

        AskQuestionMenu aq = new AskQuestionMenu(_stage, _color);
        aq.setRootMenu(_rootScene);
        aq.setQuestionBoard(_questionBoard);
        aq.update();
        _AskQuestionScene = new Scene(aq, 800, 600);
        _AskQuestionScene.getStylesheets().addAll(this.getStylesheets());

        _questionBoard.setRootMenu(_rootScene);
        _questionBoard.setCurrentMenu(_AskQuestionScene);
    }

    public void setGameLogic(JeopardyLogic logic){
        _logic = logic;
        _questionBoard = new QuestionBoard(_stage, _logic, _color);
        _questionBoard.getStylesheets().addAll(this.getStylesheets());

        _logic.setObserver(_questionBoard);
        _logic.setObserver(this);
    }
}
