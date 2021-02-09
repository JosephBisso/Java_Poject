package ias;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FactoryTestPublic {

    @Test
    public void createGame_withValidName_isNotNull() throws GameException {
        Game game = Factory.createGame("Test");
        assertNotNull(game);
    }

    @Test
    public void loadGame_withSSPFile_isNotNull() throws GameException {
        assertNotNull(Factory.loadGame("src/test/resources/SSP.game"));
    }

    @Test
    public void loadGame_withInvalidPropertyLine_throws() {
        assertThrows(GameException.class, () -> Factory.loadGame("src/test/resources/InvalidProperty.game"));
    }

    @Test
    public void loadGame_withDuplicateGame_throws() {
        assertThrows(GameException.class, () -> Factory.loadGame("src/test/resources/DuplicateGame.game"));
    }

    @Test
    public void loadGame_withMagicFile_hasCorrectGameName() throws GameException {
        Game game = Factory.loadGame("src/test/resources/Magic.game");
        TestUtil.assertArrayEqualsUnordered(new String[] {"Magic"}, game.get("game", "*"));
    }

    @Test
    public void loadGame_withBasicFile_hasAllCards() throws GameException {
        Game game = Factory.loadGame("src/test/resources/Basic.game");
        assertEquals(3, game.get("card", "*").length);
    }

    @Test
    public void loadGame_withBasicFile_hasCorrectPropertyName() throws GameException {
        Game game = Factory.loadGame("src/test/resources/Basic.game");
        TestUtil.assertArrayEqualsUnordered(new String[] {"color"}, game.get("property", "color"));
    }

    @Test
    public void loadGame_withBasicFile_hasCorrectStringRuleNames() throws GameException {
        Game game = Factory.loadGame("src/test/resources/Basic.game");
        TestUtil.assertArrayEqualsUnordered(new String[] {"color:red>black"}, game.get("rule", "*"));
    }

    @Test
    public void loadGame_withBasicFile_canAddCard() throws GameException {
        Game game = Factory.loadGame("src/test/resources/Basic.game");
        game.defineCard("Four");
        game.defineCard("Five");
        TestUtil.assertArrayEqualsUnordered(new String[] {"One", "Two", "Three", "Four", "Five"},
                game.get("card", "*"));
    }

    @Test
    public void loadGame_withSSPFile_canAddRule() throws GameException {
        Game game = Factory.loadGame("src/test/resources/SSP.game");
        game.defineRule("name", "squirrel", "scissor");
        game.defineRule("name", "squirrel", "stone");
        game.defineRule("name", "squirrel", "paper");
        TestUtil.assertArrayEqualsUnordered(new String[] {"name:squirrel>stone"},
                game.get("rule", "name:squirrel>stone"));
    }
}
