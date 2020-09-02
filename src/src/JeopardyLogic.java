package src;

import java.io.*;

public class JeopardyLogic {
    private int _numOfQuestions;
    private int _numOfCategories;
    private int[] scores;
    private String[][] questions;
    private String[][] answers;

    public JeopardyLogic(){
        countCategories();
        countQuestions();

        scores = new int[_numOfQuestions];
        questions = new String[_numOfCategories][_numOfQuestions];
        answers = new String[_numOfCategories][_numOfQuestions];

        
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
