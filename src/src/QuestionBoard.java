package src;


import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.util.List;

/**
 * This class is a grid pane which shows the scores of questions that haven't
 * been asked
 */
public class QuestionBoard extends GridPane {

    private List<Button> _buttons;
    private JeopardyLogic _logic;
    private int[] _scores;

    public QuestionBoard(JeopardyLogic logic){
        _logic = logic;
        _scores = logic.getScores();

        for(int i = 0; i < logic.numberOfCategories(); i++){
            for(int j = 0; j < _scores.length; j++){
                if(logic.isAnswered(i,j)){

                }else {
                    Button button = new Button(_scores[j]+"");
                    this.add(button, i, j);
                }
            }
        }

    }
}
