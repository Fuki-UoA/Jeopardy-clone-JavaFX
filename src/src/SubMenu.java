package src;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SubMenu extends BorderPane {
    protected Color _color;
    protected Scene _titleMenu;

    public SubMenu(Stage stage, Color color){
        _color = color;
        this.setBackground(new Background(new BackgroundFill(_color, CornerRadii.EMPTY, Insets.EMPTY)));

        //Create return button to title menu
        Button back = new Button("Return");

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

    public void setTitlemenu(Scene titleMenu){
        _titleMenu = titleMenu;
    }

}
