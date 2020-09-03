package src;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AskQuestionMenu extends SubMenu{

    public AskQuestionMenu(Stage stage, Color color) {
        super(stage, color);
    }

    public void update(){
        this.setCenter(super._qb);
    }
}
