package src;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class SubMenu extends BorderPane {
    protected Color _color;
    protected Scene _titleMenu;
    protected JeopardyLogic _logic;
    protected List<Button> _buttons;
    protected QuestionBoard _qb;

    public SubMenu(Stage stage, Color color){
        _color = color;
        this.setBackground(new Background(new BackgroundFill(_color, CornerRadii.EMPTY, Insets.EMPTY)));
        _buttons = new ArrayList<>();

        //Create return button to title menu
        Button back = new Button("Return");

        _buttons.add(back);

        //Add functionality to back
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.setScene(_titleMenu);
            }
        });

        this.setTop(back);
        BorderPane.setMargin(back, new Insets(5));
    }

    public void setTitleMenu(Scene titleMenu){
        _titleMenu = titleMenu;
    }

    public void setGameLogic(JeopardyLogic logic){
        _logic = logic;
    }

    public void setQuestionBoard(QuestionBoard qb){
        _qb = qb;
    }
}
