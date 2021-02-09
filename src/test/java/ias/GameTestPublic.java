package ias;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class GameTestPublic {

    @TempDir
    File tempDir;
    private Game game;

    @BeforeEach
    public void createGame() throws GameException {
        game = Factory.createGame("Test");
    }

    @Test
    public void defineCard_withUniqueNames_doesNotThrow() throws GameException {
        game.defineCard("Card 1");
        game.defineCard("Card 2");
        game.defineCard("Card 3");
    }

    @Test
    public void defineCard_withDuplicateName_throws() throws GameException {
        game.defineCard("Card 1");
        game.defineCard("Card 2");
        assertThrows(GameException.class, () -> {
            game.defineCard("Card 1");
        });
    }

    @Test
    public void defineProperty_withValidNameAndType_doesNotThrow() throws GameException {
        game.defineProperty("type", "string");
        game.defineProperty("power", "integer");
    }

    @Test
    public void defineProperty_withInvalidType_throws() throws GameException {
        assertThrows(GameException.class, () -> { game.defineProperty("Foo", "Foobar");});
    }

    @Test
    public void setProperty_withIntegerAndValidValue_doesNotThrow() throws GameException {
        game.defineCard("One");
        game.defineProperty("power", "integer");
        game.setProperty("One", "power", 10);
    }

    @Test
    public void setProperty_withSameCardAndSamePropertyMultipleTimes_throws() throws GameException {
        game.defineCard("One");
        game.defineProperty("power", "integer");
        game.setProperty("One", "power", 10);
        assertThrows(GameException.class, () -> {
            game.setProperty("One", "power", 10);});
    }

    @Test
    public void defineRule_withOneIntegerRule_doesNotThrow() throws GameException {
        game.defineProperty("power", "integer");
        game.defineRule("power", ">");
    }

    @Test
    public void defineRule_withMultipleDifferentStringRules_doesNotThrow() throws GameException {
        game.defineProperty("type", "string");
        game.defineRule("type", "paper", "rock");
        game.defineRule("type", "rock", "scissors");
        game.defineRule("type", "scissors", "paper");
    }

    @Test
    public void defineRule_withDuplicateStringRule_throws() throws GameException {
        game.defineProperty("type", "string");
        game.defineRule("type", "rock", "scissors");
        assertThrows(GameException.class, () -> {
            game.defineRule("type", "rock", "scissors");});
    }

    @Test
    public void defineRule_withInvalidIntegerOperator_throws() throws GameException {
        game.defineProperty("power", "integer");
        assertThrows(GameException.class, () -> {
            game.defineRule("power", "!");});
    }

    @Test
    public void defineRule_withIntegerRuleAndStringProperty_throws() throws GameException {
        game.defineProperty("type", "string");
        assertThrows(GameException.class, () -> {
            game.defineRule("type", ">");});
    }

    @Test
    public void get_withGameAndAnything_returnsGameName() throws GameException {
        TestUtil.assertArrayEqualsUnordered(new String[] {"Test"}, game.get("game", "ignored"));
    }

    @Test
    public void get_withCardAndName_returnsGivenCard() throws GameException {
        game.defineCard("One");
        game.defineCard("Two");
        TestUtil.assertArrayEqualsUnordered(new String[] {"One"}, game.get("card", "One"));
    }

    @Test
    public void get_withPropertyAndWildcard_returnsAllProperties() throws GameException {
        game.defineProperty("power", "integer");
        game.defineProperty("type", "string");
        TestUtil.assertArrayEqualsUnordered(new String[] {"power", "type"}, game.get("property", "*"));
    }

    @Test
    public void get_withRuleAndName_returnsGivenStringRule() throws GameException {
        game.defineProperty("power", "integer");
        game.defineProperty("language", "string");
        game.defineRule("power", ">");
        game.defineRule("language", "Java", "JavaScript");
        game.defineRule("language", "CPP", "C");
        TestUtil.assertArrayEqualsUnordered(new String[] {"language:Java>JavaScript"},
                game.get("rule", "language:Java>JavaScript"));
    }

    @Test
    public void saveToFile_withValidPath_savesToGivenPath() throws GameException {
        File targetFile = new File(tempDir, "Test.game");
        game.saveToFile(targetFile.getAbsolutePath());
        assertTrue(targetFile.exists());
    }

    @Test
    public void saveToFile_withGameName_containsGivenName() throws GameException, IOException {
        File targetFile = new File(tempDir, "Test.game");
        game.saveToFile(targetFile.getAbsolutePath());
        String file = Files.readString(targetFile.toPath());
        assertTrue(file.startsWith("Game: Test"));
    }

    @Test
    public void saveToFile_withMultipleCards_writesAllCards() throws GameException, IOException {
        game.defineCard("Lurrus");
        game.defineCard("Yorion");
        game.defineCard("Kaheera");

        File targetFile = new File(tempDir, "Test.game");
        game.saveToFile(targetFile.getAbsolutePath());
        String file = Files.readString(targetFile.toPath());
        assertTrue(file.contains("Card: Lurrus"));
        assertTrue(file.contains("Card: Yorion"));
        assertTrue(file.contains("Card: Kaheera"));
    }

    @Test
    public void saveToFile_withMultipleCardProperties_writesAllCardProperties() throws GameException, IOException {
        game.defineCard("Arbor Elf");
        game.defineCard("Realm Cloaked Giant");
        game.defineProperty("height", "integer");
        game.defineProperty("race", "string");
        game.setProperty("Arbor Elf", "race", "elf");
        game.setProperty("Realm Cloaked Giant", "race", "giant");
        game.setProperty("Arbor Elf", "height", 4);
        game.setProperty("Realm Cloaked Giant", "height", 9000);

        File targetFile = new File(tempDir, "Test.game");
        game.saveToFile(targetFile.getAbsolutePath());
        String file = Files.readString(targetFile.toPath());
        assertTrue(file.contains("CardProperty: Arbor Elf | race | elf"));
        assertTrue(file.contains("CardProperty: Arbor Elf | height | 4"));
        assertTrue(file.contains("CardProperty: Realm Cloaked Giant | race | giant"));
        assertTrue(file.contains("CardProperty: Realm Cloaked Giant | height | 9000"));
    }

    @Test
    public void saveToFile_withBasicGame_savedFileCanBeLoadedAgain() throws GameException {
        game = Factory.loadGame("src/test/resources/Basic.game");
        File targetFile = new File(tempDir, "Test.game");
        game.saveToFile(targetFile.getAbsolutePath());
        //This does not check for correctness, only consistency
        assertNotNull(Factory.loadGame(targetFile.getAbsolutePath()));
    }
}
