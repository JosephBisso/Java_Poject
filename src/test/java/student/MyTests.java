package student;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import ias.Factory;
import ias.Game;
import ias.GameException;

/**
 * Add your own tests.
 */
public class MyTests {
    
    @Test
    public void Factory_returnssomething() throws GameException {
        Game game = Factory.createGame("Testing 123");
        assertNotNull(game);
    }
}
