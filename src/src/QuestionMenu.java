package src;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class QuestionMenu extends SubMenu{
    private TitleMenu _titleMenu;
    private JeopardyLogic _logic;
    private Font _font;
    private int _category;
    private int _question;

    public QuestionMenu(Stage stage, Color color, JeopardyLogic logic, int category, int question){
        super(stage, color);
        _logic = logic;
        _category = category;
        _question = question;

        getFont();

        Text q = new Text(logic.getQuestion(category, question));
        q.setFont(_font);
        q.setWrappingWidth(600);
        BorderPane.setAlignment(q, Pos.TOP_CENTER);

        TextField a = new TextField();
        a.setPrefWidth(2000);

        Button btn = new Button("Answer");
        btn.getStylesheets().addAll(this.getStylesheets());
        btn.setMinWidth(100);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String userAns = a.getText();
                if(userAns == null || userAns.isBlank()){
                    return;
                }

                if(logic.answer(category, question, userAns)){
                    System.out.println("correct");
                }else{
                    System.out.println("Incorrect");
                }

            }
        });

        HBox hbox = new HBox();

        hbox.getChildren().addAll(a, btn);

        runScript();

        this.setCenter(q);
        this.setBottom(hbox);
    }

    public void setTitleMenu(TitleMenu titleMenu){
        _titleMenu = titleMenu;
    }

    private void getFont(){
        String fontLocation = ".." + File.separator + "fonts" + File.separator + "ITC Korinna Regular.ttf";
        InputStream is = QuestionMenu.class.getResourceAsStream(fontLocation);
        _font = Font.loadFont(is, 75);
    }

    private void runScript(){
        ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", "echo " + _logic.getQuestion(_category, _question)
                + "| festival --tts");
        try {
            pb.start();
        } catch (IOException e) {

        }
    }

}
