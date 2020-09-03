package src;

import java.io.*;

public class JeopardyLogic {
    private int _numOfQuestions;
    private int _numOfCategories;
    private int[] scores;
    private int _winning;
    private String[][] questions;
    private String[][] answers;
    private Boolean[][] isAnswered;

    public JeopardyLogic() throws IOException {
        countCategories();
        countQuestions();

        scores = new int[_numOfQuestions];
        questions = new String[_numOfCategories][_numOfQuestions];
        answers = new String[_numOfCategories][_numOfQuestions];
        isAnswered = new Boolean[_numOfCategories][_numOfQuestions];

        for(int i = 0; i < _numOfQuestions; i++){
            for(int j = 0; j < _numOfCategories; j++){
                isAnswered[i][j] = false;
            }
        }

        BufferedReader br = new BufferedReader(new FileReader(new File(".." + File.separator +"categories")));

        int i = 0;
        int j = 0;

        String line;
        while ((line = br.readLine()) != null) {
            BufferedReader in = new BufferedReader(new FileReader(new File(".." +
                    File.separator +"categories" + File.separator + line)));

            while((line = in.readLine()) != null){
                String[] aLine = line.split(",");

                scores[i] = Integer.parseInt(aLine[0]);
                questions[i][j] = aLine[1];
                answers[i][j] = aLine[2];

                i++;
            }
            j++;
        }
        
    }

    public String[][] getAnswers(){
        String[][] result = new String[_numOfQuestions][_numOfCategories];
        for(int i = 0; i < _numOfQuestions; i++){
            for (int j = 0; j < _numOfCategories; j++){
                if(isAnswered[i][j] == true){
                    questions[i][j] = "Answered";
                } else {
                    result[i][j] = questions[i][j];
                }
            }
        }

        return result;
    }

    public int getWinning(){
        return _winning;
    }
    
    public void reset() {
        _winning = 0;
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
