import org.junit.Test;
import ru.java.FindPair.Logic;

import static org.junit.Assert.*;

public class LogicTest {
    @Test(expected = Logic.LogicException.class)
    public void checkCreation() throws Logic.LogicException {
        new Logic(7);
    }

    @Test
    public void checkBoard() throws Exception {
        Logic logic = new Logic(6);
        int[] arr = new int[18];
        for (int i = 0 ; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                arr[logic.getField(i,j)]++;
            }
        }
        for (int i = 0 ; i < 18; i++) {
            assertEquals(2, arr[i]);
        }
    }
}