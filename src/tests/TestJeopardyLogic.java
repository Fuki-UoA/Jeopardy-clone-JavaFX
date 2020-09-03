package tests;

import org.junit.Test;
import src.JeopardyLogic;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestJeopardyLogic {

    @Test
    public void testAnswers() throws IOException {
        JeopardyLogic logic = new JeopardyLogic();
        String[][] answers = logic.getAnswers();

        assertEquals("Kiwi", answers[0][0]);
    }

}
