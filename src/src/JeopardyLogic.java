package src;

import java.io.*;
import java.util.Arrays;

/**
 * This class dictates the logic behind the game Jeopardy.
 * It interacts with txt files out of src package.
 */
public class JeopardyLogic {
    private int _numOfQuestions;
    private int _numOfCategories;
    private int[] _scores;
    private int _winning;
    private String[][] _questions;
    private String[][] _answers;
    private Boolean[][] _isAnswered;

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

    public String[][] getQuestions(){
        String[][] result = new String[_numOfQuestions][_numOfCategories];
        for(int i = 0; i < _numOfQuestions; i++){
            for (int j = 0; j < _numOfCategories; j++){
                if(_isAnswered[j][i] == true){
                    _questions[j][i] = "Answered";
                } else {
                    result[j][i] = _questions[i][j];
                }
            }
        }

        return result;
    }

    public String[][] getAnswers(){
        return _answers.clone();
    }

    public int getWinning(){
        return _winning;
    }

    public void reset() {
        _winning = 0;

        for(int i = 0; i < _numOfCategories; i++){
            Arrays.fill(_isAnswered[i], false);
        }
    }

    public boolean answer(int question, int category, String answer){
        boolean result;
        if(answer.equals(_answers[question][category])){
            result = true;
        }else {
            result = false;
        }

        _isAnswered[question][category] = true;

        return result;
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
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String tmp = stdoutBuffered.readLine();
        _numOfCategories = Integer.parseInt(tmp);

    }
}