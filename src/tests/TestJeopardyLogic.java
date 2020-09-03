package tests;

import org.junit.Test;
import src.JeopardyLogic;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestJeopardyLogic {

    @Test
    public void testAnswers() throws IOException {
        JeopardyLogic logic = new JeopardyLogic();
        String answers = logic.getAnswer(0,0);

        assertEquals("Kiwi", answers);
    }

    @Test
    public void testCategories() throws IOException {
        JeopardyLogic logic = new JeopardyLogic();
        String[] cat = logic.getCategories();

        String[] expected = new String[2];
        expected[0] = "animals";
        expected[1] = "countries";

        assertEquals(expected, cat);
    }

}
