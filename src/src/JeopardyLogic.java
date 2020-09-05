package src;

import javafx.scene.Node;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class dictates the logic behind the game Jeopardy.
 * It interacts with txt files out of src package.
 * It is observable from observer class QuestionBoard
 */
public class JeopardyLogic {
    private int _numOfQuestions;
    private int _numOfCategories;
    private int[] _scores;
    private int _winning;
    private String[] _categories;
    private String[][] _questions;
    private String[][] _answers;
    private Boolean[][] _isAnswered;

    private List<Observer> _observers;

    public JeopardyLogic() throws IOException {
        //Count numbers of categories and questions for game.
        countCategories();
        countQuestions();

        //Initialise required fields.
        _winning = 0;
        _scores = new int[_numOfQuestions];
        _questions = new String[_numOfCategories][_numOfQuestions];
        _answers = new String[_numOfCategories][_numOfQuestions];
        _isAnswered = new Boolean[_numOfCategories][_numOfQuestions];
        _observers = new ArrayList<>();


        //Fill isAnswered with false
        for(int i = 0; i < _numOfCategories; i++){
            Arrays.fill(_isAnswered[i], false);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(JeopardyLogic.class.getResourceAsStream(".." + File.separator +"categories")));

        int i = 0;
        int j = 0;

        String line;
        while ((line = br.readLine()) != null) {
            String dir = ".." + File.separator +"categories" + File.separator + line;
            BufferedReader in = new BufferedReader(new InputStreamReader(JeopardyLogic.class.getResourceAsStream(dir)));

            while((line = in.readLine()) != null){
                String[] aLine = line.split(",");

                _scores[i] = Integer.parseInt(aLine[0]);
                _questions[j][i] = aLine[1];
                _answers[j][i] = aLine[2];

                i++;
            }
            i=0;
            j++;
        }
        
    }

    public String getQuestion(int category, int question){
        return _questions[category][question];
    }

    public String getAnswer(int category, int question){
        return _answers[category][question];
    }

    public int getWinning(){
        return _winning;
    }

    public void reset() {
        _winning = 0;

        for(int i = 0; i < _numOfCategories; i++){
            Arrays.fill(_isAnswered[i], false);
        }

        notifyObservers();
    }

    public int[] getScores(){
        return _scores;
    }

    public boolean isAnswered(int category, int question){
        return _isAnswered[category][question];
    }

    public int numberOfCategories(){
        return _numOfCategories;
    }

    public String[] getCategories(){
        return _categories;
    }

    public boolean answer(int category, int question, String answer){
        boolean result;
        if(answer.equalsIgnoreCase(_answers[category][question])){
            result = true;
            _winning = _winning + _scores[question];
        }else {
            result = false;
            _winning = _winning - _scores[question];
        }

        _isAnswered[category][question] = true;

        notifyObservers();

        return result;
    }

    public void setObserver(Observer ob){
        _observers.add(ob);
    }

    private void notifyObservers(){
        for (Observer observer : _observers){
            observer.update();
        }
    }

    private void countQuestions() throws IOException {
        ProcessBuilder pb =
                new ProcessBuilder("/bin/bash", "-c", "ls src/categories/* | xargs cat | wc -l" );

        Process process = pb.start();

        BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(process.getInputStream()));

        _numOfQuestions = Integer.parseInt(stdoutBuffered.readLine()) / _numOfCategories;
    }

    private void countCategories() throws IOException {
        ProcessBuilder pb =
                new ProcessBuilder("/bin/bash","-c", "ls src/categories | wc -l" );

        Process process = pb.start();

        BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String tmp = stdoutBuffered.readLine();
        _numOfCategories = Integer.parseInt(tmp);

        pb = new ProcessBuilder("/bin/bash", "-c", "ls src/categories");
        process = pb.start();
        stdoutBuffered = new BufferedReader(new InputStreamReader(process.getInputStream()));

        _categories = new String[_numOfCategories];
        String line = null;
        int i = 0;
        while((line = stdoutBuffered.readLine()) != null){
            _categories[i] = line;
            i++;
        }
    }
}