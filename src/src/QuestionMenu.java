package src;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

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

        this.setTop(null);

        getFont();

        Text q = new Text(logic.getQuestion(category, question));
        q.setFont(_font);
        q.setWrappingWidth(600);
        BorderPane.setAlignment(q, Pos.CENTER);

        TextField a = new TextField();
        a.setPrefWidth(2000);
        a.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String userAns = a.getText();
                if(userAns == null || userAns.isBlank()){
                    return;
                }

                ResultMenu result = resultMenu(stage, userAns);

                result.getStylesheets().addAll(getStylesheets());
                result.setRootMenu(_rootMenu);
                stage.setScene(new Scene(result, 800, 600));
            }
        });



        Button btn = new Button("Answer");
        btn.getStylesheets().addAll(getStylesheets());
        btn.setMinWidth(100);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String userAns = a.getText();
                if(userAns == null || userAns.isBlank()){
                    return;
                }

                ResultMenu result = resultMenu(stage, userAns);

                result.getStylesheets().addAll(getStylesheets());
                result.setRootMenu(_rootMenu);
                stage.setScene(new Scene(result, 800, 600));
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

    private ResultMenu resultMenu(Stage stage, String userAns){
        if(_logic.answer(_category, _question, userAns)){
            return new ResultMenu(stage, _color, true ,_logic, _category, _question);
        }else{
            return new ResultMenu(stage, _color, false ,_logic, _category, _question);
        }

    }

    private void getFont(){
        String fontLocation = "fonts" + File.separator + "ITC Korinna Regular.ttf";
        FontLoader font = new FontLoader(fontLocation, 75.0);
        _font = font.getFont();
    }

    private void runScript(){
        ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", "echo "
                + _logic.getQuestion(_category, _question).replaceAll("'", "")
                + "| festival --tts");
        try {
            pb.start();
        } catch (IOException e) {

        }
    }

}
