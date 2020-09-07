package src;


import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a grid pane which shows the scores of questions that haven't
 * been asked. Has a observer-observable pattern relationship with JeopardyLogic
 */
public class QuestionBoard extends GridPane implements Observer{

    private Scene _rootMenu;
    private Scene _menu;

    private JeopardyLogic _logic;
    private int[] _scores;
    private Button[][] _buttons;
    private Color _color;


    public QuestionBoard(Stage stage, JeopardyLogic logic, Color color){
        _logic = logic;
        _scores = logic.getScores();
        _color = color;

        _buttons = new Button[logic.numberOfCategories()][_scores.length];

        this.setBackground(new Background(new BackgroundFill(_color, CornerRadii.EMPTY, Insets.EMPTY)));
        this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        List<ColumnConstraints> cols = new ArrayList<>();

        for(int i = 0; i < logic.numberOfCategories(); i++){
            for(int j = 0; j < _scores.length; j++){
                    Button button = new Button("$"+_scores[j]+"");
                    _buttons[i][j] = button;

                    button.setPrefHeight(500);
                    button.setMaxWidth(Double.MAX_VALUE);
                    GridPane.setFillWidth(button, true);

                    button.setStyle("-fx-border-color: black");
                    this.add(button, i, j+1);
            }
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(100/(double)logic.numberOfCategories());
            cols.add(col);
        }

        String[] categories = _logic.getCategories();
        for(int i = 0; i < categories.length; i++){
            Label text = new Label(categories[i]);

            text.setPrefHeight(500);
            text.setMaxWidth(Double.MAX_VALUE);
            GridPane.setFillWidth(text, true);

            text.setStyle("-fx-border-color: black");
            text.getStylesheets().addAll(this.getStylesheets());
            text.setContentDisplay(ContentDisplay.CENTER);
            text.setAlignment(Pos.CENTER);
            this.add(text, i, 0);
        }

        this.getColumnConstraints().addAll(cols);

        for(int i = 0; i < _logic.numberOfCategories(); i++){
            for(int j = 0; j < _scores.length; j++){
                int col = i;
                int row = j;
                String style = getClass().getResource("style.css").toExternalForm();

                _buttons[i][j].setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        if(!logic.isAnswered(col, row)){
                            QuestionMenu qm = new QuestionMenu(stage, color, logic, col, row);
                            qm.setRootMenu(_menu);
                            qm.getStylesheets().addAll(style);

                            Scene scene = new Scene(qm, 800, 600);
                            stage.setScene(scene);
                        }
                    }
                });
            }
        }
    }

    public void setRootMenu(Scene rootMenu){
        _rootMenu = rootMenu;
    }

    public void setCurrentMenu(Scene menu){
        _menu = menu;
    }

    @Override
    public void update() {
        for (int i = 0; i < _logic.numberOfCategories(); i++) {
            for (int j = 0; j < _scores.length; j++) {
                if(_logic.isAnswered(i,j)) {
                    _buttons[i][j].setText("Answered");
                }else {
                    _buttons[i][j].setText(_scores[j]+"");
                }
            }

            if(_logic.isCompleted(i)){
                for(int j = 0; j < _scores.length; j++){
                    _buttons[i][j].setText("Completed!!!");

                    _buttons[i][j].setStyle("-fx-background-color: khaki;" +
                            "-fx-border-color: black;");
                }
            }else {
                for(int j = 0; j < _scores.length; j++){
                    _buttons[i][j].setStyle("-fx-border-color: black;");
                }
            }
        }
    }
}
