package src;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    public void saveGame(){
        File file = new File("./src/save_data/save.txt");

        try {
            PrintWriter pr = new PrintWriter(new FileWriter(file));

            pr.println(_winning);

            for(int i = 0; i < _numOfCategories; i++){
                pr.print(_categories[i] + " ");
            }

            pr.println("");

            for(int i = 0; i < _numOfCategories; i++){
                for(int j = 0; j < _numOfQuestions; j++){
                    if(_isAnswered[i][j]){
                        pr.println(i + ", " + j);
                    }
                }
            }

            pr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void removeGame(){
        File file = new File("./src/save_data/save.txt");
        file.delete();
    }

    public boolean resumeGame() {
        BufferedReader bf = null;
        String[] categories = null;

        try {
            File file = new File("./src/save_data/save.txt");
            bf = new BufferedReader(new FileReader(file));

             int winning = Integer.parseInt(bf.readLine());

            categories = bf.readLine().split(" ");

            if(categories.length != _numOfCategories){
                return false;
            }

            _winning = winning;

            String line = null;
            while((line = bf.readLine()) != null){
                String[] ans = line.split(", ");
                System.out.println(ans[0] + ans[1]);
                _isAnswered[Integer.parseInt(ans[0])][Integer.parseInt(ans[1])] = true;
            }
        } catch(FileNotFoundException e){
            return false;
        } catch (IOException e) {
            return false;
        }

        notifyObservers();

        return true;
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