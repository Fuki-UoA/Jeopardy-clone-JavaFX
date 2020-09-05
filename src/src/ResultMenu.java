package src;

import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.io.File;

public class ResultMenu {
    private BorderPane _resultMenu;

    public ResultMenu(boolean isCorrect, JeopardyLogic logic, int category, int question){
        _resultMenu = new BorderPane();

        FontLoader font =
                new FontLoader(".." + File.separator + "fonts" + File.separator + "ITC Korinna Regular.ttf", 75.0);
        Text text = new Text();
        text.setFont(font.getFont());

        if(isCorrect){
            text.setText("Correct!");
        }else {
            text.setText("Incorrect! The answer is...\n"+ logic.getAnswer(category,question));
        }

        Text winning = new Text("Now your winning is $" + logic.getWinning());
        _resultMenu.setTop(text);
        _resultMenu.setCenter(winning);
    }



}
