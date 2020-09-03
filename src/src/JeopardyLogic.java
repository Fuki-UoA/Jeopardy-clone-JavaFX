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
        Arrays.fill(_isAnswered, false);

        BufferedReader br = new BufferedReader(new FileReader(new File(".." + File.separator +"categories")));

        int i = 0;
        int j = 0;

        String line;
        while ((line = br.readLine()) != null) {
            BufferedReader in = new BufferedReader(new FileReader(new File(".." +
                    File.separator +"categories" + File.separator + line)));

            while((line = in.readLine()) != null){
                String[] aLine = line.split(",");

                _scores[i] = Integer.parseInt(aLine[0]);
                _questions[i][j] = aLine[1];
                _answers[i][j] = aLine[2];

                i++;
            }
            j++;
        }
        
    }

    public String[][] getQuestions(){
        String[][] result = new String[_numOfQuestions][_numOfCategories];
        for(int i = 0; i < _numOfQuestions; i++){
            for (int j = 0; j < _numOfCategories; j++){
                if(_isAnswered[i][j] == true){
                    _questions[i][j] = "Answered";
                } else {
                    result[i][j] = _questions[i][j];
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
        Arrays.fill(_isAnswered, false);
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

    private int readFirstLine(ProcessBuilder p) throws IOException {
        ProcessBuilder pb = p;

        Process process = pb.start();

        InputStream stdout = process.getInputStream();
        BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));

        String line = stdoutBuffered.readLine();
        return Integer.parseInt(line);
    }

    private void countQuestions() {
        ProcessBuilder pb =
                new ProcessBuilder("/bin/bash","-c", "ls | head -n1 | xargs cat | wc -l" );
        pb.directory(new File("../categories"));

        try {
            _numOfQuestions = readFirstLine(pb);
        } catch (IOException e) {

        }
    }

    private void countCategories(){
        ProcessBuilder pb =
                new ProcessBuilder("/bin/bash","-c", "ls | wc -l" );
        pb.directory(new File("../categories"));

        try {
            _numOfCategories = readFirstLine(pb);
        } catch (IOException e) {

        }
    }
}
