package src;

import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ResultMenu extends SubMenu{

    private int _category;
    private int _question;
    private JeopardyLogic _logic;

    public ResultMenu(Stage stage, Color color, boolean isCorrect, JeopardyLogic logic, int category, int question) {
        super(stage, color);

        _category = category;
        _question = question;
        _logic = logic;

        FontLoader font =
                new FontLoader(".." + File.separator + "fonts" + File.separator + "ITC Korinna Regular.ttf", 75.0);
        Text text = new Text();

        if (isCorrect) {
            text.setText("Correct!");
        } else {
            text.setText("Incorrect! The answer is...\n" + logic.getAnswer(category, question));
            font.setFontSize(60.0);
            runScript();
        }
        text.setFont(font.getFont());

        Text winning = new Text("Now your winning is $" + logic.getWinning());
        font.setFontSize(50.0);
        winning.setFont(font.getFont());

        //BorderPane.setAlignment(text, Pos.TOP_CENTER);
        BorderPane.setAlignment(winning, Pos.TOP_CENTER);

        this.setCenter(text);
        this.setBottom(winning);
    }

    private void runScript(){
        ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", "echo " + _logic.getAnswer(_category, _question)
                + "| festival --tts");
        try {
            pb.start();
        } catch (IOException e) {

        }
    }
}
