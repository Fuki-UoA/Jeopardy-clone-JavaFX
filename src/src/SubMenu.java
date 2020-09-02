package src;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SubMenu extends BorderPane {
    protected Color _color;
    public SubMenu(Stage stage, Color color){
        _color = color;
        this.setBackground(new Background(new BackgroundFill(_color, CornerRadii.EMPTY, Insets.EMPTY)));
    }

}
